package com.hyd.proxy.staticState;

public class OrderServiceProxy implements OrderService {
    /**
     * 被代理的对象
     */
    private OrderService orderService;

    public OrderServiceProxy(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 实现静态代理
     */
    @Override
    public void order() {
        beforeOrder();
        orderService.order();
        afterOrder();
    }

    public void beforeOrder() {
        System.out.println("开始记录日志...");

    }

    public void afterOrder() {
        System.out.println("结束记录日志...");

    }
}
