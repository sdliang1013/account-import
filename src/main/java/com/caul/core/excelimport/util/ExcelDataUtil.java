package com.caul.core.excelimport.util;

import com.caul.core.excelimport.bean.ExcelData;
import com.caul.core.excelimport.bean.ImportCellDesc;
import com.caul.core.excelimport.bean.SimpleExcelData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * 针对ExcelData的简单的处理类
 * 
 * @author Liang,He
 */
public class ExcelDataUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataUtil.class);

	private ExcelDataUtil() {

	}
	
	/**
	 * 将ExcelData转化为SimpleExcelData
	 */
	public static SimpleExcelData changeExcelDataToSimple(ExcelData excelData) {
		if(excelData == null) {
			return null;
		}
		SimpleExcelData simpleExcelData = new SimpleExcelData();
		
		Map<String, ImportCellDesc> onceData = excelData.getOnceData();
		if(onceData != null && onceData.size() > 0) {
			Map<String, String> simpleOnceData = new HashMap<String, String>();
			Set<String> keys = onceData.keySet();
			for(String key : keys) {
				if(StringUtils.isEmpty(key) || onceData.get(key) == null)
					continue;
				simpleOnceData.put(key,
						toPlainString(onceData.get(key).getFieldValue()));
			}
			simpleExcelData.setOnceData(simpleOnceData);
		}
		
		List<Map<String, ImportCellDesc>> repeatData = excelData.getRepeatData();
		if(repeatData != null && repeatData.size() > 0) {
			List<Map<String, String>> simpleRepeatData = new ArrayList<Map<String,String>>();
			for(Map<String, ImportCellDesc> map : repeatData) {
				if(map == null)
					continue;
				Map<String, String> tmp = new HashMap<String, String>();
				Set<String> keys = map.keySet();
				for(String key : keys) {
					if(StringUtils.isEmpty(key) || map.get(key) == null)
						continue;
					tmp.put(key, toPlainString(map.get(key).getFieldValue()));
				}
				simpleRepeatData.add(tmp);
			}
			simpleExcelData.setRepeatData(simpleRepeatData);
		}
		return simpleExcelData;
	}

	/**
	 * 将科学计数转成普通数字,不带指数字段
	 *
	 * @param input
	 * @return
	 */
	public static String toPlainString(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
		String str = input;
		try {
			BigDecimal bd = new BigDecimal(input);
			str = bd.toPlainString();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return str;

	}
}