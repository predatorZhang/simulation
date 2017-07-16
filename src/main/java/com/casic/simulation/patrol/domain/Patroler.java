package com.casic.simulation.patrol.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lenovo on 2017/4/28.
 */



@Entity
@Table(name = "XJ_PATROL")
@SequenceGenerator(name = "SEQ_XJ_PATROL_ID", sequenceName = "SEQ_XJ_PATROL_ID",allocationSize=1,initialValue=1)
public class Patroler implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    private String userName;
    private String password;
    private String sex;
    private String phoneNumber;
    private String age;
    private boolean active;



    @Column(name = "USERSEX")
    public String getSex()
    {
        return sex;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    @Column(name = "PHONENUMBER")
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Column(name="ACTIVE")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    @Column(name = "USERAGE")
    public String getAge()
    {
        return age;
    }
    public void setAge(String age)
    {
        this.age = age;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_XJ_PATROL_ID")
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }

    @Column(name = "USERNAME")
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Column(name = "PASSWORD")
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

}