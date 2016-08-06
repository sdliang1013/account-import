package com.caul.modules.account;

import cn.easybuild.pojo.StringPojo;

import java.util.Date;

/**
 * Created by BlueDream on 2016-03-19.
 */
public class Account extends StringPojo {

    public static final String DEFAULT_STR = "无";

    private String accountName = DEFAULT_STR;
    private String qq = DEFAULT_STR;
    private String mobile = DEFAULT_STR;
    private int sendState = SendState.Unsent.getCode();//派送状态(未派送,已派送,已拒绝)
    private boolean arbitrage = false;//是否套利
    private Date createTime;//创建时间
    private Integer handsel = 0;//彩金

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public boolean isArbitrage() {
        return arbitrage;
    }

    public void setArbitrage(boolean arbitrage) {
        this.arbitrage = arbitrage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getHandsel() {
        return handsel;
    }

    public void setHandsel(Integer handsel) {
        this.handsel = handsel;
    }
}
