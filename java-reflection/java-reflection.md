# java反射

---
## 一、什么是反射
Java反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。

本质是JVM得到class对象之后，再通过class对象进行反编译，从而获取对象的各种信息。

Java属于先编译再运行的语言，程序中对象的类型在编译期就确定下来了，而当程序在运行时可能需要动态加载某些类，这些类因为之前用不到，所以没有被加载到JVM。通过反射，可以在运行时动态地创建对象并调用其属性，不需要提前在编译期知道运行的对象是谁。

## 二、反射的原理
**原理**  
Class类对象是方法区中关于类的原数据的一个访问入口。  
通过访问类加载之后存放到堆中的Class对象，来获取一个类的方法、变量、接口、类名、类修饰符等信息。

Class对象总结：  
（1）Class也是类，继承于Object类。  
（2）Class对象不是new出来的，而是系统创建的。  
（3）对于某个类的Class对象，在内存中只有一份，因为类加载只进行一次，多线程环境下是线程安全的。  
（4）每个类的实例都会记得自己是由哪个Class实例所生成。  
（5）通过Class对象可以完整的得到一个类的完整结构，通过一系列API。  
（6）Class对象是存放在堆的。  
类的二进制字节码数据是放在方法区的。

## 三、反射的功能
（1）在运行时判断任意一个对象所属的类。  
（2）在运行时构造任意一个类的对象。  
（3）在运行时判断任意一个类所具有的成员变量和方法。  
（4）在运行时调用任意一个对象的方法。  
（5）生成动态代理。

## 四、反射机制的意义（为什么要有反射）
见反射的优点和使用场景。

## 五、反射的基本使用
（1）创建对象的两种方式  
第一种，new 对象。  
第二种，通过反射创建。
```java
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
```
（2）获取Class对象的三种方式

Java提供了三种方式获取Class对象，一种是使用.class，另外一种是使用Class.forName()，还有对象.getClass()。
.class方式适用于在编译时已经知道具体的类。
```java
//1.通过类的全限定名字符串
Class<?> clazz1 = Class.forName("java.lang.String");
//2.通过类的class属性
Class<String> clazz2 = String.class;
//3.通过对象的getClass()方法（Object超类方法）
Class<? extends String> clazz3 = new String().getClass();
```
通过这三种方式获取的一个类的Class对象是同一个，因为类只会加载一次，对应的Class对象在类加载阶段存放到堆当中。
```java
//查看是否是同一个Class对象，输出结果一致
System.out.println(clazz1.hashCode());
System.out.println(clazz2.hashCode());
System.out.println(clazz3.hashCode());
System.out.println(clazz1 == clazz2);
System.out.println(clazz1 == clazz3);
System.out.println(clazz2 == clazz3);
```
输出结果
```
685325104
685325104
685325104
true
true
true
```
（3）java.lang.Class 类
```java
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
```

（4)java.lang.reflect.Field类
```java
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
```

(5)java.lang.reflect.Method类
```java
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
```

(6)java.lang.reflect.Constructor类
```java
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
```

(7)生成实例对象
```java
        //生成无参构造函数实例对象（可以转User类型）
        Object user0 = clazz.newInstance();
        //生成有参构造函数实例对象（可以转User类型）
        Object user1 = clazzConstructor.newInstance("Jerry", 10);
        //访问私有构造函数
        Constructor clazzDeclaredConstructor = clazz.getDeclaredConstructor(String.class);
        clazzDeclaredConstructor.setAccessible(true);
        Object user2 = clazzDeclaredConstructor.newInstance("female");
        System.out.println(user2);
```

(8)成员变量取值赋值
```java
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
```

(9)反射调用方法
```java
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
```





（10）反射的执行速度  
反射基于解释执行，对执行速度有影响
```java
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
```
（11）通过反射越过泛型检查  
泛型用在编译期，编译过后泛型擦除（消失掉），所以是可以通过反射越过泛型检查的
```java
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
```
输出结果：
```
11
22
aaa
true
java.lang.Object@28d93b30
3.14159
```

## 四、反射的优缺点
### 优点
在运行时获得类的各种内容，进行反编译，对于Java这种先编译再运行的语言，能够让我们很方便的创建灵活的代码，这些代码可以在运行时装配，无需在组件之间进行源代码的链接，更加容易实现面向对象。
### 缺点
（1）反射会消耗一定的系统资源，因此，如果不需要动态地创建一个对象，那么就不需要用反射。  
（2）反射调用方法时可以忽略权限检查，因此可能会破坏封装性而导致安全问题。

## 五、反射的使用场景
Java在编译时候就必须知道所引用的类所在地方，但是在实际编程中，在某些场合，可能需要引用一个并不在编译空间的类，这个时候常规方法就很难实现了。在Java中，Class配合反射能够很好的解决这种场景。Java里面的反射可以帮助我们在运行程序时候加载、使用编译期间完全未知的class，简单来说就是Java可以加载一个运行时候才得知名称的class，获得其完整的构造，并生成实例化对象，对其成员变量赋值，调用其方法等等。

在具体的研发中，通过反射获取类的实例，大大提高系统的灵活性和扩展性，同时由于反射的性能较低，而且它极大的破坏了类的封装性(通过反射获取类的私有方法和属性)，在大部分场景下并不适合使用反射，但是在大型的一些框架中，会大范围使用反射来帮助架构完善一些功能。

（1）数据库连接和事务管理。例如Spring框架有一个事务代理，可以启动和提交/回滚事务。  
（2）用于单元测试的动态模拟对象。  
（3）类似AOP的方法拦截。  
（4）JDBC利用反射将数据库的表字段映射到java对象的getter/setter方法。  
（5）Jackson, GSON, Boon等类库也是利用反射将JSON文件的属性映射到java对的象getter/setter方法。  
（6）反编译：.class --> .java。  
（7）当我们在使用IDE,比如Eclipse、IDEA时，当我们输入一个对象或者类，并想调用他的属性和方法是，一按点号，编译器就会自动列出他的属性或者方法，这里就是用到反射。  
（8）反射最重要的用途就是开发各种通用框架。比如很多框架（Spring）都是配置化的（比如通过XML文件配置Bean），为了保证框架的通用性，他们可能需要根据配置文件加载不同的类或者对象，调用不同的方法，这个时候就必须使用到反射了，运行时动态加载需要的加载的对象，通过反射运行配置文件内容。  
（9）通过反射越过泛型检查。泛型用在编译期，编译过后泛型擦除（消失掉），所以是可以通过反射越过泛型检查的.


## 七、总结

---

参考链接：  
[Java_Basic_Introduction/动态代理.md at master · JackChan1999/Java_Basic_Introduction · GitHub](https://github.com/JackChan1999/Java_Basic_Introduction/blob/master/第10章 反射机制/动态代理.md)  
[Java_Basic_Introduction/反射.md at master · JackChan1999/Java_Basic_Introduction · GitHub](https://github.com/JackChan1999/Java_Basic_Introduction/blob/master/第10章 反射机制/反射.md)  
[大白话说Java反射：入门、使用、原理 - 陈树义 - 博客园 (cnblogs.com)](https://www.cnblogs.com/chanshuyi/p/head_first_of_reflection.html)
---
