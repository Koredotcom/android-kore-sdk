package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.WidgetTaskViewLayoutBinding;
import kore.botssdk.dialogs.WidgetActionSheetFragment;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.MultiAction;
import kore.botssdk.models.WTaskTemplateModel;
import kore.botssdk.models.Widget;
import kore.botssdk.models.WidgetTaskTemplateResponse;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.WidgetViewMoreEnum;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;

public class WidgetKoraTasksListAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {
    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;
    private Context context;
    private float maxWidth;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean isExpanded = false;
    private boolean isFromFullView;
    private int DATA_FOUND = 1;
    private int NO_DATA = 0;
    private int MESSAGE=2;
    private String nodata_meesage = "";
    private int previewLength;
    String msg;
    Drawable errorIcon;
    public Widget.Hook getApi() {
        return api;
    }
    String userId;
    public void setApi(Widget.Hook api) {
        this.api = api;
    }

    private Widget.Hook api;
    public String getNodata_meesage() {
        return nodata_meesage;
    }

    public void setNodata_meesage(String nodata_meesage) {
        this.nodata_meesage = nodata_meesage;
    }

    //created for widget
    private boolean from_widget = false;

    public boolean isFrom_widget() {
        return from_widget;
    }

    public void setFrom_widget(boolean from_widget) {
        this.from_widget = from_widget;
    }

    public void addTaskTemplateModels(ArrayList<WTaskTemplateModel> models) {
        this.models.addAll(models);
    }

    public ArrayList<String> getSelectedTasks() {
        return selectedTasks;
    }

    public void setSelectedTasks(ArrayList<String> selectedTasks) {
        this.selectedTasks = selectedTasks;
    }

    private ArrayList<String> selectedTasks = new ArrayList<>();
    WidgetViewMoreEnum widgetViewMoreEnum;
    public void setViewMoreEnum(WidgetViewMoreEnum widgetViewMoreEnum) {
        this.widgetViewMoreEnum=widgetViewMoreEnum;
    }
    private void addOrRemoveSelectedTask(String taskId) {
        if (selectedTasks.contains(taskId)) {
            selectedTasks.remove(taskId);
        } else {
            selectedTasks.add(taskId);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (models != null && models.size() > 0) {
            return DATA_FOUND;
        }
        if(msg!=null&&!msg.equalsIgnoreCase(""))
        {
            return MESSAGE;
        }
        return NO_DATA;
    }

    public void addSelectedTasks(ArrayList<String> tasks) {
        selectedTasks.addAll(tasks);
    }

    private boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    private WidgetTaskTemplateResponse taskTemplateResponse;
    private boolean showButton;
    private ArrayList<WTaskTemplateModel> models;

    public ArrayList<MultiAction> getMultiActions() {
        return multiActions;
    }

    public void setMultiActions(ArrayList<MultiAction> multiActions) {
        this.multiActions = multiActions;
    }

    private ArrayList<MultiAction> multiActions;

    public WidgetKoraTasksListAdapter(Context context, WidgetTaskTemplateResponse taskTemplateResponse,ArrayList<MultiAction> multiActions, boolean showButtons, boolean isFromFullView) {
        this.context = context;
        this.taskTemplateResponse = taskTemplateResponse;
        this.showButton = showButtons;
        this.multiActions = multiActions;
        if(taskTemplateResponse!=null) {
            this.models = taskTemplateResponse.getTaskData();
        }
        selectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_on);
        unSelectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_off);
        this.isFromFullView = isFromFullView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NO_DATA||viewType==MESSAGE) {

            View view = LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        } else {
            return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_task_view_layout, parent, false));

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHoldermain, int position) {
        if (viewHoldermain.getItemViewType() == DATA_FOUND) {

            ViewHolder holder = (ViewHolder) viewHoldermain;
            WTaskTemplateModel taskTemplateModel = models.get(position);
            holder.taskViewLayoutBinding.setWidgetTask(taskTemplateModel);
            boolean isSelected = selectedTasks.contains(taskTemplateModel.getId()) && isShowButton();
            boolean isClosed = "close".equalsIgnoreCase(taskTemplateModel.getData().getStatus());
            holder.taskViewLayoutBinding.getRoot().setSelected(isSelected);
            holder.taskViewLayoutBinding.getRoot().setEnabled(!isClosed && isShowButton());
            holder.taskViewLayoutBinding.titleView.setPaintFlags(isClosed ? Paint.STRIKE_THRU_TEXT_FLAG : holder.taskViewLayoutBinding.titleView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskViewLayoutBinding.checkbox.setImageDrawable(isSelected ? selectedCheck : unSelectedCheck);
            holder.taskViewLayoutBinding.checkbox.setVisibility(isShowButton() && selectedTasks.size() > 0 ? View.VISIBLE : View.GONE);
            holder.taskViewLayoutBinding.iconDown.setVisibility(isShowButton() && selectedTasks.size() > 0 ? View.GONE : View.VISIBLE);
            holder.taskViewLayoutBinding.checkbox.setEnabled(!isClosed);
            holder.taskViewLayoutBinding.checkbox.setAlpha(isClosed ? 0.4f : 1.0f);
            holder.taskViewLayoutBinding.titleView.setTypeface(null, isClosed ? Typeface.NORMAL : Typeface.BOLD);

            if(userId!=null&& !TextUtils.isEmpty(userId))
            {
                try {
                    holder.taskViewLayoutBinding.assigneeView.setVisibility(View.VISIBLE);
                    String tempOwnerUserId = models.get(position).getData().getOwner().get_id();
                    String tempAssigneeUserId = models.get(position).getData().getAssignee().get_id();
                    if (userId.equals(tempOwnerUserId) && userId.equals(tempAssigneeUserId)) {
                        holder.taskViewLayoutBinding.creatorView.setText("You");
                        holder.taskViewLayoutBinding.assigneeView.setVisibility(View.INVISIBLE);
                    } else {
                        holder.taskViewLayoutBinding.creatorView.setText(userId.equals(tempOwnerUserId) ? "You" : models.get(position).getData().getOwner().getNameInFirstNameFormat());
                        holder.taskViewLayoutBinding.assigneeView.setText(userId.equals(tempAssigneeUserId) ? "You" : models.get(position).getData().getAssignee().getNameInFirstNameFormat());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    holder.taskViewLayoutBinding.assigneeView.setVisibility(View.VISIBLE);
                    holder.taskViewLayoutBinding.creatorView.setText(models.get(position).getData().getOwner().getNameInFirstNameFormat());
                    holder.taskViewLayoutBinding.assigneeView.setText(models.get(position).getData().getAssignee().getNameInFirstNameFormat());
                }
            }
            else {
                holder.taskViewLayoutBinding.assigneeView.setVisibility(View.VISIBLE);
                holder.taskViewLayoutBinding.creatorView.setText(models.get(position).getData().getOwner().getNameInFirstNameFormat());
               holder.taskViewLayoutBinding.assigneeView.setText(models.get(position).getData().getAssignee().getNameInFirstNameFormat());
            }


          holder.taskViewLayoutBinding.rootLayout.setBackground(isFromFullView?context.getResources().getDrawable(R.drawable.task_view_background):null);
//            holder.taskViewLayoutBinding.iconDown.setTypeface(ResourcesCompat.getFont(context, R.font.icomoon));
            /*Drawable drawable = ContextCompat.getDrawable(context, R.drawable.round_shape_common);
            try {
                ((GradientDrawable) drawable).setColor(context.getResources().getColor(R.color.color_d8d8d8));

            } catch (Exception e) {

            }
            holder.taskViewLayoutBinding.iconDown.setBackground(drawable);*/



            holder.taskViewLayoutBinding.iconDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                    bottomSheetDialog.setisFromFullView(isFromFullView);
                    bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper);
                    bottomSheetDialog.setData(taskTemplateModel);
                    bottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "add_tags");


                }
            });


            holder.taskViewLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFrom_widget()) {
                        if (showButton && !"close".equalsIgnoreCase(taskTemplateModel.getData().getStatus()) && selectedTasks.size() > 0) {
                            updateThings(taskTemplateModel);
                        }
                    } else {

                        if (selectedTasks != null && selectedTasks.size() > 0) {
                            updateThings(taskTemplateModel);
                        } else /*if(!isFromFullView)*/ {
                   /*         WidgetDialogActivityTask dialogActivity = new WidgetDialogActivityTask(context, taskTemplateModel, taskTemplateModel,isFromFullView);

                            dialogActivity.show();

                            dialogActivity.findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialogActivity.dissmissanim();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialogActivity.dismiss();
                                        }
                                    }, 400);

                                }
                            });*/

                           /* WidgetActionSheetFragment bottomSheetDialog = new WidgetActionSheetFragment();
                            bottomSheetDialog.setisFromFullView(isFromFullView);
                            bottomSheetDialog.setData(taskTemplateModel);
                            bottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "add_tags");
*/

                        }
                    }
                }
            });
            holder.taskViewLayoutBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //  if (!isFrom_widget()) {
                    updateThings(taskTemplateModel);

                    return true;
                    // }
                    // return false;
                }
            });
        } else {
            EmptyWidgetViewHolder emptyHolder = (EmptyWidgetViewHolder) viewHoldermain;
            emptyHolder.tv_disrcription.setText(viewHoldermain.getItemViewType() == NO_DATA?getNodata_meesage():msg);
            emptyHolder.img_icon.setImageDrawable(viewHoldermain.getItemViewType() == NO_DATA?ContextCompat.getDrawable(context, R.drawable.no_meeting):errorIcon);
        }
    }

    private void updateThings(WTaskTemplateModel taskTemplateModel) {
        if (verticalListViewActionHelper != null) {
            if (showButton && !"close".equalsIgnoreCase(taskTemplateModel.getData().getStatus())) {
                addOrRemoveSelectedTask(taskTemplateModel.getId());
                SelectionUtils.setSelectedTasks(selectedTasks);
                if (verticalListViewActionHelper != null)
                    verticalListViewActionHelper.tasksSelectedOrDeselected(selectedTasks.size() > 0);
                notifyDataSetChanged();
            }
        }
    }


    @Override
    public int getItemCount() {
        if(widgetViewMoreEnum!=null&&widgetViewMoreEnum==WidgetViewMoreEnum.EXPAND_VIEW)
        {
            return models != null && models.size() > 0 ?models.size():1;
        }
        return models != null && models.size() > 0 ? (!isExpanded && models.size() > previewLength ? previewLength : models.size()) : 1;
    }


    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public ArrayList getData() {
        return models;
    }

    @Override
    public void setData(ArrayList data) {
        models = data;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public WidgetTaskTemplateResponse getTaskTemplateResponse() {
        return taskTemplateResponse;
    }

    public void setTaskTemplateResponse(WidgetTaskTemplateResponse taskTemplateResponse) {
        this.taskTemplateResponse = taskTemplateResponse;
    }

    public void setPreviewLength(int previewLength) {
        this.previewLength=previewLength;
    }

    public void setMessage(String msg, Drawable errorIcon) {
        this.msg=msg;
        this.errorIcon=errorIcon;
    }

    public void setUserId(String userId) {

        this.userId=userId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WidgetTaskViewLayoutBinding taskViewLayoutBinding;

        public ViewHolder(@NonNull WidgetTaskViewLayoutBinding binding) {
            super(binding.getRoot());
            this.taskViewLayoutBinding = binding;
        }
    }
}
