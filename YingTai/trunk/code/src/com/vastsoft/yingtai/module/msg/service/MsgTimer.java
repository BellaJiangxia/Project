package com.vastsoft.yingtai.module.msg.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.vastsoft.util.exception.BaseException;
import com.vastsoft.yingtai.core.SysConfig;
import com.vastsoft.yingtai.module.diagnosis2.contants.DiagnosisStatus2;
import com.vastsoft.yingtai.module.diagnosis2.entity.FDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.entity.TDiagnosis;
import com.vastsoft.yingtai.module.diagnosis2.service.DiagnosisService2;
import com.vastsoft.yingtai.module.msg.constants.MsgStatus;
import com.vastsoft.yingtai.module.msg.entity.TMessage;
import com.vastsoft.yingtai.module.user.entity.TUserConfig;
import com.vastsoft.yingtai.module.user.service.UserService;
import com.vastsoft.yingtai.utils.sms.SmsServer;

public class MsgTimer {
	private Timer timer;
	private TimerTask timerTask;

	public MsgTimer() {
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				// 初始化系统配置
				boolean tip_msg = SysConfig.instance.isTip_msg();
//				System.out.println("发送短消息定时器启动配置：" + tip_msg + "！");
				if (tip_msg)
					sendMessage();
				goBackDiagnosis();
			}
		};
	}

	/** 启动定时器 */
	public void start() {
		timer.schedule(timerTask, 10000, 180000);
	}

	/** 关闭定时器 */
	public void close() {
		timer.cancel();
	}

	// 回退超时诊断申请
	private void goBackDiagnosis() {
		try {
			List<FDiagnosis> listDiagnosis = DiagnosisService2.instance
					.queryDiagnosisByStatus(DiagnosisStatus2.NOTDIAGNOSIS);
			if (listDiagnosis == null || listDiagnosis.size() <= 0)
				return;
			for (TDiagnosis diagnosis : listDiagnosis) {
				try {
					if (diagnosis.getCreate_time() != null && diagnosis.getCreate_time()
							.getTime() > (System.currentTimeMillis() - (1000 * 60 * 60 * 24)))
						continue;
					DiagnosisService2.instance.cancelDiagnosis(diagnosis.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		尊敬的祝医生，您所在的乐山市金口河区人民医院有新提交诊断的影像已经诊断完成，请及时查看和打印报告。【影泰科技】
		SmsServer.instance.sendSMS("18111262633", "尊敬的用户，您的影泰平台账户审核已经通过，请登录平台进行后续操作！【影泰科技】");
	}
	
	// 发送短消息
	private void sendMessage() {
		try {
			List<String> listUid = MsgService.instance.queryNeedSendMessageUidList(null);
			if (listUid == null || listUid.size() <= 0)
				return;
			for (String uid : listUid) {
				List<TMessage> listMessage = MsgService.instance.queryMessageByUid(null, uid);
				if (listMessage == null || listMessage.size() <= 0)
					continue;
				for (TMessage message : listMessage) {
					try {
						if (message.getStatus() != MsgStatus.WAITING.getCode())
							continue;
						TUserConfig userConfig = UserService.instance.takePersonalConfig(message.getRecv_user_id());
						if (userConfig != null && !userConfig.needSendMsgByType(message.getMsg_type())) {
							message.setStatus(MsgStatus.USER_REJECT.getCode());
							continue;
						}
						SmsServer.instance.sendSMS(message.getRecv_mobile(), message.getContent());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				MsgService.instance.finishSendMessage(null, uid);
			}
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
		}
	}
}
