/*
package kore.botssdk.BotDb;

import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.Collection;
public interface BotBaseDao<T, ID> extends Dao<T, ID> {

    public void createIfNotExists(final Collection<T> dataList);

    public void createOrUpdate(final Collection<T> dataList);

    public Cursor getAllCursor() throws SQLException;

    public Cursor getCursor(PreparedQuery<T> query) throws SQLException;

    public boolean exists(T data, String columnName, Object value)
            throws SQLException;
}
*/
