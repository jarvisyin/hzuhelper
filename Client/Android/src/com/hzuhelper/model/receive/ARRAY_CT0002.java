package com.hzuhelper.model.receive;

import java.util.Date;

/**
 * ChatComment1Info
 * @author jarvisyin
 *
 */
public class ARRAY_CT0002 {
    private int    id;
    private String content;
    private int    tweetId;
    private String authorId;
    private int    statu;
    private String responseUserId;
    private Date   publishDate;

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

    public String getAuthorId(){
        return authorId;
    }

    public void setAuthorId(String authorId){
        this.authorId = authorId;
    }

    public int getStatu(){
        return statu;
    }

    public void setStatu(int statu){
        this.statu = statu;
    }

    public String getResponseUserId(){
        return responseUserId;
    }

    public void setResponseUserId(String responseUserId){
        this.responseUserId = responseUserId;
    }

    public Date getPublishDate(){
        return publishDate;
    }

    public void setPublishDate(Date publishDate){
        this.publishDate = publishDate;
    }

}
