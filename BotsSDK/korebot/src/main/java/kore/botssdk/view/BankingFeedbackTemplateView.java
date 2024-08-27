package kore.botssdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BankingFeedbackButtonsAdapter;
import kore.botssdk.adapter.BankingFeedbackListAdapter;
import kore.botssdk.adapter.BankingFeedbackTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.FeedbackExperienceUpdateListner;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.FeedbackExperienceContentModel;
import kore.botssdk.models.FeedbackListModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.utils.Utility;

public class BankingFeedbackTemplateView extends LinearLayout implements FeedbackExperienceUpdateListner
{
    private AutoExpandListView lvExperience, lvFeedback;
    private TextView tvFeedbackHeading, tvEmpathyMessage;
    private RecyclerView rvButtons;
    private float dp1;
    private LinearLayout llFeedbackSelected;
    private BankingFeedbackTemplateAdapter bankingFeedbackTemplateAdapter;
    private FeedbackExperienceContentModel selectedExperienceModel;
    private ArrayList<FeedbackListModel> feedbackListModels;
    private EditText edtSuggestions;
    private LinearLayout feedback_layout;

    public BankingFeedbackTemplateView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.banking_feedback_template, this, true);
        KaFontUtils.applyCustomFont(getContext(), view);
        feedback_layout = view.findViewById(R.id.feedback_layout);
        lvExperience = view.findViewById(R.id.lvExperience);
        tvFeedbackHeading = view.findViewById(R.id.tvFeedbackHeading);
        tvEmpathyMessage = view.findViewById(R.id.tvEmpathyMessage);
        lvFeedback = view.findViewById(R.id.lvFeedback);
        rvButtons = view.findViewById(R.id.rvButtonList);
        llFeedbackSelected = view.findViewById(R.id.llFeedbackSelected);
        edtSuggestions = view.findViewById(R.id.edtSuggestions);
        dp1 = Utility.convertDpToPixel(getContext(), 1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvButtons.setLayoutManager(layoutManager);
        rvButtons.addItemDecoration(new SpacesItemDecoration((int)(10 * dp1)));
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;

    public void populateData(final PayloadInner payloadInner, boolean isEnabled)
    {
        if(payloadInner != null && payloadInner.getExperienceContent() != null
                && payloadInner.getExperienceContent().size() > 0)
        {
            lvExperience.setAdapter(bankingFeedbackTemplateAdapter = new BankingFeedbackTemplateAdapter(getContext(), payloadInner.getExperienceContent(), BankingFeedbackTemplateView.this, isEnabled));
            tvFeedbackHeading.setText(payloadInner.getHeading());
            lvFeedback.setAdapter(new BankingFeedbackListAdapter(getContext(), payloadInner.getFeedbackList(), BankingFeedbackTemplateView.this, isEnabled));
            rvButtons.setAdapter(new BankingFeedbackButtonsAdapter(payloadInner.getButtons(), BankingFeedbackTemplateView.this, isEnabled));

//            if(isEnabled)
//            {
//                edtSuggestions.setClickable(false);
//                edtSuggestions.setFocusable(false);
//            }

            feedback_layout.setAlpha(isEnabled ? 1.0f : 0.5f);
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildAdapterPosition(view) != 0) {
                outRect.left = space;
            }
        }
    }

    @Override
    public void updateExperienceList(int position, ArrayList<FeedbackExperienceContentModel> feedbackExperienceContentModels) {
        for (int i = 0; i < feedbackExperienceContentModels.size(); i++)
        {
            if(i == position)
            {
                feedbackExperienceContentModels.get(i).setChecked(true);
                llFeedbackSelected.setVisibility(VISIBLE);
                tvEmpathyMessage.setVisibility(VISIBLE);
                tvEmpathyMessage.setText(feedbackExperienceContentModels.get(i).getEmpathyMessage());
                selectedExperienceModel = feedbackExperienceContentModels.get(i);
            }
            else
                feedbackExperienceContentModels.get(i).setChecked(false);
        }

        if(bankingFeedbackTemplateAdapter != null)
            bankingFeedbackTemplateAdapter.refresh(feedbackExperienceContentModels);
    }

    @Override
    public void updateFeedbackList(ArrayList<FeedbackListModel> arrFeedbackListModels)
    {
        this.feedbackListModels = new ArrayList<>();
        feedbackListModels.addAll(arrFeedbackListModels);
    }

    @Override
    public void sendFeedback()
    {
        try
        {
            JSONObject mainObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if(feedbackListModels != null && feedbackListModels.size() > 0)
            {
                for (int i = 0; i < feedbackListModels.size(); i++)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", feedbackListModels.get(i).getId());
                    jsonObject.put("value", feedbackListModels.get(i).getValue());
                    jsonArray.put(jsonObject);
                }
            }
            mainObject.put("selectedFeedback", jsonArray);

            if(selectedExperienceModel != null)
            {
                JSONObject expObject = new JSONObject();
                expObject.put("id", selectedExperienceModel.getId());
                expObject.put("value", selectedExperienceModel.getValue());
                expObject.put("empathyMessage", selectedExperienceModel.getEmpathyMessage());
                mainObject.put("selectedExperience", expObject);
            }

             mainObject.put("userSuggestion", edtSuggestions.getText().toString());

            composeFooterInterface.onSendClick("", mainObject.toString(), false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
