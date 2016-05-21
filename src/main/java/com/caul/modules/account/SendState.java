package com.caul.modules.account;

import com.caul.modules.base.IEnum;

import java.util.Arrays;
import java.util.List;

/**
 * Created by BlueDream on 2016-03-19.
 */
public enum SendState implements IEnum {

    Unsent(1, "未派送"),
    Sent(2, "已派送"),
    Rejected(3, "已拒绝");

    private String text;
    private int code;

    SendState(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static SendState parse(int type) {
        switch (type) {
            case 1:
                return SendState.Unsent;
            case 2:
                return SendState.Sent;
            case 3:
                return SendState.Rejected;
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

    public static List<SendState> getValues() {
        return Arrays.asList(values());
    }
}
