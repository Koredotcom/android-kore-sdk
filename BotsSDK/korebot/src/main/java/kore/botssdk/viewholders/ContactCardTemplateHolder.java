package kore.botssdk.viewholders;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.ContactCardItemAdapter;
import kore.botssdk.itemdecoration.VerticalSpaceItemDecoration;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.ContactTemplateModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.StringUtils;

public class ContactCardTemplateHolder extends BaseViewHolder {
    private final RecyclerView recyclerView;
    private final TextView botListViewTitle;

    public static ContactCardTemplateHolder getInstance(ViewGroup parent) {
        return new ContactCardTemplateHolder(createView(R.layout.template_contact_card, parent));
    }

    private ContactCardTemplateHolder(@NonNull View itemView) {
        super(itemView, itemView.getContext());
        recyclerView = itemView.findViewById(R.id.botCustomListView);
        botListViewTitle = itemView.findViewById(R.id.botListViewTitle);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
    }

    @Override
    public void bind(BaseBotMessage baseBotMessage) {
        PayloadInner payloadInner = getPayloadInner(baseBotMessage);
        if (payloadInner == null) return;
        ArrayList<ContactTemplateModel> botListModelArrayList = payloadInner.getContactCardModel();
        botListViewTitle.setVisibility(GONE);
        if (!StringUtils.isNullOrEmpty(payloadInner.getTitle())) {
            botListViewTitle.setVisibility(VISIBLE);
            botListViewTitle.setText(payloadInner.getTitle());
        }

        ContactCardItemAdapter botListTemplateAdapter;
        botListTemplateAdapter = new ContactCardItemAdapter(botListModelArrayList);
        recyclerView.setAdapter(botListTemplateAdapter);
    }
}
