package com.hyd;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWrite {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public void read() {
		lock.readLock().lock();
		if (!lock.isWriteLocked()) {
			System.out.println(Thread.currentThread().getName() + "当前为读锁");
		}
		try {
			for (int i = 0; i <= 5; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "......正在进行读操作...read");
			}
			System.out.println(Thread.currentThread().getName() + "读操作完毕！");
		} finally {
			System.out.println(Thread.currentThread().getName() + "释放读锁");
			lock.readLock().unlock();
		}

	}

	public void write() {
		lock.writeLock().lock();
		if (lock.isWriteLocked()) {
			System.out.println(Thread.currentThread().getName() + "当前为写锁");
		}
		try {
			for (int i = 0; i <= 5; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "......正在进行写操作...write");
			}
			System.out.println(Thread.currentThread().getName() + "写操作完毕！");
		} finally {
			System.out.println(Thread.currentThread().getName() + "释放写锁");
			lock.writeLock().unlock();
		}
	}
}

//练习读写锁操作
public class ReadWriteLockDemo {
	public static void main(String[] args) {
		ReadWrite r = new ReadWrite();
		for (int i = 0; i < 4; i++) {
			new Thread(() -> {
				r.read();
			}).start();
		}
		for (int i = 0; i < 4; i++) {
			new Thread(() -> {
				r.write();
			}).start();
		}
	}
}