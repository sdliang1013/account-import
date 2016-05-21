package com.caul.sys.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public final class SpringContextHolder implements ApplicationContextAware,
        DisposableBean {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();

        Map<String, T> map = applicationContext.getBeansOfType(clazz);
        if (map.size() == 1) {
            T obj = null;
            for (String o : map.keySet()) {
                obj = map.get(o);
            }
            return obj;
        } else {
            if (map.size() == 0) {
                throw new IllegalStateException("在Spring容器中没有类型为"
                        + clazz.getName() + "的Bean");
            } else {
                throw new IllegalStateException("在Spring容器中有多个类型为"
                        + clazz.getName() + "的Bean");
            }
        }
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBeansOfType(clazz);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException(
                    "applicaitonContext注册失败,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        applicationContext = null;
    }

}
