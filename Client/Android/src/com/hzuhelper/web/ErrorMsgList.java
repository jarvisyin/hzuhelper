package com.hzuhelper.web;

import java.util.HashMap;

public class ErrorMsgList{

  private HashMap<String,String> map;

  private static ErrorMsgList    instance;

  private ErrorMsgList(){
    map = new HashMap<String,String>();
  }

  public ErrorMsgList getInstance(){
    if (instance==null) {
      synchronized (ErrorMsgList.class) {
        if (instance==null) instance = new ErrorMsgList();
      }
    }
    return instance;
  }

}
