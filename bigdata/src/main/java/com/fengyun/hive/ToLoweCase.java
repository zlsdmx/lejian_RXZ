package com.fengyun.hive;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * 自定义hive函数的写法。
 * @author zhengss
 *
 */

public class ToLoweCase extends UDF {
    public Text evaluate(final Text s) {
        if (s == null) {
            return null;
        }
        return new Text(s.toString().toLowerCase());
    }
}
