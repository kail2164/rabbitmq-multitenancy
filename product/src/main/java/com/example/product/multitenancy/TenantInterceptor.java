package com.example.product.multitenancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.common.constants.GlobalConstants;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.util.JwtUtils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@NoArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {

	private JwtUtils jwtUtil;

	@Autowired
	public TenantInterceptor(JwtUtils jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		String accountId = req.getHeader(GlobalConstants.X_ACCOUNT_ID);
		if (!jwtUtil.isInWhiteList(req.getRequestURI())) {
			if (ObjectUtils.isEmpty(accountId) || accountId.equals("0") || accountId.equals("null")) {
				throw new CustomException(APIStatus.INVALID_TOKEN, "Invalid token");
			} else {
				TenantContext.setCurrentTenant("account" + accountId);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
	}

}
