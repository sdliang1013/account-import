package com.caul.core.excelimport.bean;

public class ExcelImportException extends RuntimeException {
    
	private static final long serialVersionUID = 8036129856262757376L;

	private String msg;
	
	public ExcelImportException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public ExcelImportException(String msg, Throwable tx) {
		super(msg, tx);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}