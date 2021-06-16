package com.hyd.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        checkPermission();
        Object result = method.invoke(target, args);
        logging();
        return result;
    }

    public void checkPermission(){
        System.out.println("***权限校验***");

    }
    public void logging(){
        System.out.println("***日志记录***");
    }

}
