package com.caul.sys.spring;

import cn.easybuild.core.exceptions.BusinessException;
import cn.easybuild.core.exceptions.SystemException;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jackson Fu on 14/11/7.
 */
public abstract class JsonBasedExceptionHandlerAdvice extends JsonBasedController {

    private static final Log LOG = LogFactory.getLog(JsonBasedExceptionHandlerAdvice.class);

    /**
     * 处理业务异常
     *
     * @param e
     * @param response
     * @param request
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public JSONObject onBusinessException(BusinessException e, HttpServletResponse response, HttpServletRequest request) {
        LOG.error("业务异常！", e);
        return jsonWithException(e);
    }

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    public JSONObject onSystemException(SystemException e, HttpServletResponse response, HttpServletRequest request) {
        LOG.error("系统异常！", e);
        return jsonWithSystemException();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JSONObject onUnknowException(Exception e, HttpServletResponse response, HttpServletRequest request) {
        LOG.error("未知异常！", e);
        return jsonWithSystemException();
    }
}
