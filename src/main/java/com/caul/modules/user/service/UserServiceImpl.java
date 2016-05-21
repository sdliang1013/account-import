package com.caul.modules.user.service;

import cn.easybuild.core.dao.AppBaseDao;
import cn.easybuild.core.exceptions.InvalidOperationException;
import cn.easybuild.core.service.StringPojoAppBaseServiceImpl;
import cn.easybuild.pojo.DataSet;
import com.caul.modules.config.ApplicationConfig;
import com.caul.modules.user.User;
import com.caul.modules.user.UserQueryParam;
import com.caul.modules.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by BlueDream on 2016-03-20.
 */
@Service
public class UserServiceImpl extends StringPojoAppBaseServiceImpl<User>
        implements UserService {

    private UserDao userDao;

    private ApplicationConfig applicationConfig;

    private static int position = 0;

    private static String code = null;

    private StringBuilder sb = new StringBuilder();

    @Autowired(required = false)
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired(required = false)
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    protected AppBaseDao<User, String> getDao() {
        return userDao;
    }

    @Override
    public User login(String username, String password) {
        User user = userDao.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("无效的用户名!");
        }
        if (!checkPwd(user.getPassword(), password)) {
            throw new RuntimeException("密码错误!");
        }

        return user;
    }

    @Override
    public String save(User entity) {
        User user = userDao.getByUsername(entity.getUserName());
        if (user != null) {
            throw new InvalidOperationException("该账号已存在!");
        }
        return super.save(entity);
    }

    @Override
    public DataSet<User> queryForManage(UserQueryParam queryParam) {
        return userDao.queryForManage(queryParam);
    }

    private boolean checkPwd(String dbPwd, String inputPwd) {
        return inputPwd.equals(resetPwd(dbPwd));
    }

    private String resetPwd(String dbPwd) {
        sb.delete(0, sb.length());
        sb.append(dbPwd.substring(0, getPosition())).append(
                getCode()).append(dbPwd.substring(getPosition()));
        return sb.toString();
    }

    public int getPosition() {
        if (position == 0) {
            position = applicationConfig.getEncryptPosition();
        }
        return position;
    }

    public String getCode() {
        if (code == null) {
            code = applicationConfig.getEncryptCode();
        }
        return code;
    }
}
