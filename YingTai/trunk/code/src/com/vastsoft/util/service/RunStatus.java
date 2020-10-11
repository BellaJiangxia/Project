package com.vastsoft.util.service;

public class RunStatus {
	private RunStatus(int iCode, String strName) {
		this.iCode = iCode;
		this.strName = strName;
	}

	private int iCode;
	private String strName;

	public int getCode() {
		return this.iCode;
	}

	public String getName() {
		return this.strName;
	}

	@Override
	public String toString() {
		return this.strName;
	}

	@Override
	public int hashCode() {
		final int prime = 75;
		int result = 3;
		result = prime * result + new Integer(this.iCode).hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		RunStatus other = (RunStatus) obj;

		if (this.iCode == other.iCode)
			return true;
		else
			return false;
	}

	/**
	 * 根据代码获得对象
	 * 
	 * @param strCode
	 * @return
	 */
	public static RunStatus parseFrom(int iCode) {
		if (iCode == 0)
			return RunStatus.LIVE;
		else if (iCode == 1)
			return RunStatus.STOP;
		else
			return null;
	}

	/**
	 * 运行中
	 */
	public final static RunStatus LIVE = new RunStatus(0, "运行中");

	/**
	 * 已停止
	 */
	public final static RunStatus STOP = new RunStatus(1, "已停止");

}
