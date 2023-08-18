package kore.botssdk.adapter;

import static android.view.View.GONE;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.AdvanceButtonClickListner;
import kore.botssdk.models.AdvanceListTableModel;
import kore.botssdk.models.AdvanceOptionsModel;
import kore.botssdk.models.CardTemplateButtonModel;
import kore.botssdk.models.CardTemplateModel;
import kore.botssdk.models.HeaderStyles;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.view.viewUtils.RoundedCornersTransform;

public class CardTemplateAdapter extends RecyclerView.Adapter<CardTemplateAdapter.CardViewHolder> implements AdvanceButtonClickListner
{
    private final ArrayList<CardTemplateModel> arrCardTemplateModels;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private View popUpView;

    public CardTemplateAdapter(Context context, ArrayList<CardTemplateModel> arrCardTemplateModels)
    {
        this.arrCardTemplateModels = arrCardTemplateModels;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        popUpView = LayoutInflater.from(context).inflate(R.layout.advancelist_drop_down_popup, null);
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
    }

    @NonNull
    @io.reactivex.annotations.NonNull
    @Override
    public CardTemplateAdapter.CardViewHolder onCreateViewHolder(@NonNull @io.reactivex.annotations.NonNull ViewGroup parent, int viewType) {
        return new CardTemplateAdapter.CardViewHolder(layoutInflater.inflate(R.layout.card_template_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @io.reactivex.annotations.NonNull CardTemplateAdapter.CardViewHolder holder, int position) {
        CardTemplateModel cardTemplateModel = arrCardTemplateModels.get(position);
        holder.bot_list_item_subtitle.setVisibility(View.GONE);
        holder.bot_list_item_image.setVisibility(View.GONE);
        holder.tvOnlyTitle.setVisibility(GONE);
        holder.rlTitle.setVisibility(GONE);
        holder.tvCardButton.setVisibility(GONE);
        holder.rvDescription.setVisibility(GONE);
        holder.vBorder.setVisibility(GONE);

        if(cardTemplateModel != null)
        {
            if(cardTemplateModel.getCardHeading() != null) {
                holder.tvOnlyTitle.setVisibility(View.VISIBLE);
                holder.tvOnlyTitle.setText(cardTemplateModel.getCardHeading().getTitle());

                if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardHeading().getDescription())) {
                    holder.tvOnlyTitle.setVisibility(GONE);
                    holder.rlTitle.setVisibility(View.VISIBLE);

                    holder.bot_list_item_title.setText(cardTemplateModel.getCardHeading().getTitle());
                    holder.bot_list_item_subtitle.setVisibility(View.VISIBLE);
                    holder.bot_list_item_subtitle.setText(cardTemplateModel.getCardHeading().getDescription());
                }

                if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardHeading().getIcon())) {
                    holder.bot_list_item_image.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
                    holder.bot_list_item_image.setLayoutParams(layoutParams);

                    if(!StringUtils.isNullOrEmpty(cardTemplateModel.getCardHeading().getIconSize()))
                    {
                        if(cardTemplateModel.getCardHeading().getIconSize().equalsIgnoreCase("large"))
                        {
                            layoutParams.height = 200;
                            layoutParams.width = 200;

                            holder.bot_list_item_image.setLayoutParams(layoutParams);
                        }
                        else if(cardTemplateModel.getCardHeading().getIconSize().equalsIgnoreCase("small"))
                        {

                            layoutParams.height = 50;
                            layoutParams.width = 50;

                            holder.bot_list_item_image.setLayoutParams(layoutParams);
                        }
                    }

                    try {
                        String imageData;
                        imageData = cardTemplateModel.getCardHeading().getIcon();
                        if (imageData.contains(",")) {
                            imageData = imageData.substring(imageData.indexOf(",") + 1);
                            byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            holder.bot_list_item_image.setImageBitmap(decodedByte);
                        } else {
                            Picasso.get().load(cardTemplateModel.getCardHeading().getIcon()).transform(new RoundedCornersTransform()).into(holder.bot_list_item_image);
                        }
                    } catch (Exception e) {
                        holder.bot_list_item_image.setVisibility(GONE);
                    }
                }

                if (cardTemplateModel.getCardHeading().getHeaderStyles() != null)
                {
                    HeaderStyles headerStyles = cardTemplateModel.getCardHeading().getHeaderStyles();

                    holder.tvOnlyTitle.setTextColor(Color.parseColor(headerStyles.getColor()));
                    GradientDrawable rightDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_title_bg);
                    if (rightDrawable != null) {
                        rightDrawable.setColor(Color.parseColor(headerStyles.getBackground_color()));
                        if (!StringUtils.isNullOrEmpty(headerStyles.getBorder())) {
                            String[] border = headerStyles.getBorder().split("#");

                            if (border.length > 0)
                                rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#" + border[1]));
                        } else
                            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#00000000"));
                    }

                    holder.tvOnlyTitle.setBackground(rightDrawable);
                    if(!StringUtils.isNullOrEmpty(headerStyles.getFont_weight()))
                    {
                        if(headerStyles.getFont_weight().equalsIgnoreCase(BundleConstants.BOLD))
                            holder.tvOnlyTitle.setTypeface(holder.tvOnlyTitle.getTypeface(), Typeface.BOLD);
                    }
                }

                if(cardTemplateModel.getCardHeading().getHeaderExtraInfo() != null)
                {
                    AdvanceListTableModel.AdvanceTableRowDataModel headerOptions = cardTemplateModel.getCardHeading().getHeaderExtraInfo();

                    if(!StringUtils.isNullOrEmpty(headerOptions.getTitle())) {
                        holder.tvheaderExtraTitle.setVisibility(View.VISIBLE);
                        holder.tvheaderExtraTitle.setText(headerOptions.getTitle());
                    }

                    if(!StringUtils.isNullOrEmpty(headerOptions.getIcon()))
                    {
                        holder.ivheaderExtra.setVisibility(View.VISIBLE);

                        try {
                            String imageData;
                            imageData = headerOptions.getIcon();
                            if (imageData.contains(",")) {
                                imageData = imageData.substring(imageData.indexOf(",") + 1);
                                byte[] decodedString = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                holder.ivheaderExtra.setImageBitmap(decodedByte);
                            } else {
                                Picasso.get().load(headerOptions.getIcon()).transform(new RoundedCornersTransform()).into(holder.ivheaderExtra);
                            }
                        } catch (Exception e) {
                            holder.ivheaderExtra.setVisibility(GONE);
                        }
                    }

                    if(!StringUtils.isNullOrEmpty(headerOptions.getType()) && headerOptions.getType().equalsIgnoreCase(BundleConstants.DROP_DOWN) && headerOptions.getDropdownOptions() != null && headerOptions.getDropdownOptions().size() > 0)
                    {
                        RecyclerView recyclerView = popUpView.findViewById(R.id.rvDropDown);
                        ImageView ivDropDownCLose = popUpView.findViewById(R.id.ivDropDownCLose);

                        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        AdvanceListButtonAdapter advanceListButtonAdapter = new AdvanceListButtonAdapter(context, headerOptions.getDropdownOptions(), BundleConstants.FULL_WIDTH, CardTemplateAdapter.this, null, null);
                        recyclerView.setAdapter(advanceListButtonAdapter);
                        ivDropDownCLose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(popupWindow != null)
                                    popupWindow.dismiss();
                            }
                        });

                        holder.ivheaderExtra.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.showAsDropDown(holder.tvheaderExtraTitle, -170, 0);
                            }
                        });
                    }
                }
                else
                {
                    holder.tvheaderExtraTitle.setVisibility(GONE);
                    holder.ivheaderExtra.setVisibility(GONE);
                }
            }

            if(cardTemplateModel.getCardDescription() != null)
            {
                holder.rvDescription.setVisibility(View.VISIBLE);
                holder.rvDescription.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                if(!StringUtils.isNullOrEmpty(cardTemplateModel.getCardType()) &&
                        cardTemplateModel.getCardType().equalsIgnoreCase("list"))
                    holder.rvDescription.setLayoutManager(new GridLayoutManager(context, 3));

                holder.rvDescription.setAdapter(new CardTemplateListAdapter(context, cardTemplateModel.getCardDescription(), null, null));
            }

            if(cardTemplateModel.getButtons() != null && cardTemplateModel.getButtons().size() > 0)
            {
                if(cardTemplateModel.getButtons().size() == 1)
                {
                    CardTemplateButtonModel cardTemplateButtonModel = cardTemplateModel.getButtons().get(0);

                    holder.tvCardButton.setVisibility(View.VISIBLE);
                    holder.tvCardButton.setText(cardTemplateButtonModel.getTitle());

                    if(cardTemplateButtonModel.getButtonStyles() != null)
                    {
                        GradientDrawable rightDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_btn_bg);
                        if (rightDrawable != null) {
//                            if(!StringUtils.isNullOrEmpty(cardTemplateButtonModel.getButtonStyles().getColor()))
//                                holder.tvCardButton.setTextColor(Color.parseColor(cardTemplateButtonModel.getButtonStyles().getColor()));

                            if(!StringUtils.isNullOrEmpty(cardTemplateButtonModel.getButtonStyles().getBackground_color()))
                                rightDrawable.setColor(Color.parseColor(cardTemplateButtonModel.getButtonStyles().getBackground_color()));
                            else
                                rightDrawable.setColor(Color.parseColor("#ffffff"));

                            if (!StringUtils.isNullOrEmpty(cardTemplateButtonModel.getButtonStyles().getBorder())) {
                                String[] border = cardTemplateButtonModel.getButtonStyles().getBorder().split("#");

                                if (border.length > 0)
                                    rightDrawable.setStroke((int) (1 * dp1), Color.parseColor(("#" + border[1].replace(";", ""))));
                            } else
                                rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#00000000"));
                        }

                        holder.tvCardButton.setBackground(rightDrawable);
                    }

                }
            }

            if(cardTemplateModel.getCardStyles() != null)
            {
                GradientDrawable rightDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_template_top_bg);
                if (rightDrawable != null) {

                    if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardStyles().getBorderLeft())) {
                        String[] border = cardTemplateModel.getCardStyles().getBorderLeft().split(" ");

                        if (border.length > 1)
                        {
                            rightDrawable.setColor(Color.parseColor((border[2].replace(";", ""))));
                            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor((border[2].replace(";", ""))));
                        }
                    }
                    else if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardStyles().getBorderRight())) {
                        String[] border = cardTemplateModel.getCardStyles().getBorderRight().split(" ");

                        if (border.length > 1)
                        {
                            rightDrawable.setColor(Color.parseColor((border[2].replace(";", ""))));
                            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor((border[2].replace(";", ""))));
                        }

                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(0, 0, 15, 0);
                        holder.llCardView.setLayoutParams(buttonLayoutParams);

                    }
                    else if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardStyles().getBorderTop())) {
                        String[] border = cardTemplateModel.getCardStyles().getBorderTop().split(" ");

                        if (border.length > 1)
                        {
                            rightDrawable.setColor(Color.parseColor((border[2].replace(";", ""))));
                            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor((border[2].replace(";", ""))));
                        }

                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(0, 15, 0, 0);
                        holder.llCardView.setLayoutParams(buttonLayoutParams);

                    }
                    else if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardStyles().getBorderBottom())) {
                        String[] border = cardTemplateModel.getCardStyles().getBorderBottom().split(" ");

                        if (border.length > 1)
                        {
                            rightDrawable.setColor(Color.parseColor((border[2].replace(";", ""))));
                            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor((border[2].replace(";", ""))));
                        }

                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        buttonLayoutParams.setMargins(0, 0, 0, 15);
                        holder.llCardView.setLayoutParams(buttonLayoutParams);

                    }
                    else
                        rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#00000000"));
                }

                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(0, 10, 0, 0);
                holder.llCardViewTop.setLayoutParams(buttonLayoutParams);
                holder.llCardViewTop.setBackground(rightDrawable);

                GradientDrawable templateBb = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_template_bg);
                if (templateBb != null)
                {
                    templateBb.setColor(Color.parseColor("#ffffff"));

                    if(!StringUtils.isNullOrEmpty(cardTemplateModel.getCardStyles().getBackground_color()))
                        templateBb.setColor(Color.parseColor(cardTemplateModel.getCardStyles().getBackground_color()));

                    templateBb.setStroke((int) (1 * dp1), Color.parseColor("#84959B"));
                }

                holder.llCardView.setBackground(templateBb);
            }
            else
            {
                GradientDrawable rightDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_template_top_bg);
                if (rightDrawable != null) {
                    rightDrawable.setColor(Color.parseColor("#ffffff"));
                    rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#00000000"));
                    holder.llCardViewTop.setBackground(rightDrawable);
                }

                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(0, 10, 0, 0);
                holder.llCardView.setLayoutParams(buttonLayoutParams);
            }

            if(cardTemplateModel.getCardContentStyles() != null)
            {
                GradientDrawable rightDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_template_bg);
                if (rightDrawable != null) {

                    if(!StringUtils.isNullOrEmpty(cardTemplateModel.getCardContentStyles().getBackground_color()))
                        rightDrawable.setColor(Color.parseColor(cardTemplateModel.getCardContentStyles().getBackground_color()));
                    else
                        rightDrawable.setColor(Color.parseColor("#ffffff"));

                    if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardContentStyles().getBorder())) {
                        String[] border = cardTemplateModel.getCardContentStyles().getBorder().split("#");

                        if (border.length > 0)
                            rightDrawable.setStroke((int) (1 * dp1), Color.parseColor(("#" + border[1].replace(";", ""))));
                    } else
                        rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#84959B"));
                }

                if (!StringUtils.isNullOrEmpty(cardTemplateModel.getCardContentStyles().getBorderLeft())) {
                    String[] border = cardTemplateModel.getCardContentStyles().getBorderLeft().split(" ");
                    holder.vBorder.setVisibility(View.VISIBLE);
                    GradientDrawable viewDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_view_bg);
                    if (viewDrawable != null && border.length > 1) {
                        viewDrawable.setColor(Color.parseColor((border[2].replace(";", ""))));
                    }
                    holder.vBorder.setBackground(viewDrawable);
                }
                else {
                    holder.vBorder.setVisibility(GONE);
                }

                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(0, 10, 0, 0);
                holder.llCardView.setLayoutParams(buttonLayoutParams);
                holder.llCardView.setBackground(rightDrawable);
            }
            else
            {
                GradientDrawable rightDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.card_template_bg);
                if(rightDrawable != null)
                {
                    rightDrawable.setColor(Color.parseColor("#ffffff"));

                    if(cardTemplateModel.getCardStyles() != null)
                    {
                        if(!StringUtils.isNullOrEmpty(cardTemplateModel.getCardStyles().getBackground_color()))
                            rightDrawable.setColor(Color.parseColor(cardTemplateModel.getCardStyles().getBackground_color()));
                    }

                    rightDrawable.setStroke((int) (1 * dp1), Color.parseColor("#84959B"));
                    holder.llCardView.setBackground(rightDrawable);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return arrCardTemplateModels.size();
    }

    @Override
    public void advanceButtonClick(ArrayList<AdvanceOptionsModel> viewType) {

    }

    @Override
    public void closeWindow() {
        if(popupWindow != null)
            popupWindow.dismiss();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private final ImageView bot_list_item_image, ivheaderExtra;
        private final TextView bot_list_item_title, bot_list_item_subtitle, tvOnlyTitle, tvCardButton, tvheaderExtraTitle;
        private final RelativeLayout rlTitle;
        private final RecyclerView rvDescription;
        private final LinearLayout llCardView, llCardViewTop;
        private final View vBorder;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            llCardView = itemView.findViewById(R.id.llCardView);
            llCardViewTop = itemView.findViewById(R.id.llCardViewTop);
            bot_list_item_title = itemView.findViewById(R.id.bot_list_item_title);
            bot_list_item_subtitle = itemView.findViewById(R.id.bot_list_item_subtitle);
            bot_list_item_image = itemView.findViewById(R.id.bot_list_item_image);
            tvOnlyTitle = itemView.findViewById(R.id.tvOnlyTitle);
            rlTitle = itemView.findViewById(R.id.rlTitle);
            rvDescription = itemView.findViewById(R.id.rvDescription);
            tvCardButton = itemView.findViewById(R.id.tvCardButton);
            vBorder = itemView.findViewById(R.id.vBorder);
            ivheaderExtra = itemView.findViewById(R.id.ivheaderExtra);
            tvheaderExtraTitle = itemView.findViewById(R.id.tvheaderExtraTitle);
        }
    }
}
