package kware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"cetus", "kware"})
@EnableCaching
//이구문이 없으면, componentscan은 현재 패키지가 basepackage가 되어 cetus package는 scan하지 않는다.
public class Application {

	private static String WAS_NAME = null;
	@Bean
    public WebServerFactoryCustomizer webServerFactoryCustomizer() {
        return factory -> {
            if (factory instanceof TomcatServletWebServerFactory) {
            	WAS_NAME = "Tomcat";
            } else if (factory instanceof JettyServletWebServerFactory) {
            	WAS_NAME = "Jetty";
            } else if (factory instanceof UndertowServletWebServerFactory) {
            	WAS_NAME = "Undertow";
            } else {
            	WAS_NAME= "기타";
            }
        };
    }
	
	public static void main(String[] args) throws IOException {
		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
    	Environment environment = applicationContext.getEnvironment();
    	String serverPort = environment.getProperty("server.port");
		System.setProperty("java.awt.headless", "true");
        log.info("======================= {} Server is running on port: {} ===========================",WAS_NAME, serverPort);
	}

}
