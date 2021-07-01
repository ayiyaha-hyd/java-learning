package com.hyd;

import java.util.concurrent.*;

/**
 * 线程池优点：
 * 重用线程，减少开销
 * 控制并发，可周期执行
 * ThreadPoolExecutor-extends-> AbstractExecutorService -implements-> ExecutorService -extends-> Executor
 */
public class ThreadPoolDemo {
    public static void main(String[] args) {
        ThreadPoolDemo t = new ThreadPoolDemo();
//        t.pool1();
//        t.pool2();
//        t.pool3();
//        t.pool4();
        t.pool5();

    }

    /**
     * newFixedThreadPool--固定线程数量的线程池
     * 超出的线程在无界队列中等待
     */
    public void pool1() {
        ExecutorService service1 = Executors.newFixedThreadPool(4);
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            try {
                System.out.println(Thread.currentThread().getName() + ":" + index);
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service1.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "......" + index);
            });
        }
        service1.shutdown();
    }

    /**
     * newCachedThreadPool--可缓存的线程池
     * 如果当前存在可用线程，则复用线程，不存在可用线程，创建新线程，规定时间内未使用的线程会被删除。
     */
    public void pool2() {
        ExecutorService service1 = Executors.newCachedThreadPool();
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            try {
                System.out.println(Thread.currentThread().getName() + ":" + index);
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service1.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "......" + index);
            });
        }
        service1.shutdown();
    }

    /**
     * newSingleThreadExecutor--单工作线程的线程池
     * 使用单工作线程处理无界队列中的线程任务
     * 等效于newFixedThreadPool(1)
     */
    public void pool3() {
        ExecutorService service1 = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            try {
                System.out.println(Thread.currentThread().getName() + ":" + index);
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service1.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "......" + index);
            });
        }
        service1.shutdown();
    }

    /**
     * newScheduledThreadPool--固定线程数量的线程池，支持周期性任务
     */
    public void pool4() {
        ExecutorService service1 = Executors.newScheduledThreadPool(4);
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            try {
                System.out.println(Thread.currentThread().getName() + ":" + index);
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service1.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "......" + index);
            });
        }
        service1.shutdown();
    }

    /**
     * 手动创建线程池 ThreadPoolExecutor
     */
    public void pool5() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory());
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            try {
                System.out.println(Thread.currentThread().getName() + ":" + index);
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "......" + index);
            });
        }
        executor.shutdown();
    }
}
/*
Java中的BlockingQueue主要有两种实现，分别是ArrayBlockingQueue 和 LinkedBlockingQueue。

ArrayBlockingQueue是一个用数组实现的有界阻塞队列，必须设置容量。

LinkedBlockingQueue是一个用链表实现的有界阻塞队列，容量可以选择进行设置，不设置的话，将是一个无边界的阻塞队列，最大长度为Integer.MAX_VALUE。

这里的问题就出在：不设置的话，将是一个无边界的阻塞队列，最大长度为Integer.MAX_VALUE。也就是说，如果我们不设置LinkedBlockingQueue的容量的话，其默认容量将会是Integer.MAX_VALUE。

而newFixedThreadPool中创建LinkedBlockingQueue时，并未指定容量。此时，LinkedBlockingQueue就是一个无边界队列，对于一个无边界队列来说，是可以不断的向队列中加入任务的，这种情况下就有可能因为任务过多而导致内存溢出问题。

上面提到的问题主要体现在newFixedThreadPool和newSingleThreadExecutor两个工厂方法上，并不是说newCachedThreadPool和newScheduledThreadPool这两个方法就安全了，这两种方式创建的最大线程数可能是Integer.MAX_VALUE，而创建这么多线程，必然就有可能导致OOM。

创建线程池的正确姿势
避免使用Executors创建线程池，主要是避免使用其中的默认实现，那么我们可以自己直接调用ThreadPoolExecutor的构造函数来自己创建线程池。在创建的同时，给BlockQueue指定容量就可以了。

private static ExecutorService executor = new ThreadPoolExecutor(10, 10,
        60L, TimeUnit.SECONDS,
        new ArrayBlockingQueue(10));
这种情况下，一旦提交的线程数超过当前可用线程数时，就会抛出java.util.concurrent.RejectedExecutionException，这是因为当前线程池使用的队列是有边界队列，队列已经满了便无法继续处理新的请求。但是异常（Exception）总比发生错误（Error）要好。
 */