package com.belatrix.apadea.datamanager;

import android.content.Context;

import com.belatrix.apadea.datamodel.User;
import com.belatrix.apadea.datamodel.UserDao;

import java.util.ArrayList;

import de.greenrobot.dao.query.QueryBuilder;

public class UserManager extends BaseManager {

    private static UserManager sInstance = null;
    //private static Context context = null;

    private UserDao mUserDao;

    private UserManager(Context context) {
        super(context);
        mUserDao = getDaoSession().getUserDao();
    }

    public static UserManager getsInstance(Context pContext) {
        if (sInstance == null) {
            sInstance = new UserManager(pContext);
        }
        //context = pContext;
        return sInstance;
    }

    public ArrayList<User> fetchUsers() {
        return (ArrayList<User>) mUserDao.queryBuilder().orderAsc(UserDao.Properties.FirstName).list();
    }

    public ArrayList<User> fetchUsersByType(String userType) {
        QueryBuilder<User> queryBuilder = mUserDao.queryBuilder().where(UserDao.Properties.Type.eq(userType));

        return (ArrayList<User>) queryBuilder.orderAsc(UserDao.Properties.FirstName).list();
    }

    public User fetchUserById(long userId) {
        return mUserDao.queryBuilder().where(UserDao.Properties.Id.eq(userId)).unique();
    }
}
