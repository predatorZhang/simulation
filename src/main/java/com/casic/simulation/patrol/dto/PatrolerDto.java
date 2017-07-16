package com.casic.simulation.patrol.dto;

import com.casic.simulation.patrol.domain.Patroler;

/**
 * Created by lenovo on 2017/4/28.
 */
public class PatrolerDto {
    private long dbId;
    private String userName;
    //TODO LIST：密码返回前台干什么？
    private String password;
    private String active;
    private String sex;
    private String phoneNumber;
    private String age;


    public PatrolerDto() {
    }

    public PatrolerDto(Patroler patroler)
    {
        this.setDbId(patroler.getId());
        this.setUserName(patroler.getUserName());
        this.setAge(patroler.getAge());
        this.setSex(patroler.getSex());
        this.setPhoneNumber(patroler.getPhoneNumber());
        this.setPassword(patroler.getPassword());
        if(patroler.isActive() == true)
            this.setActive("正常状态");
        else
            this.setActive("注销状态");
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) { this.active = active;}

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
