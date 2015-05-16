package com.hzuhelper.web;

public class ResultObj{

  public final static int    STATE_SUCCESS       = 1;
  public final static int    STATE_FAIL          = 2;
  public final static String DEFAULT_ERRORMSG    = "未知错误";

  public final static String ERRORMSG_UNKNOW     = "UN0001";
  public final static String ERRORMSG_CLIENT     = "CL0001";
  public final static String ERRORMSG_SERVER     = "SE0001";
  public final static String ERRORMSG_CONNECTION = "CO0001";

  private Object             result;
  private int                state               = STATE_FAIL;
  private String             errorCode           = ERRORMSG_UNKNOW;
  private String             errorMsg            = DEFAULT_ERRORMSG;

  public ResultObj(){}

  public ResultObj(Object result,int state,String errorCode,String errorMsg){
    this.result = result;
    this.state = state;
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  public Object getResult(){
    return result;
  }

  public void setResult(Object result){
    this.result = result;
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
