package kore.botssdk.viewmodels.chat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kore.botssdk.bot.BotClient;
import kore.botssdk.listener.BotChatViewListener;

public class BotChatViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    BotChatViewListener chatView;
    BotClient botClient;

    public BotChatViewModelFactory(Context context, BotClient botClient, BotChatViewListener chatView) {
        this.context = context;
        this.chatView = chatView;
        this.botClient = botClient;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BotChatViewModel(context, botClient, chatView);
    }
}
