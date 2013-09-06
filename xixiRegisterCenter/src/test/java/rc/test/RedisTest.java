package rc.test;

import java.util.Date;

import org.springframework.util.SerializationUtils;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xix.rc.bean.ModuleInstanceStatusInfo;
import xixi.common.util.ConfigUtils;

public class RedisTest {

	private static JedisPool pool;
	
	static{
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
	
	
	public static void main(String[] sf) {
	/*	 ModuleStatusInfo moduleStatusInfo = new ModuleStatusInfo();
		 moduleStatusInfo.setModuleId(Short.valueOf("20001"));
		 moduleStatusInfo.setIpAddress("192.0.0.1");
		 moduleStatusInfo.setLastHBTime(new Date());
		 byte[] retByte = SerializationUtils.serialize(moduleStatusInfo);
		 ModuleStatusInfo moduleStatusInfo2  = (ModuleStatusInfo)SerializationUtils.deserialize(retByte);*/
		 
		 //System.out.println("moduleStatusInfo2 is " + moduleStatusInfo2);
		Long ret = executeJedisTask(new BinaryJedisTask<Long>() {

			@Override
			public Long execute(BinaryJedisCommands jedis) {
				return jedis.sadd("guan".getBytes(), "fengyq".getBytes());
			}

		});
		System.out.println("ret is " + ret);
		
		Long ret2 = executeJedisTask(new BinaryJedisTask<Long>() {

			@Override
			public Long execute(BinaryJedisCommands jedis) {
				 jedis.hset("101".getBytes(), "192.0.0.0".getBytes(), "jasonyan".getBytes());
				 ModuleInstanceStatusInfo moduleStatusInfo = new ModuleInstanceStatusInfo();
				 moduleStatusInfo.setModuleId(Short.valueOf("20001"));
				 moduleStatusInfo.setIpAddress("192.0.0.1");
				 moduleStatusInfo.setLastHBTime(new Date());
				 return jedis.hset("102".getBytes(), "192.0.0.1".getBytes(), SerializationUtils.serialize(moduleStatusInfo));
			}

		});

		System.out.println("ret2 is " + ret);
		
		
	}
	
	private static <T> T executeJedisTask(BinaryJedisTask<T> task) {
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
}
