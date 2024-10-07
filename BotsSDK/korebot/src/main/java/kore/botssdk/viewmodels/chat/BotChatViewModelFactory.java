package kore.botssdk.viewmodels.chat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kore.botssdk.bot.BotClient;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.repository.branding.BrandingRepository;
import kore.botssdk.repository.webhook.WebHookRepository;

public class BotChatViewModelFactory implements ViewModelProvider.Factory {
    final BrandingRepository repository;
    final WebHookRepository webHookRepository;
    Context context;
    BotChatViewListener chatView;
    BotClient botClient;

    public BotChatViewModelFactory(Context context, BotClient botClient, BotChatViewListener chatView, BrandingRepository repository, WebHookRepository webHookRepository) {
        this.repository = repository;
        this.context = context;
        this.chatView = chatView;
        this.webHookRepository = webHookRepository;
        this.botClient = botClient;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BotChatViewModel.class)) {
            return (T) new BotChatViewModel(context, botClient, chatView, repository, webHookRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
