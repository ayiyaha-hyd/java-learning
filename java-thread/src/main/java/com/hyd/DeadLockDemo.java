package com.hyd;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 死锁演示
 */
public class DeadLockDemo {
	public static void main(String[] args) {
		DeadLockDemo d = new DeadLockDemo();
//		d.show1();
		d.show2();
	}

	/**
	 * 发生异常不释放锁
	 */
	public void show2() {
		ReentrantLock lock = new ReentrantLock();
		Condition con1 = lock.newCondition();
		Condition con2 = lock.newCondition();

		new Thread(() -> {
			try {
//				lock.lock();//使用trylock避免死锁
				lock.tryLock(3,TimeUnit.SECONDS);
				TimeUnit.SECONDS.sleep(1
				);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("o1 --> o2");
			int i = 1 / 0;//发生异常时，不会释放锁
			lock.unlock();
		}).start();
		new Thread(() -> {
			try {
				lock.tryLock(3,TimeUnit.SECONDS);
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("o2 --> o1");
			lock.unlock();
		}).start();
	}

	/**
	 * synchronized 演示死锁，发生异常释放锁
	 */
	public void show1() {
		Object o1 = new Object();
		Object o2 = new Object();

		new Thread(() -> {

			synchronized (o1) {
				try {
					TimeUnit.SECONDS.sleep(1
					);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("o1 --> o2");
				//				int i = 1/0;//发生异常时，会释放锁
				synchronized (o2) {
				}
			}
		}).start();
		new Thread(() -> {
			synchronized (o2) {
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("o2 --> o1");
				synchronized (o1) {

				}
			}
		}).start();
	}
}
