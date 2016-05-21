package com.caul.core.excelimport.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanyouz on 2015/9/14.
 */
public class MemberImportExcelData extends ExcelData {

    //用户导入 excel 数据，key 为列名，值为一列的数据
    private Map<String, List<ImportCellDesc>> memberImportColumnMap = new HashMap<>();

    public MemberImportExcelData() {
    }

    public MemberImportExcelData(ExcelData excelData) {
        this.setErrorMsgList(excelData.getErrorMsgList());
        this.setOnceData(excelData.getOnceData());
        this.setRepeatData(excelData.getRepeatData());
    }

    public Map<String, List<ImportCellDesc>> getMemberImportColumnMap() {
        return memberImportColumnMap;
    }

    public void setMemberImportColumnMap(Map<String, List<ImportCellDesc>> memberImportColumnMap) {
        this.memberImportColumnMap = memberImportColumnMap;
    }

    public List<ImportCellDesc> getCellDescByColumnName(String column) {
        return memberImportColumnMap.get(column);
    }

    public void initMemberImportColumnMap() {
        List<ImportCellDesc> usernameCellDescList = new ArrayList<>();
        List<ImportCellDesc> passwordCellDescList = new ArrayList<>();
        List<ImportCellDesc> nameCellDescList = new ArrayList<>();
        List<ImportCellDesc> genderCellDescList = new ArrayList<>();
        List<ImportCellDesc> departmentNameCellDescList = new ArrayList<>();
        List<ImportCellDesc> positionNameCellDescList = new ArrayList<>();
        List<ImportCellDesc> mobileCellDescList = new ArrayList<>();
        List<ImportCellDesc> emailCellDescList = new ArrayList<>();

        List<Map<String, ImportCellDesc>> repeatData = this.getRepeatData();
        for (Map<String, ImportCellDesc> cellDescMap : repeatData) {
            usernameCellDescList.add(cellDescMap.get("username"));
            passwordCellDescList.add(cellDescMap.get("password"));
            nameCellDescList.add(cellDescMap.get("name"));
            genderCellDescList.add(cellDescMap.get("gender"));
            departmentNameCellDescList.add(cellDescMap.get("departmentName"));
            positionNameCellDescList.add(cellDescMap.get("positionName"));
            mobileCellDescList.add(cellDescMap.get("mobile"));
            emailCellDescList.add(cellDescMap.get("email"));
        }

        memberImportColumnMap.put("username", usernameCellDescList);
        memberImportColumnMap.put("password", passwordCellDescList);
        memberImportColumnMap.put("name", nameCellDescList);
        memberImportColumnMap.put("gender", genderCellDescList);
        memberImportColumnMap.put("departmentName", departmentNameCellDescList);
        memberImportColumnMap.put("positionName", positionNameCellDescList);
        memberImportColumnMap.put("mobile", mobileCellDescList);
        memberImportColumnMap.put("email", emailCellDescList);
    }
}
