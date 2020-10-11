package com.vastsoft.util.service;

import java.util.Date;

public interface Worker {
	public String getName();

	public boolean open();

	public boolean close();

	public RunStatus getStatus();

	public Date getCreatedDate();

	public Date getLastStartDate();
}
