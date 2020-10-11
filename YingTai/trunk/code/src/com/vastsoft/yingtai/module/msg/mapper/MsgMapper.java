package com.vastsoft.yingtai.module.msg.mapper;

import java.util.List;
import java.util.Map;

import com.vastsoft.yingtai.module.msg.entity.FMessage;
import com.vastsoft.yingtai.module.msg.entity.TMessage;

public interface MsgMapper {

	public void addMessage(TMessage msg);

	public List<FMessage> searchMsg(Map<String, Object> mapArg);

	public int searchMsgCount(Map<String, Object> mapArg);

	public void modifyMessageStatusAndSendTimeByUid(Map<String, Object> mapArg);

	public List<String> queryNeedSendMessageUidList(String now);

	public List<TMessage> queryMessageByUid(String uid);

	public void modifyMessageStatusByDiagnosisIdAndType(Map<String, Object> mapArg);

}
