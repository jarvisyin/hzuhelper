package com.hzuhelper.model.receive;

import com.hzuhelper.model.RECEIVE;

/**
 * 课程表
 * @author jarvisyin
 *
 */
public class P6004 extends RECEIVE{

    private int    id;
    private int    weektime;
    private int    daytime;
    private int    coursetime1;
    private int    coursetime2;
    private String name;
    private String teacher;
    private String site;
    private int    statu;
    private int    starttime;
    private int    endtime;
    private int    serverid;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getWeektime(){
        return weektime;
    }

    public void setWeektime(int weektime){
        this.weektime = weektime;
    }

    public int getDaytime(){
        return daytime;
    }

    public void setDaytime(int daytime){
        this.daytime = daytime;
    }

    public int getCoursetime1(){
        return coursetime1;
    }

    public void setCoursetime1(int coursetime1){
        this.coursetime1 = coursetime1;
    }

    public int getCoursetime2(){
        return coursetime2;
    }

    public void setCoursetime2(int coursetime2){
        this.coursetime2 = coursetime2;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getTeacher(){
        return teacher;
    }

    public void setTeacher(String teacher){
        this.teacher = teacher;
    }

    public String getSite(){
        return site;
    }

    public void setSite(String site){
        this.site = site;
    }

    public int getStatu(){
        return statu;
    }

    public void setStatu(int statu){
        this.statu = statu;
    }

    public int getStarttime(){
        return starttime;
    }

    public void setStarttime(int starttime){
        this.starttime = starttime;
    }

    public int getEndtime(){
        return endtime;
    }

    public void setEndtime(int endtime){
        this.endtime = endtime;
    }

    public int getServerid(){
        return serverid;
    }

    public void setServerid(int courseid){
        this.serverid = courseid;
    }

}
