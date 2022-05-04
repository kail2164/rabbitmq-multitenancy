package com.example.product.publisher;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.common.constants.RabbitMQConstant;
import com.example.common.dto.CustomException;
import com.example.common.util.RabbitMQUtil;

@Service
public class AccountPublisher {
	public List<String> fetchAllSchemas() throws CustomException {
		return RabbitMQUtil.sendAndReceiveList(RabbitMQConstant.TOPIC_ACCOUNT, RabbitMQConstant.ROUTING_ACCOUNT_GET_ALL_SCHEMAS,
				"", String.class);
	}
}
