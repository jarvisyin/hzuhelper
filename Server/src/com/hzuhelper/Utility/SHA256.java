package com.hzuhelper.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256 {

	/**
	 * 用SHA-256算法加密字符串
	 * 
	 * @param orignal
	 * @return 加密后的字符串
	 */
	public static String SHA256Encrypt(String orignal) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (null != md) {
			byte[] origBytes = orignal.getBytes();
			md.update(origBytes);
			byte[] digestRes = md.digest();
			String digestStr = getDigestStr(digestRes);
			return digestStr;
		}
		return null;
	}

	/**
	 * 将byte[]转换为字符串
	 * 
	 * @param origBytes
	 * @return
	 */
	private static String getDigestStr(byte[] origBytes) {
		String tempStr = null;
		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < origBytes.length; i++) {
			// 这里按位与是为了把字节转整时候取其正确的整数，java中一个int是4个字节
			// 如果origBytes[i]最高位为1，则转为int时，int的前三个字节都被1填充了
			tempStr = Integer.toHexString(origBytes[i] & 0xff);
			if (tempStr.length() == 1) {
				stb.append("0");
			}
			stb.append(tempStr);

		}
		return stb.toString();
	}

	private static SecureRandom random;

	/**
	 * 获取盐值
	 * 
	 * @return 盐值
	 */
	public static String getSalt() {
		if (random == null)
			random = new SecureRandom();

		byte bytes[] = new byte[32];
		random.nextBytes(bytes);
		String digestStr = getDigestStr(bytes);
		return digestStr;
	}
}