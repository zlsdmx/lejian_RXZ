package com.fengyun.bigdata.mr.provinceflow;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 通过重新partitioner类的getPartition方法来改变 reducer的默认排序规则:
 * reducer将根据 getPartition方法返回的int值将不同的maptast结果分配给不同的reducer
 * 
 * 
 * K2  V2  对应的是map输出kv的类型
 * @author
 *
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean>{

	public static HashMap<String, Integer> proviceDict = new HashMap<String, Integer>();
	//下面用静态代码块儿 自定义一个归属地map分类，替代真实的号码归属地
	static{
		proviceDict.put("136", 0);
		proviceDict.put("137", 1);
		proviceDict.put("138", 2);
		proviceDict.put("139", 3);
	}
	
	@Override
	public int getPartition(Text key, FlowBean value, int numPartitions) {
		String prefix = key.toString().substring(0, 3);
		Integer provinceId = proviceDict.get(prefix);
		
		return provinceId==null?4:provinceId;
	}



}
