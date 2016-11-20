package com.caul.modules.account.dao;

import cn.easybuild.pojo.DataSet;
import com.caul.modules.account.Account;
import com.caul.modules.account.AccountQueryParam;
import com.caul.modules.account.SendState;
import com.caul.modules.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BlueDream on 2016-03-23.
 */
@Repository
public class AccountDaoImpl extends BaseDaoImpl<Account> implements AccountDao {

    public static final String T_ACCOUNT = "t_account";

    @Override
    public DataSet<Account> queryByParam(AccountQueryParam queryParam) {
        return selectDataSet(getPrefix() + "queryByParam", queryParam);
    }

    @Override
    public int batchDelete(List<String> ids) {
        return delete(getPrefix() + "batchDelete", ids);
    }

    @Override
    public int batchUpdate(List<String> ids, SendState sendState, Boolean arbitrage) {
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        if (sendState != null) {
            map.put("sendState", sendState.getCode());
        }
        map.put("arbitrage", arbitrage);
        return update(getPrefix() + "batchUpdate", map);
    }

    @Override
    protected String getTableName() {
        return T_ACCOUNT;
    }

    @Override
    protected Class<Account> getPojoClass() {
        return Account.class;
    }

    @Override
    public boolean existsAccount(Account entity) {
        Integer count = selectOne(getPrefix() + "existsAccount", entity);
        return count > 0;
    }

    @Override
    public void batchInsertTemp(String tempTabName, List<Account> list) {
        Map<String, Object> params = new HashMap<>();
        params.put("tempTabName", tempTabName);
        params.put("dataList", list);
        insert(getPrefix() + "batchInsertTemp", params);
    }

    @Override
    public void joinTempData(String tempTabName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tempTabName", tempTabName);
        update(getPrefix() + "joinTempData", params);
    }

    @Override
    public void truncateTempData(String tempTabName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tempTabName", tempTabName);
        delete(getPrefix() + "truncateTempData", params);
    }

    @Override
    public void clear(Date dateEnd) {
        delete(getPrefix() + "clear", dateEnd);
    }
}
