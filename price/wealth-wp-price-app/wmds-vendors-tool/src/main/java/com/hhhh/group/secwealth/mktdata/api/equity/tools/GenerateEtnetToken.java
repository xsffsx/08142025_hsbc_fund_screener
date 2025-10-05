package com.hhhh.group.secwealth.mktdata.api.equity.tools;

import com.hhhh.group.secwealth.mktdata.api.equity.utlis.HttpClientUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.utlis.StringUtil;
import org.apache.commons.codec.binary.Base64;

public class GenerateEtnetToken {
	
	private static final String GET_TOKEN_URL = "https://hsp2gp2uat.etnet.com.hk/HSAPI/Auth/GetToken";
	
	private static final String PROXY_HOST = "intpxy6.hk.hhhh";
	private static final int PROXY_PORT = 18084;
	
	/**
	 * the format: username:password
	 */
	private static final String USERNAME_COLON_PASSWORD = "hs11user:hset1234";

	private static String encode(String encodeString) throws Exception {
		if (StringUtil.isInValid(encodeString)) {
			throw new Exception();
		}
		return Base64.encodeBase64String(encodeString.getBytes());
	}

	public static void main(String[] args) throws Exception {

		String token = HttpClientUtil.doGet(GET_TOKEN_URL, "Basic " + encode(USERNAME_COLON_PASSWORD), PROXY_HOST,
				PROXY_PORT);
		System.out.println(token);
	}

}
