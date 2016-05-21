package com.caul.core.mybatis.interceptors;

import cn.easybuild.core.exceptions.InvalidException;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Jackson Fu on 15/1/5.
 */
public class DialectFactory {

    public static Dialect buildDialect(String alias) {

        if (StringUtils.isEmpty(alias)) {
            throw new InvalidException("Invaid dialect alias");
        }

        if ("mysql".equals(alias.toLowerCase())) {
            return new MySqlDialect();
        }

        throw new InvalidException("Invaid dialect alias");
    }
}
