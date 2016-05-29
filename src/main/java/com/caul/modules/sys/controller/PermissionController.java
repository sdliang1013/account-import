package com.caul.modules.sys.controller;

import com.caul.modules.base.AdminBaseController;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by BlueDream on 2016-05-29.
 */
@RestController
public class PermissionController extends AdminBaseController {

    private static final String MODULE_NAME = "/permission";

    @RequestMapping(value = MODULE_NAME + "/check")
    public JSONObject check() {
        checkPermission("对不起,您没有该操作权限!");
        return jsonWithStandardStatus();
    }
}
