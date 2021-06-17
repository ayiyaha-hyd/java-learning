package com.hyd;

import com.hyd.staticState.service.OrderServiceImpl;
import com.hyd.staticState.proxy.OrderServiceProxy;

public class Client {
    public static void main(String[] args) {
        OrderServiceProxy serviceProxy = new OrderServiceProxy(new OrderServiceImpl());
        serviceProxy.order();
    }
}
