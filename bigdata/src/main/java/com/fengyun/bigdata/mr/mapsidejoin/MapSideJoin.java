package com.fengyun.bigdata.mr.mapsidejoin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 通过map端直接连接订单orde信息和产品信息product
 * 
 * @author zhengss
 *
 */
public class MapSideJoin {
    
    public static class MapSideJoninMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        // 在mapper之前使用setUp方法将数据字典文件读取到内存中放在一个hashmap中待使用
        HashMap<String, String> productInfo = new HashMap<String, String>();
        //创建一个text对象用存放最后的输出结果
        Text k = new Text();
        
        @Override
        protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:/Users/zhengss/Documents/product.txt")));
            String line;
            while (StringUtils.isNotEmpty(line = br.readLine())) {
                String[] pFields = line.split("\t");
                String pId = pFields[0];
                productInfo.put(pId, pFields[1]);
            }
            // 关闭输入流资源
            br.close();

        }

        /**
         * mapper端实现join连接并直接通过输出key以text格式输出，job设置reduceTask 不分配任何机器容器
         */
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
                throws IOException, InterruptedException {
            //每次读取一行，执行一次此方法，对所读取的行进行处理
            String orderLine = value.toString();
            String[] oFields = orderLine.split("\t");
            String pdName = productInfo.get(oFields[2]);
            k.set(orderLine+"\t"+pdName);
            context.write(k, NullWritable.get());
        }

    }

    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(MapSideJoin.class);
        //指定mapper任务执行类
        job.setMapperClass(MapSideJoninMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        // job设置reduceTask 不分配任何机器容器
        job.setNumReduceTasks(0);
        
        
        // 将产品表文件缓存到task工作节点的工作目录中去
        job.addCacheFile(new URI("file:/C:/Users/zhengss/Documents/product.txt"));
        
        //本地运行，设置输入、输出文件路径
        FileInputFormat.setInputPaths(job, new Path("C:/Users/zhengss/Documents/order.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:/Users/zhengss/Documents/temp/output"));
        boolean b = job.waitForCompletion(true);
        //退出jvm虚拟机
        System.exit(b ? 0 : 1);
        
        
    }

}
