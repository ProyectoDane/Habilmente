package com.belatrix.apadea.datamanager;

import android.content.Context;

import com.belatrix.apadea.datamodel.Session;
import com.belatrix.apadea.datamodel.SessionDao;

import java.util.List;

public class SessionManager extends BaseManager {

    private static SessionManager sInstance = null;
    //private static Context context = null;

    private SessionDao mSessionDao;

    private SessionManager(Context context) {
        super(context);
        mSessionDao = getDaoSession().getSessionDao();
    }

    public static SessionManager getsInstance(Context pContext) {
        if (sInstance == null) {
            sInstance = new SessionManager(pContext);
        }
        //context = pContext;
        return sInstance;
    }

    public List<Session> fetchSessionsByUser(long userId) {
        return mSessionDao.queryBuilder().where(SessionDao.Properties.SubjectId.eq(userId)).list();
    }

    public Session fetchSessionById(long sessionId) {
        return mSessionDao.queryBuilder().where(SessionDao.Properties.Id.eq(sessionId)).unique();
    }

    public boolean hasSessionsOpened(long subjectId,long therapistId) {
        return mSessionDao.queryBuilder().where(SessionDao.Properties.SubjectId.eq(subjectId), SessionDao.Properties.TherapistId.eq(therapistId), SessionDao.Properties.Closed.eq(false)).count() > 0;
    }

    public Long getLastSession(Long subjectId, Long therapistId) {
        List<Session> sessions = mSessionDao.queryBuilder().where(SessionDao.Properties.SubjectId.eq(subjectId), SessionDao.Properties.TherapistId.eq(therapistId), SessionDao.Properties.Closed.eq(false)).limit(1).list();
        return sessions.get(sessions.size()-1).getId();
    }
}
