package kore.botssdk.view.viewUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.fragment.ComposeFooterFragment;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraSearchDataSetModel;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */


public class KoraCarousalViewHelper {

    public static class KoraCarousalViewHolder {
        public View emailView;
        public View knowledgeView;
        public View showMoreView;
        public View viewRoot;

    }

    public static void initializeViewHolder(View view) {
        KoraCarousalViewHolder carouselViewHolder = new KoraCarousalViewHolder();
        carouselViewHolder.emailView = view.findViewById(R.id.email_view_root);
        carouselViewHolder.knowledgeView = view.findViewById(R.id.knowledge_view_root);
        carouselViewHolder.showMoreView = view.findViewById(R.id.show_more_view);
        carouselViewHolder.viewRoot = view.findViewById(R.id.item_view_root);
        view.setTag(carouselViewHolder);
    }

    public static void populateStuffs(KoraCarousalViewHolder carouselViewHolder,
                                      final ComposeFooterFragment.ComposeFooterInterface composeFooterInterface,
                                      final InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                                      final KoraSearchDataSetModel dataSetModel,
                                      final Context activityContext) {
        carouselViewHolder.knowledgeView.setVisibility(View.GONE);
        carouselViewHolder.emailView.setVisibility(View.GONE);
        carouselViewHolder.showMoreView.setVisibility(View.GONE);
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
            if (emailModel.getCc() != null) {
                cc.setText(StringEscapeUtils.unescapeHtml4(StringUtils.join(emailModel.getCc(), ", ")));
                ccLabel.setVisibility(View.GONE);
                cc.setVisibility(View.GONE);
            }else{
                ccLabel.setVisibility(View.VISIBLE);
                cc.setVisibility(View.VISIBLE);
            }
            title.setText(emailModel.getSubject());
            desc.setText(emailModel.getDesc());
            attachment.setText(String.format(activityContext.getResources().getString(R.string.atatchment_format),1));
            attachment.setVisibility(View.GONE);
            emailType.setText(emailModel.getSource());
            createdInfo.setText(emailModel.getDate().substring(0,emailModel.getDate().indexOf("+")));
            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            listView.setAdapter(botCarouselItemButtonAdapter);
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
                            HashMap<String,String> redirectUrls = botCaourselButtonModel.getRedirectUrl();
                            if(redirectUrls == null || redirectUrls.size()==0 ) {
                                invokeGenericWebViewInterface.handleUserActions(emailModel.getDesc(), botCaourselButtonModel.getType());
                            }else{
                                String desktopUrl = redirectUrls.get("dweb");
                                String mobUrl = redirectUrls.get("mob");
                                if (mobUrl != null) {
                                    invokeGenericWebViewInterface.invokeGenericWebView(mobUrl);
                                  /*  try {
                                        Intent intent = activityContext.getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                                        activityContext.startActivity(intent);
                                    } catch (Exception e) {

                                    }*/
                                }


                            }
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
                            invokeGenericWebViewInterface.handleUserActions(((KnowledgeDetailModel)knowledgeDetailModel).getId(),botCaourselButtonModel.getType());
                        }
                    }
                }
            });


        } else if (dataSetModel.getViewType() == KoraSearchDataSetModel.ViewType.SHOW_MORE_VIEW) {

        }


    }

    private static int getButtonHeight(Context context, int itemCount, float dp1) {
        return (int) (context.getResources().getDimension(R.dimen.carousel_view_button_height_individual) * dp1 + 4 * dp1);
    }
}