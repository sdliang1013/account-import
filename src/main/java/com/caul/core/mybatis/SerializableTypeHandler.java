package com.caul.core.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jackson Fu on 14-10-16.
 */
public class SerializableTypeHandler extends BaseTypeHandler<Serializable> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Serializable parameter, JdbcType jdbcType) throws SQLException {

        if (parameter instanceof String) {
            ps.setString(i, (String)parameter);
        } else {
            ps.setObject(i, parameter);
        }
    }

    @Override
    public Serializable getNullableResult(ResultSet rs, String columnName) throws SQLException {

        Object ret = rs.getObject(columnName);
        return (ret instanceof  Serializable)
                ? (Serializable)ret
                : null;
    }

    @Override
    public Serializable getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        Object ret = rs.getObject(columnIndex);
        return (ret instanceof  Serializable)
                ? (Serializable)ret
                : null;
    }

    @Override
    public Serializable getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

        Object ret = cs.getObject(columnIndex);
        return (ret instanceof  Serializable)
                ? (Serializable)ret
                : null;
    }
}
