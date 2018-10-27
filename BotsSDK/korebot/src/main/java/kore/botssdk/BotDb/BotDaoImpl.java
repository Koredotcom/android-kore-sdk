/*
package kore.botssdk.BotDb;

import android.database.Cursor;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;


public class BotDaoImpl<T, ID> extends BaseDaoImpl<T, ID> implements BotBaseDao<T, ID> {

    protected final String LOG_TAG = getClass().getSimpleName();

    public BotDaoImpl(ConnectionSource connectionSource, Class<T> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public T createIfNotExists(T data) throws SQLException {
        if (data instanceof BotDbModel)
            ((BotDbModel) data).processForPersistance();
        return super.createIfNotExists(data);
    }

    public int create(T data) throws SQLException {
        if (data instanceof BotDbModel)
            ((BotDbModel) data).processForPersistance();
        return super.create(data);
    }

    public int update(T data) throws SQLException {
        if (data instanceof BotDbModel)
            ((BotDbModel) data).processForPersistance();
        return super.update(data);
    }

    public T createIfNotExists(T data, String columnName, Object value)
            throws SQLException {
        List<T> tObjects = queryBuilder().where().eq(columnName, value).query();
        if (tObjects.size() > 0) {
            update(data);
        } else {
            create(data);
        }
        return super.createIfNotExists(data);
    }

    public boolean exists(T data, String columnName, Object value)
            throws SQLException {
        return queryForEq(columnName, value).size() > 0;
    }

    public void createIfNotExists(final Collection<T> dataList) {
        try {
            TransactionManager.callInTransaction(getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (T koreObject : dataList) {
                                try {
                                    createIfNotExists(koreObject);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }
                    });
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public CreateOrUpdateStatus createOrUpdate(T data) throws SQLException {
        */
/*if (data instanceof BotDbModel)
            ((BotDbModel) data).processForPersistance();*//*

        return super.createOrUpdate(data);
    }

    public void createOrUpdate(final Collection<T> dataList) {
        try {
            TransactionManager.callInTransaction(getConnectionSource(),
                    new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (T koreObject : dataList) {
                                try {
                                    createOrUpdate(koreObject);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (IllegalStateException ise) {
                                    ise.printStackTrace();
                                }
                            }
                            return null;
                        }
                    });
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



    public int update(T data, String columnName, Object value)
            throws SQLException {
        updateBuilder().where().eq(columnName, value);
        updateBuilder().updateColumnValue(columnName, value);
        int ret = updateBuilder().update();
        updateBuilder().reset();
        return ret;
    }

    public Cursor getCursor(PreparedQuery<T> query) throws SQLException {
        if (!connectionSource.isOpen())
            return null;
        DatabaseConnection readOnlyConn = connectionSource
                .getReadOnlyConnection();
        AndroidCompiledStatement stmt = (AndroidCompiledStatement) query
                .compile(readOnlyConn, StatementBuilder.StatementType.SELECT);

        Cursor base = stmt.getCursor();
        */
/*String idColumnName = getTableInfo().getIdField().getColumnName();
        int idColumnIndex = base.getColumnIndex(idColumnName);
        KoreNoIdCursorWrapper wrapper = new KoreNoIdCursorWrapper(base, idColumnIndex);*//*

        return base;
    }

    public Cursor getAllCursor() throws SQLException {
        return getCursor(queryBuilder().prepare());
    }
}
*/
