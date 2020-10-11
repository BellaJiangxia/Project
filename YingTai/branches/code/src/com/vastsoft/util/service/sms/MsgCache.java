package com.vastsoft.util.service.sms;

import java.util.ArrayList;
import java.util.List;

public class MsgCache {
	private List<Msg> listMsgs = new ArrayList<Msg>();

	MsgCache() {
	}

	public synchronized void putMsg(Msg msg) {
		this.listMsgs.add(msg);
	}

	public synchronized Msg takeMsg() {
		if (this.listMsgs.isEmpty())
			return null;
		else
			return this.listMsgs.remove(0);
	}

	public synchronized boolean isEmpty() {
		if (this.listMsgs.size() == 0)
			return true;
		else
			return false;
	}

	public synchronized void clear() {
		this.listMsgs.clear();
	}

}
