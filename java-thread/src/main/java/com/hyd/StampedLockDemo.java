package com.hyd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock使用示例
 * @author PIG
 */
public class StampedLockDemo {
	private StampedLock stampedLock = new StampedLock();
	private int x = 1;
	private int y = 1;

	public void read() {
		/* 获取乐观读锁 */
		long stamp = stampedLock.tryOptimisticRead();
		int curX = x;
		int curY = y;
		System.out.println(Thread.currentThread().getName() + "当前读取的值 x:" + x + ",y:" + y);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/* 检查乐观读锁后，是否有其它写锁发生 */
		if (!stampedLock.validate(stamp)) {
			/* 获取悲观读锁 */
			stamp = stampedLock.readLock();
			System.out.println(Thread.currentThread().getName() + " 读取过程中，有写入新值...");
			try {
				/* 获取最新值 */
				curX = x;
				curY = y;
				System.out.println(Thread.currentThread().getName() + " 重新读取值 x:" + x + ",y:" + y);
			} finally {
				/* 释放悲观读锁 */
				stampedLock.unlockRead(stamp);
			}
		}

	}

	public void write(int x, int y) {
		/* 获取写锁 */
		long stamp = stampedLock.writeLock();
		this.x = x;
		this.y = y;
		System.out.println(Thread.currentThread().getName()+"写值 x:"+x+",y:"+y);
		/* 释放写锁 */
		stampedLock.unlockWrite(stamp);
	}

	public static void main(String[] args) {
		StampedLockDemo target = new StampedLockDemo();
		ExecutorService service = Executors.newCachedThreadPool();
			service.execute(target::read);
			service.execute(() -> {
				target.write(2,3);
			});

		service.shutdown();
	}
}
