package com.hzuhelper.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
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
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;

public class WebRequest implements Runnable{

    public static final short              METHOD_POST       = 1;
    public static final short              METHOD_GET        = 0;
    public static final String             SYSTEM_ENCODING   = "UTF-8";

    private CookieStore                    cookieStore;

    private String                         baseUrl;
    private LinkedList<BasicNameValuePair> params;
    private HashMap<String,String>         headers;
    private int                            connectionTimeout = 10000;
    private int                            soTimeout         = 20000;
    private short                          method;

    private DefaultHttpClient              httpClient;
    private HttpResponse                   response;

    private ResultObj                      resultObj;

    public WebRequest(String domain,String baseUrl,short method){
        this.method = method;
        this.baseUrl = domain+baseUrl;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams,soTimeout);
        this.httpClient = new DefaultHttpClient(httpParams);
        this.cookieStore = CookiesUtils.getInstances().getCookieStore();
    }

    public WebRequest(String baseUrl,short method){
        this(staticURL.DOMAINNAME, baseUrl, METHOD_POST);
    }

    public WebRequest(String domain,String baseUrl){
        this(domain, baseUrl, METHOD_POST);
    }

    public WebRequest(String baseUrl){
        this(staticURL.DOMAINNAME, baseUrl);
    }

    public void start(){
        new Thread(this).start();
    }

    @Override
    public void run(){
        switch (method) {
        case METHOD_POST:
            post();
            break;
        case METHOD_GET:
            get();
            break;
        default:
            get();
            break;
        }
    }

    private void post(){
        httpClient.setCookieStore(cookieStore);
        HttpPost postMethod = new HttpPost(baseUrl);
        if (headers!=null) {
            Set<String> c = headers.keySet();
            for (String key : c) {
                postMethod.addHeader(key,headers.get(key));
            }
        }
        String result = null;
        int state = ResultObj.STATE_SUCCESS;
        String errorCode = null;
        String errorMsg = null;
        try {
            if (params!=null) postMethod.setEntity(new UrlEncodedFormEntity(params,SYSTEM_ENCODING)); // 将参数填入POST
            response = httpClient.execute(postMethod); // 执行POST方法
            int statusCode = response.getStatusLine().getStatusCode(); // 获取响应码
            result = EntityUtils.toString(response.getEntity(),SYSTEM_ENCODING); // 获取响应内容
            CookiesUtils.getInstances().updateCookies(httpClient.getCookieStore());
            if (statusCode!=HttpStatus.SC_OK) {
                state = ResultObj.STATE_FAIL;
                errorCode = ResultObj.ERRORCODE_CONNECTION;
                errorMsg = "HttpStatus = "+statusCode;
            }
        } catch (Exception e) {
            state = ResultObj.STATE_FAIL;
            errorCode = ResultObj.ERRORCODE_CLIENT;
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
        webRequestFinished(result,state,errorCode,errorMsg);
    }

    public void get(){
        HttpGet getMethod;
        if (params!=null) {
            String param = URLEncodedUtils.format(params,SYSTEM_ENCODING);
            getMethod = new HttpGet(baseUrl+"?"+param);
        } else {
            getMethod = new HttpGet(baseUrl);
        }
        if (headers!=null) {
            Set<String> c = headers.keySet();
            for (String key : c) {
                getMethod.addHeader(key,headers.get(key));
            }
        }
        String result = null;
        int state = ResultObj.STATE_SUCCESS;
        String errorCode = null;
        String errorMsg = null;
        try {
            response = httpClient.execute(getMethod); // 发起GET请求
            int statusCode = response.getStatusLine().getStatusCode(); // 获取响应码
            result = EntityUtils.toString(response.getEntity(),SYSTEM_ENCODING);// 获取服务器响应内容 
            CookiesUtils.getInstances().updateCookies(httpClient.getCookieStore());
            if (statusCode!=HttpStatus.SC_OK) {
                state = ResultObj.STATE_FAIL;
                errorCode = ResultObj.ERRORCODE_CONNECTION;
                errorMsg = "HttpStatus = "+statusCode;
            }
        } catch (Exception e) {
            state = ResultObj.STATE_FAIL;
            errorCode = ResultObj.ERRORCODE_CLIENT;
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
        webRequestFinished(result,state,errorCode,errorMsg);
    }

    private void webRequestFinished(String result,int state,String errorCode,String errorMsg){
        JSONObject jsonObject = null;
        if (state!=ResultObj.STATE_FAIL) {
            try {
                jsonObject = new JSONObject(result);
                state = jsonObject.getInt(StaticValues.state);
                if (state==ResultObj.STATE_FAIL) {
                    errorMsg = jsonObject.getString(StaticValues.errorMsg);
                    errorCode = jsonObject.getString(StaticValues.errorCode);
                }
            } catch (Exception e) {
                state = ResultObj.STATE_FAIL;
                errorMsg = ResultObj.ERRORMSG_JSON;
                errorCode = ResultObj.ERRORCODE_CLIENT;
            }
        }
        resultObj = new ResultObj(result,state,errorCode,errorMsg);
        Message msg = new Message();
        msg.obj = WebRequest.this;
        handler.sendMessage(msg);
    }

    /**
     * 私有变量get和set
     * @param _params
     */
    public void setParams(HashMap<String,String> _params){
        params = new LinkedList<BasicNameValuePair>();
        Set<String> keys = _params.keySet();
        for (String key : keys) {
            params.add(new BasicNameValuePair(key,_params.get(key)));
        }
    }

    public void setParam(String key,String value){
        if (params==null) params = new LinkedList<BasicNameValuePair>();
        this.params.add(new BasicNameValuePair(key,value));
    }

    public void setHeaders(Map<String,String> headers){
        this.headers = (HashMap<String,String>)headers;
    }

    public void setConnectionTimeout(int connectionTimeout){
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout){
        this.soTimeout = soTimeout;
    }

    public Header[] getHeaders(){
        if (response!=null) return response.getAllHeaders();
        return null;
    }

    public void setMethod(short method){
        this.method = method;
    }

    protected void onFinished(ResultObj resultObj){
        if (resultObj.getState()==ResultObj.STATE_SUCCESS) {
            onSuccess(resultObj);
        } else {
            onFailure(resultObj);
        }
    }

    protected void onSuccess(ResultObj resultObj){}

    protected void onFailure(ResultObj resultObj){}

    private static Handler handler = new Handler(){
                                       @SuppressWarnings({"unchecked","rawtypes"})
                                       @Override
                                       public void handleMessage(Message msg){
                                           WebRequest wq = (WebRequest)msg.obj;
                                           wq.onFinished(wq.resultObj);
                                       }
                                   };

}
