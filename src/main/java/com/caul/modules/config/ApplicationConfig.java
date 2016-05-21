package com.caul.modules.config;

/**
 * Created by BlueDream on 2016-03-19.
 */
public class ApplicationConfig {

    private int encryptPosition;
    private String encryptCode;

    private String xmlImportAccount;

    public String getEncryptCode() {
        return encryptCode;
    }

    public void setEncryptCode(String encryptCode) {
        this.encryptCode = encryptCode;
    }

    public int getEncryptPosition() {
        return encryptPosition;
    }

    public void setEncryptPosition(int encryptPosition) {
        this.encryptPosition = encryptPosition;
    }

    public String getXmlImportAccount() {
        return xmlImportAccount;
    }

    public void setXmlImportAccount(String xmlImportAccount) {
        this.xmlImportAccount = xmlImportAccount;
    }
}
