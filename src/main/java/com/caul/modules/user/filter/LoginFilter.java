package com.caul.modules.user.filter;

import com.caul.modules.base.Constants;
import com.caul.modules.base.UserContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    private String loginPath;
    private String blankUri;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        //先判断是否登陆
        UserContext uc = (UserContext) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (uc != null) {
            chain.doFilter(req, res);
            return;
        }
        //URI白名单过滤
        String requestURI = request.getRequestURI();
        if (StringUtils.isNotEmpty(blankUri)) {
            for (String uri : blankUri.split(",")) {
                if (StringUtils.isEmpty(uri)) {
                    continue;
                }
                if (PatternMatchUtils.simpleMatch(blankUri, requestURI)) {
                    chain.doFilter(req, res);
                    return;
                }
            }
        }
        //转到登陆界面
        response.sendRedirect(request.getContextPath() + loginPath);
    }

    private String buildRequestPath(HttpServletRequest request, String requestPath) {

        StringBuilder path = new StringBuilder();
        path.append(request.getScheme());
        path.append("://");
        path.append(request.getServerName());
        path.append(":");
        path.append(request.getServerPort());
        path.append(request.getContextPath());
        path.append("/").append(requestPath);

        return path.toString();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        loginPath = filterConfig.getInitParameter("loginPath");
        blankUri = filterConfig.getInitParameter("blankUri");
    }
}