package com.enkaizen.fileuploader.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.enkaizen.fileuploader.config.MessagingConfig;
import com.enkaizen.fileuploader.dto.Status;

@Component
public class Consumer {
	String mesaage = "";

	@RabbitListener(queues = MessagingConfig.QUEUE)
	public void consumeMessageFromQueue(Status status) {
		System.out.println("Message recieved from queue : " + status.getMessage());
		mesaage = status.getMessage();
	}

	public String getMessage() {
		return mesaage;
	}
}
