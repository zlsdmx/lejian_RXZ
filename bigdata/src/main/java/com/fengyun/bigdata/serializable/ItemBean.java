package com.fengyun.bigdata.serializable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class ItemBean implements Writable  {

    
    private long l;
    private float f;
    
    public ItemBean() {
        super();
    }

    public ItemBean(long l, float f) {
        this.l = l;
        this.f = f;
    }

    public void write(DataOutput out) throws IOException {
        out.writeLong(l);
        out.writeFloat(f);
    }

    public void readFields(DataInput in) throws IOException {
        this.l = in.readLong();
        this.f = in.readFloat();
    }

}
