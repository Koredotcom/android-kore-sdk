package kore.botssdk.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.databinding.TaskViewLayoutBinding;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.models.TaskTemplateResponse;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.Utils;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> implements RecyclerViewDataAccessor {
    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;
    private Context context;
    private float maxWidth;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean isExpanded = false;

    public void addTaskTemplateModels(ArrayList<TaskTemplateModel> models){
        this.models.addAll(models);
    }
    public ArrayList<String> getSelectedTasks() {
        return selectedTasks;
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

    public void addSelectedTasks(ArrayList<String> tasks) {
        selectedTasks.addAll(tasks);
    }

    private boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    private TaskTemplateResponse taskTemplateResponse;
    private boolean showButton;
    private ArrayList<TaskTemplateModel> models;

    public TasksListAdapter(Context context, TaskTemplateResponse taskTemplateResponse, boolean showButtons) {
        this.context = context;
        this.taskTemplateResponse = taskTemplateResponse;
        this.showButton = showButtons;
        this.models  = taskTemplateResponse.getTaskData();
        selectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_on);
        unSelectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_off);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.task_view_layout, parent, false));
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TaskTemplateModel taskTemplateModel = models.get(position);
        holder.taskViewLayoutBinding.setTask(taskTemplateModel);
        boolean isSelected = selectedTasks.contains(taskTemplateModel.getId()) && isShowButton();
        boolean isClosed = "close".equalsIgnoreCase(taskTemplateModel.getStatus());
        holder.taskViewLayoutBinding.getRoot().setSelected(isSelected);
        holder.taskViewLayoutBinding.getRoot().setEnabled(!isClosed && isShowButton());
        holder.taskViewLayoutBinding.titleView.setPaintFlags(isClosed ? Paint.STRIKE_THRU_TEXT_FLAG : holder.taskViewLayoutBinding.titleView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.taskViewLayoutBinding.checkbox.setImageDrawable(isSelected ? selectedCheck : unSelectedCheck);
        holder.taskViewLayoutBinding.checkbox.setVisibility(isShowButton() && selectedTasks.size() > 0 ?  View.VISIBLE  : View.GONE);
        holder.taskViewLayoutBinding.checkbox.setEnabled(!isClosed);
        holder.taskViewLayoutBinding.checkbox.setAlpha(isClosed ? 0.4f : 1.0f);
        holder.taskViewLayoutBinding.titleView.setTypeface(null, isClosed ? Typeface.NORMAL : Typeface.BOLD);

        holder.taskViewLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showButton && !"close".equalsIgnoreCase(taskTemplateModel.getStatus()) && selectedTasks.size() > 0) {
                   updateThings(taskTemplateModel);
                }
            }
        });
        holder.taskViewLayoutBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                updateThings(taskTemplateModel);
                return true;
            }
        });
    }
    private void updateThings(TaskTemplateModel taskTemplateModel){
        if(showButton && !"close".equalsIgnoreCase(taskTemplateModel.getStatus())) {
            addOrRemoveSelectedTask(taskTemplateModel.getId());
            SelectionUtils.setSelectedTasks(selectedTasks);
            verticalListViewActionHelper.tasksSelectedOrDeselected(selectedTasks.size() > 0);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return models != null ? (!isExpanded && models.size() > 3 ? 3 : models.size()) : 0;
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

    public void setTaskTemplateResponse(TaskTemplateResponse taskTemplateResponse) {
        this.taskTemplateResponse = taskTemplateResponse;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TaskViewLayoutBinding taskViewLayoutBinding;

        public ViewHolder(@NonNull TaskViewLayoutBinding binding) {
            super(binding.getRoot());
            this.taskViewLayoutBinding = binding;
        }
    }
}
