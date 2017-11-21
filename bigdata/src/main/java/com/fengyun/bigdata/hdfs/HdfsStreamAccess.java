package com.fengyun.bigdata.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileOutputCommitter;
import org.junit.Before;
import org.junit.Test;

public class HdfsStreamAccess {

    // 首先创建文件系统
    FileSystem fs = null;

    // 初始化配置文件系统
    @Before
    public void init() throws IOException, InterruptedException, URISyntaxException {

        Configuration conf = new Configuration();
        // 直接传入uri和用户
        fs = FileSystem.get(new URI("hdfs://mini0:9000"), conf, "fengyun");
    }
    
    /**
     * 流式上传
     * 读取本地文件用文件流方式上传到hadoop的hdfs
     * @throws IllegalArgumentException
     * @throws IOException
     */
    @Test
    public void testUpload() throws IllegalArgumentException, IOException{
        FSDataOutputStream outputStream = fs.create(new Path("/helloHdfsStream.properties"),true);
        FileInputStream inputStream = new FileInputStream("d:/log4j.properties");
        //使用文件拷贝流工具将输入留拷贝到输出流
        IOUtils.copy(inputStream, outputStream);
    }
    
    /**
     * 下载
     * @throws IllegalArgumentException
     * @throws IOException
     */
    @Test
    public void testDownload() throws IllegalArgumentException, IOException{
        FSDataInputStream inputStream = fs.open(new Path("/helloHdfsStream.properties"));
        FileOutputStream outputStream = new FileOutputStream(new File("d:/hadpdownload.txt"));
        IOUtils.copy(inputStream, outputStream);
    }
    
}
