package com.caul.modules.sys.controller;

import cn.easybuild.core.exceptions.InvalidException;
import com.caul.modules.account.SendState;
import com.caul.modules.base.AdminBaseController;
import com.caul.modules.base.EnumCommon;
import com.caul.modules.base.IEnum;
import com.caul.modules.user.UserType;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
public class EnumController extends AdminBaseController {

    private static final String MODULE_NAME = "/sys/enum";

    private static final Map<String, List<? extends IEnum>> enumMap = new ConcurrentHashMap<>();

    private static final String[] DICTIONARY_LIST_FILE = {
            "code", "text"
    };

    /**
     * 数据字典列表
     *
     * @param enumName
     * @return
     */
    @RequestMapping(value = MODULE_NAME + "/load")
    @ResponseBody
    public Object queryDictionaryList(String enumName) {

        if (StringUtils.isEmpty(enumName)) {
            throw new InvalidException("枚举名称为空!");
        }

        List<EnumCommon> enumList = new ArrayList<>();
        List<? extends IEnum> tmpList = enumMap.get(enumName);
        if (CollectionUtils.isNotEmpty(tmpList)) {
            for (IEnum iEnum : tmpList) {
                enumList.add(new EnumCommon(iEnum.getCode(), iEnum.getText()));
            }
        }
        JsonConfig config = getJsonConfig(EnumCommon.class, DICTIONARY_LIST_FILE);

        return JSONArray.fromObject(enumList, config);
    }

    static {
        enumMap.put("userType", UserType.getValues());
        enumMap.put("sendState", SendState.getValues());
    }
}