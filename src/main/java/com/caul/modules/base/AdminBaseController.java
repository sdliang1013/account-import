package com.caul.modules.base;

import cn.easybuild.core.exceptions.InvalidException;
import com.caul.modules.config.ApplicationConfig;
import com.caul.sys.spring.JsonBasedExceptionHandlerAdvice;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by Jackson Fu on 14/11/7.
 */
@RequestMapping(value = AdminBaseController.ADMIN_BASE_URI)
public abstract class AdminBaseController extends JsonBasedExceptionHandlerAdvice {

    protected Logger logger = Logger.getLogger(AdminBaseController.class);

    /**
     * 管理模块下URI前缀
     */
    public static final String ADMIN_BASE_URI = "/mgr";

    public static final String SUCCESS = "1";
    public static final String ERROR = "0";

    private HttpSession httpSession;

    private ApplicationConfig applicationConfig;

    @Autowired(required = false)
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    @Autowired(required = false)
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    protected void setSessionAttribute(String key, Object value) {
        httpSession.setAttribute(key, value);
    }

    protected <T> T getSessionAttribute(String key) {
        return (T) httpSession.getAttribute(key);

    }

    protected void removeSessionAttribute(String key) {
        httpSession.removeAttribute(key);

    }

    /**
     * 保存已登录用户在Session中
     *
     * @param userContext
     */
    protected void setUserContext(UserContext userContext) {
        setSessionAttribute(Constants.CURRENT_MEMBER, userContext);
    }

    /**
     * 获取已登录用户
     */
    protected UserContext getUserContext() {
        return getSessionAttribute(Constants.CURRENT_MEMBER);
    }

    /**
     * 移除Session中的用户信息
     */
    protected void removeUserContext() {
        removeSessionAttribute(Constants.CURRENT_MEMBER);
    }

    protected boolean isManager() {
        return getUserContext().isAdmin();
    }

    protected boolean checkPermission(String errMsg) {
        if (!isManager()) {
            throw new InvalidException(errMsg);
        }
        return true;
    }

}
