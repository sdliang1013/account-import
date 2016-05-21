package com.caul.core.excelimport.validate;


import org.apache.commons.lang.StringUtils;

/**
 * 校验数据不能为空
 * 
 * @author Liang,He
 */
public class NotNullValidator extends AbstractValidator {
	public String processValidate() {
		if(StringUtils.isEmpty(getFieldValue())) {
			return getCellRef() + "单元格数据：不可以为空!";
		}
		return OK;
	}
}