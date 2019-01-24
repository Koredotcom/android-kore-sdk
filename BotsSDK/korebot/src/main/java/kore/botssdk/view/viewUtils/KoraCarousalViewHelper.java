package kore.botssdk.view.viewUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraSearchDataSetModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */


public class KoraCarousalViewHelper {

    public static class KoraCarousalViewHolder {
        public View emailView;
        public View knowledgeView;
        public View showMoreView;

    }
    public static class KoraFilesCarousalViewHolder {
        public View fileLookupViewRoot;
    }

    public static void initializeViewHolder(View view, KoraSearchDataSetModel.ViewType viewType) {
        KoraCarousalViewHolder carouselViewHolder = new KoraCarousalViewHolder();
        if(viewType == KoraSearchDataSetModel.ViewType.EMAIL_VIEW) {
            carouselViewHolder.emailView = view.findViewById(R.id.email_view_root);
        }else {
            carouselViewHolder.knowledgeView = view.findViewById(R.id.knowledge_view_root);
        }
        carouselViewHolder.showMoreView = view.findViewById(R.id.show_more_view);
        view.setTag(carouselViewHolder);
    }
    public static void initializeFileLookupViewHolder(View view) {
        KoraFilesCarousalViewHolder carouselViewHolder = new KoraFilesCarousalViewHolder();
        carouselViewHolder.fileLookupViewRoot = view.findViewById(R.id.file_lookup_view_root);
        view.setTag(carouselViewHolder);
    }


    public static void populateStuffs(KoraCarousalViewHolder carouselViewHolder,
                                      final ComposeFooterInterface composeFooterInterface,
                                      final InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                                      final KoraSearchDataSetModel dataSetModel,
                                      final Context activityContext) {
        if (dataSetModel.getViewType() == KoraSearchDataSetModel.ViewType.EMAIL_VIEW) {
            carouselViewHolder.emailView.setVisibility(View.VISIBLE);
            TextView from = (TextView) carouselViewHolder.emailView.findViewById(R.id.from_info);
            TextView to = (TextView) carouselViewHolder.emailView.findViewById(R.id.to_info);
            TextView cc = (TextView) carouselViewHolder.emailView.findViewById(R.id.cc_info);
            TextView title = (TextView) carouselViewHolder.emailView.findViewById(R.id.title);
            TextView desc = (TextView) carouselViewHolder.emailView.findViewById(R.id.description);
            TextView attachment = (TextView) carouselViewHolder.emailView.findViewById(R.id.attachment_view);
            ListView listView = (ListView) carouselViewHolder.emailView.findViewById(R.id.list_view);
            TextView emailType = (TextView) carouselViewHolder.emailView.findViewById(R.id.email_type);
            TextView createdInfo = (TextView) carouselViewHolder.emailView.findViewById(R.id.mail_date);
            TextView ccLabel = (TextView)carouselViewHolder.emailView.findViewById(R.id.cc_label);


            final EmailModel emailModel = (EmailModel) dataSetModel.getPayload();
            from.setText(StringEscapeUtils.unescapeHtml4(emailModel.getFrom()));
            if (emailModel.getTo() != null) {
                to.setText(StringEscapeUtils.unescapeHtml4(StringUtils.join(emailModel.getTo(), ", ")));
            }
            if (emailModel.getCc() != null && emailModel.getCc().length > 0) {
                cc.setText(StringEscapeUtils.unescapeHtml4(StringUtils.join(emailModel.getCc(), ", ")));
                ccLabel.setVisibility(View.VISIBLE);
                cc.setVisibility(View.VISIBLE);
            }else{
                ccLabel.setVisibility(View.GONE);
                cc.setVisibility(View.GONE);
            }
            title.setText(emailModel.getSubject());
            if(!kore.botssdk.utils.StringUtils.isNullOrEmpty(emailModel.getDesc())) {
                desc.setText(StringEscapeUtils.unescapeHtml4(emailModel.getDesc()));
                desc.setVisibility(View.VISIBLE);
            }else{
                desc.setVisibility(View.GONE);
            }
            int attachment_count = emailModel.getAttachments() != null ? emailModel.getAttachments().length :0;
            if(attachment_count > 0) {
                attachment.setText(activityContext.getResources().getQuantityString(R.plurals.attachment_count,
                        attachment_count,attachment_count));

                //attachment.setText(String.format(activityContext.getResources().getString(R.string.atatchment_format), emailModel.getAttachments().length));
                attachment.setVisibility(View.VISIBLE);
            }else {
                attachment.setVisibility(View.GONE);
            }
            emailType.setText(emailModel.getSource());

            createdInfo.setText(emailModel.getDate() != null && emailModel.getDate().indexOf("+") != -1 ?emailModel.getDate().substring(0,emailModel.getDate().indexOf("+")):emailModel.getDate());
            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            listView.setAdapter(botCarouselItemButtonAdapter);
           // listView.getLayoutParams().height = (int)(emailModel.getButtons() != null ? emailModel.getButtons().size() * (48 * dp1) : 0);
            botCarouselItemButtonAdapter.setBotCaourselButtonModels(emailModel.getButtons() != null ? emailModel.getButtons() : new ArrayList<BotCaourselButtonModel>());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        BotCaourselButtonModel botCaourselButtonModel = (BotCaourselButtonModel) parent.getAdapter().getItem(position);
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botCaourselButtonModel.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            String buttonPayload = botCaourselButtonModel.getPayload();
                            String buttonTitle = botCaourselButtonModel.getTitle();
                            composeFooterInterface.onSendClick(buttonTitle, buttonPayload);
                        }else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                                invokeGenericWebViewInterface.handleUserActions(botCaourselButtonModel.getAction(), botCaourselButtonModel.getCustomData());
                        }
                    }
                }
            });

        } else if (dataSetModel.getViewType() == KoraSearchDataSetModel.ViewType.KNOWLEDGE_VIEW) {
            carouselViewHolder.knowledgeView.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) carouselViewHolder.knowledgeView.findViewById(R.id.knowledge_image);
            TextView title = (TextView) carouselViewHolder.knowledgeView.findViewById(R.id.knowledge_title);
            TextView desc = (TextView) carouselViewHolder.knowledgeView.findViewById(R.id.knowledge_description);
            TextView hashTagView = (TextView)carouselViewHolder.knowledgeView.findViewById(R.id.hash_tags_view);
            TextView createdDate = (TextView)carouselViewHolder.knowledgeView.findViewById(R.id.created_date);
            TextView knowledgeType = (TextView)carouselViewHolder.knowledgeView.findViewById(R.id.knowledge_type);
            TextView createrInfo = (TextView)carouselViewHolder.knowledgeView.findViewById(R.id.creator_info);
            ListView listView = (ListView) carouselViewHolder.knowledgeView.findViewById(R.id.list_view);

            final KnowledgeDetailModel knowledgeDetailModel = (KnowledgeDetailModel) dataSetModel.getPayload();
            title.setText(knowledgeDetailModel.getTitle());
            desc.setText(knowledgeDetailModel.getDesc());
            try {
                if(knowledgeDetailModel.getImage_url() != null && !knowledgeDetailModel.getImage_url().isEmpty())
                    Picasso.with(activityContext).load(knowledgeDetailModel.getImage_url()).into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(knowledgeDetailModel.getButtons() != null) {
                BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
                listView.setAdapter(botCarouselItemButtonAdapter);
                botCarouselItemButtonAdapter.setBotCaourselButtonModels(knowledgeDetailModel.getButtons());
            }

            hashTagView.setText(StringUtils.join(knowledgeDetailModel.getHashTag(), "#"));
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (composeFooterInterface != null && invokeGenericWebViewInterface != null) {
                        BotCaourselButtonModel botCaourselButtonModel = (BotCaourselButtonModel) parent.getAdapter().getItem(position);
                        if (BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            invokeGenericWebViewInterface.invokeGenericWebView(botCaourselButtonModel.getUrl());
                        } else if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            String buttonPayload = botCaourselButtonModel.getPayload();
                            String buttonTitle = botCaourselButtonModel.getTitle();
                            composeFooterInterface.onSendClick(buttonTitle, buttonPayload);
                        }else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                            invokeGenericWebViewInterface.handleUserActions(botCaourselButtonModel.getAction(),botCaourselButtonModel.getCustomData());
                        }
                    }
                }
            });


        } else if (dataSetModel.getViewType() == KoraSearchDataSetModel.ViewType.SHOW_MORE_VIEW) {

        }


    }

    public static void populateFileLookUpStuffs(KoraFilesCarousalViewHolder carouselViewHolder, final KaFileLookupModel dataModel, final Context activityContext) {
            carouselViewHolder.fileLookupViewRoot.setVisibility(View.VISIBLE);
            TextView txtFileType = (TextView) carouselViewHolder.fileLookupViewRoot.findViewById(R.id.txtFileType);
            TextView txtTitle = (TextView) carouselViewHolder.fileLookupViewRoot.findViewById(R.id.txtTitle);
            TextView txtFileSize = (TextView) carouselViewHolder.fileLookupViewRoot.findViewById(R.id.txtFileSize);
            TextView txtSharedByName = (TextView) carouselViewHolder.fileLookupViewRoot.findViewById(R.id.txtSharedByName);
            TextView txtLastEdited = (TextView) carouselViewHolder.fileLookupViewRoot.findViewById(R.id.txtLastEdited);
            ListView listView = (ListView) carouselViewHolder.fileLookupViewRoot.findViewById(R.id.flist_view);


        GradientDrawable tvBackground = (GradientDrawable) txtFileType.getBackground();
        int icon = FileUtils.getColorForFileType(dataModel.getFileType());
        tvBackground.setStroke((int)(3*dp1),icon);
        txtFileType.setBackground(tvBackground);
        txtFileType.setTextColor(icon);
        txtFileType.setText(dataModel.getFileType()==null?"FILE":dataModel.getFileType().toUpperCase());
        txtTitle.setText(dataModel.getFileName());
        txtFileSize.setText(dataModel.getFileSize()==null?"":dataModel.getFileSize());
        txtSharedByName.setText(dataModel.getSharedBy());
        try{
        Date dt = DateUtils.isoFormatter.parse(dataModel.getLastModified());
        txtLastEdited.setText("Last Edited "+ DateUtils.calendar_list_format.format(dt));
        }catch (Exception e){

        }
            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            listView.setAdapter(botCarouselItemButtonAdapter);
            // listView.getLayoutParams().height = (int)(emailModel.getButtons() != null ? emailModel.getButtons().size() * (48 * dp1) : 0);
            botCarouselItemButtonAdapter.setBotCaourselButtonModels(dataModel.getButtons() != null ? dataModel.getButtons() : new ArrayList<BotCaourselButtonModel>());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BotCaourselButtonModel botCaourselButtonModel = (BotCaourselButtonModel) parent.getAdapter().getItem(position);
                    LinkedTreeMap<String, String> map = (LinkedTreeMap<String, String>) botCaourselButtonModel.getCustomData().get("redirectUrl");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map.get("mob")));
                    activityContext.startActivity(browserIntent);
                }
            });

    }

    private static int getButtonHeight(Context context, int itemCount, float dp1) {
        return (int) (context.getResources().getDimension(R.dimen.carousel_view_button_height_individual) * dp1 + 4 * dp1);
    }

}