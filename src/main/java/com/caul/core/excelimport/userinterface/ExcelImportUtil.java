package com.caul.core.excelimport.userinterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.caul.core.excelimport.bean.ExcelData;
import com.caul.core.excelimport.bean.ExcelImportException;
import com.caul.core.excelimport.bean.ExcelStruct;
import com.caul.core.excelimport.bean.SimpleExcelData;
import com.caul.core.excelimport.util.ExcelDataReader;
import com.caul.core.excelimport.util.ExcelDataUtil;
import com.caul.core.excelimport.util.ParseXMLUtil;
import org.jdom.JDOMException;

/**
 * 读取导入的Excel的内容
 * 模板要求：开始重复行与End行有且只能有 一空行
 *
 * @author Liang, He
 */
public class ExcelImportUtil {

    public static ExcelData readExcel(InputStream xmlFile, InputStream importExcelStream, boolean needValidation) throws ExcelImportException {
        return readExcel(xmlFile, importExcelStream, null, needValidation);
    }

    /**
     * 读取导入的Excel的文件内容
     *
     * @param xmlFile           描述被导入的Excel的格式的XML文件
     * @param importExcelStream 被导入的XML文件
     * @param params            校验时需要的参数
     * @return Excel中需要导入的数据
     */
    public static ExcelData readExcel(InputStream xmlFile, InputStream importExcelStream, Map<String, Object> params, boolean needValidation) throws ExcelImportException {
        if (xmlFile == null || importExcelStream == null) {
            return null;
        }
        try {
            // 1. 解析XML描述文件
            ExcelStruct excelStruct = ParseXMLUtil.parseImportStruct(xmlFile);
            // 2. 按照XML描述文件，来解析Excel中文件的内容
            return ExcelDataReader.readExcel(excelStruct, importExcelStream, 0, params, needValidation);
        } catch (FileNotFoundException e) {
            throw new ExcelImportException("导入Excel失败 - XML描述文件未找到 : ", e);
        } catch (JDOMException e) {
            throw new ExcelImportException("导入Excel失败 - 解析XML描述文件异常 : ", e);
        } catch (IOException e) {
            throw new ExcelImportException("导入Excel失败 - IO异常 : ", e);
        } catch (Exception e) {
            throw new ExcelImportException("导入Excel失败 : ", e);
        }
    }

    /**
     * 读取导入的Excel的文件内容
     *
     * @param xmlFile           描述被导入的Excel的格式的XML文件
     * @param importExcelStream 被导入的XML文件
     * @return Excel中需要导入的数据
     */
    public static SimpleExcelData simpleReadExcel(InputStream xmlFile, InputStream importExcelStream, boolean needValidation) throws ExcelImportException {
        ExcelData excelData = readExcel(xmlFile, importExcelStream, needValidation);
        return ExcelDataUtil.changeExcelDataToSimple(excelData);
    }

    public static void main(String[] args) {
        try {
            InputStream xmlStream =
                    ExcelImportUtil.class.getClassLoader().getResourceAsStream("import/account.xml");
            FileInputStream fileInputStream = new FileInputStream("E:\\sdliang\\account-import.xls");
            try {
                SimpleExcelData simpleExcelData =
                        simpleReadExcel(xmlStream, fileInputStream, true);
                System.out.println(simpleExcelData);
            } finally {
                fileInputStream.close();
                xmlStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}