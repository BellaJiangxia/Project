package com.vastsoft.util;

public class GUID {
	private java.util.UUID uuid;

	public GUID() {
		this.uuid = java.util.UUID.randomUUID();
	}

	/**
	 * 返回GUID字符串，不带 - 号
	 */
	@Override
	public String toString() {
		return this.toString(false);
	}

	/**
	 * 返回GUID字符串
	 */
	public String toString(boolean bHasDashes) {
		if (bHasDashes) {
			return this.uuid.toString();
		} else {
			String str = this.uuid.toString();
			return str.replace("-", "");
		}
	}

	public static void main(String[] args) {
		GUID guid = new GUID();

		System.out.println(guid.toString(false));
	}
}
