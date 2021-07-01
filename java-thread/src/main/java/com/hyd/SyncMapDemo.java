package com.hyd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncMapDemo {
	private static Map<String, Integer> hashTable = null;
	private static Map<String, Integer> synchronizedMap = null;
	private static Map<String, Integer> concurrentHashMap = null;
	public static void main(String[] args) {
		hashTable = new Hashtable<>();
		synchronizedMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		concurrentHashMap = new ConcurrentHashMap<String, Integer>();
		SyncMapDemo.exec(hashTable);
		SyncMapDemo.exec(synchronizedMap);
		SyncMapDemo.exec(concurrentHashMap);
	}
	public static void exec(Map<String, Integer> map){
		System.out.println(map.getClass().getSimpleName());
		long averageTime = 0;
		//五次，取平均值
		for(int k=0;k<5;k++){
			ExecutorService service = Executors.newFixedThreadPool(5);
			long startTime = System.nanoTime();
			//5个线程
			for(int j=0;j<5;j++){
				service.execute(()->{
					//执行500万读取
					for(int i=0;i<5000000;i++){
						Integer num = (int)Math.random()*100000;
						map.put(String.valueOf(num), num);
						map.get(String.valueOf(num));
					}
				});
			}
			service.shutdown();
			long endTime = System.nanoTime();
			long totalTime = (endTime-startTime);
			averageTime +=totalTime;
			System.out.println("500万次读取 -->totalTime: "+totalTime);
		}
		System.out.println("averageTime: "+averageTime/5+"ns");

	}
}
