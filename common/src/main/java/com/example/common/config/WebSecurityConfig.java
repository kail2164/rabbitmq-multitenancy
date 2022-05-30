package com.example.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.common.constants.RabbitMQConstants;
import com.example.common.dto.CustomException;
import com.example.common.dto.UserDTO;
import com.example.common.util.RabbitMQUtils;

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
	private CustomAuthenticationProvider customAuthenticationProvider;
	private RabbitMQUtils rabbitMQUtils;

	public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter,
			CustomAuthenticationProvider customAuthenticationProvider, RabbitMQUtils rabbitMQUtils) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtRequestFilter = jwtRequestFilter;
		this.customAuthenticationProvider = customAuthenticationProvider;
		this.rabbitMQUtils = rabbitMQUtils;
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
		AuthenticationService authenServicePrivate = new AuthenticationService();
		auth.userDetailsService(authenServicePrivate).passwordEncoder(passwordEncoder);
		auth.authenticationProvider(customAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests()
				.antMatchers(COMMON_AUTH_PATH, HEALTH_CHECK_PATH, SWAGGER_PATH, WEB_JARS_PATH, V3_PATH,
						SWAGGER_RESOURCES_PATH, "/csrf", "/", "/api/docs/swagger-ui.html")
				.permitAll()
				// all other requests need to be authenticated
				.anyRequest().authenticated().and()
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	private class AuthenticationService implements UserDetailsService {
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User userDetails;
			try {
				UserDTO userDTO = rabbitMQUtils.sendAndReceive(RabbitMQConstants.TOPIC_ACCOUNT,
						RabbitMQConstants.ROUTING_ACCOUNT_GET_USER_DETAILS, username, UserDTO.class);
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(userDTO.getRole()));
				userDetails = new User(userDTO.getUsername(), userDTO.getPassword(), authorities);
			} catch (CustomException e) {
				e.printStackTrace();
				throw new UsernameNotFoundException("Username not found");
			}
			return userDetails;
		}
	}
}
