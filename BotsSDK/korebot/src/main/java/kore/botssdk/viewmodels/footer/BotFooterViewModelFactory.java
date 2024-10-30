package kore.botssdk.viewmodels.footer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import kore.botssdk.listener.ComposeFooterUpdate;

public class BotFooterViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    ComposeFooterUpdate chatView;

    public BotFooterViewModelFactory(Context context, ComposeFooterUpdate chatView) {
        this.context = context;
        this.chatView = chatView;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BotFooterViewModel.class)) {
            return (T) new BotFooterViewModel(context, chatView);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
