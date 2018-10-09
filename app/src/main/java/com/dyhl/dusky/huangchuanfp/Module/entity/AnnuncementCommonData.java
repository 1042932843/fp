package com.dyhl.dusky.huangchuanfp.Module.entity;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/7/4
 * @DESCRIPTION:
 */
public class AnnuncementCommonData {
    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    boolean light;

    public String getV_title() {
        return v_title;
    }

    public void setV_title(String v_title) {
        this.v_title = v_title;
    }

    public String getV_source() {
        return v_source;
    }

    public void setV_source(String v_source) {
        this.v_source = v_source;
    }

    public String getV_publics() {
        return v_publics;
    }

    public void setV_publics(String v_publics) {
        this.v_publics = v_publics;
    }

    public String getV_start() {
        return v_start;
    }

    public void setV_start(String v_start) {
        this.v_start = v_start;
    }

    public String getV_end() {
        return v_end;
    }

    public void setV_end(String v_end) {
        this.v_end = v_end;
    }

    String v_title="";
    String v_source="";
    String v_publics="";
    String v_start="";
    String v_end="";

    public String getV_code() {
        return v_code;
    }

    public void setV_code(String v_code) {
        this.v_code = v_code;
    }

    String v_code;
}
