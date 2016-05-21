package com.caul.core.mybatis.interceptors;

/**
 * Created by Jackson Fu on 15/1/5.
 */
public interface Dialect {

    /**
     * 将sql包装成数据库支持的特有查询语句
     *
     * @param sql SQL语句
     * @param offset 开始位置
     * @param limit 每页显示的记录数
     * @return 数据库专属分页查询sql
     */
    public String getLimitString(String sql, int offset, int limit);
}
