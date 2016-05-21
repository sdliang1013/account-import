package com.caul.core.excelimport.validate;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QQValidator extends AbstractValidator {

    public String processValidate() {
        if (StringUtils.isNotEmpty(getFieldValue())) {
            Pattern pattern = Pattern.compile("^(\\d+)$");
            Matcher matcher = pattern.matcher(getFieldValue().trim());
            if (!matcher.matches()) {
                return getCellRef() + "单元格数据 : " + getFieldValue() + ", 移动电话格式不合法!";
            }
        }
        return OK;
    }
}