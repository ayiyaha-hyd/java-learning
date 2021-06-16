import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
