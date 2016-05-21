package com.caul.modules.base;

import cn.easybuild.core.utils.UUIDGenerator;
import cn.easybuild.pojo.DataSet;
import cn.easybuild.pojo.QueryParam;
import cn.easybuild.pojo.StringPojo;
import com.caul.core.mybatis.StringPojoMyBatisDaoImpl;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jackson Fu on 14/11/13.
 */
public abstract class BaseDaoImpl<P extends StringPojo> extends StringPojoMyBatisDaoImpl<P> {

    /**
     * 获取要操作的表名
     *
     * @return
     */
    protected abstract String getTableName();

    @Override
    public String save(P pojo) {

        setUpPojoEntityId(pojo);

        return super.save(pojo);
    }

    /**
     * 如果pojo未设置id值，则生成UUID并设置到pojo中
     *
     * @param pojo
     */
    protected void setUpPojoEntityId(P pojo) {
        if (StringUtils.isEmpty(pojo.getId())) {
            pojo.setId(UUIDGenerator.generateUUID());
        }
    }

    public void toggleEnableStatus(String id, boolean enabled) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableName", getTableName());
        param.put("id", id);
        param.put("enabled", enabled);

        update("toggleEnableStatus", param);
    }

    /**
     * 结果集查询
     *
     * @param mappingId
     * @param param
     * @param <E>
     * @return
     */
    public <E> DataSet<E> selectDataSet(String mappingId, QueryParam param) {
        List<E> dataList = selectList(mappingId, param);
        return new DataSet<>(dataList, param.getPaging());
    }
}
