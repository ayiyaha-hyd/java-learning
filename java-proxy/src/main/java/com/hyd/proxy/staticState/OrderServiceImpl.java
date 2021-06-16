package com.hyd.proxy.staticState;

public class OrderServiceImpl implements OrderService{
    @Override
    public void order() {
        System.out.println("下订单");
    }
}
