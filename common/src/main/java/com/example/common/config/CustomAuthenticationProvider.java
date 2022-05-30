package com.example.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.common.constants.RabbitMQConstants;
import com.example.common.dto.CustomException;
import com.example.common.dto.UserDTO;
import com.example.common.util.RabbitMQUtils;
import com.example.common.util.StringUtils;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private RabbitMQUtils rabbitMQUtils;

	public CustomAuthenticationProvider(RabbitMQUtils rabbitMQUtils) {
		this.rabbitMQUtils = rabbitMQUtils;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			if(StringUtils.isNullOrEmpty(authentication.getName())) {
				throw new BadCredentialsException("Username cannot be null");
			}
			UserDTO userDTO = rabbitMQUtils.sendAndReceive(RabbitMQConstants.TOPIC_ACCOUNT,
					RabbitMQConstants.ROUTING_ACCOUNT_GET_USER_DETAILS, authentication.getName(), UserDTO.class);
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(userDTO.getRole()));
			User user = new User(userDTO.getUsername(), userDTO.getPassword(), authorities);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					user, null, user.getAuthorities());
			return usernamePasswordAuthenticationToken;
		} catch (CustomException e) {
			throw new BadCredentialsException("Wrong username or password");
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
