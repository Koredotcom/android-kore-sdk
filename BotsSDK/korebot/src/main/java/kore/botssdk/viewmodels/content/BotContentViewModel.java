package kore.botssdk.viewmodels.content;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.repository.history.HistoryRepository;
import kore.botssdk.retroresponse.ServerBotMsgResponse;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.viewmodels.BaseViewModel;

@SuppressWarnings("UnKnownNullness")
public class BotContentViewModel extends BaseViewModel<BotContentFragmentUpdate> {
    private Context context;
    private final BotContentFragmentUpdate chatView;
    private final HistoryRepository repository;
    private int offset = 0;

    public BotContentViewModel(Context context, BotContentFragmentUpdate chatView, HistoryRepository repository) {
        this.context = context.getApplicationContext();
        this.repository = repository;
        this.chatView = chatView;
    }

    public void loadChatHistory(final int _offset, final int limit, String jwt) {
        Observer<ServerBotMsgResponse> observer = new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ServerBotMsgResponse re) {
                ArrayList<BaseBotMessage> list = re.getBotMessages();
                offset = (!SDKConfiguration.Client.isWebHook ? _offset : 0) + re.getOriginalSize();

                if (list != null && !list.isEmpty()) {
                    chatView.onChatHistory(list, offset, _offset == 0);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                chatView.onChatHistory(null, 0, false);
            }

            @Override
            public void onComplete() {
                chatView.onChatHistory(null, 0, false);
            }
        };
        if (!SDKConfiguration.Client.isWebHook)
            repository.getHistoryRequest(_offset, limit, jwt).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        else
            repository.getWebHookHistoryRequest(_offset, limit, jwt).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void loadReconnectionChatHistory(final int _offset, final int limit, String jwt, ArrayList<BaseBotMessage> baseBotMessageList) {
        Observer<ServerBotMsgResponse> observer = new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull ServerBotMsgResponse re) {
                ArrayList<BaseBotMessage> list;
                list = re.getBotMessages();
                offset = _offset + re.getOriginalSize();

                if (list != null && !list.isEmpty()) {
                    int pos = 0;
                    ArrayList<BaseBotMessage> botResp = new ArrayList<>();
                    ArrayList<BaseBotMessage> requiredList = new ArrayList<>();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof BotResponse) {
                            botResp.add(list.get(i));
                        }
                    }

                    for (int i = 0; i < baseBotMessageList.size(); i++) {
                        for (int j = 0; j < botResp.size(); j++) {
                            if (baseBotMessageList.get(i).getCreatedInMillis() == botResp.get(j).getCreatedInMillis()) {
                                pos = j;
                            }
                        }
                    }

                    if (pos != 0 && (pos + 1) < botResp.size()) {
                        for (int i = pos + 1; i < botResp.size(); i++) {
                            if (((BotResponse) botResp.get(i)).getMessage() != null && ((BotResponse) botResp.get(i)).getMessage().get(0) != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent() != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent().getPayload() != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent().getPayload().getPayload() != null && ((BotResponse) botResp.get(i)).getMessage().get(0).getComponent().getPayload().getPayload().getTemplate_type().equalsIgnoreCase(BotResponse.LIVE_AGENT))
                                ((BotResponse) botResp.get(i)).setFromAgent(true);
                            requiredList.add(botResp.get(i));
                        }
                    } else {
                        requiredList.addAll(list);
                    }

                    chatView.onReconnectionChatHistory(requiredList, offset, false);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                chatView.onChatHistory(null, 0, false);
            }

            @Override
            public void onComplete() {
                chatView.onChatHistory(null, 0, false);
            }
        };
        if (!SDKConfiguration.Client.isWebHook)
            repository.getHistoryRequest(_offset, limit, jwt).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        else
            repository.getWebHookHistoryRequest(_offset, limit, jwt).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public CalendarConstraints.Builder minRange(String startDate, String date, String format) {
        if (StringUtils.isNullOrEmpty(date) && StringUtils.isNullOrEmpty(startDate)) {
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(DateValidatorPointForward.now());

            return constraintsBuilderRange;
        } else if (!StringUtils.isNullOrEmpty(startDate) && !StringUtils.isNullOrEmpty(date)) {
            CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(DateUtils.getDateFromFormat(startDate, format, 0));
            CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(DateUtils.getDateFromFormat(date, format, 1));

            ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
            listValidators.add(dateValidatorMin);
            listValidators.add(dateValidatorMax);

            CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(validators);

            return constraintsBuilderRange;
        } else if (!StringUtils.isNullOrEmpty(startDate)) {
            CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(DateUtils.getDateFromFormat(startDate, format, 0));

            ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
            listValidators.add(dateValidatorMin);

            CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(validators);

            return constraintsBuilderRange;
        } else if (!StringUtils.isNullOrEmpty(date)) {
            CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(DateUtils.getDateFromFormat(date, format, 1));

            ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
            listValidators.add(dateValidatorMax);

            CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(validators);

            return constraintsBuilderRange;
        } else {
            CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
            constraintsBuilderRange.setValidator(DateValidatorPointForward.now());

            return constraintsBuilderRange;
        }
    }
}
