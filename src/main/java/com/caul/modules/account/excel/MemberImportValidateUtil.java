package com.caul.modules.account.excel;

import com.caul.core.excelimport.bean.ExcelData;
import com.caul.core.excelimport.bean.ImportCellDesc;
import com.caul.core.excelimport.bean.MemberImportExcelData;
import com.caul.core.excelimport.validate.AbstractValidator;
import com.caul.core.excelimport.validate.MobileValidator;
import com.caul.core.excelimport.validate.QQValidator;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 处理人员导入Excel数据校验
 * 人员导入 excel 会有大数据量的情况，此时数据校验是系统瓶颈
 * 采用批量校验，缓存机制提高运行速度
 */
public class MemberImportValidateUtil {

    private MemberImportValidateUtil() {

    }

    /**
     * @param excelData
     * @param params
     * @param isPlatform 是否平台用户
     */
    public static void processValidate(MemberImportExcelData excelData, Map<String, Object> params, boolean isPlatform) {
        if (excelData == null) {
            return;
        }

        // 校验多次导入的数据
        validateRepeateData(excelData, params, isPlatform);
    }

    private static void validateRepeateData(MemberImportExcelData excelData, Map<String, Object> params, boolean isPlatform) {
        // 多次导入的数据
        List<Map<String, ImportCellDesc>> repeatData = excelData.getRepeatData();
        if (repeatData == null || repeatData.size() <= 0) {
            return;
        }
        //账号列
        List<ImportCellDesc> usernameCellDescList = excelData.getCellDescByColumnName("username");

        // 账号列不能重复：
        MultiUserNameValidator userNameValidator = new MultiUserNameValidator();
        // 设置单元格的值、位置
        initMultiCellValidatorInfo(userNameValidator, excelData, params);
        userNameValidator.setUserNameCellList(usernameCellDescList);
        // 执行校验
        String userNameErrorMsg = userNameValidator.processValidate();
        // 校验结果信息
        if (StringUtils.isNotEmpty(userNameErrorMsg) || !AbstractValidator.OK.equals(userNameErrorMsg)) {
            excelData.addErrorMsg("账号列", userNameErrorMsg);
        }

        //手机列
        MobileValidator mobileValidator = new MobileValidator();
        List<ImportCellDesc> mobileCellDescList = excelData.getCellDescByColumnName("mobile");
        validateCellDescList(mobileValidator, mobileCellDescList, excelData, params);

        //QQ 列
        QQValidator emailValidator = new QQValidator();
        List<ImportCellDesc> emailCellDescList = excelData.getCellDescByColumnName("email");
        validateCellDescList(emailValidator, emailCellDescList, excelData, params);
    }

    private static void validateCellDescList(AbstractValidator validator, List<ImportCellDesc> cellDescList, MemberImportExcelData excelData, Map<String, Object> params) {
        for (ImportCellDesc cellDesc : cellDescList) {
            // 设置单元格的值、位置
            initCellValidatorInfo(validator, cellDesc, excelData, params);

            // 执行校验
            String errorMsg = validator.processValidate();
            // 校验结果信息
            if (StringUtils.isNotEmpty(errorMsg) || !AbstractValidator.OK.equals(errorMsg)) {
                excelData.addErrorMsg(cellDesc.getCellRef(), errorMsg);
            }
        }
    }

    private static void initCellValidatorInfo(AbstractValidator validator, ImportCellDesc cellDesc, ExcelData excelData, Map<String, Object> params) {
        validator.setCellRef(cellDesc.getCellRef());
        validator.setFieldValue(cellDesc.getFieldValue());
        validator.setCell(cellDesc);
        validator.setExcelData(excelData);
        validator.setOnceCellList(excelData.getOnceCellList());
        validator.setRepeatCellList(excelData.getRepeatCellList());
        validator.setAllCellList(excelData.getAllCellList());
//      validator.setCurrentRow(currentRow);
        validator.setParams(params);
    }

    private static void initMultiCellValidatorInfo(AbstractValidator validator, ExcelData excelData, Map<String, Object> params) {
        validator.setExcelData(excelData);
        validator.setOnceCellList(excelData.getOnceCellList());
        validator.setRepeatCellList(excelData.getRepeatCellList());
        validator.setAllCellList(excelData.getAllCellList());
//      validator.setCurrentRow(currentRow);
        validator.setParams(params);
    }

}