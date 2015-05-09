package com.hzuhelper.tools;

import java.util.LinkedList;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class PrintLog {

	public static final String slidlist = "slidlist";

	public static void webRequest(LinkedList<BasicNameValuePair> params,
			String baseUrl, int statusCode, String errorMsg, String result) {
		Log.i("Web_request", "baseUrl: " + baseUrl + "   httpCode="
				+ statusCode);
		if (errorMsg != null) {
			Log.i("Web_request", "  errorMsg=" + errorMsg);
		}
		if (result != null) {
			if (result.length() > 80) {
				Log.i("Web_request", "  result="
						+ result.substring(0, 80).replace("\n", ""));
			} else {
				Log.i("Web_request", "  result=" + result.replace("\n", ""));
			}
		}

		if (params != null) {
			for (BasicNameValuePair basicNameValuePair : params) {
				Log.i("Web_request",
						"  name   = " + basicNameValuePair.getName());
				Log.i("Web_request",
						"   value = " + basicNameValuePair.getValue());
			}
		} else {
			Log.i("Web request", "  params : null");
		}
	}
}
