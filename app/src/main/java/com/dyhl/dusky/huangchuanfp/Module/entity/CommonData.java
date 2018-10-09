package com.dyhl.dusky.huangchuanfp.Module.entity;

import java.io.Serializable;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/11
 * @DESCRIPTION:
 */
public class CommonData implements Serializable {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    String code="";

    String name="";

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getOutpoverty() {
        return outpoverty;
    }

    public void setOutpoverty(String outpoverty) {
        this.outpoverty = outpoverty;
    }

    String village="";
    String outpoverty="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getPvertyattribute() {
        return pvertyattribute;
    }

    public void setPvertyattribute(String pvertyattribute) {
        this.pvertyattribute = pvertyattribute;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    String responsible="";
    String pvertyattribute="";

    public String getOverpvertyattribute() {
        return overpvertyattribute;
    }

    public void setOverpvertyattribute(String overpvertyattribute) {
        this.overpvertyattribute = overpvertyattribute;
    }

    String overpvertyattribute="";
    String position="";
    String reson="";
    String phone="";
    String start="";
    String end="";
    String id="";
    String departmentname="";
    String incharge="";

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    String reply;

    public String getPublics() {
        return publics;
    }

    public void setPublics(String publics) {
        this.publics = publics;
    }

    String publics="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getIncharge() {
        return incharge;
    }

    public void setIncharge(String incharge) {
        this.incharge = incharge;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    String contact="";

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    boolean light;
}
