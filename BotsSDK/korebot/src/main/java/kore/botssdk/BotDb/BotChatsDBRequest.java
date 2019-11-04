/*
package kore.botssdk.BotDb;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;
public class BotChatsDBRequest extends SpiceRequest<BotChatDBResponse> {

    private int limit;
    private int offset;
    private String userId;
    private Context mContext;

    public BotChatsDBRequest(Context mContext, String userId, int offset, int limit) {
        super(BotChatDBResponse.class);
        this.userId = userId;
        this.mContext = mContext;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public BotChatDBResponse loadDataFromNetwork() throws Exception {
        DBHelper helper = new DBHelper(mContext);
        BotMessageDao botMessageDao = helper.getBotMessageDao();
        BotChatDBResponse botMessageDBModelList;

        botMessageDBModelList = botMessageDao.getLimitedBotMessages(mContext,userId,offset,limit);
        helper.close();

        return botMessageDBModelList;
    }
}
*/
