package com.example.common.constants;

import com.example.common.util.StringUtils;

public interface RabbitMQConstants {
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
	String DETAILS = "details";
	String LOGOUT = "logout";
	String LOGIN = "login";
	String TOKEN = "token";
	String USER = "user";
	String VALIDATE = "validate";
	
	String FANOUT_SCHEMA = StringUtils.getExchange(SCHEMA, FANOUT);
	
	String TOPIC_ACCOUNT = StringUtils.getExchange(ACCOUNT, TOPIC);
	
	String QUEUE_ACCOUNT_GET_ALL_SCHEMAS = StringUtils.getQueueName(ACCOUNT, GET, ALL, SCHEMAS);
	String QUEUE_ACCOUNT_LOGOUT = StringUtils.getQueueName(ACCOUNT, LOGOUT);
	String QUEUE_ACCOUNT_LOGIN = StringUtils.getQueueName(ACCOUNT, LOGIN);
	String QUEUE_ACCOUNT_VALIDATE_TOKEN = StringUtils.getQueueName(ACCOUNT, VALIDATE, TOKEN);
	String QUEUE_ACCOUNT_GET_USER_DETAILS = StringUtils.getQueueName(ACCOUNT, GET, USER, DETAILS);

	String ACTION_ACCOUNT_GET_ALL_SCHEMAS = StringUtils.getAction(ACCOUNT, GET, ALL, SCHEMAS);
	String ACTION_ACCOUNT_LOGOUT = StringUtils.getAction(ACCOUNT, LOGOUT);
	String ACTION_ACCOUNT_LOGIN = StringUtils.getAction(ACCOUNT, LOGIN);
	String ACTION_ACCOUNT_VALIDATE_TOKEN = StringUtils.getAction(ACCOUNT, VALIDATE, TOKEN);
	String ACTION_ACCOUNT_GET_USER_DETAILS = StringUtils.getAction(ACCOUNT, GET, USER, DETAILS);

	String ROUTING_ACCOUNT_GET_ALL_SCHEMAS = StringUtils.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_GET_ALL_SCHEMAS, ZERO_OR_MORE);
	String ROUTING_ACCOUNT_LOGOUT = StringUtils.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_LOGOUT, ZERO_OR_MORE);
	String ROUTING_ACCOUNT_LOGIN = StringUtils.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_LOGIN, ZERO_OR_MORE);
	String ROUTING_ACCOUNT_VALIDATE_TOKEN = StringUtils.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_VALIDATE_TOKEN, ZERO_OR_MORE);
	String ROUTING_ACCOUNT_GET_USER_DETAILS = StringUtils.getRoutingKey(ACCOUNT, ACTION_ACCOUNT_GET_USER_DETAILS, ZERO_OR_MORE);

	String TOPIC_PRODUCT = StringUtils.getExchange(PRODUCT, TOPIC);
	
	String QUEUE_PRODUCT_GET_ALL_BY_ACCOUNT_ID = StringUtils.getQueueName(PRODUCT, GET, ALL, BY, ACCOUNT, ID);
	String QUEUE_PRODUCT_SCHEMA = StringUtils.getQueueName(PRODUCT, SCHEMA);
	
	String ACTION_PRODUCT_GET_ALL_BY_ACCOUNT_ID = StringUtils.getAction(PRODUCT, GET, ALL, BY, ACCOUNT, ID);
	
	String ROUTING_PRODUCT_GET_ALL_BY_ACCOUNT_ID = StringUtils.getRoutingKey(PRODUCT, ACTION_PRODUCT_GET_ALL_BY_ACCOUNT_ID, ZERO_OR_MORE);
}
