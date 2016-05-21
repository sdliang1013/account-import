package com.caul.core.excelimport.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.caul.core.excelimport.bean.ExcelImportException;
import com.caul.core.excelimport.bean.ExcelStruct;
import com.caul.core.excelimport.bean.ImportCellDesc;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 解析导入Excel的XML描述文件
 * 
 * @author Liang,He
 */
public class ParseXMLUtil {
    
	private ParseXMLUtil() {
	    
	}

	/**
	 * 根据给定的XML文件解析出Excel的结构
	 */
	@SuppressWarnings({ "unused", "unchecked" })
    public static ExcelStruct parseImportStruct(InputStream xmlFile) throws JDOMException, IOException {
		if(xmlFile == null) {
			return null;
		}
		InputStream is = xmlFile;
		if(is == null) {
			throw new FileNotFoundException("Excel的描述文件 : " + xmlFile + " 未找到.");
		}
		SAXBuilder saxBuilder = new SAXBuilder(false);
		Document document = saxBuilder.build(is);
		// 根节点
		Element root = document.getRootElement();
		// 一次导入
		List<Element> onceList = root.getChildren("onceImport");
		// 重复导入
		List<Element> repeatList = root.getChildren("repeatImport");
		// 校验器的定义
		List<Element> validators = root.getChildren("validators");
		// 单元格校验
		List<Element> cellValidators = root.getChildren("cell-validators");
		
		ExcelStruct excelStruct = new ExcelStruct();
		// 读取校验器配置
		parseValidatorConfig(excelStruct, validators, cellValidators);
		// 解析一次导入
		simpleParseOnceImport(excelStruct, onceList);
		// 解析重复导入
		simpleParseRepeatImport(excelStruct, repeatList);
		
		is.close();
		return excelStruct;
	}
	
	/**
	 * 读取校验器的相关配置
	 */
	@SuppressWarnings("unchecked")
    private static void parseValidatorConfig(ExcelStruct excelStruct, List<Element> validators, List<Element> cellValidators) {
		if(excelStruct == null || validators == null || validators.size() <= 0 || cellValidators == null || cellValidators.size() <= 0) {
			return;
		}
		// 1.读取校验器的定义
		Element validElem = (Element)validators.get(0);
		if(validElem == null) {
			return;
		}
		List<Element> validatorList = validElem.getChildren("validator");
		if(validatorList == null || validatorList.size() <= 0) {
			return;
		}
		for(Object obj : validatorList) {
			if(obj == null) {
				continue;
			}
			Element validator = (Element)obj;
			String name = validator.getAttributeValue("name");
			String value = validator.getAttributeValue("value");
			excelStruct.addSysValidator(name, value);
		}
		// 2.读取单元格的校验器
		Element cellValidElem = (Element)cellValidators.get(0);
		if(cellValidElem == null) {
			return;
		}
		List<Element> cellValidatorList = cellValidElem.getChildren("cell-validator");
		if(cellValidatorList == null || cellValidatorList.size() <= 0) {
			return;
		}
		for(Element obj : cellValidatorList) {
			if(obj == null) {
				continue;
			}
			Element cellValidator = obj;
			// 单元格名称
			String cellname = cellValidator.getAttributeValue("cellname");
			if(StringUtils.isEmpty(cellname)) {
				continue;
			}
			// 单元格所使用的校验器
			List<Element> cValidators = cellValidator.getChildren("validator");
			if(cValidators == null || cValidators.size() <= 0) {
				continue;
			}
			for(Object tmp : cValidators) {
				if(tmp == null) {
					continue;
				}
				Element validator = (Element)tmp;
				String validatorName = validator.getAttributeValue("name");
				excelStruct.addCellValidator(cellname, validatorName);
			}
		}
	}
	
	private static void simpleParseOnceImport(ExcelStruct excelStruct, List<Element> onceList) {
		if(onceList == null || onceList.size() <= 0) {
			return;
		}
		Element onceElem = (Element)onceList.get(0);
		// 获取CDATA区内的内容
		String cdata = onceElem.getText();
		List<ImportCellDesc> onceImportCells = changeCDATAToList(excelStruct, cdata);
		if(onceImportCells == null || onceImportCells.size() <= 0) {
			return;
		}
		excelStruct.setOnceImportCells(onceImportCells);
	}
	
	private static void simpleParseRepeatImport(ExcelStruct excelStruct, List<Element> repeatList) {
		if(repeatList == null || repeatList.size() <= 0) {
			return;
		}
		Element repElem = (Element)repeatList.get(0);
		// 获取CDATA区内的内容
		String cdata = repElem.getText();
		List<ImportCellDesc> repeatImportCells = changeCDATAToList(excelStruct, cdata);
		if(repeatImportCells == null || repeatImportCells.size() <= 0) {
			return;
		}
		// 读取终止行
		String endCode = null;
		try {
			endCode = ((Element)repElem.getChildren("endCode").get(0)).getTextTrim();
		} catch (IndexOutOfBoundsException e) {
			throw new ExcelImportException("导入Excel失败 : 请在XML描述文件中添加<endCode/>配置项!");
		}
		excelStruct.setEndCode(endCode);
		excelStruct.setRepeatImportCells(repeatImportCells);
	}
	
	/**
	 * 将CDATA区中的数据转换成我们需要的对象
	 */
	private static List<ImportCellDesc> changeCDATAToList(ExcelStruct excelStruct, String cdata) {
		if(StringUtils.isEmpty(cdata)) {
			return null;
		}
		// 去掉空白字符
		cdata = cdata.trim();
		cdata = cdata.replaceAll("\\s", "");
		if(StringUtils.isEmpty(cdata)) {
			return null;
		}
		String[] arr = cdata.split(",");
		if(arr == null || arr.length <= 0) {
			return null;
		}
		List<ImportCellDesc> list = new ArrayList<ImportCellDesc>();
		for(int i = 0; i < arr.length; i++) {
			if(StringUtils.isEmpty(arr[i])) {
				continue;
			}
			String[] kv = arr[i].split("=");
			if(kv == null || kv.length < 2) {
				continue;
			}
			ImportCellDesc cellDesc = new ImportCellDesc();
			cellDesc.setCellRef(kv[0]);
			cellDesc.setFieldName(kv[1]);
			if(excelStruct != null) {
				cellDesc.setValidatorList(excelStruct.getCellValidatorMap().get(cellDesc.getCellRef()));
			}
			list.add(cellDesc);
		}
		return list;
	}
}