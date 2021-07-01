# 为什么重写equals还要重写hashcode

---

为什么重写equals还要重写hashcode？

equals和hashcode间的关系：

1.如果两个对象相同（即equals比较返回true），那么他们的hashcode一定要相等

2.如果他们的hashcode相等，他们的equals不一定相等

3.如果hashcode不等，则他们的equals一定不等。


关于equals方法很明确的是用于比较两个对象是否相等。而对于hashCode方法重点是为了在类似HashMap场景下提升效率，毕竟毕竟hash效率高，只算是技术要求。

由于为了提高程序的效率才实现了hahscode方法，先进行hashcode比较，如果不同就没必要进行equals比较了，这样大大减少了equals比较的次数，当比较的数量大的时候提高的效率就很可观