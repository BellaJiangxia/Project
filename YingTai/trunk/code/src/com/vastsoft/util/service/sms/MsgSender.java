package com.vastsoft.util.service.sms;

import com.vastsoft.util.service.Worker;

public interface MsgSender extends Worker {
	public void init(MsgSenderConfig cfg, MsgCache cacheMsg) throws MsgSenderException;
}
