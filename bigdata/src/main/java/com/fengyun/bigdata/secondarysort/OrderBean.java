package com.fengyun.bigdata.secondarysort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * 
 * @author duanhaitao@itcast.cn
 *
 */
public class OrderBean implements WritableComparable<OrderBean>{

	private Text itemid;
	private DoubleWritable amount;

	public OrderBean() {
	}

	public OrderBean(Text itemid, DoubleWritable amount) {
		set(itemid, amount);

	}

	public void set(Text itemid, DoubleWritable amount) {

		this.itemid = itemid;
		this.amount = amount;

	}



	public Text getItemid() {
		return itemid;
	}

	public DoubleWritable getAmount() {
		return amount;
	}



	public int compareTo(OrderBean o) {
		int cmp = this.itemid.compareTo(o.getItemid());
		if (cmp == 0) {
			cmp = -this.amount.compareTo(o.getAmount());
		}
		return cmp;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(itemid.toString());
		out.writeDouble(amount.get());
		
	}

	public void readFields(DataInput in) throws IOException {
		String readUTF = in.readUTF();
		double readDouble = in.readDouble();
		
		this.itemid = new Text(readUTF);
		this.amount= new DoubleWritable(readDouble);
	}


	@Override
	public String toString() {

		return itemid.toString() + "\t" + amount.get();
		
	}

}
