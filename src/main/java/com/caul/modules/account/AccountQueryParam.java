package com.caul.modules.account;

import com.caul.core.mybatis.QueryParamAdapter;
import org.apache.commons.lang.StringUtils;

/**
 * Created by BlueDream on 2016-03-21.
 */
public class AccountQueryParam extends QueryParamAdapter {

    private String accountName;
    private String qq;
    private String mobile;
    private Integer sendState;//派送状态(未派送,已派送,已拒绝)
    private Boolean arbitrage;//是否套利

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

    public Integer getSendState() {
        return sendState;
    }

    public void setSendState(Integer sendState) {
        this.sendState = sendState;
    }

    public Boolean getArbitrage() {
        return arbitrage;
    }

    public void setArbitrage(Boolean arbitrage) {
        this.arbitrage = arbitrage;
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(getAccountName())
                && StringUtils.isEmpty(getQq())
                && StringUtils.isEmpty(getMobile())
                && getSendState() == null
                && getArbitrage() == null;
    }
}
