package com.kore.ai.widgetsdk.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.listeners.WidgetComposeFooterInterface;
import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;
import com.kore.ai.widgetsdk.models.searchskill.PanelLevelData;
import com.kore.ai.widgetsdk.utils.WidgetConstants;
import com.kore.ai.widgetsdk.utils.WidgetViewMoreEnum;
import com.kore.ai.widgetsdk.viewholder.ArticleWidgetViewHolder;
import com.kore.ai.widgetsdk.listeners.UpdateRefreshItem;
import com.kore.ai.widgetsdk.models.PanelBaseModel;
import com.kore.ai.widgetsdk.views.widgetviews.ArticlesWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.BarChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.ChartListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.DefaultWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.FormWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.GenericWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.LineChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.ListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.MeetingWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.PieChartWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.SkillWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.TableListWidgetView;
import com.kore.ai.widgetsdk.views.widgetviews.TrendingHashTagView;

import java.util.HashMap;


public class KaWidgetBaseAdapterNew extends RecyclerView.Adapter implements UpdateRefreshItem, VerticalListViewActionHelper {

    private Context kaWidgetFragmentContext;
    private Context activityObj;
    private WidgetViewMoreEnum widgetViewMoreEnum;
    PanelLevelData panelData;
    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }
    public void setFirstLaunch(boolean firstLaunch) {
        isFirstLaunch = firstLaunch;
    }
    private boolean isFirstLaunch;
    private String skillName;
    private String jwtToken;
    public PanelBaseModel getWidget() {
        return widget;
    }

    public void setWidget(PanelBaseModel widget, String jwtToken) {
        this.widget = widget;
        if(this.widget!=null&&this.widget.getData()!=null&&this.widget.getData().get_id()!=null) {
            PanelLevelData panelData=new PanelLevelData();
            panelData.set_id(widget.getData().get_id());
            panelData.setSkillId(widget.getData().getSkillId());
            panelData.setName(widget.getData().getName()!=null?widget.getData().getName():"");
            this.panelData=panelData;
            this.jwtToken = jwtToken;
        }
        widgetTemp = this.widget;
        notifyDataSetChanged();
    }

    private PanelBaseModel widget = null;
    private PanelBaseModel widgetTemp = null;

    public KaWidgetBaseAdapterNew(Context kaWidgetFragmentContext, WidgetViewMoreEnum widgetViewMoreEnum, boolean isFirstLaunch, String skillName) {
        this.kaWidgetFragmentContext = kaWidgetFragmentContext;
        activityObj = this.kaWidgetFragmentContext;
        this.widgetViewMoreEnum = widgetViewMoreEnum;
        this.isFirstLaunch = isFirstLaunch;
        this.skillName = skillName;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int templateType) {
        View view = null;
        switch (templateType) {
            case WidgetConstants.MEETINGS_TEMPLATE:
                MeetingWidgetView mView = new MeetingWidgetView(parent.getContext(), this, widget.getData().getName(), widgetViewMoreEnum);
                return new MeetingViewHolder(mView);
            case WidgetConstants.CHART_LIST_TEMPLATE:
                ChartListWidgetView cw = new ChartListWidgetView(activityObj, this, widget.getData().getName(), widgetViewMoreEnum);
                return new ChartListViewHolder(cw);

//            case WidgetConstants.FILES_TEMPLATE:
//            case WidgetConstants.TASK_LIST_TEMPLATE:
//                TaskWidgetView taskWidgetView = new TaskWidgetView(parent.getContext(), widgetViewMoreEnum);
//                return new GenericWidgetViewHolder(taskWidgetView);
            case WidgetConstants.ARTICLES_TEMPLATE:
                ArticlesWidgetView articlesWidgetView = new ArticlesWidgetView(parent.getContext(), widgetViewMoreEnum);
                return new ArticleWidgetViewHolder(articlesWidgetView);
            case WidgetConstants.HASH_TAG_TEMPLATE:
                TrendingHashTagView hashTagView = new TrendingHashTagView(activityObj, this, widget.getData().getName(), widgetViewMoreEnum);
                return new HashTagViewHolder(hashTagView);
//            case WidgetConstants.ANNOUNCEMENTS_TEMPLATE:
//                AnnouncementWidgetView annView = new AnnouncementWidgetView(activityObj, this,
//                        widget.getData().getName(), widgetViewMoreEnum);
//                return new AnnouncementViewHolder(annView);

            case WidgetConstants.SKILL_TEMPLATE:
                SkillWidgetView bView = new SkillWidgetView(parent.getContext(), widget.getData().getName(), widgetViewMoreEnum);
                return new SkillViewHolder(bView);

            case WidgetConstants.FILES_SINGLE_TEMPLATE:
            case WidgetConstants.TASKS_SINGLE_TEMPLATE:
                GenericWidgetView gwv = new GenericWidgetView(activityObj, 0, this, null,
                        widget.getData().getName(), "180", false, widgetViewMoreEnum);
                return new GenericWidgetSingleViewHolder(gwv);

            case WidgetConstants.DEFAULT_TEMPLATE:
                DefaultWidgetView dView = new DefaultWidgetView(parent.getContext(), widget.getData().getName(), widgetViewMoreEnum);
                return new DefaultViewHolder(dView);

//            case WidgetConstants.SUMMARY_CARD_TEMPLATE:
//                WelcomeSummaryWidgetView welcomeSummaryView = new WelcomeSummaryWidgetView(parent.getContext(), isFirstLaunch, skillName);
//                return new WeatherViewHolder(welcomeSummaryView);
//
//            case WidgetConstants.CLOUD_TEMPLATE:
//                WelcomeSummaryGreeting welcomeSummaryGreeting = new WelcomeSummaryGreeting(parent.getContext(), isFirstLaunch, skillName);
//                return new WeatherViewGreetingHolder(welcomeSummaryGreeting);


//            case WidgetConstants.HEADLINE_TEMPLATE:
//                WelcomeSummaryViewWidget welcomesummaryview = new WelcomeSummaryViewWidget(parent.getContext(), isFirstLaunch, skillName);
//                return new WelcomeSummaryViewHolder(welcomesummaryview);
//              /*  WeatherWidgetView weatherWidgetView = new WeatherWidgetView(parent.getContext());
//                return new WeatherViewHolder(weatherWidgetView);*/
//
//            case WidgetConstants.QUICK_ACTION_TEMPLATE:
//                QuickActionWidgetView actionWidgetView = new QuickActionWidgetView(parent.getContext(), skillName);
//                return new QuickActionViewHolder(actionWidgetView);

            case WidgetConstants.PIE_CHART_TEMPLATE:
                PieChartWidgetView pieChartWidgetView = new PieChartWidgetView(parent.getContext(),skillName);
                return new PieChartViewHolder(pieChartWidgetView);

            case WidgetConstants.BAR_CHART_TEMPLATE:
                BarChartWidgetView barChartWidgetView = new BarChartWidgetView(parent.getContext());
                return new BarChartViewHolder(barChartWidgetView);

            case WidgetConstants.LINE_CHART_TEMPLATE:
                LineChartWidgetView lineChartWidgetView = new LineChartWidgetView(parent.getContext());
                return new LineChartViewHolder(lineChartWidgetView);
            case WidgetConstants.LIST_WIDGET_TEMPLATE:
                ListWidgetView listWidgetView = new ListWidgetView(parent.getContext(),widget.getData().getName(), widgetViewMoreEnum);
                return new ListWidgetViewHolder(listWidgetView);
            case WidgetConstants.TABLE_LIST_TEMPLATE:
                TableListWidgetView tableListWidgetView = new TableListWidgetView(parent.getContext(),widget.getData().getName(), widgetViewMoreEnum);
                return new TableListWidgetViewHolder(tableListWidgetView);
            case WidgetConstants.FORM_TEMPLATE:
                FormWidgetView formWidgetView = new FormWidgetView(parent.getContext(), widget.getData().getName());
                return new FormWidgetViewHolder(formWidgetView);
            /*case EDIT_TEMPLATE_TEMP:
                view = LayoutInflater.from(kaWidgetFragmentContext).inflate(R.layout.widget_edit_button, parent, false);
                return new EditViewHolder(view);*/
            default:
                PieChartWidgetView pieChartWidgetView1 = new PieChartWidgetView(parent.getContext(),skillName);
                return new PieChartViewHolder(pieChartWidgetView1);

        }
//        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//        if ((holder instanceof GenericWidgetViewHolder && getItemViewType(position) == WidgetConstants.TASK_LIST_TEMPLATE) ||
//
//                (holder instanceof GenericWidgetViewHolder && getItemViewType(position) == WidgetConstants.FILES_TEMPLATE)) {
//            ((GenericWidgetViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position), position, getItemViewType(position), false,panelData);
//
//        } else
        if (holder instanceof GenericWidgetSingleViewHolder) {
//            ((GenericWidgetSingleViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position), false,panelData);
        } else if (holder instanceof ArticleWidgetViewHolder) {
//            ((ArticleWidgetViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position), position, false,panelData);
        } else if (holder instanceof MeetingViewHolder) {
            ((MeetingViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position),panelData);
        } else if (holder instanceof SkillViewHolder) {
//            ((SkillViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position),panelData);
        }
        else if (holder instanceof FormWidgetViewHolder) {
            ((FormWidgetViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position),panelData, jwtToken);
        }
//        else if (holder instanceof AnnouncementViewHolder) {
////            ((AnnouncementViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position), false,panelData);
//        }
        else if (holder instanceof DefaultViewHolder) {
            ((DefaultViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position),panelData, jwtToken);
        } else if (holder instanceof ChartListViewHolder) {
//            ((ChartListViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position),panelData);
        }
//        else if (holder instanceof WeatherViewHolder) {
////            ((WeatherViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position));
////            ((WeatherViewHolder) holder).getCustomView().setComposeFooterInterface((PanelMainActivity) activityObj);
//        } else if (holder instanceof QuickActionViewHolder) {
////            ((QuickActionViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position));
//        }
        else if (holder instanceof EditViewHolder) {

            EditViewHolder editViewHolder=(EditViewHolder)holder;

            editViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Intent intent=new Intent(kaWidgetFragmentContext, WidgetReOrderActivity.class);
//                    intent.putExtra("data",widget);
//
//                    kaWidgetFragmentContext.startActivity(intent);

                }
            });
        }else if (holder instanceof PieChartViewHolder) {
            ((PieChartViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position),panelData,jwtToken);
        }else if (holder instanceof BarChartViewHolder) {
            ((BarChartViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position),panelData, jwtToken);
        }else if (holder instanceof LineChartViewHolder) {
            ((LineChartViewHolder) holder).getCustomView().setWidget(widget.getData().getName(),widget.getData().getWidgets().get(position),panelData, jwtToken);
        }
//        else if (holder instanceof WeatherViewGreetingHolder) {
////            ((WeatherViewGreetingHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position));
////            ((WeatherViewGreetingHolder) holder).getCustomView().setComposeFooterInterface((PanelMainActivity) activityObj);
//        }
//        else if (holder instanceof WelcomeSummaryViewHolder) {
////            ((WelcomeSummaryViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position));
////            ((WelcomeSummaryViewHolder) holder).getCustomView().setComposeFooterInterface((PanelMainActivity) activityObj);
//        }
        else if (holder instanceof ListWidgetViewHolder) {
            ((ListWidgetViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position),panelData,"", jwtToken);
        }
        else if (holder instanceof TableListWidgetViewHolder) {
            ((TableListWidgetViewHolder) holder).getCustomView().setWidget(widget.getData().getWidgets().get(position),panelData, "", jwtToken);
        }
    }


    @Override
    public int getItemCount() {
        if (widgetTemp != null && widgetTemp.getData().getWidgets() != null) {
            return widgetTemp.getData().getWidgets().size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {

        switch (widgetTemp.getData().getWidgets().get(position).getTemplateType().toLowerCase()) {
            case WidgetConstants.MEETINGS_TEMPLATE_SERVER:
                return WidgetConstants.MEETINGS_TEMPLATE;
            case WidgetConstants.CHART_LIST:
                return WidgetConstants.CHART_LIST_TEMPLATE;

            case WidgetConstants.TASK_LIST:
                if (widget.getData().getWidgets().get(position).getTemplateType().toLowerCase().equals("list")) {
                    return WidgetConstants.TASKS_SINGLE_TEMPLATE;
                } else {
                    return WidgetConstants.TASK_LIST_TEMPLATE;
                }

            case WidgetConstants.FILES_TEMPLATE_SERVER:
                if (widget.getData().getWidgets().get(position).getTemplateType().toLowerCase().equals("list")) {
                    return WidgetConstants.FILES_SINGLE_TEMPLATE;
                } else {
                    return WidgetConstants.FILES_TEMPLATE;
                }

            case WidgetConstants.HASH_TAG_TEMPLATE_SERVER:
                return WidgetConstants.HASH_TAG_TEMPLATE;

            case WidgetConstants.ARTICLES_TEMPLATE_SERVER:
                return WidgetConstants.ARTICLES_TEMPLATE;

            case WidgetConstants.ANNOUNCEMENTS_TEMPLATE_SERVER:
                return WidgetConstants.ANNOUNCEMENTS_TEMPLATE;

            case WidgetConstants.SKILL_TEMPLATE_SERVER:
                return WidgetConstants.SKILL_TEMPLATE;

            case WidgetConstants.SUMMARY_CARD_SUMMARY:
                return WidgetConstants.SUMMARY_CARD_TEMPLATE;
            case WidgetConstants.PIE_CHART:
                return WidgetConstants.PIE_CHART_TEMPLATE;
            case WidgetConstants.BAR_CHART:
                return WidgetConstants.BAR_CHART_TEMPLATE;
            case WidgetConstants.LINE_CHART:
                return WidgetConstants.LINE_CHART_TEMPLATE;
            case WidgetConstants.LIST_WIDGET:
                return WidgetConstants.LIST_WIDGET_TEMPLATE;

            case WidgetConstants.CLOUD_TEMPLATE_SERVER:
                return WidgetConstants.CLOUD_TEMPLATE;


            case WidgetConstants.HEADLINE_TEMPLATE_SERVER:
                return WidgetConstants.HEADLINE_TEMPLATE;

            case WidgetConstants.STANDARD:
                return WidgetConstants.TABLE_LIST_TEMPLATE;

            default:

                if (widget.getData().getWidgets().get(position).getTemplateType() != null && widget.getData().getWidgets().get(position).getTemplateType().equalsIgnoreCase("UtterancesList")) {
                    return WidgetConstants.QUICK_ACTION_TEMPLATE;
                }
                return WidgetConstants.DEFAULT_TEMPLATE;

        }
    }


    class EditViewHolder extends RecyclerView.ViewHolder {

        View btnEdit;
        public EditViewHolder(@NonNull View itemView) {
            super(itemView);
//            btnEdit=itemView.findViewById(R.id.btnEdit);
        }
    }

    private Drawable changeColorOfDrawable(Context context, int colorCode) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.round_shape_common);
        try {
            ((GradientDrawable) drawable).setColor(context.getResources().getColor(colorCode));
            return drawable;
        } catch (Exception e) {
            return drawable;
        }
    }

    @Override
    public void knowledgeItemClicked(Bundle extras, boolean isKnowledge) {
//        KaUtility.launchViewDetailsActivity((Activity) activityObj, extras, false);

    }

    @Override
    public void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel) {

    }

    @Override
    public void emailItemClicked(String action, HashMap customData) {

    }

    @Override
    public void calendarItemClicked(String action, BaseCalenderTemplateModel model) {

    }

    @Override
    public void tasksSelectedOrDeselected(boolean selecetd) {

    }

    @Override
    public void widgetItemSelected(boolean isSelected, int count) {

    }

    @Override
    public void navigationToDialAndJoin(String actiontype, String actionLink) {

    }

    @Override
    public void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel) {

    }

    @Override
    public void meetingNotesNavigation(Context context, String mId, String eId) {

    }

    @Override
    public void meetingWidgetViewMoreVisibility(boolean visible) {

    }

    @Override
    public void calendarContactItemClick(ContactViewListModel model) {

    }

    @Override
    public void welcomeSummaryItemClick(WelcomeChatSummaryModel model) {

    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }


//    public class WeatherViewHolder extends RecyclerView.ViewHolder {
//        private WelcomeSummaryWidgetView customView;
//
//        public WeatherViewHolder(View v) {
//            super(v);
//            customView = (WelcomeSummaryWidgetView) v;
//        }
//
//        public WelcomeSummaryWidgetView getCustomView() {
//            return customView;
//        }
//    }
//
//    public class WeatherViewGreetingHolder extends RecyclerView.ViewHolder {
//        private WelcomeSummaryGreeting customView;
//
//        public WeatherViewGreetingHolder(View v) {
//            super(v);
//            customView = (WelcomeSummaryGreeting) v;
//        }
//
//        public WelcomeSummaryGreeting getCustomView() {
//            return customView;
//        }
//    }
//
//    public class WelcomeSummaryViewHolder extends RecyclerView.ViewHolder {
//        private WelcomeSummaryViewWidget customView;
//
//        public WelcomeSummaryViewHolder(View v) {
//            super(v);
//            customView = (WelcomeSummaryViewWidget) v;
//        }
//
//        public WelcomeSummaryViewWidget getCustomView() {
//            return customView;
//        }
//    }
//
//    public class QuickActionViewHolder extends RecyclerView.ViewHolder {
//        private QuickActionWidgetView customView;
//
//        public QuickActionViewHolder(View v) {
//            super(v);
//            customView = (QuickActionWidgetView) v;
//        }
//
//        public QuickActionWidgetView getCustomView() {
//            return customView;
//        }
//    }

    public class MeetingViewHolder extends RecyclerView.ViewHolder {
        private MeetingWidgetView customView;

        public MeetingViewHolder(View v) {
            super(v);
            customView = (MeetingWidgetView) v;
        }

        public MeetingWidgetView getCustomView() {
            return customView;
        }
    }

    public class ChartListViewHolder extends RecyclerView.ViewHolder {
        private ChartListWidgetView customView;

        public ChartListViewHolder(View v) {
            super(v);
            customView = (ChartListWidgetView) v;
        }

        public ChartListWidgetView getCustomView() {
            return customView;
        }
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        private DefaultWidgetView customView;

        public DefaultViewHolder(View v) {
            super(v);
            customView = (DefaultWidgetView) v;
        }

        public DefaultWidgetView getCustomView() {
            return customView;
        }
    }

    public class PieChartViewHolder extends RecyclerView.ViewHolder {
        private PieChartWidgetView customView;

        public PieChartViewHolder(View v) {
            super(v);
            customView = (PieChartWidgetView) v;
        }

        public PieChartWidgetView getCustomView() {
            return customView;
        }
    }
    public class BarChartViewHolder extends RecyclerView.ViewHolder {
        private BarChartWidgetView customView;

        public BarChartViewHolder(View v) {
            super(v);
            customView = (BarChartWidgetView) v;
        }

        public BarChartWidgetView getCustomView() {
            return customView;
        }
    }

    public class FormWidgetViewHolder extends RecyclerView.ViewHolder {
        private FormWidgetView customView;

        public FormWidgetViewHolder(View v) {
            super(v);
            customView = (FormWidgetView) v;
        }

        public FormWidgetView getCustomView() {
            return customView;
        }
    }
    public class LineChartViewHolder extends RecyclerView.ViewHolder {
        private LineChartWidgetView customView;

        public LineChartViewHolder(View v) {
            super(v);
            customView = (LineChartWidgetView) v;
        }

        public LineChartWidgetView getCustomView() {
            return customView;
        }
    }

    public class ListWidgetViewHolder extends RecyclerView.ViewHolder {
        private ListWidgetView customView;

        public ListWidgetViewHolder(View v) {
            super(v);
            customView = (ListWidgetView) v;
        }

        public ListWidgetView getCustomView() {
            return customView;
        }
    }

    public class TableListWidgetViewHolder extends RecyclerView.ViewHolder {
        private TableListWidgetView customView;

        public TableListWidgetViewHolder(View v) {
            super(v);
            customView = (TableListWidgetView) v;
        }

        public TableListWidgetView getCustomView() {
            return customView;
        }
    }

    public class HashTagViewHolder extends RecyclerView.ViewHolder {
        private TrendingHashTagView customView;

        public HashTagViewHolder(View v) {
            super(v);
            customView = (TrendingHashTagView) v;
        }

        public TrendingHashTagView getCustomView() {
            return customView;
        }
    }

//    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
//        private AnnouncementWidgetView customView;
//
//        public AnnouncementViewHolder(View v) {
//            super(v);
//            customView = (AnnouncementWidgetView) v;
//        }
//
//        public AnnouncementWidgetView getCustomView() {
//            return customView;
//        }
//    }

    public class SkillViewHolder extends RecyclerView.ViewHolder {
        private SkillWidgetView customView;

        public SkillViewHolder(View v) {
            super(v);
            customView = (SkillWidgetView) v;
        }

        public SkillWidgetView getCustomView() {
            return customView;
        }
    }

    public class GenericWidgetSingleViewHolder extends RecyclerView.ViewHolder {
        private GenericWidgetView customView;

        public GenericWidgetSingleViewHolder(View v) {
            super(v);
            customView = (GenericWidgetView) v;
        }

        public GenericWidgetView getCustomView() {
            return customView;
        }
    }

    @Override
    public void updateItemToRefresh(int pos) {

    }

    @Override
    public void updateWeatherWidgetSummery(int type, String summery) {

    }

    @Override
    public void onWidgetMenuButtonClicked() {
    }


}
