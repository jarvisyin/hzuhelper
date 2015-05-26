package com.hzuhelper.utils.web;


public class ResultObj {

    public final static int    STATE_SUCCESS        = 1;
    public final static int    STATE_FAIL           = 2;
    public final static String ERRORMSG_DEFAULT     = "未知错误";
    public final static String ERRORMSG_JSON        = "JSON解析异常";

    public final static String ERRORCODE_UNKNOW     = "UN0001";
    public final static String ERRORCODE_CLIENT     = "CL0001";
    public final static String ERRORCODE_SERVER     = "SE0001";
    public final static String ERRORCODE_CONNECTION = "CO0001";

    private String             resultObj;
    private int                state                = STATE_FAIL;
    private String             errorCode            = ERRORCODE_UNKNOW;
    private String             errorMsg             = ERRORMSG_DEFAULT;

    public ResultObj(){}

    public ResultObj(String result,int state,String errorCode,String errorMsg){
        this.resultObj = result;
        this.state = state;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getResult(){
        return resultObj;
    }

    public void setResult(String result){
        this.resultObj = result;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }

    public String getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(String errorCode){
        this.errorCode = errorCode;
    }

    public String getErrorMsg(){
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }

}
