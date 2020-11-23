package com.kore.findlysdk.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kore.findlysdk.R;
import com.kore.findlysdk.listners.ComposeFooterInterface;
import com.kore.findlysdk.models.BotFormFieldButtonModel;
import com.kore.findlysdk.models.BotFormTemplateModel;
import com.kore.findlysdk.models.CardsTemplateModel;
import com.kore.findlysdk.utils.KaFontUtils;

import java.util.ArrayList;

public class CardTemplateAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<CardsTemplateModel> arrCardsTemplateModels;
    private ComposeFooterInterface composeFooterInterface;
    private LayoutInflater ownLayoutInflator;
    private String master_cardImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAJmSURBVHgBtVJLS5RhFH7O+10cZ2jmM5sgUfg0BAkq3VqhRrhx0biQ2ql7w7atnF2rsD8Q02XZQluGhhVBdIHGS4Q7JSOi0hk/57t/7+l1umCouzqr9+Wc5znP83CA/10rKyvmzcfexcFbbttBfdr78V7adiqFs+r5DHpjCazfm88sLTx4sd2ba9TFBaTnRvLt6VrgT2uJX2wcqqyLvQSGoXWzSM/CtCYhMjbM/MyluK/QdST7+sqZ9POeEVCU0Qp6Sh9L0FDYxYi925UeizTzNoy2IvT8GkR2DfqJuzdOD02c7yKnkyjQTbqspXTWGrTuvwh2twsjVQI0C5BPYeQLysYayNwCZYu8OjhV96wJW+gCrInufRnw8qkijJYpkFGGblnQsrYiK3Pw2SYEOSTeIyTfCzIMOHCiarr/Y9MfAn57chSGfh3G0UXox0chjAq0TC5xqyRMDYh3QOyoQY9l4FNY3Wa4sr1ugZlFJJPFenAk+iCrs+DISpwNEoYaSDzI0FV61T49T2Q0MyeC/DDsEfMfuPn+E6dpYvXNF8T+ACQssCxId2NLaDFIOpDepiKpMWSkWgTJJslEpcXIiaXlDXduHeeqntn68Nj71ST8Oo54Uw37yl9NAatKlKfk7yj8jhLmIXbVW+WQEK3XM7h6p9bS20HiWn/jJyJiftc5xoJKP0/NrEvn0EfkBsqvjsjxkYR+2Rre7CEcUtGrjqIimyLzKJga1PYAcVW5dF2WsVxUEgeahiuVQwl2y11onSQ2pmHmiJXn2PmGJHLL+AXedwcH1daMZZNmlCDJrt8Ex+O/wf+kfgAhFxenJ2BlUQAAAABJRU5ErkJggg==";

    public CardTemplateAdapter(Context context, ArrayList<CardsTemplateModel> arrCardsTemplateModels)
    {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.arrCardsTemplateModels = arrCardsTemplateModels;
    }

    @Override
    public int getCount()
    {
        return arrCardsTemplateModels.size();
    }

    @Override
    public Object getItem(int i) {
        return arrCardsTemplateModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ownLayoutInflator.inflate(R.layout.card_template_cell_view, null);
            holder.tvCardTitle = convertView.findViewById(R.id.tvCardTitle);
            holder.tvCardNumber = convertView.findViewById(R.id.tvCardNumber);
            holder.tvDueAmount = convertView.findViewById(R.id.tvDueAmount);
            holder.tvDueDate = convertView.findViewById(R.id.tvDueDate);
            holder.tvPayNow = convertView.findViewById(R.id.tvPayNow);
            holder.ivCardImage = convertView.findViewById(R.id.ivCardImage);

            convertView.setTag(holder);
//            KaFontUtils.applyCustomFont(context, convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        populateView(holder, position);

        return convertView;
    }

    private void populateView(final ViewHolder holder, int position)
    {
        final CardsTemplateModel item = arrCardsTemplateModels.get(position);
        holder.tvCardTitle.setTag(item);
        holder.tvCardTitle.setText(item.getBiller_name());

        holder.tvCardNumber.setText(item.getCard_number());
        holder.tvDueAmount.setText(item.getBill_amount());
        holder.tvDueDate.setText(item.getDue_date());

        holder.tvPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (composeFooterInterface != null)
                {
                    composeFooterInterface.onSendClick(item.getPostback_value(),false);
                }
            }
        });

        holder.ivCardImage.setImageDrawable(context.getResources().getDrawable(R.drawable.master_card));
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private class ViewHolder {
        TextView tvCardTitle, tvCardNumber, tvDueAmount, tvDueDate, tvPayNow;
        ImageView ivCardImage;
    }
}
