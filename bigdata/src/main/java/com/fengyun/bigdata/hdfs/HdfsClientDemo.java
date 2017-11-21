package com.fengyun.bigdata.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

public class HdfsClientDemo {

    // 首先创建文件系统
    FileSystem fs = null;

    // 初始化文件系统
    @Before
    public void init() throws IOException, InterruptedException, URISyntaxException {

        Configuration conf = new Configuration();
        // 设置使用hdfs文件系统作为默认文件系统（而不是默认的本地local文件系统）
        //conf.set("fs.defaultFS", "hdfs://mini0:9000");
        // fs = FileSystem.get(conf);
        // 直接传入uri和用户
        fs = FileSystem.get(new URI("hdfs://mini0:9000"), conf, "fengyun");
    }

    @Test
    public void testUpload() throws IllegalArgumentException, IOException {
        fs.copyFromLocalFile(new Path("D:/log4j.properties"), new Path("/"));
        fs.close();
    }

    @Test
    public void testDownload() throws IllegalArgumentException, IOException {
        // fs.copyToLocalFile(new Path("/log4j.properties"), new Path("d:/"));
        fs.copyToLocalFile(false, new Path("/log4j.properties"), new Path("d:/"), true);
        fs.close();
    }

    @Test
    public void testMkdir() throws IllegalArgumentException, IOException {
        boolean flag = fs.mkdirs(new Path("/testMkdir/aaa/bbb"));
        System.out.println(flag);

    }

}
