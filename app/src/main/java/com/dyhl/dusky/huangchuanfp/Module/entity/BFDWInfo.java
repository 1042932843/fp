package com.dyhl.dusky.huangchuanfp.Module.entity;

import java.io.Serializable;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/6/12
 * @DESCRIPTION:
 */
public class BFDWInfo implements Serializable{
    String id;
    String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    String departmentid;
    String department;
    String incharge;
    String contact;
}
