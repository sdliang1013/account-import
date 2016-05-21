package com.caul.sys.spring;

import cn.easybuild.core.json.CustomJsonConfig;
import cn.easybuild.core.json.JsonResponseDef;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Jackson Fu on 14/10/31.
 */
public class JsonBasedController {

    /**
     * 获取标准输出JSON
     *
     * @return
     */
    public JSONObject jsonWithStandardStatus() {
        JSONObject json = new JSONObject();
        json.put(JsonResponseDef.STATUS_NODE_NAME, JsonResponseDef.OK_STATUS);

        return json;
    }

    /**
     * 输出异常节点JSON信息
     *
     * @param exceptionName
     */
    public JSONObject jsonWithException(String exceptionName) {
        JSONObject json = jsonWithStandardStatus();
        json.put(JsonResponseDef.EXCEPTION_NODE_NAME, exceptionName);

        return json;
    }

    /**
     * 输出异常节点JSON信息
     *
     * @param e
     */
    public JSONObject jsonWithException(Exception e) {
        JSONObject json = jsonWithStandardStatus();
        json.put(JsonResponseDef.EXCEPTION_NODE_NAME, e.getClass().getSimpleName());

        if (StringUtils.isNotEmpty(e.getMessage())) {
            json.put(JsonResponseDef.EXCEPTION_MESSAGE_NODE_NAME, e.getMessage());
        }

        return json;
    }

    /**
     * 获取系统异常节点JSON信息
     */
    public JSONObject jsonWithSystemException() {
        JSONObject json = new JSONObject();
        json.put(JsonResponseDef.STATUS_NODE_NAME, JsonResponseDef.SYSTEM_EXCEPTION_STATUS);

        return json;
    }

    /**
     * 获取系统异常节点JSON信息
     */
    public JSONObject jsonWithSystemException(Exception e) {
        JSONObject json = new JSONObject();
        json.put(JsonResponseDef.STATUS_NODE_NAME, JsonResponseDef.SYSTEM_EXCEPTION_STATUS);
        json.put(JsonResponseDef.EXCEPTION_NODE_NAME, e.getClass().getSimpleName());

        if (StringUtils.isNotEmpty(e.getMessage())) {
            json.put(JsonResponseDef.EXCEPTION_MESSAGE_NODE_NAME, e.getMessage());
        }

        return json;
    }

    /**
     * 获取其它状态的JSON信息
     *
     * @param status
     */
    public JSONObject jsonWithSpecialStatus(int status) {
        JSONObject json = new JSONObject();
        json.put(JsonResponseDef.STATUS_NODE_NAME, status);

        return json;
    }

    /**
     * 输出单一记录节点的JSON信息
     *
     * @param record
     */
    public JSONObject jsonWithRecord(JSONObject record) {
        JSONObject json = jsonWithStandardStatus();
        json.put(JsonResponseDef.RECORD_NODE_NAME, record);

        return json;
    }

    /**
     * 输出单一记录节点的JSON信息
     *
     * @param recordset
     */
    public JSONObject jsonWithRecordSet(JSONArray recordset) {
        JSONObject json = jsonWithStandardStatus();
        json.put(JsonResponseDef.RECORDSET_NODE_NAME, recordset);

        return json;
    }

    /**
     * 获取JSONObject解析配置对象
     *
     * @return
     */
    protected JsonConfig getJsonConfig() {
        return new CustomJsonConfig();
    }

    /**
     * @param clz
     * @return
     */
    protected JsonConfig getJsonConfig(Class<?> clz) {
        return new CustomJsonConfig(clz);
    }

    /**
     * @param clz
     * @param includeFields
     * @return
     */
    protected JsonConfig getJsonConfig(Class<?> clz, String[] includeFields) {
        return new CustomJsonConfig(clz, includeFields);
    }

}
