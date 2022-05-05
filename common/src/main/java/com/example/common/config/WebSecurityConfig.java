package com.example.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String HEALTH_CHECK_PATH = "/health-check";
	private static final String SWAGGER_PATH = "/api/docs/**";
	private static final String WEB_JARS_PATH = "/webjars/**";
	private static final String V3_PATH = "/v3/**";
	private static final String SWAGGER_RESOURCES_PATH = "/swagger-resources/**";
	private static final String COMMON_AUTH_PATH = "/api/auth/**";

	
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;		
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired	
	public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
			JwtRequestFilter jwtRequestFilter) {
		super();
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
		// dont authenticate this particular request
		.authorizeRequests().antMatchers(COMMON_AUTH_PATH, HEALTH_CHECK_PATH, SWAGGER_PATH, WEB_JARS_PATH, V3_PATH, SWAGGER_RESOURCES_PATH, "/csrf", "/", "/api/docs/swagger-ui.html").permitAll()
		// all other requests need to be authenticated
		.anyRequest().authenticated().and()
		// make sure we use stateless session; session won't be used to
		// store user's state.
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
