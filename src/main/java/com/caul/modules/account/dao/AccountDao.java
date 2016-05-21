package com.caul.modules.account.dao;

import cn.easybuild.core.dao.StringPojoAppBaseDao;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.account.Account;
import com.caul.modules.account.AccountQueryParam;
import com.caul.modules.account.SendState;

import java.util.Date;
import java.util.List;

/**
 * Created by BlueDream on 2016-03-23.
 */
public interface AccountDao extends StringPojoAppBaseDao<Account> {
    DataSet<Account> queryByParam(AccountQueryParam queryParam);

    int batchDelete(List<String> ids);

    int batchUpdate(List<String> ids, SendState sendState, Boolean arbitrage);

    boolean existsAccount(Account entity);

    void batchInsertTemp(List<Account> list);

    void joinTempData();

    void truncateTempData();

    void clear(Date dateEnd);
}
