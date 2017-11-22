package com.fengyun.bigdata.serializable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;

import org.apache.hadoop.io.Text;

public class TestSeri {
    
    public static void main(String[] args) throws Exception {
        // 定义两个ByteArrayOutputStream，用来接收不同序列化机制的序列化结果
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ByteArrayOutputStream ba2 = new ByteArrayOutputStream();

        // 定义两个DataOutputStream，用于将普通对象进行jdk标准序列化
        DataOutputStream dout = new DataOutputStream(ba);
        DataOutputStream dout2 = new DataOutputStream(ba2);
        ObjectOutputStream obout = new ObjectOutputStream(dout2);
        // 定义两个bean，作为序列化的源对象
        ItemBeanSer itemBeanSer = new ItemBeanSer(1000L, 89.9f);//实现java.io.serializable序列化接口
        ItemBean itemBean = new ItemBean(1000L, 89.9f);         //实现了apache.hadoop.writable序列化接口

        // 用于比较String类型和Text类型的序列化差别
        Text atext = new Text("a");
         atext.write(dout);
//        itemBean.write(dout);

        byte[] byteArray = ba.toByteArray();

        // 比较序列化结果
        System.out.println(byteArray.length);
        for (byte b : byteArray) {

            System.out.print(b);
            System.out.print(":");
        }

        System.out.println("-----------------------");

        String astr = "a";
        // dout2.writeUTF(astr);
        obout.writeObject(itemBeanSer);

        byte[] byteArray2 = ba2.toByteArray();
        System.out.println(byteArray2.length);
        for (byte b : byteArray2) {
            System.out.print(b);
            System.out.print(":");
        }
    }
}