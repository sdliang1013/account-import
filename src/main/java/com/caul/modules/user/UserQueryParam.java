package com.caul.modules.user;

import com.caul.core.mybatis.QueryParamAdapter;

/**
 * Created by BlueDream on 2016-03-21.
 */
public class UserQueryParam extends QueryParamAdapter {

    private String userName;
    private String realName;
    private Integer userType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
