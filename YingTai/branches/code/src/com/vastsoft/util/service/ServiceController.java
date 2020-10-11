package com.vastsoft.util.service;

import java.util.List;

public interface ServiceController {
	public boolean start();

	public boolean stop();

	public RunStatus getStatus();

	public List<Worker> getWorkerList();

	public Worker getWorkerByName(String strWorkerName);
}
