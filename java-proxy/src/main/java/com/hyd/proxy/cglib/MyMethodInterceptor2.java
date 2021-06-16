package com.hyd.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor2  implements MethodInterceptor {

    private Object target;
    public MyMethodInterceptor2(Object target){
        this.target = target;
    }
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("hh");
        Object result = proxy.invoke(target, args);
        System.out.println("heihei");
        return result;
    }
}
