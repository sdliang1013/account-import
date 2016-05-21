package com.caul.core.excelimport.validate;

import java.util.List;
import java.util.Map;

import com.caul.core.excelimport.bean.ExcelData;
import com.caul.core.excelimport.bean.ImportCellDesc;

/**
 * 单元格数据校验
 * 
 * @author Liang,He
 */
public abstract class AbstractValidator {
    
	// 如果校验没有错误，则返回这个串
	public static final String OK = "OK";
	
	public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
	
	// 单元格的位置
	private String cellRef;
	
	// 单元格的值
	private String fieldValue;
	
	// 单元格
	private ImportCellDesc cell;
	
	private ExcelData excelData;
	
	// 一次导入的单元格集合
	private List<ImportCellDesc> onceCellList;
	
	// 循环导入的单元格集合
	private List<ImportCellDesc> repeatCellList;
	
	// Excel中所有的单元格集合
	private List<ImportCellDesc> allCellList;
	
	// 校验过程中需要的参数列表
	private Map<String, Object> params;
	
	// 当前行
	private Map<String, ImportCellDesc> currentRow;
	
	public String getCellRef() {
        return cellRef;
    }

    public void setCellRef(String cellRef) {
        this.cellRef = cellRef;
    }
	
	public final String getFieldValue() {
		return fieldValue==null?"":fieldValue;
	}

	public final void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	public final ImportCellDesc getCell() {
        return cell;
    }

    public final void setCell(final ImportCellDesc cell) {
        this.cell = cell;
    }
    
	public ExcelData getExcelData() {
		return excelData;
	}

	public void setExcelData(ExcelData excelData) {
		this.excelData = excelData;
	}

	public final List<ImportCellDesc> getOnceCellList() {
		return onceCellList;
	}

	public final void setOnceCellList(final List<ImportCellDesc> onceCellList) {
		this.onceCellList = onceCellList;
	}

	public final List<ImportCellDesc> getRepeatCellList() {
		return repeatCellList;
	}

	public final void setRepeatCellList(final List<ImportCellDesc> repeatCellList) {
		this.repeatCellList = repeatCellList;
	}

	public final List<ImportCellDesc> getAllCellList() {
		return allCellList;
	}

	public final void setAllCellList(final List<ImportCellDesc> allCellList) {
		this.allCellList = allCellList;
	}
	
	public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    public Map<String, ImportCellDesc> getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(Map<String, ImportCellDesc> currentRow) {
        this.currentRow = currentRow;
    }

    public Object getParamValue(String paramName) {
        if(params == null) {
            return null;
        } else {
            return params.get(paramName);
        }
    }

    /**
	 * 进行数据校验
	 * 
	 * @return 如果数据合法，返回空；否则返回不合法的原因。
	 */
	public abstract String processValidate();
}