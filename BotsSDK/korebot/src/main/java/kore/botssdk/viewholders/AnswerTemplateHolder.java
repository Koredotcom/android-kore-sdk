package kore.botssdk.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.adapter.AnswerSourceAdapter;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.PayloadInner;

public class AnswerTemplateHolder extends BaseViewHolder {
    public static AnswerTemplateHolder getInstance(ViewGroup parent) {
        return new AnswerTemplateHolder(createView(R.layout.template_answer, parent));
    }

    public AnswerTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        TextView tvAnswerContent = itemView.findViewById(R.id.answer_content);
        RecyclerView sourceRecycler = itemView.findViewById(R.id.linksRecycler);
        tvAnswerContent.setText(payloadInner.getAnswer());
        AnswerSourceAdapter adapter = new AnswerSourceAdapter(payloadInner.getAnswerPayload().getCenterPanel().getDataModels().get(0).getSnippetContents().get(0).getSources());
        adapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
        sourceRecycler.setAdapter(adapter);

    }
}
