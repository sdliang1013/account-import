package com.caul.modules.user.controller;

import cn.easybuild.core.exceptions.InvalidException;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.base.AdminBaseController;
import com.caul.modules.base.UserContext;
import com.caul.modules.user.User;
import com.caul.modules.user.UserQueryParam;
import com.caul.modules.user.UserType;
import com.caul.modules.user.service.UserService;
import com.caul.sys.view.Grid;
import com.caul.sys.view.GridAdapter;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by BlueDream on 2016-03-19.
 */
@Controller
public class AdminUserController extends AdminBaseController {

    private static final String MODULE_NAME = "/user";

    private static final String VIEW_PATH = ADMIN_BASE_URI + MODULE_NAME;

    private UserService userService;

    @Autowired(required = false)
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = MODULE_NAME + "/login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(String userName, String password, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
                throw new RuntimeException("用户名和密码不能为空！");
            }
            if (getUserContext() != null
                    && !userName.equals(getUserContext().getUser().getUserName())) {
                throw new RuntimeException("您登录过一个用户了，不能再登录其他用户！");
            }
            User user = userService.login(userName, password);

            // 用户信息
            UserContext userContext = new UserContext();
            userContext.setUser(user);
            UserType.parse(user.getUserType());
            boolean admin = UserType.parse(user.getUserType()) == UserType.Manager;
            userContext.setAdmin(admin);

            setUserContext(userContext);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return jsonWithException("登陆错误:" + e.getMessage());
        }
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/logout")
    @ResponseBody
    public JSONObject logout() {
        removeUserContext();

        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/manage")
    public String toManage() {
        checkPermission("您没有账号管理权限!");
        return VIEW_PATH + "/userMgr";
    }

    @RequestMapping(value = MODULE_NAME + "/to-add")
    public String toAdd() {
        return VIEW_PATH + "/userAdd";
    }

    @RequestMapping(value = MODULE_NAME + "/to-edit")
    public String toEdit() {
        return VIEW_PATH + "/userEdit";
    }

    @RequestMapping(value = MODULE_NAME + "/list", method = RequestMethod.POST)
    @ResponseBody
    public Grid<User> list(UserQueryParam queryParam) {
        DataSet<User> dataSet = userService.queryForManage(queryParam);
        return new GridAdapter<>(dataSet);
    }

    @RequestMapping(value = MODULE_NAME + "/detail")
    @ResponseBody
    public User detail(String id) {
        return userService.getById(id);
    }

    @RequestMapping(value = MODULE_NAME + "/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject add(User user) {
        checkPermission("您没有新增操作权限!");
        if (user.getUserType() == 0) {
            user.setUserType(UserType.Operator.getCode());
        }
        userService.save(user);
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/edit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject edit(User user) {
        checkPermission("您没有编辑操作权限!");
//        if (oldUser != null && UserType.isManager(oldUser.getUserType())) {
//            throw new InvalidException("不能编辑管理员!");
//        }
        userService.update(user);
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/delete")
    @ResponseBody
    public JSONObject delete(String id) {
        checkPermission("您没有删除操作权限!");
        User oldUser = userService.getById(id);
        if (oldUser != null && UserType.isManager(oldUser.getUserType())) {
            throw new InvalidException("不能删除管理员!");
        }
        userService.deleteById(id);
        return jsonWithStandardStatus();
    }

    @RequestMapping(value = MODULE_NAME + "/is-admin")
    @ResponseBody
    public JSONObject isAdmin() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("admin", isManager());
        return jsonObject;
    }
}
