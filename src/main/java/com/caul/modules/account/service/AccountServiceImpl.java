package com.caul.modules.account.service;

import cn.easybuild.core.dao.AppBaseDao;
import cn.easybuild.core.exceptions.BusinessException;
import cn.easybuild.core.exceptions.InvalidOperationException;
import cn.easybuild.core.service.StringPojoAppBaseServiceImpl;
import cn.easybuild.core.utils.BeanHelper;
import cn.easybuild.core.utils.ThreadPoolUtils;
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
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Created by BlueDream on 2016-03-23.
 */
@Service
public class AccountServiceImpl extends StringPojoAppBaseServiceImpl<Account>
        implements AccountService {

    private static int LIMIT_POOL = 20;//最大线程数
    private static int LIMIT_STEP = 500;//每次提交大小
    private static int LIMIT_COUNT = 10000;//excel总限制大小
    private static String TAB_TEMP = "t_account_temp";

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
        //获取数据
        InputStream xmlStream = getClass().getClassLoader().getResourceAsStream(
                applicationConfig.getXmlImportAccount());
        SimpleExcelData excelData =
                ExcelImportUtil.simpleReadExcel(xmlStream, inputStream, closeIS);
        //最大限制
        if (excelData.getRepeatData().size() > LIMIT_COUNT) {
            throw new BusinessException(
                    "数量太大,最大允许行" + LIMIT_COUNT + "记录.");
        }
        //多线程运行
        ExecutorService executorService = ThreadPoolUtils.getCustomThreadPool(LIMIT_POOL);
        try {
            List<Account> list = new CopyOnWriteArrayList<>();
            Account account;
            try {
                int idx = 0;
                int dataLen = excelData.getRepeatData().size();
                for (Map<String, String> dataMap : excelData.getRepeatData()) {
                    idx++;
                    account = toAccount(dataMap);
                    if (account != null) {
                        list.add(account);
                    }
                    if (list.size() >= LIMIT_STEP || idx >= dataLen) {
                        final String tempTabName = TAB_TEMP + Double.valueOf(idx / LIMIT_STEP).intValue();
                        final List<Account> stepList = new ArrayList<>();
                        stepList.addAll(list);
                        executorService.submit(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                //写入数据库
                                batchInsert(tempTabName, stepList);
                                return null;
                            }
                        });
                        list.clear();
                    }
                }
            } finally {
                ThreadPoolUtils.shutdownAndAwaitTermination(executorService, 1800);
                while (true) {
                    if (executorService.isTerminated()) {
                        //临时表写入账号表
                        accountDao.joinTempData(TAB_TEMP);
                        //清除临时表
                        accountDao.truncateTempData(TAB_TEMP);
                        break;
                    }
                    Thread.sleep(5000l);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("拷贝Excel数据错误:" + e.getMessage(), e);
        }

    }

    private void batchInsert(String tempTabName, List<Account> list) {
        try {
            //写入临时表
            accountDao.batchInsertTemp(TAB_TEMP, list);
            //临时表写入账号表
//            accountDao.joinTempData(tempTabName);
            //清除临时表
//            accountDao.truncateTempData(tempTabName);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private Account toAccount(Map<String, String> dataMap) {
        Account account = null;
        if (StringUtils.isEmpty(dataMap.get("qq"))
                && StringUtils.isEmpty(dataMap.get("mobile"))) {//跳过空记录
            return account;
        }
        try {
            account = new Account();
            removeEmptyValue(dataMap);
            BeanHelper.copyExitProperties(account, dataMap);
            account.setHandsel(Double.valueOf(dataMap.get("handsel")).intValue());
            account.setId(UUIDGenerator.generateUUID());
            account.setCreateTime(new Date());
        } catch (Exception e) {
            throw new BusinessException("拷贝Excel数据错误:" + e.getMessage(), e);
        }
        return account;
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
