package com.hyd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量
 */
public class SemaphoreDemo {
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(5);
		ExecutorService service = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			service.execute(
					() -> {
						System.out.println(Thread.currentThread().getName() + "请求...");
						if (semaphore.availablePermits() == 0) {
							System.out.println(Thread.currentThread().getName()+"无可用资源...");
						}
						try {
							semaphore.acquire();
							System.out.println(Thread.currentThread().getName() + "申请...");
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						semaphore.release();
						System.out.println(Thread.currentThread().getName() + "归还...");
					});
		}
		service.shutdown();
	}
}
