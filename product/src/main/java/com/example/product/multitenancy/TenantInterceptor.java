package com.example.product.multitenancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.common.constants.GlobalConstant;
import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;
import com.example.common.util.JwtUtil;

@Component
public class TenantInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		String clientId = req.getHeader(GlobalConstant.X_ACCOUNT_ID);
		if (!JwtUtil.isInWhiteList(req.getRequestURI())) {
			if (ObjectUtils.isEmpty(clientId) || clientId.equals("0") || clientId.equals("null")) {
				throw new CustomException(APIStatus.INVALID_TOKEN, "Invalid token");
			} else {
				TenantContext.setCurrentTenant("client" + clientId);
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
