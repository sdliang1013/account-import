package com.caul.modules.user.service;

import cn.easybuild.core.service.StringPojoAppBaseService;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.user.User;
import com.caul.modules.user.UserQueryParam;

/**
 * Created by BlueDream on 2016-03-20.
 */
public interface UserService extends StringPojoAppBaseService<User> {

    User login(String username, String password);

    DataSet<User> queryForManage(UserQueryParam queryParam);
}
