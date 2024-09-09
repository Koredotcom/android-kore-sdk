package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;

public class ClockTemplateHolder extends BaseViewHolder {
    private final String MERIDIAN_AM = "AM";
    private final String MERIDIAN_PM = "PM";
    private final AppCompatSeekBar seekbarHours;
    private final AppCompatSeekBar seekbarMinutes;
    private final TextView hoursText;
    private final TextView minutesText;
    private final TextView amPm;
    private final TextView confirm;

    private String msgId;

    public static ClockTemplateHolder getInstance(ViewGroup parent) {
        return new ClockTemplateHolder(createView(R.layout.template_clock, parent));
    }

    private ClockTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        seekbarHours = itemView.findViewById(R.id.seekbar_hours);
        seekbarMinutes = itemView.findViewById(R.id.seekbar_minutes);
        hoursText = itemView.findViewById(R.id.hours_text);
        minutesText = itemView.findViewById(R.id.minutes_text);
        amPm = itemView.findViewById(R.id.am_pm);
        confirm = itemView.findViewById(R.id.confirm);
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        msgId = ((BotResponse) baseBotMessage).getMessageId();
        setSeekbarListener(seekbarHours, seekbarMinutes);
        setSeekbarListener(seekbarMinutes, seekbarHours);
        Map<String, Object> contentState = ((BotResponse) baseBotMessage).getContentState();
        String selectedTime = contentState != null ? (String) contentState.get(BotResponse.SELECTED_TIME) : itemView.getContext().getString(R.string.default_click_time);
        if (!selectedTime.isEmpty()) {
            String[] currentTime = selectedTime.split(":");
            hoursText.setText(currentTime[0]);
            minutesText.setText(currentTime[1]);
            amPm.setText(currentTime[2]);
            int hours = Integer.parseInt(currentTime[0]);
            int progress;

            if (hours == 12 && currentTime[2].equals(MERIDIAN_AM)) {
                progress = 0;
            } else if (currentTime[2].equals(MERIDIAN_PM) && hours < 12) {
                progress = hours + 12;
            } else {
                progress = hours;
            }
            seekbarHours.setProgress(progress);
            seekbarMinutes.setProgress(Integer.parseInt(currentTime[1]));
        }
        confirm.setOnClickListener(view -> {
            String msg = hoursText.getText() + ":" + minutesText.getText() + " " + amPm.getText();
            composeFooterInterface.onSendClick(msg, msg, false);
        });
        confirm.setClickable(isLastItem());
        seekbarHours.setEnabled(isLastItem());
        seekbarMinutes.setEnabled(isLastItem());
    }

    private void setSeekbarListener(SeekBar seekbar, SeekBar otherSeekBar) {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int p1, boolean p2) {
                if (seekBar.isSelected()) {
                    if (seekbar.getId() == seekbarHours.getId()) {
                        String hours = "";
                        if (p1 == 0) {
                            hours = "12";
                        } else if (p1 < 10) {
                            hours = "0" + p1;
                        } else if (p1 <= 12) {
                            hours = "" + p1;
                        } else if ((p1 - 12) < 10) {
                            hours = "0" + (p1 - 12);
                        } else {
                            hours = "" + (p1 - 12);
                        }
                        hoursText.setText(hours);
                        amPm.setText((p1 >= 12) ? MERIDIAN_PM : MERIDIAN_AM);
                    } else {
                        minutesText.setText((p1 < 10) ? "0" + p1 : "" + p1);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (!isLastItem()) return;
                seekbar.setSelected(true);
                otherSeekBar.setSelected(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!isLastItem()) return;
                String selTime = hoursText.getText().toString() + ":" + minutesText.getText().toString() + ":" + amPm.getText().toString();
                contentStateListener.onSaveState(msgId, selTime, BotResponse.SELECTED_TIME);
            }
        });
    }
}
