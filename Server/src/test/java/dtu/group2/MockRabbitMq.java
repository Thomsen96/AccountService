package dtu.group2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MockRabbitMq {

	private static final String EXCHANGE_NAME = "pub-sub-queue";
	private static final String ROUTING_KEY = "account";
	
	Channel channel;
	
	public MockRabbitMq() {
		try {
			ConnectionFactory mqFactory = new ConnectionFactory();
			mqFactory.setHost("localhost");
			Connection connection = mqFactory.newConnection();
			
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void PublishMsg(String message) throws UnsupportedEncodingException, IOException {
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
	}
	
}
