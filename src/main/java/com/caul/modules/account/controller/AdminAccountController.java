package com.caul.modules.account.controller;

import cn.easybuild.core.exceptions.BusinessException;
import cn.easybuild.core.utils.DateUtils;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.account.Account;
import com.caul.modules.account.AccountQueryParam;
import com.caul.modules.account.SendState;
import com.caul.modules.account.service.AccountService;
import com.caul.modules.base.AdminBaseController;
import com.caul.sys.view.Grid;
import com.caul.sys.view.GridAdapter;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by BlueDream on 2016-03-23.
 */
@Controller
public class AdminAccountController extends AdminBaseController {


    private static final String MODULE_NAME = "/account";
    private static final String VIEW_PATH = ADMIN_BASE_URI + MODULE_NAME;

    private AccountService accountService;

    @Autowired(required = false)
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = MODULE_NAME + "/manage")
    public String toManage() {
        return VIEW_PATH + "/accountMgr";
    }

    @RequestMapping(value = MODULE_NAME + "/to-add")
    public String toAdd() {
        checkPermission("您没有增加记录的权限!");
        return VIEW_PATH + "/accountAdd";
    }

    @RequestMapping(value = MODULE_NAME + "/to-edit")
    public String toEdit() {
        checkPermission("您没有修改记录的权限!");
        return VIEW_PATH + "/accountEdit";
    }

    @RequestMapping(value = MODULE_NAME + "/import-clear")
    public String importAndClear() {
        checkPermission("您没有导入和清理记录的权限!");
        return VIEW_PATH + "/accountImportClear";
    }

    @RequestMapping(value = MODULE_NAME + "/list", method = RequestMethod.POST)
    @ResponseBody
    public Grid<Account> list(AccountQueryParam queryParam) {
        boolean flag = checkListPermission(queryParam);
        DataSet<Account> dataSet = new DataSet<>();
        if (flag) {
            dataSet = accountService.queryByParam(queryParam);
        }
        return new GridAdapter<>(dataSet);
    }

    @RequestMapping(value = MODULE_NAME + "/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject add(Account account) {
        checkPermission("您没有增加记录的权限!");
        accountService.save(account);
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/edit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject edit(Account account) {
        checkPermission("您没有修改记录的权限!");
        accountService.update(account);
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/batch-delete")
    @ResponseBody
    public JSONObject batchDelete(String ids) {
        checkPermission("您没有删除记录的权限!");
        accountService.batchDelete(Arrays.asList(ids.split(",")));
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/detail")
    @ResponseBody
    public Account detail(String id) {
        return accountService.getById(id);
    }

    /**
     * 设置为未派送
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = MODULE_NAME + "/set-unsent")
    @ResponseBody
    public JSONObject batchSetUnsent(String ids) {
        checkPermission("您没有设置为'未派送'的权限!");
        accountService.batchUpdate(Arrays.asList(ids.split(",")),
                SendState.Unsent, null);
        return jsonWithStandardStatus();
    }

    /**
     * 设置为已派送
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = MODULE_NAME + "/set-sent")
    @ResponseBody
    public JSONObject batchSetSent(String ids) {
        accountService.batchUpdate(Arrays.asList(ids.split(",")),
                SendState.Sent, null);
        return jsonWithStandardStatus();
    }

    /**
     * 设置为拒绝
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = MODULE_NAME + "/set-rejected")
    @ResponseBody
    public JSONObject batchSetRejected(String ids) {
        accountService.batchUpdate(Arrays.asList(ids.split(",")),
                SendState.Rejected, null);
        return jsonWithStandardStatus();
    }

    /**
     * 设置为套利
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = MODULE_NAME + "/set-arbitrage")
    @ResponseBody
    public JSONObject batchSetArbitrage(String ids) {
        accountService.batchUpdate(Arrays.asList(ids.split(",")),
                SendState.Rejected, true);
        return jsonWithStandardStatus();
    }

    /**
     * 设置为未套利
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = MODULE_NAME + "/set-unarbitrage")
    @ResponseBody
    public JSONObject batchSetUnArbitrage(String ids) {
        checkPermission("您没有设置为'未套利'的权限!");
        accountService.batchUpdate(Arrays.asList(ids.split(",")),
                SendState.Unsent, false);
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/import", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject importAccount(MultipartFile file, HttpServletRequest request) {
        try {
            accountService.importAccount(file.getInputStream(), true);
        } catch (Exception e) {
            throw new BusinessException("导入文件失败:" + e.getMessage(), e);
        }
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/clear", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject clear(String dateStr, HttpServletRequest request) {
        accountService.clear(DateUtils.getDate(dateStr, "yyyy-MM-dd"));
        return jsonWithStandardStatus();
    }

    /**
     * 检查参数
     *
     * @param queryParam
     * @return
     */
    private boolean checkListPermission(AccountQueryParam queryParam) {
        if (isManager() || !queryParam.isEmpty()) {
            return true;
        }
        return false;
    }
}
