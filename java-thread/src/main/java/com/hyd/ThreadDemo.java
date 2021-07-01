package com.hyd;

import java.util.ArrayList;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * 实现多线程的方式：
 * 继承Thread，重写run方法
 */
class MyThread extends Thread{
	@Override
	public void run(){
		System.out.println("myThread run ...");
	}

	public static void main(String[] args) {
		new MyThread().start();
	}

}

/**
 * 实现多线程方式：
 * 实现Runnable接口，重写run方法
 */
class MyThread2 implements Runnable{
	@Override
	public void run() {
		System.out.println("myThread2 run ...");
	}

	public static void main(String[] args) {
		MyThread2 target = new MyThread2();
		new Thread(target).start();
		new Thread(target).start();
	}
}

/**
 * 实现多线程方式：
 * Callable，借助线程池submit()返回Future结果
 */
class MyThread3 implements Callable<Integer>{
	private static int i = 0;
	@Override
	public synchronized Integer  call() throws Exception {
		System.out.println("myThread3 run ..."+i);
		return i++;
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ExecutorService pool = newCachedThreadPool();
		MyThread3 target = new MyThread3();
		ArrayList<Future<Integer>> result = new ArrayList<>();
		for(int i=0;i<10;i++){
			result.add(pool.submit(target));
		}
		for (Future<Integer> res : result) {
			if(res.isDone()){
				System.out.println(res.get());
			}else {
				System.out.println("task haven't finished");
			}
		}
		pool.shutdown();
	}
}

/**
 * 实现多线程方式：
 *
 */
class MyThread4 implements Callable<Integer>{
private static int i = 0;
	@Override
	public synchronized Integer call() throws Exception {
		System.out.println("myThread4 run ..."+i);
		return i++;
	}

	public static void main(String[] args) {
		ExecutorService pool = newCachedThreadPool();
		MyThread4 target = new MyThread4();
		//FutureTask在高并发下确保任务只执行一次
		FutureTask<Integer> futureTask = new FutureTask<>(target);
		pool.submit(futureTask);
		pool.submit(futureTask);
		pool.submit(futureTask);
		pool.shutdown();
	}
}
public class ThreadDemo {
	public static void main(String[] args) {

	}
}
