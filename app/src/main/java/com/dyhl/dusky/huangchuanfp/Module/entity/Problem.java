package com.dyhl.dusky.huangchuanfp.Module.entity;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/10
 * @DESCRIPTION:
 */
public class Problem {
    String id;
    String name;
    String idcard;
    String time;
    String num;//问题编号

    public String getPublics() {
        return publics;
    }

    public void setPublics(String publics) {
        this.publics = publics;
    }

    public String getReplycount() {
        return replycount;
    }

    public void setReplycount(String replycount) {
        this.replycount = replycount;
    }

    String publics;
    String replycount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getResponsibleid() {
        return responsibleid;
    }

    public void setResponsibleid(String responsibleid) {
        this.responsibleid = responsibleid;
    }

    String responsible;//帮扶人
    String responsibleid;//帮扶人id
}
