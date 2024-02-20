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
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.databinding.TaskViewLayoutBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.models.TaskTemplateResponse;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.viewHolder.EmptyWidgetViewHolder;

public class TasksListAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {
    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;
    private final Context context;
    private float maxWidth;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean isExpanded = false;
    private final int DATA_FOUND = 1;
    private final int NO_DATA = 0;
    private final String nodata_meesage = "";

    public String getNodata_meesage() {
        return nodata_meesage;
    }

    //created for widget
    private final boolean from_widget = false;

    public boolean isFrom_widget() {
        return from_widget;
    }

    public void setSelectedTasks(ArrayList<String> selectedTasks) {
        this.selectedTasks = selectedTasks;
    }

    private ArrayList<String> selectedTasks = new ArrayList<>();

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
        return NO_DATA;
    }

    private boolean isShowButton() {
        return showButton;
    }

    private final TaskTemplateResponse taskTemplateResponse;
    private final boolean showButton;
    private ArrayList<TaskTemplateModel> models;

    public TasksListAdapter(Context context, TaskTemplateResponse taskTemplateResponse, boolean showButtons) {
        this.context = context;
        this.taskTemplateResponse = taskTemplateResponse;
        this.showButton = showButtons;
        this.models = taskTemplateResponse.getTaskData();
        selectedCheck = ResourcesCompat.getDrawable(context.getResources() , R.mipmap.checkbox_on, context.getTheme());
        unSelectedCheck = ResourcesCompat.getDrawable(context.getResources() , R.mipmap.checkbox_off, context.getTheme());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NO_DATA) {

            View view = LayoutInflater.from(context).inflate(R.layout.card_empty_widget_layout, parent, false);
            return new EmptyWidgetViewHolder(view);
        } else {
            return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.task_view_layout, parent, false));

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
            TaskTemplateModel taskTemplateModel = models.get(position);
            holder.taskViewLayoutBinding.setTask(taskTemplateModel);
            boolean isSelected = selectedTasks.contains(taskTemplateModel.getId()) && isShowButton();
            boolean isClosed = "close".equalsIgnoreCase(taskTemplateModel.getStatus());
            holder.taskViewLayoutBinding.getRoot().setSelected(isSelected);
            holder.taskViewLayoutBinding.getRoot().setEnabled(!isClosed && isShowButton());
            holder.taskViewLayoutBinding.titleView.setPaintFlags(isClosed ? Paint.STRIKE_THRU_TEXT_FLAG : holder.taskViewLayoutBinding.titleView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.taskViewLayoutBinding.checkbox.setImageDrawable(isSelected ? selectedCheck : unSelectedCheck);
            holder.taskViewLayoutBinding.checkbox.setVisibility(isShowButton() && selectedTasks.size() > 0 ? View.VISIBLE : View.GONE);
            holder.taskViewLayoutBinding.checkbox.setEnabled(!isClosed);
            holder.taskViewLayoutBinding.checkbox.setAlpha(isClosed ? 0.4f : 1.0f);
            holder.taskViewLayoutBinding.titleView.setTypeface(null, isClosed ? Typeface.NORMAL : Typeface.BOLD);

            if(Utility.userId !=null&& !TextUtils.isEmpty(Utility.userId))
            {
                try {
                    holder.taskViewLayoutBinding.assigneeView.setVisibility(View.VISIBLE);
                    String tempOwnerUserId = models.get(position).getOwner().get_id();
                    String tempAssigneeUserId = models.get(position).getAssignee().get_id();
                    if (Utility.userId.equals(tempOwnerUserId) && Utility.userId.equals(tempAssigneeUserId)) {
                        holder.taskViewLayoutBinding.creatorView.setText(R.string.you);
                        holder.taskViewLayoutBinding.assigneeView.setVisibility(View.INVISIBLE);
                    } else {
                        holder.taskViewLayoutBinding.creatorView.setText(Utility.userId.equals(tempOwnerUserId) ? "You" : models.get(position).getOwner().getNameInFirstNameFormat());
                        holder.taskViewLayoutBinding.assigneeView.setText(Utility.userId.equals(tempAssigneeUserId) ? "You" : models.get(position).getAssignee().getNameInFirstNameFormat());
                    }
                }catch (Exception e)
                {
                    holder.taskViewLayoutBinding.assigneeView.setVisibility(View.VISIBLE);
                    holder.taskViewLayoutBinding.creatorView.setText(models.get(position).getOwner().getNameInFirstNameFormat());
                    holder.taskViewLayoutBinding.assigneeView.setText(models.get(position).getAssignee().getNameInFirstNameFormat());
                }
            }
            else {
                holder.taskViewLayoutBinding.assigneeView.setVisibility(View.VISIBLE);
                holder.taskViewLayoutBinding.creatorView.setText(models.get(position).getOwner().getNameInFirstNameFormat());
                holder.taskViewLayoutBinding.assigneeView.setText(models.get(position).getAssignee().getNameInFirstNameFormat());
            }



            if(position == models.size()-1 && models.size()<3)
                holder.taskViewLayoutBinding.divider.setVisibility(View.GONE);
            holder.taskViewLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFrom_widget()) {
                        if (showButton && !"close".equalsIgnoreCase(taskTemplateModel.getStatus()) && selectedTasks.size() > 0) {
                            updateThings(taskTemplateModel);
                        }
                    } else {

                        if (selectedTasks != null && selectedTasks.size() > 0) {
                            updateThings(taskTemplateModel);
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

            emptyHolder.tv_disrcription.setText(getNodata_meesage());
            emptyHolder.img_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_meeting));
        }
    }

    private void updateThings(TaskTemplateModel taskTemplateModel) {
        if (verticalListViewActionHelper != null) {
            if (showButton && !"close".equalsIgnoreCase(taskTemplateModel.getStatus())) {
                addOrRemoveSelectedTask(taskTemplateModel.getId());
                SelectionUtils.setSelectedTasks(selectedTasks);
                if (verticalListViewActionHelper != null)
                    verticalListViewActionHelper.tasksSelectedOrDeselected(selectedTasks.size() > 0);
            }
        }
    }


    @Override
    public int getItemCount() {
        return models != null && models.size() > 0 ? (!isExpanded && models.size() > 3 ? 3 : models.size()) : 1;
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

    public TaskTemplateResponse getTaskTemplateResponse() {
        return taskTemplateResponse;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TaskViewLayoutBinding taskViewLayoutBinding;

        public ViewHolder(@NonNull TaskViewLayoutBinding binding) {
            super(binding.getRoot());
            this.taskViewLayoutBinding = binding;
        }
    }
}
