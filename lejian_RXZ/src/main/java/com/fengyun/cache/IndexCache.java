package com.fengyun.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 首页缓存
 * 
 */
public class IndexCache {
	private static Log log = LogFactory.getLog(IndexCache.class);

	//首页商品信息
	
	//首页广告信息
	
	

	public static void hearbeat(){
		final Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (;;) {
					log.info("开始刷新首页数据!");
					try {
						
					} catch (Exception e) {
						log.error("心跳出错:IndexCache", e);
					}

					try {
						Thread.sleep(60 * 60 * 1000);
					} catch (InterruptedException e) {
						log.error("心跳线程出错:IndexCache", e);
					}

				}
			}
		}, "HeartBeat Thread IndexCache");
		t.start();
		
	}

	
	
	
}
