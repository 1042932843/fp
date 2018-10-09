package com.dyhl.dusky.huangchuanfp.Module.entity;

import java.util.ArrayList;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/5/12
 * @DESCRIPTION:
 */
public class Town {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String code;
    String name;

    public ArrayList<Village> getVillages() {
        return villages;
    }

    public void setVillages(ArrayList<Village> villages) {
        this.villages = villages;
    }

    ArrayList<Village> villages;
}
