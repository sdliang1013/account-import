package com.caul.core.mybatis.interceptors;

import cn.easybuild.pojo.Paging;
import cn.easybuild.pojo.QueryParam;
import com.caul.core.jdbc.SQLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by jackson on 14-9-5.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PaginationInterceptor implements Interceptor {

    private static final String BOUND_SQL_META_KEY = "delegate.boundSql.sql";

    private Dialect dialect;
    private String statementIdPattern = ".*query.*";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        String statementId = mappedStatement.getId();
        if (!interceptStatement(statementId)) {
            return invocation.proceed();
        }

        BoundSql boundSql = statementHandler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();

        if (parameterObject == null) {
            return invocation.proceed();
        }

        Paging paging = null;

        if (parameterObject instanceof QueryParam) {
            QueryParam param = (QueryParam) parameterObject;
            paging = param.getPaging();
        } else if (parameterObject instanceof Paging) {
            paging = (Paging) parameterObject;
        } else if (parameterObject instanceof Map) {
            Map map = (Map) parameterObject;
            paging = (Paging) map.get("paging");
        }

        if (paging != null) {
            calculateRecords((Connection) invocation.getArgs()[0], statementHandler, boundSql, parameterObject, paging);

            String originalSql = (String) metaStatementHandler.getValue(BOUND_SQL_META_KEY);
            metaStatementHandler.setValue(BOUND_SQL_META_KEY, dialect.getLimitString(originalSql, paging.getStartRow(), paging.getPageSize()));
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

        String dialectClass = properties.getProperty("dialectClass");
        if (StringUtils.isNotEmpty(dialectClass)) {
            try {
                dialect = (Dialect) Class.forName(dialectClass).newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Plug-in [PaginationInterceptor] cannot create dialect instance by dialectClass: " + dialectClass);
            }
        } else {
            String dialectAlias = properties.getProperty("dialect");
            if (StringUtils.isNotEmpty(dialectAlias)) {
                dialect = DialectFactory.buildDialect(dialectAlias);
            }
        }

        if (dialect == null) {
            dialect = new MySqlDialect();
        }

        String statementIdPattern = properties.getProperty("statementIdPattern");
        if (StringUtils.isNotEmpty(statementIdPattern)) {
            this.statementIdPattern = statementIdPattern;
        }
    }

    private void calculateRecords(Connection connection, StatementHandler statementHandler, BoundSql boundSql, Object parameterObject, Paging paging) throws SQLException {

        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        String countSql = SQLUtils.generateCountSqlByParser(boundSql.getSql()); //SQLUtils.generateCountStr(boundSql.getSql());

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(countSql);
            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
            handler.setParameters(stmt);
            rs = stmt.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            paging.setRecords(count);

        } finally {
            closeStatement(rs, stmt);
        }
    }

    private void closeStatement(ResultSet rs, Statement statement) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // ignore
        }
    }

    private boolean interceptStatement(String id) {

        Pattern pattern = Pattern.compile(statementIdPattern);
        return pattern.matcher(id).matches();
    }

}
