package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.fragments.FeedbackSheetFragment;
import com.kore.ai.widgetsdk.interfaces.FeedBackButtonState;
import com.kore.ai.widgetsdk.models.FeedbackDataResponse;
import com.kore.ai.widgetsdk.models.FeedbakResponse;

import java.util.ArrayList;
import java.util.List;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.FeedBackViewHolder> {


    Context context;
    List<FeedbackDataResponse.Option> options;
    FeedbackSheetFragment object;
    FeedBackButtonState feedBackButtonState;
    RecyclerView recyclerView;

    public FeedBackAdapter(Context context, List<FeedbackDataResponse.Option> options, RecyclerView recyclerView) {
        this.context = context;
        // this.listData = listData;
        this.options = options;
        this.recyclerView = recyclerView;
        notifyDataSetChanged();

    }


    public void refreshButtonState() {
        boolean enableButton = false;
        if (options != null) {
            for (FeedbackDataResponse.Option opt : options) {
                if (opt.isUserAction()) {
                    enableButton = true;
                    break;
                }
            }
        }
        if (options == null || options.size() < 1) {
            enableButton = true;
        }

        feedBackButtonState.notify(true);

    }

    @NonNull
    @Override
    public FeedBackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feedback_item_view, parent, false);
        return new FeedBackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackViewHolder holder, int position) {

        holder.tv_question.setText(options.get(position).getLabel());
        holder.checkBox_btn.setChecked(options.get(position).isUserAction());


        if (options.get(position).getAction().equalsIgnoreCase("inputText")) {
            holder.edt_freetext.setVisibility(View.VISIBLE);
            holder.checkBox_btn.setVisibility(View.GONE);
            holder.tv_question.setVisibility(View.GONE);
            holder.edt_freetext.setHint(options.get(position).getPlaceholder());
            holder.edt_freetext.setText(options.get(position).getUserAnswer());

            //   if (getItemCount() == 1) {
            options.get(position).setUserAction(true);
            if (getItemCount() == 1) {
                holder.edt_freetext.requestFocus();
            } else {
                holder.edt_freetext.clearFocus();
            }
            // }
            // refreshButtonState();
        } else {
            holder.checkBox_btn.setVisibility(View.VISIBLE);
            holder.tv_question.setVisibility(View.VISIBLE);
            holder.edt_freetext.setVisibility(View.GONE);
        }


        holder.edt_freetext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                }
            }
        });

        holder.edt_freetext.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (holder.edt_freetext.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        holder.checkBox_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                options.get(position).setUserAction(holder.checkBox_btn.isChecked());

                if (options.get(position).getAction().equalsIgnoreCase("inputText")) {
                    holder.edt_freetext.setVisibility(holder.checkBox_btn.isChecked() ? View.VISIBLE : View.GONE);
                }
                refreshButtonState();
            }
        });


        holder.edt_freetext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String data = s.toString().trim();
                options.get(position).setUserAnswer(data);
                /*if (StringUtils.isNullOrEmptyWithTrim(data)) {
                  // options.get(position).setUserAction(false);
                    refreshButtonState();

                } else {
                   // options.get(position).setUserAction(true);
                    refreshButtonState();
                }*/

                // listData.get(position).setAnswerText(data);
                // answers.put(question, data);
            }
        });

    }


    public FeedbakResponse getSubmitData() {
        if (options != null) {
            FeedbakResponse fb = new FeedbakResponse();
            ArrayList<FeedbackDataResponse.Option> newOpt = new ArrayList<>();
            for (FeedbackDataResponse.Option opt : options) {
                if (opt.isUserAction()) {
                    newOpt.add(opt);
                }
            }
            if (newOpt.size() > 0) {
                fb.setOptions(newOpt);
                return fb;
            }
            return null;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return options != null ? options.size() : 0;
    }

    public void setCallBack(FeedBackButtonState feedBackButtonState) {
        this.feedBackButtonState = feedBackButtonState;
        refreshButtonState();
    }

    class FeedBackViewHolder extends RecyclerView.ViewHolder {

        TextView tv_question;
        EditText edt_freetext;
        CheckBox checkBox_btn;

        public FeedBackViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
            checkBox_btn = itemView.findViewById(R.id.checkBox_btn);
            edt_freetext = itemView.findViewById(R.id.edt_freetext);
        }
    }
}
