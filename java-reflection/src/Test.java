import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws Exception {
//        t1();
        t2();
//        t3();
//        t4();
//        t5();
    }

    public static void t1() throws Exception {
        //创建对象的两种方式
        //1.通过new 创建对象
        User user1 = new User();
        user1.show();
        System.out.println("---");
        //2.通过反射创建对象
        Class<?> clazz2 = Class.forName("User");
        Object user2 = clazz2.newInstance();
        Method show = clazz2.getMethod("show");
        show.invoke(user2);
        //3.通过实现Cloneable接口，覆写clone方法
        User user3 = (User) user1.clone();
        user3.show();
        //4.反射构造器
        Constructor<User> constructor = User.class.getConstructor();
        User user4 = constructor.newInstance();
        user4.show();

    }

    public static void t2() throws Exception {
        //1.通过类的全限定名字符串
        Class<?> clazz1 = Class.forName("java.lang.String");
        //2.通过类的class属性
        Class<String> clazz2 = String.class;
        //3.通过调用运行时对象的getClass()方法（Object超类方法）
        Class<? extends String> clazz3 = new String().getClass();
        //4.类加载器classloader
        ClassLoader classLoader = Test.class.getClassLoader();
        Class<?> clazz4 = classLoader.loadClass("java.lang.String");

        //查看是否是同一个Class对象，输出结果一致
        System.out.println(clazz1.hashCode());
        System.out.println(clazz2.hashCode());
        System.out.println(clazz3.hashCode());
        System.out.println(clazz4.hashCode());
        System.out.println(clazz1 == clazz2);
        System.out.println(clazz1 == clazz3);
        System.out.println(clazz2 == clazz3);
    }

    public static void t3() throws Exception {
        long start;
        long end;

        //1.new对象，调用方法
        Date date1 = new Date();
        start = System.currentTimeMillis();
        for (int i = 0; i <= 999999; i++) {
            date1.getTime();
        }
        end = System.currentTimeMillis();
        System.out.println("普通调用 耗时" + (end - start) + "毫秒");

        //2.反射调用方法
        Class<?> clazz2 = Class.forName("java.util.Date");
        Object date2 = clazz2.newInstance();
        Method getTime2 = clazz2.getMethod("getTime");
        start = System.currentTimeMillis();
        for (int i = 0; i <= 999999; i++) {
            getTime2.invoke(date2);
        }
        end = System.currentTimeMillis();
        System.out.println("反射 耗时" + (end - start) + "毫秒");

        //反射关闭访问检查，调用方法
        Class<?> clazz3 = Class.forName("java.util.Date");
        Object date3 = clazz3.newInstance();
        Method getTime3 = clazz3.getMethod("getTime");
        getTime3.setAccessible(true);//禁用访问检查
        start = System.currentTimeMillis();
        for (int i = 0; i <= 999999; i++) {
            getTime3.invoke(date2);
        }
        end = System.currentTimeMillis();
        System.out.println("反射关闭访问检查 耗时" + (end - start) + "毫秒");
    }

    public static void t4() throws Exception {
        Class clazz = User.class;

        //获取全限定类名（包名+类名）
        String clazzName = clazz.getName();
        //获取类名
        String clazzSimpleName = clazz.getSimpleName();


        //获取public修饰的成员变量（子类和父类 public 修饰的属性字段）
        Field[] clazzFields = clazz.getFields();
        //获取本类中所有成员变量（public,private等所有属性字段）
        Field[] clazzDeclaredFields = clazz.getDeclaredFields();


        //获取public修饰的方法（子类和父类 public 修饰的方法）
        Method[] methods = clazz.getMethods();
        //获取本类所有方法（public,private等所有修饰符修饰的方法）
        Method[] clazzDeclaredMethods = clazz.getDeclaredMethods();


        //获取本类中public修饰的构造器（本类public 修饰的构造器）
        Constructor[] clazzConstructors = clazz.getConstructors();
        //获取本类中所有构造器（public,private等所有构造函数）
        Constructor[] clazzDeclaredConstructors = clazz.getDeclaredConstructors();


        //获取父类Class对象
        Class clazzSuperclass = clazz.getSuperclass();
        //获取注解
        Annotation[] clazzAnnotations = clazz.getAnnotations();
        //获取接口信息
        Class[] clazzInterfaces = clazz.getInterfaces();
        //获取包名
        Package clazzPackage = clazz.getPackage();


        //获取修饰符（可以获取类、接口、方法、字段、构造函数的修饰符）
        int clazzModifiers = clazz.getModifiers();
        //输出修饰符
        String modifiderStr = Modifier.toString(clazzModifiers);
        //判断修饰符类型
        boolean isPublic = Modifier.isPublic(clazzModifiers);








        Field field = clazz.getDeclaredField("gender");
        //设置访问检查（true关闭访问检查）
        field.setAccessible(true);
        //获取属性名称
        String name = field.getName();
        //获取属性字段类型
        Class<?> type = field.getType();
        //获取修饰符
        int modifiers = field.getModifiers();
        //获取属性注解
        Annotation[] annotations = field.getAnnotations();



        Method method = clazz.getMethod("show2", int.class);
        //设置安全检查
        method.setAccessible(true);
        //获取方法名
        String methodName = method.getName();
        //获取参数Class类型
        Class<?>[] methodParameterTypes = method.getParameterTypes();
        //获取返回值类型
        Class<?> methodReturnType = method.getReturnType();
        //获取参数
        Parameter[] methodParameters = method.getParameters();
        //获取访问修饰符
        int methodModifiers = method.getModifiers();




        //获取具体构造函数
        Constructor clazzConstructor = clazz.getConstructor(String.class, int.class);
        //设置安全检查
        clazzConstructor.setAccessible(true);
        //获取构造函数参数类型
        Class[] constructorParameterTypes = clazzConstructor.getParameterTypes();
        //获取构造函数参数
        Parameter[] clazzConstructorParameters = clazzConstructor.getParameters();
        //获取构造函数修饰符
        int clazzConstructorModifiers = clazzConstructor.getModifiers();



        //生成无参构造函数实例对象（可以转User类型）
        Object user0 = clazz.newInstance();
        //生成有参构造函数实例对象（可以转User类型）
        Object user1 = clazzConstructor.newInstance("Jerry", 10);
        //访问私有构造函数
        Constructor clazzDeclaredConstructor = clazz.getDeclaredConstructor(String.class);
        clazzDeclaredConstructor.setAccessible(true);
        Object user2 = clazzDeclaredConstructor.newInstance("female");
        System.out.println(user2);


        //成员变量赋值取值
        Field age = clazz.getField("age");
        System.out.println(age.get(user0));
        age.set(user0, 6);
        Object o = age.get(user0);
        System.out.println(o);
        //访问私有变量，赋值，取值
        Field male = clazz.getDeclaredField("gender");
        male.setAccessible(true);//true为禁用安全检查
        male.set(user0, "female");
        Object o1 = male.get(user0);
        System.out.println(o1);



        //调用普通方法
        Method show1 = clazz.getMethod("show1", String.class);
        show1.invoke(user0, "running ...");
        //调用静态方法
        Method sleep = clazz.getMethod("sleep", String.class, int.class);
        sleep.invoke(null, "hei", 2);
        //访问私有方法
        Method show2 = clazz.getDeclaredMethod("show2", int.class);
        show2.setAccessible(true);
        show2.invoke(user0, 20);


    }

    public static void t5() throws Exception {
        /*
        Generics
        通过反射越过泛型检查
        泛型用在编译期，编译过后泛型擦除（消失掉），所以是可以通过反射越过泛型检查的。
         */

        //
        ArrayList<Integer> list = new ArrayList<>();
        list.add(11);
        list.add(22);

        //list.add("haha");//编译期会报错

        Class<? extends ArrayList> listClass = list.getClass();
        Method add = listClass.getMethod("add", Object.class);

        //通过反射在运行期间调用
        add.invoke(list, "aaa");
        add.invoke(list, true);
        add.invoke(list, new Object());
        add.invoke(list, 3.14159);

        for (Object item : list) {
            System.out.println(item);
        }
    }
}