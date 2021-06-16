# java代理

---

## 动态代理
```java
public interface com.hyd.demo.reflect.dynamic.UserDao {
    void add();

    void delete();

    void update();

    void find();
}
```

```java
import com.hyd.demo.reflect.dynamic.UserDao;

public class UserDaoImpl implements UserDao {
    @Override
    public void add() {
        System.out.println("添加功能");
    }

    @Override
    public void delete() {
        System.out.println("删除功能");
    }

    @Override
    public void update() {
        System.out.println("修改功能");
    }

    @Override
    public void find() {
        System.out.println("查找功能");
    }
}
```

```java
public class com.hyd.demo.reflect.dynamic.MyInvocationHandler implements InvocationHandler {
    private Object target;

    public com.hyd.demo.reflect.dynamic.MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("***权限校验***");
        Object result = method.invoke(target, args);
        System.out.println("***日志记录***");
        return result;
    }
}
```

```java
import com.hyd.demo.reflect.dynamic.UserDao;

public class ProxyTest {
    public static void main(String[] args) throws Exception {
        System.out.println("---不使用代理---");
        UserDao userService = new com.hyd.demo.reflect.dynamic.UserDaoImpl();
        userService.add();
        userService.delete();
        userService.update();
        userService.find();

        System.out.println("---使用代理对象---");
        com.hyd.demo.reflect.dynamic.MyInvocationHandler handler = new com.hyd.demo.reflect.dynamic.MyInvocationHandler(userService);
        UserDao proxyUserService = (UserDao) Proxy.newProxyInstance(userService.getClass().getClassLoader(), userService.getClass().getInterfaces(), handler);
        proxyUserService.add();
        proxyUserService.delete();
        proxyUserService.update();
        proxyUserService.find();

        System.out.println("---使用代理对象 反射完整版（假装从其他位置反射调用相关类）---");
        //handler Class
        Class<?> handlerClass = Class.forName("com.hyd.demo.reflect.dynamic.MyInvocationHandler");
        //实现类Class
        Class<?> userDaoImplClass = Class.forName("com.hyd.demo.reflect.dynamic.UserDaoImpl");

        //实例化handler
        InvocationHandler invocationHandler = (InvocationHandler) handlerClass
                .getConstructor(Object.class)
                .newInstance(userDaoImplClass.newInstance());

        //接口Class
        Class<?> userDaoClass = Class.forName("com.hyd.demo.reflect.dynamic.UserDao");

        //创建代理对象实例
        Object proxyInstance = Proxy.newProxyInstance(
                //类加载器
                userDaoClass.getClassLoader(),
                //调用类的接口
                new Class[]{userDaoClass},
                invocationHandler);

        //获取方法
        Method add = userDaoClass.getDeclaredMethod("add");
        Method delete = userDaoClass.getMethod("delete");
        Method update = userDaoClass.getMethod("update");
        Method find = userDaoClass.getMethod("find");

        //代理对象调用目标方法
        add.invoke(proxyInstance);
        delete.invoke(proxyInstance);
        update.invoke(proxyInstance);
        find.invoke(proxyInstance);

    }
}
```
控制台输出结果：
```
---不使用代理---
添加功能
删除功能
修改功能
查找功能
---使用代理对象---
***权限校验***
添加功能
***日志记录***
***权限校验***
删除功能
***日志记录***
***权限校验***
修改功能
***日志记录***
***权限校验***
查找功能
***日志记录***
---使用代理对象 反射完整版（假装从其他位置反射调用相关类）---
***权限校验***
添加功能
***日志记录***
***权限校验***
删除功能
***日志记录***
***权限校验***
修改功能
***日志记录***
***权限校验***
查找功能
***日志记录***
```

---
## 静态代理


---
## cglib动态代理
a(){b()}分析
Cglib源码分析 invoke和invokeSuper的差别
循环调用，一直到栈溢出报错。所以，invoke会造成OOM的问题。

https://www.cnblogs.com/lvbinbin2yujie/p/11135396.html#cglib--invoke%E4%B8%BA%E4%BB%80%E4%B9%88%E4%BC%9A%E6%AD%BB%E5%BE%AA%E7%8E%AF

https://blog.csdn.net/MakeContral/article/details/79593732
在Spring的AOP编程中:
如果加入容器的目标对象有实现接口,用JDK代理
如果目标对象没有实现接口,用Cglib代理   
---