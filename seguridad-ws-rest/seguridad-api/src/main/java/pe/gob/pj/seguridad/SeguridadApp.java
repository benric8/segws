package pe.gob.pj.seguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "pe.gob.pj.*" })
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class SeguridadApp extends SpringBootServletInitializer  {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SeguridadApp.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SeguridadApp.class, args);
	}
}
