# java动态代理

---

动态代理
```java
public interface UserDao {
    void add();

    void delete();

    void update();

    void find();
}
```

```java
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
public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    public MyInvocationHandler(Object target) {
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
public class ProxyTest {
    public static void main(String[] args) throws Exception {
        System.out.println("---不使用代理---");
        UserDao userDao = new UserDaoImpl();
        userDao.add();
        userDao.delete();
        userDao.update();
        userDao.find();

        System.out.println("---使用代理对象---");
        MyInvocationHandler handler = new MyInvocationHandler(userDao);
        UserDao proxyUserDao = (UserDao) Proxy.newProxyInstance(userDao.getClass().getClassLoader(), userDao.getClass().getInterfaces(), handler);
        proxyUserDao.add();
        proxyUserDao.delete();
        proxyUserDao.update();
        proxyUserDao.find();

        System.out.println("---使用代理对象 反射完整版（假装从其他位置反射调用相关类）---");
        //handler Class
        Class<?> handlerClass = Class.forName("MyInvocationHandler");
        //实现类Class
        Class<?> userDaoImplClass = Class.forName("UserDaoImpl");

        //实例化handler
        InvocationHandler invocationHandler = (InvocationHandler) handlerClass
                .getConstructor(Object.class)
                .newInstance(userDaoImplClass.newInstance());

        //接口Class
        Class<?> userDaoClass = Class.forName("UserDao");

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