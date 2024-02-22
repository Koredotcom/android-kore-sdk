package kore.botssdk.view.viewUtils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotCarouselItemButtonAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KoraSearchDataSetModel;
import kore.botssdk.utils.BundleConstants;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */


public class KoraCarousalViewHelper {

    public static class KoraCarousalViewHolder {
        View emailView;
        View knowledgeView;
        View showMoreView;

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
    public static void populateStuffs(KoraCarousalViewHolder carouselViewHolder,
                                      final ComposeFooterInterface composeFooterInterface,
                                      final InvokeGenericWebViewInterface invokeGenericWebViewInterface,
                                      final KoraSearchDataSetModel dataSetModel,
                                      final Context activityContext) {
        if (dataSetModel.getViewType() == KoraSearchDataSetModel.ViewType.EMAIL_VIEW) {
            carouselViewHolder.emailView.setVisibility(View.VISIBLE);
            TextView from = carouselViewHolder.emailView.findViewById(R.id.from_info);
            TextView to = carouselViewHolder.emailView.findViewById(R.id.to_info);
            TextView cc = carouselViewHolder.emailView.findViewById(R.id.cc_info);
            TextView title = carouselViewHolder.emailView.findViewById(R.id.title);
            TextView desc = carouselViewHolder.emailView.findViewById(R.id.description);
            TextView attachment = carouselViewHolder.emailView.findViewById(R.id.attachment_view);
            ListView listView = carouselViewHolder.emailView.findViewById(R.id.list_view);
            TextView emailType = carouselViewHolder.emailView.findViewById(R.id.email_type);
            TextView createdInfo = carouselViewHolder.emailView.findViewById(R.id.mail_date);
            TextView ccLabel = carouselViewHolder.emailView.findViewById(R.id.cc_label);


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

            createdInfo.setText(emailModel.getDate() != null && emailModel.getDate().contains("+") ?emailModel.getDate().substring(0,emailModel.getDate().indexOf("+")):emailModel.getDate());
            BotCarouselItemButtonAdapter botCarouselItemButtonAdapter = new BotCarouselItemButtonAdapter(activityContext);
            listView.setAdapter(botCarouselItemButtonAdapter);
            botCarouselItemButtonAdapter.setBotCarouselButtonModels(emailModel.getButtons() != null ? emailModel.getButtons() : new ArrayList<>());
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
                            composeFooterInterface.onSendClick(buttonTitle, buttonPayload,false);
                        }else if (BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(botCaourselButtonModel.getType())) {
                                invokeGenericWebViewInterface.handleUserActions(botCaourselButtonModel.getAction(), botCaourselButtonModel.getCustomData());
                        }
                    }
                }
            });
        }
    }
}