package com.caul.core.mybatis;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Jackson Fu on 14-10-16.
 */
public class MyBatisDaoImpl {

    private SqlSessionTemplate sessionTemplate;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory factory) {
        sessionTemplate = new SqlSessionTemplate(factory);
    }

    protected int insert(String statement) {
        return sessionTemplate.insert(statement);
    }

    protected int insert(String statement, Object parameter) {
        return sessionTemplate.insert(statement, parameter);
    }

    protected int update(String statement) {
        return  sessionTemplate.update(statement);
    }

    protected int update(final String statement, final Object parameter) {
        return sessionTemplate.update(statement, parameter);
    }

    public int delete(String statement) {
        return sessionTemplate.delete(statement);
    }

    public int delete(String statement, Object parameter) {
        return sessionTemplate.delete(statement, parameter);
    }

    public <T> T selectOne(String statement) { 
        return sessionTemplate.selectOne(statement);
    }

    public <T> T selectOne(String statement, Object parameter) {
        return sessionTemplate.selectOne(statement, parameter);
    }

    public <K, V> Map<K,V> selectMap(String statement, String mapKey) {
        return sessionTemplate.selectMap(statement, mapKey);
    }

    public <K, V> Map<K,V> selectMap(String statement, Object parameter, String mapKey) { 
        return sessionTemplate.selectMap(statement, parameter, mapKey);
    }

    public <K, V> Map<K,V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return sessionTemplate.selectMap(statement, parameter, mapKey, rowBounds);
    }

    public <E> List<E> selectList(String statement) {
        return sessionTemplate.selectList(statement);
    }

    public <E> List<E> selectList(String statement, Object parameter) {
        return sessionTemplate.selectList(statement, parameter);
    }

    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return sessionTemplate.selectList(statement, parameter, rowBounds);
    }

    public void select(String statement, ResultHandler handler) {
        sessionTemplate.select(statement, handler);
    }

    public void select(String statement, Object parameter, ResultHandler handler) {
        sessionTemplate.select(statement, parameter, handler);
    }

    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        sessionTemplate.select(statement, parameter, rowBounds, handler);
    }
}
