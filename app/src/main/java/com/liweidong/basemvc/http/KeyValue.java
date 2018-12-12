package com.liweidong.basemvc.http;

/**
 * Created by Administrator on 2018/12/12.
 */

public class KeyValue {
    //当final修饰一个基本数据类型时，表示该基本数据类型的值一旦在初始化后便不能发生变化
    //final修饰一个成员变量（属性），必须要显示初始化。
    // 这里有两种初始化方式，一种是在变量声明的时候初始化；第二种方法是在声明变量的时候不赋初值，但是要在这个变量所在的类的所有的构造函数中对这个变量赋初值。
    public final String key;
    public final Object value;

    public KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    //hashCode是jdk根据对象的地址或者字符串或者数字算出来的int类型的数值
    //在 Java 应用程序执行期间，在对同一对象多次调用 hashCode 方法时，必须一致地返回相同的整数，前提是将对象进行hashcode比较时所用的信息没有被修改。
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    /**
     * 重写toString方法
     */
    public String toString() {
        return "KeyValue{" + "key='" + key + '\'' + ", value=" + value + '}';
    }

    /**
     * 重写equals方法
     */
    public boolean equals(Object o) {
        //检测this与otherObject是否引用同一个对象
        if(this==o){
            return true;
        }
        //检测otherObject是否为null，如果是，则返回false
        if(o==null){
            return false ;
        }
        //比较this和otherObject是否属于同一个类，如果equals的语义在每个子类中有所改变，就是用getClass()方法检测
        if(getClass()!=o.getClass()){
            return false;
        }
        //测试字段是否具有相同的值
        KeyValue keyValue = (KeyValue) o;
        return key == null ? keyValue.key == null : key.equals(keyValue.key);
    }


}
