# HashMap底层原理

---
## 时间复杂度（查找，插入，删除）
时间复杂度：O(1)

## 存储结构
jdk1.7及以前：数组+链表；  
jdk1.8及以后：数组+链表/红黑树（链表长度大于8自动转红黑树）；  

HashMap 定义了一个哈希桶数组table，它是一个Node数组。
```java
transient Node<K,V>[] table;
```
Node是HashMap的一个内部类，实现了Map.Entry接口，本质是一个映射（键值对），也就是链表。
```java
    static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
    //...
}
```
链表长度大于8以后，转为红黑树，红黑树结点TreeNode父类同样实现了Map.Entry接口
```java
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K, V> parent;  // red-black tree links
    TreeNode<K, V> left;
    TreeNode<K, V> right;
    TreeNode<K, V> prev;    // needed to unlink next upon deletion
    boolean red;

    TreeNode(int hash, K key, V val, Node<K, V> next) {
        super(hash, key, val, next);
    }
    //...
}
```
## 重写hashcode和equals
内部类存储键值对数据的Node静态内部类结点重写了hashcode和equals方法  
hashcode
```java
        public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
```
equals
```java
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
```
在Java中，保存数据有两种比较简单的数据结构：数组和链表。数组的特点是：寻址容易，插入和删除困难；而链表的特点是：寻址困难，插入和删除容易。上面我们提到过，常用的哈希函数的冲突解决办法中有一种方法叫做链地址法，其实就是将数组和链表组合在一起，发挥了两者的优势，我们可以将其理解为链表的数组。  
HashMap就是使用哈希表来存储的。哈希表为解决冲突，可以采用开放地址法和链地址法等来解决问题，Java中HashMap采用了链地址法。链地址法，简单来说，就是数组加链表的结合。在每个数组元素上都一个链表结构，当数据被Hash后，得到数组下标，把数据放在对应下标元素的链表上。  
JDK8为什么引入红黑树？  
哈希碰撞，会带来链化，效率会变低  
引入红黑树会提高查找效率
## 解决哈希冲突
### 哈希算法
对任意一组输入数据进行计算，得到一个固定长度的输出摘要。  
相同的输入一定得到相同的输出； 不同的输入大概率得到不同的输出。  
通常要对数据取模，限制生成范围。
### 哈希冲突
由于哈希算法被计算的数据是无限的，而计算后的结果范围有限，因此总会存在不同的数据经过计算后得到的值相同，这就是哈希冲突。（两个不同的数据计算后的结果一样）
### 哈希冲突解决办法
#### 开放地址法（再散列法）
//todo
#### 链地址法
//todo
#### 再哈希法
//todo
## HashMap扩容机制

//todo 

## 确定哈希桶数组索引位置
不管查找，插入，还是删除，定位到哈希桶数组索引位置都是很关键的一步。  
**定位元素的hash()方法**
```java
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```

此处哈希算法通过key的hashCode和无符号右位移16位相与。  
因为32位int值，大部分hashCode有效位都在低16位，  
右位移16位，正好是32bit的一半，高半区和低半区做异或，就是为了混合原始哈希码的高位和低位，以此来加大低位的随机性。而且混合后的低位掺杂了高位的部分特征，这样高位的信息也被变相保留下来。

**获取角标位置的方法**
jdk1.7方法，1.8直接用的里边的表达式
```java
    static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        return h & (length-1);
    }
```
indexFor方法其实主要是将hash生成的整型转换成链表数组中的下标。那么return h & (length-1);是什么意思呢？其实，他就是取模。Java之所有使用位运算(&)来代替取模运算(%)，最主要的考虑就是效率。**位运算(&)效率要比代替取模运算(%)高很多，主要原因是位运算直接对内存数据进行操作，不需要转成十进制，因此处理速度非常快。  
为什么可以使用位运算(&)来实现取模运算(%)呢？  
X % 2^n = X & (2^n - 1)

2^n表示2的n次方，也就是说，一个数对2^n取模 == 一个数和(2^n - 1)做按位与运算 。  
所以，return h & (length-1);只要保证length的长度是2^n的话，就可以实现取模运算了。而HashMap中的length也确实是2的倍数，初始值是16，之后每次扩充为原来的2倍。


## HashMap插入(put方法)

put()方法  
```java
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        //新建几个变量用于比较
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //将table赋值给tab
        //1.判断table是否为null||tablle长度是否为0（短路运算）
        if ((tab = table) == null || (n = tab.length) == 0)
            //第一次put的时候才进行内部table的初始化
            n = (tab = resize()).length;
        // 将hash生成的值转换为数组下标
        // (n - 1) & hash //使用位运算(&)来代替取模运算(%)，将值限制在(n-1)范围内
        //hash % 2^k = (2^k - 1) & hash   //2^k表示2的k次方 hashtable扩容为2的n次方就是为了此处方便计算
        //判断角标table元素，如果为空
        if ((p = tab[i = (n - 1) & hash]) == null)
            //将元素放入该位置
            tab[i] = newNode(hash, key, value, null);
        //如果不为空
        else {
            Node<K,V> e; K k;
            //p.hash == hash   //hash通过hashcode生成，需要重写
            //(k = p.key) == key  //判断当前hash桶角标一致的元素key和要放入元素的key和是否相等
            //key != null && key.equals(k)  //判断要放入的key和当前hash桶key是否相等（对于自定义对象，需要重写equals，因为咱们一般要内容一致就行，原equals比较的是内存地址是否一致
            //如果是当前角标hash桶位置存放的链表首元素
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                //用Node e临时保存当前结点（之后返回当前结点旧的value，并用新值value覆盖旧值）
                e = p;
            //判断是否当前hash桶放的结点Node是树结点
            else if (p instanceof TreeNode)
                //转换为树结点
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                //当前hash桶角标位置
                //（当前链表Node不为空，不和首元素相等，不是树结点）
                //将当前结点插入链表末尾
                for (int binCount = 0; ; ++binCount) {
                    //找到链表末尾
                    if ((e = p.next) == null) {
                        //将待插入的结点插入链表末尾
                        p.next = newNode(hash, key, value, null);
                        //如果链表长度大于等于8
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            //将链表存储结构转换为树存储结构（优化查找速度）
                            treeifyBin(tab, hash);
                        break;
                    }
                    //如果待放入元素是当前链表首元素，则跳过（此处不处理）
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    
                    //继续for循环，将上一结点引用指向当前结点
                    p = e;
                }
            }
            
            //
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                //如果key结点有数据，覆盖掉value
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                //结点访问后执行的操作，此处方法为空，LinkedHashMap给出了实现
                afterNodeAccess(e);
                //返回旧值
                return oldValue;
            }
        }
        ++modCount;
        //如果当前达到扩容阈值
        if (++size > threshold)
            //进行扩容
            resize();
        //结点插入后执行的操作，此处为空方法，未实现
        afterNodeInsertion(evict);
        return null;
    }
```

## HashMap扩容机制

扩容方法
HashMap内部使用数组存储，java数组无法自动扩容，扩容需要新建数组
resize()方法  
jdk1.7
```java
    void resize(int newCapacity) {
        //扩容前的hash桶数组
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        //扩容前的数组大小如果已经达到最大
        if (oldCapacity == MAXIMUM_CAPACITY) {
            //修改阈值为int最大值，这样以后就不会扩容了
            threshold = Integer.MAX_VALUE;
            return;
        }

        //复制
        //初始化一个新的数组
        Entry[] newTable = new Entry[newCapacity];
        //数据转移到新的Entry数组里
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        //将当前HashMap内部table引用指向新数组
        table = newTable;
        //修改阈值
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }
```
```java
    void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {
            while(null != e) {
                Entry<K,V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }
```

jdk1.8
```java
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
```


```java
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
```

名词：
capacity: 容量
loadFactor: 加载因子
threshold: 阈值（=容量*加载因子）超过阈值触发扩容

总结
扩容策略：  
给定容量初始值，容量扩容为不小于指定容量的2的幂次方大小（第一次put进行扩容）  
1.7中是只要不小于阈值就直接扩容2倍；  
而1.8的扩容策略会更优化，当数组容量未达到64时，以2倍进行扩容，超过64之后若桶中元素个数不小于7就将链表转换为红黑树，但如果红黑树中的元素个数小于6就会还原为链表，当红黑树中元素不小于32的时候才会再次扩容。  

HashTable扩容  
默认初始大小11，每次扩容变为原来的2n+1，给定容量初始值，直接使用给定的大小。  

ArrayList扩容  
扩容点规则是，新增的时候发现容量不够用了，就去扩容；  
JKD1.6中实现是，如果通过无参构造的话，初始数组容量为10，每次扩容为原来的1.5倍，指定容量则使用指定容量大小；  
从jdk1.7开始，如果通过无参构造的话，初始数组容量为0，当真正对数组进行添加时，才真正分配容量，初始10，然后1.5倍扩容。  


什么时候需要重写hashcode和equals方法？

在HashMap中存放自定义的键时，就需要重写自定义对象的hashcode和equals方法

当向HashMap中存入k1的时候，首先会调用Key这个类的hashcode方法，计算它的hash值，随后把k1放入hash值所指引的内存位置，在Key这个类中没有定义hashcode方法，就会调用Object类的hashcode方法，而Object类的hashcode方法返回的hash值是对象的地址。这时用k2去拿也会计算k2的hash值到相应的位置去拿，由于k1和k2的内存地址是不一样的，所以用k2拿不到k1的值

重写hashcode方法仅仅能够k1和k2计算得到的hash值相同，调用get方法的时候会到正确的位置去找，但当出现散列冲突时，在同一个位置有可能用链表的形式存放冲突元素，这时候就需要用到equals方法去对比了，由于没有重写equals方法，它会调用Object类的equals方法，Object的equals方法判断的是两个对象的内存地址是不是一样，由于k1和k2都是new出来的，k1和k2的内存地址不相同，所以这时候用k2还是达不到k1的值


---