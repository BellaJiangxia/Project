package com.vastsoft.util.common;

public class DataTools {
	/** 数据打包，会添加校验头 */
	public byte[] buildPackage(byte[] data) {
		if (data == null)
			return null;
		int len = data.length;
		return ArrayTools.concat(toBytes(len), data);
	}

	/** 数据拆解，如果数据经校验不正确抛出异常 */
	public byte[] tearPackage(byte[] data) {
		if (data == null)
			return null;
		int len = data.length;
		byte[] ll = new byte[4];
		System.arraycopy(data, 0, ll, 0, 4);
		int length = bytesToInt(ll);
		if (len != (length + 4))
			throw new RuntimeException("数据长度不正确！");
		ll = new byte[length];
		System.arraycopy(data, 4, ll, 0, length);
		return ll;
	}

	public static byte[] toBytes(int i) {
		return new byte[] { (byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF),
				(byte) (i & 0xFF) };
	}

	public static int bytesToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}
}
