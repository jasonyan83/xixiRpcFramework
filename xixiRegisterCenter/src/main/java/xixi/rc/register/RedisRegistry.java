package xixi.rc.register;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xix.rc.bean.ModuleInfo;
import xix.rc.bean.ModuleStatusInfo;
import xixi.common.util.ConfigUtils;

public class RedisRegistry extends AbstractRegister implements Registry {

	private static final Logger logger = LoggerFactory
			.getLogger(RedisRegistry.class);

	private static JedisPool pool;

	static {

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.valueOf(ConfigUtils.getProperty(
				"redis.pool.maxActive", "10")));
		config.setMaxIdle(Integer.valueOf(ConfigUtils.getProperty(
				"redis.pool.maxIdle", "10")));
		config.setMaxWait(Long.valueOf(ConfigUtils.getProperty(
				"redis.pool.maxWait", "10")));
		config.setTestOnBorrow(Boolean.valueOf(ConfigUtils.getProperty(
				"redis.pool.testOnBorrow", "true")));
		config.setTestOnReturn(Boolean.valueOf(ConfigUtils.getProperty(
				"redis.pool.testOnReturn", "true")));
		pool = new JedisPool(ConfigUtils.getProperty("redis.ip", "127.0.0.1"),
				Integer.valueOf(ConfigUtils.getProperty("redis.port", "6379")));
	}

	private final String MODULE_ID_KEY = "module.ids";
	private final String MODULE_ID_DEPENDENCY_PREFIX = "d-";

	@Override
	public boolean register(final ModuleInfo moduleInfo) throws Exception {
		boolean succeed = false;
		boolean moudleExsit = isModuleExsit(moduleInfo.getModuleId(),
				moduleInfo.getIpAddress());

		long ret = addModuleId(moduleInfo.getModuleId());

		if (moudleExsit) {
			final ModuleStatusInfo module = getModule(moduleInfo.getModuleId(),
					moduleInfo.getIpAddress());

			if (!moduleInfo.isRcConnectLost()) {
				// RC stays live
				if (!module.isLive()) {
					module.setLive(true);
					logger.warn("模块{}对应的ip{},恢复服务", moduleInfo.getModuleId(),
							moduleInfo.getIpAddress());
					saveModule(module);
					succeed = true;
				} else {
					logger.warn("模块{}已经存在相应的IP地址{}，重复注册？",
							moduleInfo.getModuleId(), moduleInfo.getIpAddress());
					succeed = false;
				}
			} else {
				// RC was down and reboot again.
				module.setLive(true);
				module.setRegisterTime(new Date());
				succeed = true;
				saveModule(module);

				logger.info("Module {} registered again after RCreboot",
						moduleInfo);
			}

		} else {
			ModuleStatusInfo moduleStatusInfo = new ModuleStatusInfo()
					.buildModuleStatusInfo(moduleInfo);
			long rets = saveModule(moduleStatusInfo);
			succeed = true;
		}
		return succeed;
	}

	@Override
	public boolean unRegister(final short moduleId, final String ipAddress) {
		logger.debug("Unregistering module {}-{}" + moduleId, ipAddress);
		boolean succeed = false;
		boolean moudleExsit = isModuleExsit(moduleId, ipAddress);

		if (moudleExsit) {
			delModule(moduleId, ipAddress);

			logger.debug("unRegistering Module " + moduleId + " with instance "
					+ ipAddress);
			succeed = true;
			this.removeInstance(moduleId, ipAddress);

		} else {
			logger.debug("The module " + moduleId + " with instance"
					+ ipAddress + "is not exsit");
		}
		return succeed;
	}

	@Override
	public List<ModuleInfo> getModuleInstances(final short moduleId) {
		final String id = moduleId + "";
		final List<ModuleInfo> moduleInfoList = new ArrayList<ModuleInfo>();
		return executeJedisTask(new BinaryJedisTask<List<ModuleInfo>>() {
			@Override
			public List<ModuleInfo> execute(BinaryJedisCommands jedis) {
				Map<byte[], byte[]> ret = jedis.hgetAll(id.getBytes());
				for (Entry<byte[], byte[]> entry : ret.entrySet()) {
					ModuleStatusInfo moduleStatusInfo = (ModuleStatusInfo) SerializationUtils
							.deserialize(entry.getValue());
					ModuleInfo m = new ModuleInfo();
					m.setIpAddress(moduleStatusInfo.getIpAddress());
					m.setModuleId(moduleId);
					m.setWeight(moduleStatusInfo.getWeight());
					m.setRouterScheduleType(moduleStatusInfo
							.getRouterScheduleType());
					moduleInfoList.add(m);
				}
				return moduleInfoList;
			}
		});
	}

	@Override
	public void buildModuleDependencyMap(final short moduleId,
			final short dependentModuleId) {
		logger.debug("build module dependency map for source module {}, depentdent module {}");
		executeJedisTask(new BinaryJedisTask<Long>() {
			@Override
			public Long execute(BinaryJedisCommands jedis) {
				return jedis.sadd(
						(MODULE_ID_DEPENDENCY_PREFIX + moduleId).getBytes(),
						(dependentModuleId + "").getBytes());
			}
		});
	}

	@Override
	public List<Short> getDependentModuleIds(final short moduleId) {
		logger.debug("Get dependent moduleId for moduleId {}", moduleId);
		return executeJedisTask(new BinaryJedisTask<List<Short>>() {
			@Override
			public List<Short> execute(BinaryJedisCommands jedis) {
				Set<byte[]> rets = jedis
						.smembers((MODULE_ID_DEPENDENCY_PREFIX + moduleId)
								.getBytes());
				List<Short> list = new ArrayList<Short>();
				for (byte[] ret : rets) {
					list.add(Short.parseShort(new String(ret)));
				}
				return list;
			}
		});
	}

	@Override
	public ModuleStatusInfo getModuleStatusInfo(short moduleId, String ipAddress) {
		return getModule(moduleId, ipAddress);
	}

	public List<ModuleStatusInfo> getAllModules() {

		final List<ModuleStatusInfo> list = new ArrayList<ModuleStatusInfo>();
		List<String> ids = executeJedisTask(new BinaryJedisTask<List<String>>() {
			@Override
			public List<String> execute(BinaryJedisCommands jedis) {
				List<String> ids = new ArrayList<String>();
				Set<byte[]> retSet = jedis.smembers(MODULE_ID_KEY.getBytes());
				for (byte[] ret : retSet) {
					ids.add(new String(ret));
				}
				return ids;
			}
		});

		for (final String id : ids) {
			executeJedisTask(new BinaryJedisTask<List<ModuleStatusInfo>>() {
				@Override
				public List<ModuleStatusInfo> execute(BinaryJedisCommands jedis) {
					Map<byte[], byte[]> ret = jedis.hgetAll(id.getBytes());
					for (Entry<byte[], byte[]> entry : ret.entrySet()) {
						ModuleStatusInfo moduleStatusInfo = (ModuleStatusInfo) SerializationUtils
								.deserialize(entry.getValue());

						list.add(moduleStatusInfo);
					}
					return list;
				}
			});
		}

		return list;
	}

	@Override
	public boolean updateModuleStatusInfo(
			final ModuleStatusInfo moduleStatusInfo) {
		logger.debug("Update moduleStatusInfo for {}", moduleStatusInfo);
	    ModuleStatusInfo module = getModule(moduleStatusInfo.getModuleId(),
				moduleStatusInfo.getIpAddress());
		if (module != null) {
			logger.debug("Current module is {}", module);
			if (!module.isLive()) {
				logger.warn("模块{}对应的ip{},恢复服务", moduleStatusInfo.getModuleId(),
						moduleStatusInfo.getIpAddress());
			}
			module = module.updateModuleStatusInfo(moduleStatusInfo);
			long ret = saveModule(module);
			return (ret == 0l) ? true : false;
		}
		else {
			logger.error("There is No exsit module instance");
			return false;
		}
	}

	@Override
	protected void deactiveInstance(short moduleId, String ipAddress) {
		ModuleStatusInfo m = getModule(moduleId, ipAddress);
		m.setLive(false);
		saveModule(m);
	}

	private Long addModuleId(short moduleId) {
		final String id = moduleId + "";
		return executeJedisTask(new BinaryJedisTask<Long>() {
			@Override
			public Long execute(BinaryJedisCommands jedis) {
				if (!jedis.sismember(MODULE_ID_KEY.getBytes(), id.getBytes())) {
					return jedis.sadd(MODULE_ID_KEY.getBytes(), id.getBytes());
				}
				return -1l;
			}
		});
	}

	private <T> T executeJedisTask(BinaryJedisTask<T> task) {
		try {
			Jedis jedis = pool.getResource();
			try {
				return task.execute(jedis);
			} finally {
				pool.returnResource(jedis);
			}
		} catch (Throwable t) {

		}
		return null;
	}

	private interface BinaryJedisTask<T> {
		T execute(BinaryJedisCommands jedis);
	}

	private ModuleStatusInfo getModule(final short moduleId,
			final String ipAddress) {
		final String id = moduleId + "";
		return executeJedisTask(new BinaryJedisTask<ModuleStatusInfo>() {
			@Override
			public ModuleStatusInfo execute(BinaryJedisCommands jedis) {
				byte[] ret = jedis.hget(id.getBytes(), ipAddress.getBytes());
				return (ModuleStatusInfo) SerializationUtils.deserialize(ret);
			}
		});
	}

	private Long saveModule(final ModuleStatusInfo module) {
		final String id = module.getModuleId() + "";
		return executeJedisTask(new BinaryJedisTask<Long>() {
			@Override
			public Long execute(BinaryJedisCommands jedis) {
				return jedis.hset(id.getBytes(), module.getIpAddress()
						.getBytes(), SerializationUtils.serialize(module));

			}
		});
	}

	private Long delModule(final short moduleId, final String ipAddress) {
		final String id = moduleId + "";
		return executeJedisTask(new BinaryJedisTask<Long>() {
			@Override
			public Long execute(BinaryJedisCommands jedis) {
				return jedis.hdel(id.getBytes(), ipAddress.getBytes());
			}

		});
	}

	private boolean isModuleExsit(final short moduleId, final String ipAddress) {
		final String id = moduleId + "";
		return executeJedisTask(new BinaryJedisTask<Boolean>() {

			@Override
			public Boolean execute(BinaryJedisCommands jedis) {
				return jedis.hexists(id.getBytes(), ipAddress.getBytes());
			}

		});
	}
}
