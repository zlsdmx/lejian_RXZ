package com.fengyun.bigdata.mr.fensi;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * A:B,C,D,F,E,O B:A,C,E,K C:F,A,D,I D:A,E,F,L E:B,C,D,M,L F:A,B,C,D,E,O,M G:A,C,D,E,F H:A,C,D,E,O
 * I:A,O J:B,O K:A,C,D L:D,E,F M:E,F,G O:A,H,I,J （数据中的好友关系是单向的） 求出哪些人两两之间有共同好友，及他俩的共同好友都是谁 比如: a-b :
 * c ,e
 * 
 * @author zhengss 已知一些 ：[ 主——>宾 ] 关系 求 [ 主_主 ——>宾，宾 ... ] 主——>宾 关系不可逆 处理思路：直接找到每两个人的好友 没有太好的思路，
 *         现将目的要求分解成两个宽松的要求：注意由于这里的朋友关系是单向的，所以，我们只能找到这个人是哪些人的共同好友，即 （key：朋友 ——> value：个人） 通过 被动的“‘宾
 *         ’是哪些‘主’的朋友的 关系”，通过 ‘宾’key<——汇聚 ‘主s’value 然后对 主s 排字典序，切割出 所需的‘主_主’ 组合 再通过‘主_主’组合为key汇聚 ‘宾’
 *         最终得到 所求： [ 主_主 ——>宾，宾 ... ]
 *
 */

public class SharedFriendsStepOne {

    public static class StepOneMapper extends Mapper<LongWritable, Text, Text, Text> {

        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] person_friends = line.split(":");
            String person = person_friends[0];
            String[] friends = person_friends[1].split(",");
            for (String friend : friends) {
                // 确定 [宾<——主 ]/[key<——value] 汇聚关系，通过宾，汇聚所有 该人是哪些‘主’的 ‘宾’，即找出所有该人相关的‘主’
                context.write(new Text(friend), new Text(person));
            }
        }

    }

    public static class StepOneReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce(Text friend, Iterable<Text> persons, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            StringBuffer sb = new StringBuffer();
            for (Text text : persons) {
                // 汇聚 [主，主...]
                sb.append(text).append(",");

            }
            // 将汇聚好的 [宾<——主，主...] 写到缓存
            context.write(friend, new Text(sb.toString()));

        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //设置类路径为jar包路径
        job.setJarByClass(SharedFriendsStepOne.class);
        //设置mapper类和输出k-v类型
        job.setMapperClass(StepOneMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //设置reducer类和输出k-v类型
        job.setReducerClass(StepOneReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.setInputPaths(job, new Path("C:/Users/zhengss/Documents/input/relations.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:/Users/zhengss/Documents/output"));
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
