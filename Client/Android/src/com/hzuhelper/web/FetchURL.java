package com.hzuhelper.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.PrintLog;

import android.content.Context;
import android.content.SharedPreferences;

public class FetchURL {

	private LinkedList<BasicNameValuePair> params;
	private HashMap<String, String> headers;
	private int connectionTimeout = 10000;
	private int soTimeout = 20000;
	private int statusCode;
	private String baseUrl;
	private String result;
	private String errorMsg;
	private HttpClient httpClient;
	private HttpResponse response;

	public FetchURL(String baseUrl) {
		this.baseUrl = baseUrl;
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
		HttpConnectionParams.setSoTimeout(httpParams, soTimeout);
		httpClient = new DefaultHttpClient(httpParams);
	}

	public String post() {
		HttpPost postMethod = new HttpPost(baseUrl);
		if (headers != null) {
			Set<String> c = headers.keySet();
			for (String key : c) {
				postMethod.addHeader(key, headers.get(key));
			}
		}
		try {
			if (params != null)
				postMethod.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); // 将参数填入POST
			response = httpClient.execute(postMethod); // 执行POST方法
			statusCode = response.getStatusLine().getStatusCode(); // 获取响应码
			result = EntityUtils.toString(response.getEntity(), "utf-8"); // 获取响应内容
			PrintLog.webRequest(params, baseUrl, statusCode, errorMsg, result);
			if (statusCode != HttpStatus.SC_OK) {
				errorMsg = "HttpStatus = " + statusCode;
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String get() {
		HttpGet getMethod;
		if (params != null) {
			// 对参数编码
			String param = URLEncodedUtils.format(params, "UTF-8");
			getMethod = new HttpGet(baseUrl + "?" + param);
		} else {
			getMethod = new HttpGet(baseUrl);
		}
		if (headers != null) {
			Set<String> c = headers.keySet();
			for (String key : c) {
				getMethod.addHeader(key, headers.get(key));
			}
		}
		try {
			response = httpClient.execute(getMethod); // 发起GET请求
			statusCode = response.getStatusLine().getStatusCode(); // 获取响应码
			result = EntityUtils.toString(response.getEntity(), "utf-8");// 获取服务器响应内容
			PrintLog.webRequest(params, baseUrl, statusCode, errorMsg, result);
			if (statusCode != HttpStatus.SC_OK) {
				errorMsg = "HttpStatus = " + statusCode;
				return null;
			}
		} catch (ClientProtocolException e) {
			errorMsg = e.toString();
			e.printStackTrace();
		} catch (IOException e) {
			errorMsg = e.toString();
			e.printStackTrace();
		}
		return result;
	}

	// 私有变量get和set
	public void setParams(HashMap<String, String> _params) {
		params = new LinkedList<BasicNameValuePair>();
		Set<String> keys = _params.keySet();
		for (String key : keys) {
			params.add(new BasicNameValuePair(key, _params.get(key)));
		}
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = (HashMap<String, String>) headers;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public Header[] getHeaders() {
		if (response != null)
			return response.getAllHeaders();
		return null;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getResult() {
		return result;
	}

	public static HashMap<String, String> getUserInfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ConstantStrUtil.COMMON_XML_NAME, Context.MODE_PRIVATE);
		HashMap<String, String> myParams = new HashMap<String, String>();
		myParams.put(ConstantStrUtil.USERNAME,
				sp.getString(ConstantStrUtil.USERNAME, null));
		myParams.put(ConstantStrUtil.PASSWORD,
				sp.getString(ConstantStrUtil.PASSWORD, null));
		return myParams;
	}

}
