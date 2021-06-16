package com.hyd.proxy;

import com.hyd.proxy.staticState.OrderServiceImpl;
import com.hyd.proxy.staticState.OrderServiceProxy;

public class Client {
    public static void main(String[] args) {
        OrderServiceProxy serviceProxy = new OrderServiceProxy(new OrderServiceImpl());
        serviceProxy.order();
    }
}
