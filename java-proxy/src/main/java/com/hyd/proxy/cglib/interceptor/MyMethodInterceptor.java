package com.hyd.proxy.cglib.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        beforeIntercept();
//        Object result = methodProxy.invoke(obj, args);
        Object result = methodProxy.invokeSuper(obj, args);
        afterIntercept();
        return result;
    }

    public void beforeIntercept(){
        System.out.println("**权限校验**");
    }
    public void afterIntercept(){
        System.out.println("**日志记录**");

    }
}
