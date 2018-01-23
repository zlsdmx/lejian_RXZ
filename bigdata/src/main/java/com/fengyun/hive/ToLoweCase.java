package com.fengyun.hive;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 自定义hive函数的写法。
 * @author zhengss
 *1、先开发一个java类，继承UDF，并重载evaluate方法
 *2、打成jar包上传到服务器
3、将jar包添加到hive的classpath
hive>add JAR /home/hadoop/udf.jar;
4、创建临时函数与开发好的java class关联
Hive>create temporary function tolowercase as 'cn.itcast.bigdata.udf.ToProvince';

5、即可在hql中使用自定义的函数strip 
 */

public class ToLoweCase extends UDF {
    public Text evaluate(final Text s) {
        if (s == null) {
            return null;
        }
        return new Text(s.toString().toLowerCase());
    }
}
