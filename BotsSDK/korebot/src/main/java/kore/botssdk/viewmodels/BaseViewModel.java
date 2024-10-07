package kore.botssdk.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

import kore.botssdk.listener.BaseView;

public class BaseViewModel<N extends BaseView> extends ViewModel {
    private WeakReference<N> view = new WeakReference<>(null);

    protected void setView(N view) {
        this.view = new WeakReference<>(view);
    }

    protected N getView() {
        return view.get();
    }
}
