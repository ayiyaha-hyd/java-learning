package com.hyd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者案例
 */
public class ProducerAndConsumerDemo {
    public static void main(String[] args) {
//        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.execute(()->{
            while (true){
                Resource.getInstance().consume();
        }
        });
        threadPool.execute(()->{
            while (true){
                Resource.getInstance().produce();
            }
        });
        threadPool.shutdown();
    }
}

class Resource{
    private static Resource instance;
    private Resource(){}
    public static Resource getInstance(){
        if(instance == null){
            synchronized (Resource.class){
                if(instance == null){
                    instance = new Resource();
                }
            }
        }
        return instance;
    }

    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Condition con_producer = lock.newCondition();
    private Condition con_consumer = lock.newCondition();

    //供生产者调用的代码
    public void produce(){
        lock.lock();
        try {
            while (count>0){
                con_producer.await();
            }
            while (count>=0 && count<5){
                System.out.println("before: "+count);
                count++;
                System.out.println(Thread.currentThread().getName()+"...生产...: "+count);
            }
            con_consumer.signal();
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

    //
    public void consume(){
        lock.lock();
        try {
            while (count == 0){
                con_consumer.await();
            }
            while (count >0 && count<=5){
                System.out.println("before: "+count);
                count--;
                System.out.println(Thread.currentThread().getName()+"...消费..."+count);
            }
            con_producer.signal();
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

}
