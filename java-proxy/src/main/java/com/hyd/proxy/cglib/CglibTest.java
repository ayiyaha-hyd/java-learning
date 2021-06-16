package com.hyd.proxy.cglib;

import com.hyd.proxy.cglib.service.LoginService;
import com.hyd.proxy.cglib.interceptor.MyMethodInterceptor;
import net.sf.cglib.proxy.Enhancer;

public class CglibTest {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(LoginService.class);
        enhancer.setCallback(new MyMethodInterceptor());
        LoginService loginServiceProxy = (LoginService)enhancer.create();
        loginServiceProxy.login();
        loginServiceProxy.logout();


    }
}
