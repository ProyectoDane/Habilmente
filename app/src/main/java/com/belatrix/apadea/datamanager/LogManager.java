package com.belatrix.apadea.datamanager;

import android.content.Context;

import com.belatrix.apadea.datamodel.Log;
import com.belatrix.apadea.datamodel.LogDao;

import java.util.List;


public class LogManager extends BaseManager {

    private static LogManager sInstance = null;
    //private static Context context = null;

    private LogDao mLogDao;

    private LogManager(Context context) {
        super(context);
        mLogDao = getDaoSession().getLogDao();
    }

    public static LogManager getsInstance(Context pContext) {
        if (sInstance == null) {
            sInstance = new LogManager(pContext);
        }
        //context = pContext;
        return sInstance;
    }

    public List<Log> getLogBySession (long sessionId) {
        return mLogDao.queryBuilder().where(LogDao.Properties.SessionId.eq(sessionId)).list();
    }
}
