package com.caul.modules.base;

/**
 * Created by BlueDream on 2016-03-21.
 */
public class EnumCommon implements IEnum {

    private int code;
    private String text;

    public EnumCommon(int code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}
