package com.hyd.exchange.direct;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class NormalConsumer {
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 5672;
	private static final String VIRTUAL_HOST = "%2Ftest";// "/test" url转移：'/'-> %2F
	private static final String USER_NAME = "test";
	private static final String PASSWORD = "123456";
	//交换机名称
	private static final String EXCHANGE_NAME = "test-exchangeName-direct_01";
	//路由key
	public final static String[] ROUTING_KEYS = {"test-routingKey_direct_1", "test-routingKey_direct_2", "test-routingKey_direct_3"};
	//队列名称
	public final static String QUEUE_NAME_01 = "test-queueName_direct_1";
	public final static String QUEUE_NAME_02 = "test-queueName_direct_2";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		//amqp://test:123456@127.0.0.1:5672//test (因为虚拟主机名叫/test,千万别把/丢掉)
		factory.setUri("amqp://" + USER_NAME + ":" + PASSWORD + "@" + HOST + ":" + PORT +"/"+ VIRTUAL_HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", false);
		//声明队列
		channel.queueDeclare(QUEUE_NAME_01, false, false, false, null);
		//通过路由key绑定交换机与队列
		channel.queueBind(QUEUE_NAME_01, EXCHANGE_NAME, ROUTING_KEYS[0]);
		System.out.println("waiting for message ...");

		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
				String msg;
				msg = new String(body, StandardCharsets.UTF_8);
				System.out.println("received [" + envelope.getRoutingKey() + "] " + msg);
			}
		};

		//消费队列消息
		channel.basicConsume(QUEUE_NAME_01, true, consumer);

		//防止控制台来不及输出语句就关闭掉
		while (channel.messageCount(QUEUE_NAME_01) == 0) {
			channel.close();
			connection.close();
			break;
		}
	}
}
