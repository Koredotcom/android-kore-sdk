package kore.botssdk.BotDb;

import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class BotMessageDaoImpl extends BotDaoImpl<BotMessageDBModel, String> implements BotMessageDao {
    public BotMessageDaoImpl(ConnectionSource connectionSource, Class<BotMessageDBModel> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public BotChatDBResponse getLimitedBotMessages(Context context, String userId, int offset, int limit) {
        try {
            QueryBuilder<BotMessageDBModel, String> mBuilder = (QueryBuilder<BotMessageDBModel, String>) queryBuilder();
            mBuilder.orderBy("id", false);
            ArrayList<BotMessageDBModel> list = (ArrayList<BotMessageDBModel>) mBuilder.offset((long) offset).limit((long) limit).query();
            return new BotChatDBResponse(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<BotMessageDBModel> getAllBotMessages(Context context, String userId) {
        return null;
    }
}
