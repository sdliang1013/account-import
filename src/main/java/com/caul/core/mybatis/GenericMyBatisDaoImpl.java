package com.caul.core.mybatis;

import cn.easybuild.pojo.Pojo;

import java.io.Serializable;

/**
 * Created by Jackson Fu on 14/11/13.
 */
public abstract class GenericMyBatisDaoImpl<T extends Serializable, P extends Pojo<T>>
        extends MyBatisDaoImpl {

    protected abstract Class<P> getPojoClass();

    /**
     * 保存领域对象
     *
     * @param pojo
     * @return
     */
    public T save(P pojo) {
        insert(insertPojoStatement(), pojo);
        return pojo.getId();
    }

    public void update(P pojo) {
        update(updatePojoStatement(), pojo);
    }

    /**
     * 根据代理主键，获取领域对象
     *
     * @param id
     * @return
     */
    public P getEntity(T id) {
        return selectOne(getEntityByIdStatement(), id);
    }

    /**
     * 根据代理主键删除记录
     *
     * @param id
     */
    public void deleteById(T id) {
        delete(deletePojoByIdStatement(), id);
    }

    protected String insertPojoStatement() {
        return "insert" + getPojoClass().getSimpleName();
    }

    protected String getEntityByIdStatement() {
        return "get" + getPojoClass().getSimpleName() + "ById";
    }

    protected String updatePojoStatement() {
        return "update" + getPojoClass().getSimpleName();
    }

    protected String deletePojoByIdStatement() {
        return "delete" + getPojoClass().getSimpleName() + "ById";
    }

    private static final String SPLIT_PACKAGE = ".";

    /**
     * 名称前缀
     *
     * @return
     */
    protected String getPrefix() {
        return getPojoClass().getName() + SPLIT_PACKAGE;
    }
}
