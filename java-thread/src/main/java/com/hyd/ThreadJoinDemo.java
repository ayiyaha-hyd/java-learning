package com.hyd;

/**
 * 有三个线程T1,T2,T3,如何保证顺序执行
 */
public class ThreadJoinDemo {
	public static void main(String[] args) {
		Thread t1 = new Thread(() -> {
			System.out.println("t1 ...");
		});
		Thread t2 = new Thread(() -> {
			try {
				//引用t1线程，等待t1线程执行完
				t1.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("t2 ...");
		});
		Thread t3 = new Thread(() -> {
			try {
				// 引用t2线程，等待t2线程执行完
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("t3 ...");
		});
		t3.start();
		t2.start();
		t1.start();
	}
}
