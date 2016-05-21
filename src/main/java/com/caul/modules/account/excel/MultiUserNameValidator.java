package com.caul.modules.account.excel;


import com.caul.core.excelimport.bean.ImportCellDesc;
import com.caul.core.excelimport.validate.AbstractValidator;
import com.caul.modules.account.service.AccountService;
import com.caul.sys.spring.SpringContextHolder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户名列同时校验，以提高校验速度
 */
public class MultiUserNameValidator extends AbstractValidator {

    private List<ImportCellDesc> userNameCellList;

    public String processValidate() {
        StringBuilder validateResult = new StringBuilder();

        List<String> userNameList = new ArrayList<>();              //用户名列表

        //校验用户名长度是否过长
        for (ImportCellDesc cellDesc : userNameCellList) {
            String username = cellDesc.getFieldValue();
            if (StringUtils.isNotEmpty(username)) {
                userNameList.add(username);
            }
        }

        //校验用户名是否已经存在于数据库中
        AccountService accountService = SpringContextHolder.getBean(AccountService.class);
        List<String> existUserName = null;// accountService.findAllExistUserName(userNameList);

        //校验用户名是否在 excel 中有重复数据
        int count = userNameList.size();
        for (int i = 0; i < count; i++) {
            String username = userNameList.get(i);
            if (userNameList.lastIndexOf(username) > i) {
                existUserName.add(username);
            }
        }

        if (existUserName.size() > 0) {
            List<ImportCellDesc> existUsernameCellDescList = new ArrayList<>();
            for (ImportCellDesc cellDesc : userNameCellList) {
                if (existUserName.contains(cellDesc.getFieldValue())) {
                    existUsernameCellDescList.add(cellDesc);
                }
            }

            for (ImportCellDesc cellDesc : existUsernameCellDescList) {
                if (validateResult.length() > 0) {
                    validateResult.append("<br/>");
                }
                String userNameExistMsg = cellDesc.getCellRef() + "单元格数据 : " + cellDesc.getFieldValue() + ", 用户名已经存在!";
                validateResult.append(userNameExistMsg);
            }
        }

        if (validateResult.length() > 0) {
            return validateResult.toString();
        }

        return OK;
    }

    public void setUserNameCellList(List<ImportCellDesc> userNameCellList) {
        this.userNameCellList = userNameCellList;
    }
}