package xixi.testModuleA.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xixi.common.respository.ArgumentTypeRepository;
import xixi.common.spring.BeanFactoryUtil;
import xixi.launcher.AppLauncher;
import xixi.testModuleB.test.Account;
import xixi.transport.netty.client.NettyTcpClient;

public class AppRoot {

	private static final Logger logger = LoggerFactory
	.getLogger(AppRoot.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

/*		
        List<java.net.URL> list = new ArrayList<java.net.URL>();
        try {
           // Enumeration<java.net.URL> urls = ClassHelper.getClassLoader().getResources(fileName);
        	Enumeration<java.net.URL> urls = AppRoot.class.getClassLoader().getResources("app.properties");
            list = new ArrayList<java.net.URL>();
            while (urls.hasMoreElements()) {
            	URL url = urls.nextElement();
                list.add(url);
                logger.debug("Loading property file" + url.getPath());
            }
            logger.debug("ending");
        } catch (Throwable t) {
            logger.error("Fail to load ");
        }
*/
		ArgumentTypeRepository.addArgumentType(Account.class.getCanonicalName(), Account.class);
		AppLauncher.main(args);
		LoginService service = (LoginService)BeanFactoryUtil.getBean("loginService");
		service.login();
	}

}
