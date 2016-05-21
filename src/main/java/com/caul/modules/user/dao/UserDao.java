package com.caul.modules.user.dao;

import cn.easybuild.core.dao.StringPojoAppBaseDao;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.user.User;
import com.caul.modules.user.UserQueryParam;

/**
 * Created by BlueDream on 2016-03-20.
 */
public interface UserDao extends StringPojoAppBaseDao<User> {
    User getByUsername(String username);

    DataSet<User> queryForManage(UserQueryParam queryParam);
}
