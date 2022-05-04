package com.example.product.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifier implements CurrentTenantIdentifierResolver {
	@Override
	public String resolveCurrentTenantIdentifier() {
		return TenantContext.getCurrentTenant().toString();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
