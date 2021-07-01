package com.hyd.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 多个消费者 consume 一个队列
 */
public class MulitConsumerOneQueue {
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

	private static class ConsumerWorker implements Runnable {
		private Connection connection;
		private String queueName;

		public ConsumerWorker(Connection connection, String queueName) {
			this.connection = connection;
			this.queueName = queueName;
		}

		@Override
		public void run() {
			try {
				Channel channel = connection.createChannel();
				/* 声明交换机 */
				channel.exchangeDeclare(EXCHANGE_NAME, "direct");
				/* 声明一个队列,rabbitmq，如果队列已存在，不会重复创建 */
				channel.queueDeclare(queueName, false, false, false, null);
				/* 绑定交换机与队列 */
				channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEYS[1]);
				String consumerName = Thread.currentThread().getName();
				System.out.println("[" + consumerName + "]" + "waiting for message ...");

				DefaultConsumer consumer = new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
						String msg;
						msg = new String(body, StandardCharsets.UTF_8);
						System.out.println("received [" + envelope.getRoutingKey() + "] " + msg);
					}
				};

				//消费队列消息
				channel.basicConsume(queueName, true, consumer);

				TimeUnit.SECONDS.sleep(1);//等待控制台输出语句
				channel.close();

			} catch (IOException | TimeoutException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		//amqp://test:123456@127.0.0.1:5672//test (因为虚拟主机名叫/test,千万别把/丢掉)
		factory.setUri("amqp://" + USER_NAME + ":" + PASSWORD + "@" + HOST + ":" + PORT + "/" + VIRTUAL_HOST);
		ExecutorService service = Executors.newCachedThreadPool();
		Connection connection = factory.newConnection();
		for (int i = 0; i < 3; i++) {
			service.execute(new ConsumerWorker(connection, QUEUE_NAME_02));
		}


		while (!service.awaitTermination(5, TimeUnit.SECONDS)) {
			connection.close();
			service.shutdown();
		}
	}
}
