package com.fengyun.bigdata.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * KEYIN：默认情况下是所读到的文本的起始偏移量（单位：字节）
 * 但是在hadoop中有自己的更精简的序列化接口，所以不直接用Long，而用LongWritable
 * VALUEIN：默认情况下，是mr框架所读到的一行文本的内容，String，同上，用Text
 * KEYOUT：是用户自定义逻辑处理完成之后输出数据中的key，在此处是单词，String，同上，用Text
 * VALUEOUT：是用户自定义逻辑处理完成之后输出数据中的value，在此处是单词次数，Integer，同上，用IntWritable
 * @author zhengss
 *
 */
public class WordcountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * map阶段的业务逻辑就写在自定义的map()方法中
     * maptask会对每一行输入数据调用一次我们自定义的map()方法
     */
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        //将maptask传给我们的一行文本内容先转换成String
        String line = value.toString();
        //将这行文本内容按空格切分成数组
        String[] words = line.split(" ");
        
        //遍历数组，以单词为key，以1次为value，将k-v对写入到上下文缓存Context中
        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
        
    }
}
