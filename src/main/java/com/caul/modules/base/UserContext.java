package com.caul.modules.base;

import com.caul.modules.user.User;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/23.
 */
public class UserContext implements Serializable {

    /**
     * 超级管理员
     */
    private boolean admin;

    private User user;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
