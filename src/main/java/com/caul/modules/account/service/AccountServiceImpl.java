package com.caul.modules.account.service;

import cn.easybuild.core.dao.AppBaseDao;
import cn.easybuild.core.exceptions.BusinessException;
import cn.easybuild.core.exceptions.InvalidOperationException;
import cn.easybuild.core.service.StringPojoAppBaseServiceImpl;
import cn.easybuild.core.utils.BeanHelper;
import cn.easybuild.core.utils.UUIDGenerator;
import cn.easybuild.pojo.DataSet;
import com.caul.core.excelimport.bean.SimpleExcelData;
import com.caul.core.excelimport.userinterface.ExcelImportUtil;
import com.caul.modules.account.Account;
import com.caul.modules.account.AccountQueryParam;
import com.caul.modules.account.SendState;
import com.caul.modules.account.dao.AccountDao;
import com.caul.modules.config.ApplicationConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by BlueDream on 2016-03-23.
 */
@Service
public class AccountServiceImpl extends StringPojoAppBaseServiceImpl<Account>
        implements AccountService {

    private static int LIMIT_COUNT = 1000;

    private AccountDao accountDao;

    private ApplicationConfig applicationConfig;

    @Autowired(required = false)
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Autowired(required = false)
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public DataSet<Account> queryByParam(AccountQueryParam queryParam) {
        return accountDao.queryByParam(queryParam);
    }

    @Override
    public void batchDelete(List<String> ids) {
        accountDao.batchDelete(ids);
    }

    @Override
    public void batchUpdate(List<String> ids, SendState sendState, Boolean arbitrage) {
        accountDao.batchUpdate(ids, sendState, arbitrage);
    }

    @Override
    public void clear(Date dateEnd) {
        accountDao.clear(dateEnd);
    }

    @Override
    public void importAccount(InputStream inputStream, boolean closeIS) {
        //TODO: 获取数据
        InputStream xmlStream = getClass().getClassLoader().getResourceAsStream(
                applicationConfig.getXmlImportAccount());
        SimpleExcelData excelData =
                ExcelImportUtil.simpleReadExcel(xmlStream, inputStream, closeIS);
        //TODO:写入临时表
        batchInsertTemp(excelData);
        //TODO:临时表写入账号表
        accountDao.joinTempData();
        //TODO:清除临时表
        accountDao.truncateTempData();
    }

    private void batchInsertTemp(SimpleExcelData excelData) {

        if(excelData.getRepeatData().size() > LIMIT_COUNT){
            throw new BusinessException(
                    "数量太大,最大允许行" + LIMIT_COUNT +"记录.");
        }
        //TODO: 考虑多线程
        List<Account> list = toAccountList(excelData);
        accountDao.batchInsertTemp(list);
    }

    @Override
    protected AppBaseDao<Account, String> getDao() {
        return accountDao;
    }

    @Override
    public String save(Account entity) {
        if (accountDao.existsAccount(entity)) {
            throw new InvalidOperationException(
                    StringUtils.defaultIfEmpty(entity.getMobile(), entity.getQq()) + "QQ或手机号已存在!");
        }
        if (entity.getSendState() == 0) {
            entity.setSendState(SendState.Unsent.getCode());
        }
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    private List<Account> toAccountList(SimpleExcelData excelData) {
        List<Account> list = new CopyOnWriteArrayList<>();
        //TODO: 考虑多线程
        Account account;
        try {
            for (Map<String, String> dataMap : excelData.getRepeatData()) {
                if (StringUtils.isEmpty(dataMap.get("qq"))
                        && StringUtils.isEmpty(dataMap.get("mobile"))) {//跳过空记录
                    continue;
                }
                account = new Account();
                removeEmptyValue(dataMap);
                BeanHelper.copyExitProperties(account, dataMap);
                account.setId(UUIDGenerator.generateUUID());
                account.setCreateTime(new Date());
                list.add(account);
            }
        } catch (Exception e) {
            throw new BusinessException("拷贝Excel数据错误:" + e.getMessage(), e);
        }
        return list;
    }

    /**
     * 取出里面的空值
     *
     * @param dataMap
     * @return
     */
    private Map<String, String> removeEmptyValue(Map<String, String> dataMap) {
        Iterator<Map.Entry<String, String>> iterator = dataMap.entrySet().iterator();

        while (iterator.hasNext()) {
            if (StringUtils.isEmpty(iterator.next().getValue())) {
                iterator.remove();
            }
        }
        return dataMap;
    }

}
