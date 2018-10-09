package com.dyhl.dusky.huangchuanfp.Module.entity;

import java.io.Serializable;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/6
 * @DESCRIPTION:
 */
public class Liable implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBfpkh() {
        return bfpkh;
    }

    public void setBfpkh(String bfpkh) {
        this.bfpkh = bfpkh;
    }

    public String getBfzfl() {
        return bfzfl;
    }

    public void setBfzfl(String bfzfl) {
        this.bfzfl = bfzfl;
    }

    String id;
    String responsible;//帮扶人姓名

    public String getResponsibleid() {
        return responsibleid;
    }

    public void setResponsibleid(String responsibleid) {
        this.responsibleid = responsibleid;
    }

    String responsibleid;//帮扶人id

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getResponsiblephone() {
        return responsiblephone;
    }

    public void setResponsiblephone(String responsiblephone) {
        this.responsiblephone = responsiblephone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    String responsiblephone;//联系电话
    String position;//单位

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    String department;//职务

    String bfpkh;//帮扶数量
    String bfzfl;//帮扶走访量
}
