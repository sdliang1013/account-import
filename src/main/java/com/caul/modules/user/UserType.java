package com.caul.modules.user;

import com.caul.modules.base.IEnum;

import java.util.Arrays;
import java.util.List;

/**
 * Created by BlueDream on 2016-03-19.
 */
public enum UserType implements IEnum {

    Manager(1, "管理员"),
    Operator(2, "操作员");

    private String text;
    private int code;

    UserType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static UserType parse(int type) {
        switch (type) {
            case 1:
                return UserType.Manager;
            case 2:
                return UserType.Operator;
        }
        return null;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

    public static List<UserType> getValues() {
        return Arrays.asList(values());
    }

    public static boolean isManager(int type) {
        return type == UserType.Manager.code;
    }
}
