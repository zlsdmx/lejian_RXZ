package com.fengyun.bigdata.serializable;

import java.io.Serializable;

public class ItemBeanSer implements Serializable{
    
    private long l;
    private float f;
    

    public ItemBeanSer() {
        super();
    }

    public ItemBeanSer(long l, float f) {
     
        this.l = l;
        this.f = f;
    }

    
    
}
