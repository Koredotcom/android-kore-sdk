package kore.botssdk.adapter;

import static android.view.View.GONE;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceButtonClickListner;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.AdvanceOptionsModel;
import kore.botssdk.models.AdvancedListModel;
import kore.botssdk.models.HeaderOptionsModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.markdown.MarkdownImageTagHandler;
import kore.botssdk.utils.markdown.MarkdownTagHandler;
import kore.botssdk.utils.markdown.MarkdownUtil;
import kore.botssdk.view.AutoExpandListView;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class AdvancedListAdapter extends BaseAdapter implements AdvanceButtonClickListner {
    private ArrayList<AdvancedListModel> botListModelArrayList = new ArrayList<>();
    private ComposeFooterInterface composeFooterInterface;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private LayoutInflater ownLayoutInflator;
    private Context context;
    private RoundedCornersTransform roundedCornersTransform;
    private ListView parentListView;
    private int count;
    private PopupWindow popupWindow;
    private View popUpView;

    public AdvancedListAdapter(Context context, ListView parentListView) {
        this.ownLayoutInflator = LayoutInflater.from(context);
        this.context = context;
        this.roundedCornersTransform = new RoundedCornersTransform();
        this.parentListView = parentListView;
        popUpView = LayoutInflater.from(context).inflate(R.layout.advancelist_drop_down_popup, null);
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
    }

    @Override
    public int getCount() {
        if (count != 0) {
            return botListModelArrayList.size() > count ? count : botListModelArrayList.size();
        }
        else
            return botListModelArrayList.size();
    }

    @Override
    public int getViewTypeCount() {
        if (count != 0) {
            return botListModelArrayList.size() > count ? count : botListModelArrayList.size();
        }
        else
            return botListModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public AdvancedListModel getItem(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        } else {
            return botListModelArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ownLayoutInflator.inflate(R.layout.advancedlist_cell, null);
        }

        if (convertView.getTag() == null) {
            initializeViewHolder(convertView);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        populateVIew(holder, position);

        return convertView;
    }

    private void populateVIew(ViewHolder holder, int position) {
        AdvancedListModel botListModel = getItem(position);

        if(!StringUtils.isNullOrEmpty(botListModel.getIcon()))
        {
            holder.botListItemImage.setVisibility(View.VISIBLE);
            try
            {
                String imageData;
                imageData = botListModel.getIcon();
                if (imageData.contains(","))
                {
                    imageData = imageData.substring(imageData.indexOf(",") + 1);
                    byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.botListItemImage.setImageBitmap(decodedByte);
                }
                else
                {
                    Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.botListItemImage);
                }
            } catch (Exception e) {
                holder.botListItemImage.setVisibility(GONE);
            }
        }

        holder.botListItemTitle.setTag(botListModel);
        holder.botListItemTitle.setText(botListModel.getTitle());
        holder.botListItemTitle.setTypeface(null, Typeface.BOLD);
        if(!StringUtils.isNullOrEmpty(botListModel.getDescription())) {
            holder.botListItemSubtitle.setVisibility(View.VISIBLE);
            holder.botListItemSubtitle.setText(botListModel.getDescription());

            if(botListModel.getDescriptionStyles() != null)
            {
                holder.botListItemSubtitle.setTextColor(Color.parseColor(botListModel.getDescriptionStyles().getColor()));
            }
        }

        LayerDrawable layerDrawable = (LayerDrawable) holder.botListItemRoot.getContext().getResources().getDrawable(R.drawable.advanced_cell_bg);

        if(botListModel.getElementStyles() != null && !StringUtils.isNullOrEmpty(botListModel.getElementStyles().getBorderWidth()))
        {
            String[] border = botListModel.getElementStyles().getBorderWidth().split(" ");

            if(border.length > 3)
            {
                String border0 = border[0].replaceAll("[^0-9]", "");
                String border1 = border[1].replaceAll("[^0-9]", "");
                String border2 = border[2].replaceAll("[^0-9]", "");
                String border3 = border[3].replaceAll("[^0-9]", "");
                layerDrawable.setLayerInset(1, Integer.parseInt(border0) * (int)dp1, Integer.parseInt(border1)* (int)dp1, Integer.parseInt(border3)* (int)dp1, Integer.parseInt(border2)* (int)dp1);
            }

            if(!StringUtils.isNullOrEmpty(botListModel.getElementStyles().getBorder()))
            {
                String[] color = botListModel.getElementStyles().getBorder().split(" ");
                GradientDrawable backgroundColor = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.item_bottom_stroke);
                backgroundColor.setColor(Color.parseColor(color[1]));
                GradientDrawable bagColor = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.item_navbar_background);

                if(!StringUtils.isNullOrEmpty(botListModel.getElementStyles().getBorderRadius()))
                {
                    String radius = botListModel.getElementStyles().getBorderRadius().replaceAll("[^0-9]", "");

                    if(!StringUtils.isNullOrEmpty(radius)) {
                        backgroundColor.setCornerRadius((Integer.parseInt(radius) * 2));
                        bagColor.setCornerRadius((Integer.parseInt(radius) * 2));
                    }
                }
            }
        }

        holder.botListItemRoot.setBackground(layerDrawable);

        if(!StringUtils.isNullOrEmpty(botListModel.getView()) && botListModel.getView().equalsIgnoreCase(BundleConstants.DEFAULT))
        {
            if(botListModel.getTextInformation() != null && botListModel.getTextInformation().size() > 0)
            {
                holder.lvDetails.setVisibility(View.VISIBLE);
                holder.lvDetails.setAdapter(new AdvanceListdetailsAdapter(context, botListModel.getTextInformation()));
            }

            if(botListModel.getButtons() != null && botListModel.getButtons().size() > 0)
            {
                holder.rvDefaultButtons.setVisibility(View.VISIBLE);
                holder.rvDefaultButtons.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                holder.rvDefaultButtons.setAdapter(new AdvanceListButtonAdapter(context, botListModel.getButtons(), 1, AdvancedListAdapter.this, composeFooterInterface, invokeGenericWebViewInterface));
            }
        }
        else if(!StringUtils.isNullOrEmpty(botListModel.getView()) && botListModel.getView().equalsIgnoreCase(BundleConstants.OPTIONS))
        {
            AdvanceOptionsAdapter advanceOptionsAdapter = null;

            if(botListModel.getOptionsData() != null && botListModel.getOptionsData().size() > 0)
            {
                holder.llOptions.setVisibility(View.VISIBLE);
                holder.lvOptions.setAdapter(advanceOptionsAdapter = new AdvanceOptionsAdapter(context, botListModel.getOptionsData()));
            }

            if(botListModel.getButtons() != null && botListModel.getButtons().size() > 0)
            {
                if(!StringUtils.isNullOrEmpty(botListModel.getButtonAligment()))
                {
                    if(botListModel.getButtonAligment().equalsIgnoreCase(BundleConstants.RIGHT))
                    {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.END;
                        holder.rvOptionButtons.setLayoutParams(params);
                    }
                    else
                    {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.START;
                        holder.rvOptionButtons.setLayoutParams(params);
                    }
                }

                holder.rvOptionButtons.setVisibility(View.VISIBLE);
                holder.rvOptionButtons.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                AdvanceListButtonAdapter advanceListButtonAdapter = new AdvanceListButtonAdapter(context, botListModel.getButtons(), 2, AdvancedListAdapter.this, composeFooterInterface, invokeGenericWebViewInterface);
                advanceListButtonAdapter.setListAdapter(advanceOptionsAdapter);
                holder.rvOptionButtons.setAdapter(advanceListButtonAdapter);
            }
        }
        else if(!StringUtils.isNullOrEmpty(botListModel.getView()) && botListModel.getView().equalsIgnoreCase(BundleConstants.TABLE))
        {
            if(botListModel.getTableListData() != null && botListModel.getTableListData().size() > 0)
            {
                holder.lvTableList.setVisibility(View.VISIBLE);
                holder.lvTableList.setAdapter(new AdvanceTableListOuterAdapter(context, botListModel.getTableListData()));
            }
        }
        else
        {
            if(botListModel.getHeaderOptions() != null && botListModel.getHeaderOptions().size() > 0)
            {
                holder.rlAction.setVisibility(View.VISIBLE);

                HeaderOptionsModel headerOptionsModel = botListModel.getHeaderOptions().get(0);

                if(!StringUtils.isNullOrEmpty(headerOptionsModel.getContenttype()))
                {
                    if(headerOptionsModel.getContenttype().equalsIgnoreCase(BundleConstants.ICON))
                    {
                        holder.ivAction.setVisibility(View.VISIBLE);
                        try {
                            String imageData;
                            imageData = botListModel.getHeaderOptions().get(0).getIcon();
                            if (imageData.contains(","))
                            {
                                imageData = imageData.substring(imageData.indexOf(",") + 1);
                                byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                holder.ivAction.setImageBitmap(decodedByte);
                            }
                            else
                            {
                                Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.ivAction);
                            }
                        }
                        catch (Exception ex)
                        {
                            holder.ivAction.setVisibility(GONE);
                        }
                    }
                    else if(headerOptionsModel.getContenttype().equalsIgnoreCase(BundleConstants.BUTTON)
                            && !StringUtils.isNullOrEmpty(headerOptionsModel.getTitle()))
                    {
                        holder.tvAction.setVisibility(View.VISIBLE);

                        String textualContent = unescapeHtml4(headerOptionsModel.getTitle().trim());
                        textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                        textualContent = MarkdownUtil.processMarkDown(textualContent);
                        CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY, new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                        holder.tvAction.setText(strBuilder);
                        holder.tvAction.setMovementMethod(null);

                        if(headerOptionsModel.getButtonStyles() != null)
                        {
                            GradientDrawable gradientDrawable = (GradientDrawable)holder.botListItemRoot.findViewById(R.id.tvAction).getBackground();
                            gradientDrawable.setCornerRadius(3 * dp1);
                            if(!StringUtils.isNullOrEmpty(headerOptionsModel.getButtonStyles().getBorderColor()))
                            {
                                if(!headerOptionsModel.getButtonStyles().getBorderColor().contains("#"))
                                    gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor("#"+headerOptionsModel.getButtonStyles().getBorderColor()));
                                else
                                    gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor(headerOptionsModel.getButtonStyles().getBorderColor()));
                            }

                            if(!StringUtils.isNullOrEmpty(headerOptionsModel.getButtonStyles().getBackgroundColor()))
                                gradientDrawable.setColor(Color.parseColor(headerOptionsModel.getButtonStyles().getBackgroundColor()));

                            if(!StringUtils.isNullOrEmpty(headerOptionsModel.getButtonStyles().getColor()))
                                holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getButtonStyles().getColor()));
                        }

                        holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);

                        holder.tvAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(composeFooterInterface != null)
                                    composeFooterInterface.onSendClick( holder.tvAction.getText().toString(), headerOptionsModel.getPayload(), true);
                            }
                        });
                    }
                    else if(!StringUtils.isNullOrEmpty(headerOptionsModel.getValue()))
                    {
                        holder.tvAction.setVisibility(View.VISIBLE);

                        String textualContent = unescapeHtml4(headerOptionsModel.getValue().trim());
                        textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                        textualContent = MarkdownUtil.processMarkDown(textualContent);
                        CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY, new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                        holder.tvAction.setText(strBuilder);
                        holder.tvAction.setMovementMethod(null);

                        GradientDrawable gradientDrawable = (GradientDrawable)holder.botListItemRoot.findViewById(R.id.tvAction).getBackground();
                        gradientDrawable.setStroke(0, null);

                        if(headerOptionsModel.getStyles() != null)
                        {
                            holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getStyles().getColor()));
                        }

                        holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);
                    }
                }
                else if(!StringUtils.isNullOrEmpty(headerOptionsModel.getType()))
                {
                    if(headerOptionsModel.getType().equalsIgnoreCase(BundleConstants.ICON))
                    {
                        holder.ivAction.setVisibility(View.VISIBLE);
                        try {
                            String imageData;
                            imageData = botListModel.getHeaderOptions().get(0).getIcon();
                            if (imageData.contains(","))
                            {
                                imageData = imageData.substring(imageData.indexOf(",") + 1);
                                byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                holder.ivAction.setImageBitmap(decodedByte);
                            }
                            else
                            {
                                Picasso.get().load(botListModel.getIcon()).transform(roundedCornersTransform).into(holder.ivAction);
                            }
                        }
                        catch (Exception ex)
                        {
                            holder.ivAction.setVisibility(GONE);
                        }
                    }
                    else if(headerOptionsModel.getType().equalsIgnoreCase(BundleConstants.BUTTON)
                            && !StringUtils.isNullOrEmpty(headerOptionsModel.getTitle()))
                    {
                        holder.tvAction.setVisibility(View.VISIBLE);

                        String textualContent = unescapeHtml4(headerOptionsModel.getTitle().trim());
                        textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                        textualContent = MarkdownUtil.processMarkDown(textualContent);
                        CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY, new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                        holder.tvAction.setText(strBuilder);
                        holder.tvAction.setMovementMethod(null);

                        if(headerOptionsModel.getButtonStyles() != null)
                        {
                            GradientDrawable gradientDrawable = (GradientDrawable)holder.botListItemRoot.findViewById(R.id.tvAction).getBackground();
                            gradientDrawable.setCornerRadius(3 * dp1);
                            if(!StringUtils.isNullOrEmpty(headerOptionsModel.getButtonStyles().getBorderColor()))
                            {
                                if(!headerOptionsModel.getButtonStyles().getBorderColor().contains("#"))
                                    gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor("#"+headerOptionsModel.getButtonStyles().getBorderColor()));
                                else
                                    gradientDrawable.setStroke((int)(1 * dp1), Color.parseColor(headerOptionsModel.getButtonStyles().getBorderColor()));
                            }
                            gradientDrawable.setColor(Color.parseColor(headerOptionsModel.getButtonStyles().getBackgroundColor()));

                            holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getButtonStyles().getColor()));
                        }

                        holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);

                    }
                    else if(headerOptionsModel.getType().equalsIgnoreCase(BundleConstants.DROP_DOWN) && headerOptionsModel.getDropdownOptions() != null && headerOptionsModel.getDropdownOptions().size() > 0)
                    {
                        holder.rlDropDown.setVisibility(View.VISIBLE);
                        RecyclerView recyclerView = popUpView.findViewById(R.id.rvDropDown);
                        ImageView ivDropDownCLose = popUpView.findViewById(R.id.ivDropDownCLose);

                        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        AdvanceListButtonAdapter advanceListButtonAdapter = new AdvanceListButtonAdapter(context, headerOptionsModel.getDropdownOptions(), 1, AdvancedListAdapter.this, composeFooterInterface, invokeGenericWebViewInterface);
                        advanceListButtonAdapter.setPopUpWindow(popupWindow);
                        recyclerView.setAdapter(advanceListButtonAdapter);
                        ivDropDownCLose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(popupWindow != null)
                                    popupWindow.dismiss();
                            }
                        });
                        holder.ivDropDownAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.showAsDropDown(holder.ivDropDownAction, -400, 0);
                            }
                        });
                    }

                    else if(!StringUtils.isNullOrEmpty(headerOptionsModel.getValue()))
                    {
                        holder.tvAction.setVisibility(View.VISIBLE);

                        String textualContent = unescapeHtml4(headerOptionsModel.getValue().trim());
                        textualContent = StringUtils.unescapeHtml3(textualContent.trim());
                        textualContent = MarkdownUtil.processMarkDown(textualContent);
                        CharSequence sequence = HtmlCompat.fromHtml(textualContent.replace("\n", "<br />"), HtmlCompat.FROM_HTML_MODE_LEGACY, new MarkdownImageTagHandler(context, holder.tvAction , textualContent), new MarkdownTagHandler());
                        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

                        holder.tvAction.setText(strBuilder);
                        holder.tvAction.setMovementMethod(null);
                        holder.tvAction.setBackground(null);

                        if(headerOptionsModel.getStyles() != null)
                        {
                            holder.tvAction.setTextColor(Color.parseColor(headerOptionsModel.getStyles().getColor()));
                        }

                        holder.tvAction.setTypeface(holder.tvAction.getTypeface(), Typeface.BOLD);
                    }
                }
            }
        }
        holder.botListItemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                    int position = parentListView.getPositionForView(v);
                    AdvancedListModel _botListModel = getItem(position);
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(_botListModel.getType())) {
                            String listElementButtonPayload = _botListModel.getPayload();
                            String listElementButtonTitle = _botListModel.getTitle();
                            composeFooterInterface.onSendClick(listElementButtonTitle, listElementButtonPayload,false);
                        }
                    }
                }
            }
        });
    }

    public void dispalyCount(int count)
    {
        this.count = count;
    }

    public void setBotListModelArrayList(ArrayList<AdvancedListModel> botListModelArrayList) {
        this.botListModelArrayList = botListModelArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    private void initializeViewHolder(View view) {
        ViewHolder holder = new ViewHolder();

        holder.botListItemRoot = view.findViewById(R.id.bot_list_item_root);
        holder.botListItemImage = view.findViewById(R.id.bot_list_item_image);
        holder.botListItemTitle = view.findViewById(R.id.bot_list_item_title);
        holder.botListItemSubtitle = view.findViewById(R.id.bot_list_item_subtitle);
        holder.botListItemButton = view.findViewById(R.id.bot_list_item_button);
        holder.ivAction = view.findViewById(R.id.ivAction);
        holder.tvAction = view.findViewById(R.id.tvAction);
        holder.rlAction = view.findViewById(R.id.rlAction);
        holder.rvDropDownButtons = view.findViewById(R.id.rvDropDownButtons);
        holder.rvDefaultButtons = view.findViewById(R.id.rvDefaultButtons);
        holder.lvDetails = view.findViewById(R.id.lvDetails);
        holder.ivDropDownAction = view.findViewById(R.id.ivDropDownAction);
        holder.rlDropDown = view.findViewById(R.id.rlDropDown);
        holder.lvOptions = view.findViewById(R.id.lvOptions);
        holder.llOptions = view.findViewById(R.id.llOptions);
        holder.rvOptionButtons = view.findViewById(R.id.rvOptionButtons);
        holder.lvTableList = view.findViewById(R.id.lvTableList);

        view.setTag(holder);
    }

    @Override
    public void advanceButtonClick(ArrayList<AdvanceOptionsModel> arrAdvanceOptionsModels)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (AdvanceOptionsModel advanceOptionsModel: arrAdvanceOptionsModels)
        {
            if(advanceOptionsModel.isChecked())
            {
                if(StringUtils.isNullOrEmpty(stringBuilder))
                    stringBuilder.append(advanceOptionsModel.getValue());
                else
                    stringBuilder.append(", ").append(advanceOptionsModel.getValue());
            }
        }

        if(composeFooterInterface != null)
            composeFooterInterface.onSendClick(stringBuilder.toString(), stringBuilder.toString(), true);
    }

    private static class ViewHolder {
        RelativeLayout botListItemRoot, rlAction, rlDropDown;
        ImageView botListItemImage, ivAction, ivDropDownAction;
        TextView botListItemTitle, tvAction;
        TextView botListItemSubtitle;
        Button botListItemButton;
        RecyclerView rvDropDownButtons, rvDefaultButtons, rvOptionButtons;
        AutoExpandListView lvDetails, lvOptions, lvTableList;
        LinearLayout llOptions;
    }
}
