package com.vastsoft.yingtai.module.weixin.pushdata.interfase;

import java.util.Timer;
import java.util.TimerTask;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.module.weixin.pushdata.assist.NetInfo;
import com.vastsoft.yingtai.utils.network.HttpRequest;

public abstract class PushDataTask {
	private Timer pushTimer;
	private int pushTimes = 0;
	private NetInfo netInfo;

	public void beginPushData() throws BaseException {
		if (pushTimer != null)
			return;
		netInfo = this.getNetInfo();
		pushTimer = new Timer();
		pushTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (pushTimes >= 3) {
						try {
							onPushDataFailed(netInfo);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							pushTimer.cancel();
						}
						return;
					}
					sendRequest(netInfo.getUrl(), netInfo.getParams());
					pushTimer.cancel();
				} catch (BaseException e) {
					e.printStackTrace();
					pushTimes++;
				}

			}
		}, 10000);
	}

	private void sendRequest(String url, String params) throws BaseException {
		String dataStr = HttpRequest.sendGet(url, params);
		this.onReponseEvent(dataStr);
	}
	/**获取推送URL和推送参数，如果抛出异常将不会推送*/
	protected abstract NetInfo getNetInfo() throws BaseException;
	/** 推送成功的回调
	 * @param jsonStr 远端返回的数据
	 * @throws BaseException
	 */
	protected abstract void onReponseEvent(String jsonStr) throws BaseException;

	/** 指定次数尝试推送失败的回调 */
	protected abstract void onPushDataFailed(NetInfo netInfo);
}
