package com.vastsoft.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.vastsoft.util.exceptions.DecryptFromBase64Exception;

public class TripleDES {

	/**
	 * 利用TripleDES加密数据
	 * 
	 * @param desKey
	 *            加密密钥
	 * @param plainData
	 *            被加密的数据
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] desKey, byte plainData[]) throws Exception {
		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(desKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);

		return cipher.doFinal(plainData);
	}

	/**
	 * 解密TripleDES加密的数据
	 * 
	 * @param desKey
	 *            加密密钥
	 * @param encryptData
	 *            被加密的数据
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] desKey, byte encryptData[]) throws Exception {
		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(desKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);

		return cipher.doFinal(encryptData);
	}

	/**
	 * 利用TripleDES加密字符串，并转换为Base64编码
	 * 
	 * @param strKey
	 *            字符串密钥
	 * @param strData
	 *            需要加密的字符串
	 * @return Base64编码格式的字符串
	 * @throws Exception
	 */
	public static String encryptToBase64(String strKey, String strData) throws Exception {
		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(strKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);

		byte[] baEncryptData = cipher.doFinal(strData.getBytes());

		return Base64.encodeBase64String(baEncryptData);
	}

	/**
	 * 解密Base64字符串，然后利用TripleDES解密
	 * 
	 * @param strKey
	 *            字符串密钥
	 * @param strBase64Data
	 *            Base64编码的加密后的字符串
	 * @return 加密前的字符串
	 * @throws Exception
	 */
	public static String decryptFromBase64(String strKey, String strBase64Data) throws DecryptFromBase64Exception {
		SecureRandom sr = new SecureRandom();
		try {
			DESKeySpec dks = new DESKeySpec(strKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key, sr);

			byte[] baEncryptData = Base64.decodeBase64(strBase64Data);

			return new String(cipher.doFinal(baEncryptData));
		} catch (Exception e) {
			throw new DecryptFromBase64Exception();
		}

	}
}
