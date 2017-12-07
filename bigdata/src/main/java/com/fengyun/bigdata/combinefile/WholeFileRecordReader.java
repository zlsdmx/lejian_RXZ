package com.fengyun.bigdata.combinefile;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * 
 * RecordReader的核心工作逻辑： 通过nextKeyValue()方法去读取数据构造将返回的key value 通过getCurrentKey 和
 * getCurrentValue来返回上面构造好的key和value
 * 
 * 
 * @author
 * 
 */
class WholeFileRecordReader extends RecordReader<NullWritable, BytesWritable> {
    private FileSplit fileSplit; // 文件分片

    private Configuration conf; // 配置

    private BytesWritable value = new BytesWritable(); // 可写比特流

    private boolean processed = false;

    /**
     * 初始化类 的两个属性：文件分片和上下文缓存
     */
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        this.fileSplit = (FileSplit) split;
        this.conf = context.getConfiguration();
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed) {
            // 根据文件分片 获取 文件分片大小、定义一个byte自己数组用于保存文件，再获取文件路径file；
            byte[] contents = new byte[(int) fileSplit.getLength()];
            Path file = fileSplit.getPath();
            //通过文件路径file和conf配置 获取到文件系统句柄，
            FileSystem fs = file.getFileSystem(conf);
            //定义一个文件系统输入流in
            FSDataInputStream in = null;
            try {
                //打开文件、写入输入流
                in = fs.open(file);
                //使用io工具将输入流的内容拷贝到 比特数组中byte[]
                IOUtils.readFully(in, contents, 0, contents.length);
                //将文件的输入流读取到的内容写入到 成员属性 可写比特流value中
                value.set(contents, 0, contents.length);
            } finally {
                //关闭输入流
                IOUtils.closeStream(in);
            }
            //设置标志变量
            processed = true;
            return true;
        }
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    /**
     * 返回当前进度
     */
    @Override
    public float getProgress() throws IOException {
        return processed ? 1.0f : 0.0f;
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}