package com.example.common.constants;

import com.example.common.util.StringUtil;

public interface RabbitMQConstant {
	String TOPIC = "topic";
	String FANOUT = "fanout";
	String QUEUE = "queue";
	String ONLY_ONE = "*";
	String ZERO_OR_MORE = "#";
	String SCHEMA = "schema";
	String SCHEMAS = "schemas";
	String PRODUCT = "product";
	String ACCOUNT = "account";
	String GET = "get";
	String ALL = "all";
	String FOR = "for";
	String ID = "id";
	String BY = "by";
	String LOGOUT = "logout";
	String TOKEN = "token";
	String VALIDATE = "validate";
	
	String FANOUT_SCHEMA = StringUtil.getExchange(SCHEMA, FANOUT);
	
	String TOPIC_ACCOUNT = StringUtil.getExchange(ACCOUNT, TOPIC);
	
	String QUEUE_ACCOUNT_GET_ALL_SCHEMAS = StringUtil.getQueueName(ACCOUNT, GET, ALL, SCHEMAS);
	String QUEUE_ACCOUNT_LOGOUT = StringUtil.getQueueName(ACCOUNT, LOGOUT);
	String QUEUE_ACCOUNT_VALIDATE_TOKEN = StringUtil.getQueueName(ACCOUNT, VALIDATE, TOKEN);

	String ACTION_ACCOUNT_GET_ALL_SCHEMAS = StringUtil.getAction(ACCOUNT, GET, ALL, SCHEMAS);
	String ACTION_ACCOUNT_LOGOUT = StringUtil.getAction(ACCOUNT, LOGOUT);
	String ACTION_ACCOUNT_VALIDATE_TOKEN = StringUtil.getAction(ACCOUNT, VALIDATE, TOKEN);

	String ROUTING_ACCOUNT_GET_ALL_SCHEMAS = StringUtil.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_GET_ALL_SCHEMAS, ZERO_OR_MORE);
	String ROUTING_ACCOUNT_LOGOUT = StringUtil.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_LOGOUT, ZERO_OR_MORE);
	String ROUTING_ACCOUNT_VALIDATE_TOKEN = StringUtil.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_VALIDATE_TOKEN, ZERO_OR_MORE);

	String TOPIC_PRODUCT = StringUtil.getExchange(PRODUCT, TOPIC);
	
	String QUEUE_PRODUCT_GET_ALL_BY_ACCOUNT_ID = StringUtil.getQueueName(PRODUCT, GET, ALL, BY, ACCOUNT, ID);
	String QUEUE_PRODUCT_SCHEMA = StringUtil.getQueueName(PRODUCT, SCHEMA);
	
	String ACTION_PRODUCT_GET_ALL_BY_ACCOUNT_ID = StringUtil.getAction(PRODUCT, GET, ALL, BY, ACCOUNT, ID);
	
	String ROUTING_PRODUCT_GET_ALL_BY_ACCOUNT_ID = StringUtil.getRoutingKey(PRODUCT, ACTION_PRODUCT_GET_ALL_BY_ACCOUNT_ID, ZERO_OR_MORE);
}
