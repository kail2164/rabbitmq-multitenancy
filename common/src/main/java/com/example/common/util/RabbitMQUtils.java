package com.example.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;


@Slf4j
//@DependsOn("rabbitMQConfig")
@Component
public class RabbitMQUtils {
	private RabbitTemplate rabbitTemplate;

	public RabbitMQUtils(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public <T extends Object> T sendAndReceive(String topic, String route, Object value, Class<T> clazz)
			throws CustomException {
		Object result = callRabbitMQ(topic, route, value);
		return clazz.cast(result);
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> sendAndReceiveList(String topic, String route, Object value,
			Class<T> clazz) throws CustomException {
		Object result = callRabbitMQ(topic, route, value);
		if (Objects.nonNull(result)) {
			if (result.getClass().isArray()) {
				return Arrays.asList((T) result);
			} else if (result instanceof Collection) {
				return new ArrayList<>((Collection<T>) result);
			} else {
				throw cannotCastException(result.getClass().getName(), clazz.getName());
			}
		}
		return null;
	}

	public void send(String topic, String route, Object value) throws CustomException {
		rabbitTemplate.convertAndSend(topic, route, value);
	}

	private Object callRabbitMQ(String topic, String route, Object value) throws CustomException {
		try {
			Object result = rabbitTemplate.convertSendAndReceive(topic, route, value);
			if (Objects.nonNull(result) && result instanceof RemoteInvocationResult) {
				RemoteInvocationResult casted = RemoteInvocationResult.class.cast(result);
				if (casted.hasException()) {
					log.error("Error in callRabbitMQ: ", casted.getException());
					throw new CustomException(APIStatus.BAD_REQUEST, casted.getException().getMessage());
				}
				return casted.getValue();
			}
			return result;
		} catch (Exception e) {
			log.error("Error in callRabbitMQ: ", e);
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
	}

	private CustomException cannotCastException(String fromClass, String toClass) {
		return new CustomException(APIStatus.BAD_REQUEST, "Cannot cast class: " + fromClass + " to: " + toClass);
	}

}
