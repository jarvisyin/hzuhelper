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

import android.os.Handler;
import android.os.Message;

public class WebRequest implements Runnable {

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

    private RequestFinishedListener        requestFinishedListener;
    private ResultObj                      resultObj;

    public WebRequest(String baseUrl){
        this.baseUrl = baseUrl;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParams,soTimeout);
        httpClient = new DefaultHttpClient(httpParams);
        resultObj = new ResultObj();
        cookieStore = CookiesUtils.getInstances().getCookieStore();
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
        Object result = null;
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
                errorCode = ResultObj.ERRORMSG_CONNECTION;
                errorMsg = "HttpStatus = "+statusCode;
            }
        } catch (Exception e) {
            state = ResultObj.STATE_FAIL;
            errorCode = ResultObj.ERRORMSG_CLIENT;
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
        resultObj = new ResultObj(result,state,errorCode,errorMsg);
        Message msg = new Message();
        msg.obj = WebRequest.this;
        handler.sendMessage(msg);
    }

    public void get(){
        HttpGet getMethod;
        if (params!=null) {
            // 对参数编码
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
        Object result = null;
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
                errorCode = ResultObj.ERRORMSG_CONNECTION;
                errorMsg = "HttpStatus = "+statusCode;
            }
        } catch (Exception e) {
            state = ResultObj.STATE_FAIL;
            errorCode = ResultObj.ERRORMSG_CLIENT;
            errorMsg = e.getMessage();
            e.printStackTrace();
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

    public void setRequestFinishedListener(RequestFinishedListener requestFinishedListener){
        this.requestFinishedListener = requestFinishedListener;
    }

    public interface RequestFinishedListener {
        public void onFinished(ResultObj resultObj);
    }

    private static Handler handler = new Handler() {
                                       @Override
                                       public void handleMessage(Message msg){
                                           WebRequest wq = (WebRequest)msg.obj;
                                           if (wq.requestFinishedListener!=null) wq.requestFinishedListener.onFinished(wq.resultObj);
                                       }
                                   };

}
