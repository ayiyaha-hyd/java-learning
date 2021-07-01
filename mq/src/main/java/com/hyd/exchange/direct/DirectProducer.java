package com.hyd.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author PIG
 */
public class DirectProducer {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 5672;
	private static final String VIRTUAL_HOST = "/test";
	private static final String USER_NAME = "test";
	private static final String PASSWORD = "123456";
	private static final String EXCHANGE_NAME = "test-exchangeName-direct_01";
	public final static String[] ROUTING_KEYS = {"test-routingKey_direct_1", "test-routingKey_direct_2", "test-routingKey_direct_3"};

	public final static String QUEUE_NAME_01 = "test-queueName_direct_1";
	public final static String QUEUE_NAME_02 = "test-queueName_direct_2";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setPort(PORT);
		factory.setVirtualHost(VIRTUAL_HOST);
		factory.setUsername(USER_NAME);
		factory.setPassword(PASSWORD);
		//factory.setUri("amqp://"+USER_NAME+":"+PASSWORD+"@"+HOST+":"+PORT+VIRTUAL_HOST);//也可使用URI进行连接
		//创建连接
		Connection connection = factory.newConnection();
		//创建信道
		Channel channel = connection.createChannel();
		//创建交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", false);

		//声明队列
		channel.queueDeclare(QUEUE_NAME_01, false, false, false, null);
		channel.queueDeclare(QUEUE_NAME_02, false, false, false, null);
		//通过路由key绑定交换机与队列
		channel.queueBind(QUEUE_NAME_01, EXCHANGE_NAME, ROUTING_KEYS[0]);
		channel.queueBind(QUEUE_NAME_02, EXCHANGE_NAME, ROUTING_KEYS[1]);
		//推送消息
		for (int i = 0; i < 9; i++) {
			//指定路由
			String routingKey = ROUTING_KEYS[i % 3];
			//消息
			String msg = "hello,rabbitmq " + routingKey + " " + i;
			//发布消息
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
			System.out.println("sent " + EXCHANGE_NAME + ":" + routingKey + ": " + msg);
		}
		channel.close();
		connection.close();
	}
}
