package com.caul.core.mybatis.interceptors;

/**
 * Created by Jackson Fu on 15/1/5.
 */
public class MySqlDialect implements Dialect {

    private static final String LIMIT_SQL = "%s limit %d, %d";

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return String.format(LIMIT_SQL, sql, offset, limit);
    }
}
