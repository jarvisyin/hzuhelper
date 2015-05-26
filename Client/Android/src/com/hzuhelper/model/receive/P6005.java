package com.hzuhelper.model.receive;

import com.hzuhelper.model.RECEIVE;

/**
 * 树洞评论，我发出的
 * @author jarvisyin
 *
 */
public class P6005 extends RECEIVE{
    private int    id;
    private String content;
    private int    tweetId;
    private String publishDate;
    private int    statu;
    private int    responseCommentId;
    private String yourContent;

    public String getPublishDate(){
        return publishDate;
    }

    public void setPublishDate(String publishDate){
        this.publishDate = publishDate;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public int getTweetId(){
        return tweetId;
    }

    public void setTweetId(int tweetId){
        this.tweetId = tweetId;
    }

    public int getStatu(){
        return statu;
    }

    public void setStatu(int statu){
        this.statu = statu;
    }

    public int getResponseCommentId(){
        return responseCommentId;
    }

    public void setResponseCommentId(int responseCommentId){
        this.responseCommentId = responseCommentId;
    }

    public String getYourContent(){
        return yourContent;
    }

    public void setYourContent(String yourContent){
        this.yourContent = yourContent;
    }
}
