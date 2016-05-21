<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="com.caul.modules.base.Constants" %>
<%@ page import="com.caul.sys.spring.SpringContextHolder" %>
<%@ page import="com.caul.modules.config.ApplicationConfig" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    if (path.length() > 1) {
        path = path + "/";
    }
    path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;

    Object currentUser = session.getAttribute(Constants.CURRENT_MEMBER);

    pageContext.setAttribute("currentUser", currentUser);
    pageContext.setAttribute("path", path);
    pageContext.setAttribute("skin", "default");//默认皮肤
    pageContext.setAttribute("encryptCode",
            SpringContextHolder.getBean(ApplicationConfig.class).getEncryptCode());
    pageContext.setAttribute("encryptPosition",
            SpringContextHolder.getBean(ApplicationConfig.class).getEncryptPosition());
%>