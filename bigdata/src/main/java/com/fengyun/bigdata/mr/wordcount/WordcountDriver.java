package com.fengyun.bigdata.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 相当于一个yarn集群的客户端 需要在此封装我们的mr程序的相关运行参数，指定jar包 最后提交给yarn
 * @author
 */
public class WordcountDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://master:9000/wordcount/input/wordcount.txt";
            args[1] = "hdfs://master:9000/wordcount/output8";
        }
        Configuration conf = new Configuration();
        //liux上面已经配置了这些信息，在linux上跑的时候默认不需要配置这些信息
        /*conf.set("mapreduce.framework.name", "yarn");  
        conf.set("yarn.resoucemanager.hostname", "mini1");
        */
        
        //本地模式时 将yarn改成local
        conf.set("mapreduce.framework.name", "local");  
        conf.set("fs.defaultFS", "file:///");
         
        
        // 指定本程序的jar包所在的本地路径
        Job job = Job.getInstance(conf);

        // 设置本程序运行的jar包所在的本地程序
        job.setJarByClass(WordcountDriver.class);

        // 设置本程序所使用的mapper和reducer业务类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        // 设置map程序的输出k-v类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //为job设置combineFileInputFormat,并设置切片最大最小值
        //如果不设置inputformat，默认是TextInputformat.class
        job.setInputFormatClass(CombineFileInputFormat.class);
        CombineFileInputFormat.setMaxInputSplitSize(job, 4194304);//4M
        CombineFileInputFormat.setMinInputSplitSize(job, 2097152);//2M
        
        
        // 指定job的输入原始文件所在目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        // 指定job的输出结果所在目录
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
        /* job.submit(); */
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);

    }
}
