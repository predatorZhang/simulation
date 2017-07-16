package com.casic.simulation.permission;

import java.io.Serializable;

/**
 * Created by admin on 2015/10/9.
 */
public class UserObj implements Serializable {

    public static final String SESSION_ATTRIBUTE_KEY = "UserObj";

    private String userName;
    private String password;
    private String appId;
    /*
    roles:角色之间以逗号分隔
     */
    private String roles;

    private Long userId;//2017.4.28增加用户ID

    /*
    authorities:权限之间以逗号分隔
    */
    private String authorities;

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
