package com.vastsoft.util.common;

import java.math.BigInteger;

public class UID {
	private static BigInteger indexUID = new BigInteger(String.valueOf(System.currentTimeMillis()) + "0000000");

	public static synchronized String nextUID() {
		indexUID = indexUID.add(BigInteger.ONE);
		return indexUID.toString();
	}
}
