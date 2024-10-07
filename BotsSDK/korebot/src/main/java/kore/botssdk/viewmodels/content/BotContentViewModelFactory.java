package kore.botssdk.viewmodels.content;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.repository.history.HistoryRepository;
import kore.botssdk.viewmodels.chat.BotChatViewModel;

public class BotContentViewModelFactory implements ViewModelProvider.Factory {
    final HistoryRepository repository;
    Context context;
    BotContentFragmentUpdate chatView;

    public BotContentViewModelFactory(Context context, BotContentFragmentUpdate chatView, HistoryRepository historyRepository) {
        this.repository = historyRepository;
        this.context = context;
        this.chatView = chatView;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BotContentViewModel.class)) {
            return (T) new BotContentViewModel(context, chatView, repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
