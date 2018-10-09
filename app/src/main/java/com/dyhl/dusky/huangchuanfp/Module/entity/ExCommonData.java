package com.dyhl.dusky.huangchuanfp.Module.entity;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/6/6
 * @DESCRIPTION:
 */
public class ExCommonData {
    public String getSinglekey() {
        return singlekey;
    }

    public void setSinglekey(String singlekey) {
        this.singlekey = singlekey;
    }

    public String getTotalkey() {
        return totalkey;
    }

    public void setTotalkey(String totalkey) {
        this.totalkey = totalkey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    String singlekey;
    String totalkey;
    String code;
    String title;
    String value;
    String name;
    String start;
    String end;

    public String getIspublic() {
        return ispublic;
    }

    public void setIspublic(String ispublic) {
        this.ispublic = ispublic;
    }

    String ispublic;

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    boolean light;

}
