package com.hyd;

/**
 * 通配符，泛型
 */
//Lev 1
class Food{}

//Lev 2
class Fruit extends Food{}
class Meat extends Food{}

//Lev 3
class Apple extends Fruit{}
class Banana extends Fruit{}
class Pork extends Meat{}
class Beef extends Meat{}

//Lev 4
class RedApple extends Apple{}
class GreenApple extends Apple{}

class Plate<T>{
	private T item;
	public Plate(T t){item=t;}
	public void set(T t){item=t;}
	public T get(){return item;}
}
public class WildcardsDemo {
	static class Generics{
		public static void main(String[] args) {

		}
	}

	/**
	 * 通配符
	 */
	static class Wildcards{
		public static void main(String[] args) {
			//Plate<Fruit> p=new Plate<Apple>(new Apple());//“装苹果的盘子” 无法转换成 “装水果的盘子”

			/* 能接收 Fruit及其子类 上界通配符 */
			Plate<? extends Fruit> p=new Plate<Apple>(new Apple());
			p = new Plate<Banana>(new Banana());
			p = new Plate<RedApple>(new RedApple());
			p = new Plate<GreenApple>(new GreenApple());

			//不能存入任何元素
			//p.set(new Fruit());//error
			//p.set(new Apple());//error

			//读取出来的东西只能存放在Fruit或它的基类里(向上转型，子类元素转父类元素)
			//Apple apple = p.get();//error
			Fruit fruit = p.get();
			Object object = p.get();//object超类也行

			/* 能接收 Meat及其父类 下界通配符 */
			Plate<? super Meat> p1 = new Plate<Meat>(new Meat());

			//存入元素正常(向下转型，父类元素转子类元素)
			p1.set(new Pork());
			p1.set(new Beef());
			p1.set(new Meat());

			//读取出来的东西只能存放在Object类里(但这样的话，元素的类型信息就全部丢失)
			//Beef beef = p1.get();//error
			//Meat meat = p1.get();/error
			Object object1 = p1.get();

			/*
			总结
			PECS原则

			获取数据(get)，适合上界Extends;
			插入数据(set), 适合下界Super;
			 */
		}
	}
}
