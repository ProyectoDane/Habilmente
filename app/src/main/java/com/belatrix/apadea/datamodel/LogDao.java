package com.belatrix.apadea.datamodel;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.belatrix.apadea.datamodel.Log;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LOG.
*/
public class LogDao extends AbstractDao<Log, Long> {

    public static final String TABLENAME = "LOG";

    /**
     * Properties of entity Log.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CurrentLevel = new Property(1, String.class, "CurrentLevel", false, "CURRENT_LEVEL");
        public final static Property Result = new Property(2, String.class, "Result", false, "RESULT");
        public final static Property Observations = new Property(3, String.class, "Observations", false, "OBSERVATIONS");
        public final static Property SessionId = new Property(4, long.class, "sessionId", false, "SESSION_ID");
    };

    private Query<Log> session_LogListQuery;

    public LogDao(DaoConfig config) {
        super(config);
    }
    
    public LogDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LOG' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CURRENT_LEVEL' TEXT," + // 1: CurrentLevel
                "'RESULT' TEXT," + // 2: Result
                "'OBSERVATIONS' TEXT," + // 3: Observations
                "'SESSION_ID' INTEGER NOT NULL );"); // 4: sessionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LOG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Log entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String CurrentLevel = entity.getCurrentLevel();
        if (CurrentLevel != null) {
            stmt.bindString(2, CurrentLevel);
        }
 
        String Result = entity.getResult();
        if (Result != null) {
            stmt.bindString(3, Result);
        }
 
        String Observations = entity.getObservations();
        if (Observations != null) {
            stmt.bindString(4, Observations);
        }
        stmt.bindLong(5, entity.getSessionId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Log readEntity(Cursor cursor, int offset) {
        Log entity = new Log( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CurrentLevel
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Result
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Observations
            cursor.getLong(offset + 4) // sessionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Log entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCurrentLevel(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setResult(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setObservations(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSessionId(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Log entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Log entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "logList" to-many relationship of Session. */
    public List<Log> _querySession_LogList(long sessionId) {
        synchronized (this) {
            if (session_LogListQuery == null) {
                QueryBuilder<Log> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.SessionId.eq(null));
                session_LogListQuery = queryBuilder.build();
            }
        }
        Query<Log> query = session_LogListQuery.forCurrentThread();
        query.setParameter(0, sessionId);
        return query.list();
    }

}