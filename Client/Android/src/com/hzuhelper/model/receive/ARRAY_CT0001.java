package com.hzuhelper.model.receive;

/**
 * 树洞内容
 * @author jarvisyin
 *
 */
public class ARRAY_CT0001{
    private int    id;
    private String content;
    private int    ding;
    private int    cai;
    private String publishDate;
    private int    statu;
    private int    tagId;
    private int    commentCount;
    private String authorId;

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

    public int getDing(){
        return ding;
    }

    public void setDing(int ding){
        this.ding = ding;
    }

    public int getCai(){
        return cai;
    }

    public void setCai(int cai){
        this.cai = cai;
    }

    public String getPublishDate(){
        return publishDate;
    }

    public void setPublish_date(String publishDate){
        this.publishDate = publishDate;
    }

    public int getStatu(){
        return statu;
    }

    public void setStatu(int statu){
        this.statu = statu;
    }

    public int getTag_id(){
        return tagId;
    }

    public void setTagId(int tagId){
        this.tagId = tagId;
    }

    public int getCommentCount(){
        return commentCount;
    }

    public void setCommentCount(int commentCount){
        this.commentCount = commentCount;
    }

    public String getAuthorId(){
        return authorId;
    }

    public void setAuthorId(String authorId){
        this.authorId = authorId;
    }
}