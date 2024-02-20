package kore.botssdk.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.KoraCarousalAdapter;
import kore.botssdk.adapter.KoraMiniTableAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraSearchDataSetModel;
import kore.botssdk.models.KoraSearchResultsModel;
import kore.botssdk.models.PayloadInner;
@SuppressLint("UnknownNullness")
public class KoraCarouselView extends LinearLayout {
    private Context mContext;
    private HeightAdjustableViewPager carousalView;

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


    public KoraCarouselView(Context mContext){

        super(mContext);
        this.mContext = mContext;
        init();
    }
    public KoraCarouselView(Context mContext, AttributeSet attributes){
        super(mContext,attributes);
        this.mContext = mContext;
        init();
    }
    public KoraCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.kora_carousel_view, this, true);
        carousalView = view.findViewById(R.id.carouselViewpager);
        int pageMargin = (int) getResources().getDimension(R.dimen.carousel_item_page_margin);

        carousalView.setPageMargin(pageMargin);
    }

    public void prepareDataSetAndPopulate(KoraSearchResultsModel koraSearchResultsModel) {

        if (invokeGenericWebViewInterface != null && composeFooterInterface != null) {
            ArrayList<KoraSearchDataSetModel> koraSearchDataSetModels = new ArrayList<>();
            if(koraSearchResultsModel != null) {
                ArrayList<EmailModel> emails = koraSearchResultsModel.getEmails();
                ArrayList<KnowledgeDetailModel> knowledgeDetailModels = koraSearchResultsModel.getKnowledge();
                if (emails != null && emails.size() > 0) {
                    for (EmailModel emailModel : emails) {
                        KoraSearchDataSetModel koraSearchDatasetModel = new KoraSearchDataSetModel();
                        koraSearchDatasetModel.setType("email");
                        koraSearchDatasetModel.setPayload(emailModel);
                        koraSearchDatasetModel.setViewType(KoraSearchDataSetModel.ViewType.EMAIL_VIEW);
                        koraSearchDataSetModels.add(koraSearchDatasetModel);
                    }

                }
                if (knowledgeDetailModels != null && knowledgeDetailModels.size() > 0) {
                    for (KnowledgeDetailModel knowledgeDetailModel : knowledgeDetailModels) {
                        KoraSearchDataSetModel koraSearchDatasetModel = new KoraSearchDataSetModel();
                        koraSearchDatasetModel.setType("knowledge");
                        koraSearchDatasetModel.setPayload(knowledgeDetailModel);
                        koraSearchDatasetModel.setViewType(KoraSearchDataSetModel.ViewType.KNOWLEDGE_VIEW);
                        koraSearchDataSetModels.add(koraSearchDatasetModel);
                    }

                }
            }

            carousalView.setOffscreenPageLimit(4);
            KoraCarousalAdapter koraCarousalAdapter = new KoraCarousalAdapter(koraSearchDataSetModels, mContext, invokeGenericWebViewInterface, composeFooterInterface);
            carousalView.setAdapter(koraCarousalAdapter);
            koraCarousalAdapter.notifyDataSetChanged();
            carousalView.setSwipeLocked(koraSearchDataSetModels.size() == 1);
        }
    }

    public void populateMiniTable(String template_type, PayloadInner payloadInner) {
        KoraMiniTableAdapter koraMiniTableAdapter;
        if(payloadInner != null) {
            carousalView.setOffscreenPageLimit(4);
            koraMiniTableAdapter = new KoraMiniTableAdapter(payloadInner.getMiniTableDataModels(), mContext, template_type);
            carousalView.setAdapter(koraMiniTableAdapter);
            koraMiniTableAdapter.notifyDataSetChanged();
            carousalView.setSwipeLocked(payloadInner.getMiniTableDataModels().size() == 1);
        }else{
            carousalView.setAdapter(null);
            koraMiniTableAdapter = null;
        }
    }
}
