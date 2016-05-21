package com.caul.modules.user.dao;

import cn.easybuild.pojo.DataSet;
import com.caul.modules.base.BaseDaoImpl;
import com.caul.modules.user.User;
import com.caul.modules.user.UserQueryParam;
import org.springframework.stereotype.Repository;

/**
 * Created by BlueDream on 2016-03-20.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    public static final String T_USER = "t_user";

    @Override
    protected String getTableName() {
        return T_USER;
    }

    @Override
    protected Class<User> getPojoClass() {
        return User.class;
    }

    @Override
    public User getByUsername(String username) {
        return selectOne(getPrefix() + "getByUsername", username);
    }

    @Override
    public DataSet<User> queryForManage(UserQueryParam queryParam) {
        return selectDataSet(getPrefix() + "queryForManage", queryParam);
    }
}
