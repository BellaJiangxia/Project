package com.vastsoft.yingtai.dataMove;

import com.vastsoft.yingtai.dataMove.service.DataMoveService;

public class Main {
	public static void main(String[] args) throws Exception {
		long aaa  = System.currentTimeMillis();
		System.out.println("开始迁移数据....");
		DataMoveService.instance.dataMove();
		System.out.println("迁移数据完成....耗时："+(System.currentTimeMillis()-aaa)+"毫秒");
	}
}
