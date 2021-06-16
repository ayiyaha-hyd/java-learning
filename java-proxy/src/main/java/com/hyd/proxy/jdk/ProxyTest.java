package com.hyd.proxy.jdk;

import com.hyd.proxy.jdk.service.UserService;
import com.hyd.proxy.jdk.service.impl.UserServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) throws Exception {
        System.out.println("---不使用代理---");
        UserService userService = new UserServiceImpl();
        userService.add();
        userService.delete();
        userService.update();
        userService.find();

        System.out.println("---使用代理对象---");
        MyInvocationHandler handler = new MyInvocationHandler(userService);
        UserService proxyUserService = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(), userService.getClass().getInterfaces(), handler);
        proxyUserService.add();
        proxyUserService.delete();
        proxyUserService.update();
        proxyUserService.find();

        System.out.println("---使用代理对象 反射完整版（假装从其他位置反射调用相关类）---");
        //handler Class
        Class<?> handlerClass = Class.forName("com.hyd.proxy.jdk.MyInvocationHandler");
        //实现类Class
        Class<?> userServiceImplClass = Class.forName("com.hyd.proxy.jdk.service.impl.UserServiceImpl");

        //实例化handler
        InvocationHandler invocationHandler = (InvocationHandler) handlerClass
                .getConstructor(Object.class)
                .newInstance(userServiceImplClass.newInstance());

        //接口Class
        Class<?> userServiceClass = Class.forName("com.hyd.proxy.jdk.service.UserService");

        //创建代理对象实例
        Object proxyInstance = Proxy.newProxyInstance(
                //类加载器
                userServiceClass.getClassLoader(),
                //调用类的接口
                new Class[]{userServiceClass},
                invocationHandler);

        //获取方法
        Method add = userServiceClass.getDeclaredMethod("add");
        Method delete = userServiceClass.getMethod("delete");
        Method update = userServiceClass.getMethod("update");
        Method find = userServiceClass.getMethod("find");

        //代理对象调用目标方法
        add.invoke(proxyInstance);
        delete.invoke(proxyInstance);
        update.invoke(proxyInstance);
        find.invoke(proxyInstance);

    }
}
