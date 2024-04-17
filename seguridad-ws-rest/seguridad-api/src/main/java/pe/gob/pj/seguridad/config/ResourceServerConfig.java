package pe.gob.pj.seguridad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey; 
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStoreDos());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/oauth/**").permitAll()
		//.antMatchers("/encriptar").permitAll()
		.antMatchers("/encriptarClave").permitAll()
		.antMatchers("/desencriptarClave").permitAll()
		.antMatchers("/generarClaveAccesoServicio").permitAll()
		.antMatchers("/verificarVersion").permitAll()
		.antMatchers("/seguridad/**").hasRole("SEG")
		.antMatchers("/mante/**").hasRole("ASE")
		.anyRequest().authenticated();
	}
	
	@Bean
	public JwtTokenStore tokenStoreDos() {
		return new JwtTokenStore(accessTokenConverterDos());
	} 

	@Bean
	public JwtAccessTokenConverter accessTokenConverterDos() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtKey);
		return tokenConverter;
	}

}