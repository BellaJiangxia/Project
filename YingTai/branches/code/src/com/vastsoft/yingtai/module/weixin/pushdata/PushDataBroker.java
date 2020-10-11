package com.vastsoft.yingtai.module.weixin.pushdata;

import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;

import com.vastsoft.yingtai.module.weixin.pushdata.interfase.PushDataTask;

public class PushDataBroker extends TimerTask{
	public static final PushDataBroker instance = new PushDataBroker();

	private Timer pushDataProgress;
	private ArrayDeque<PushDataTask> listTask=new ArrayDeque<PushDataTask>();

	private PushDataBroker() {
		pushDataProgress = new Timer();
		pushDataProgress.schedule(this, 10000);
	}

	public synchronized void pushData(PushDataTask task) {
		listTask.offer(task);
	}
	
	@Override
	protected void finalize() throws Throwable {
		pushDataProgress.cancel();
		super.finalize();
	}

	@Override
	public synchronized void run() {
		while (!listTask.isEmpty()) {
			try {
				PushDataTask taskHandle=listTask.poll();
				taskHandle.beginPushData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
