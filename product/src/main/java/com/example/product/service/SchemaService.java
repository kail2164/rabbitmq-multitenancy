package com.example.product.service;

import org.springframework.beans.factory.InitializingBean;

public interface SchemaService extends InitializingBean{
	void createSchema(String schemaName);
}
