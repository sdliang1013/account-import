package com.caul.modules.account.service;

import cn.easybuild.core.service.StringPojoAppBaseService;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.account.Account;
import com.caul.modules.account.AccountQueryParam;
import com.caul.modules.account.SendState;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by BlueDream on 2016-03-23.
 */
public interface AccountService extends StringPojoAppBaseService<Account> {

    DataSet<Account> queryByParam(AccountQueryParam queryParam);

    void batchDelete(List<String> ids);

    void batchUpdate(List<String> ids, SendState sendState, Boolean arbitrage);

    void clear(Date dateEnd);

    void importAccount(InputStream inputStream, boolean closeIS);
}
