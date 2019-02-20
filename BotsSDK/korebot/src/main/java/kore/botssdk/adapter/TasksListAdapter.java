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
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.utils.SelectionUtils;
import kore.botssdk.utils.Utils;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> implements RecyclerViewDataAccessor {
    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;
    private final int selectedColor;
    private final int unSelectedColor;
    private Context context;
    private float maxWidth;
    private int textSize;
    private VerticalListViewActionHelper verticalListViewActionHelper;
    private boolean isExpanded = false;

    public ArrayList<String> getSelectedTasks() {
        return selectedTasks;
    }

    public void setSelectedTasks(ArrayList<String> selectedTasks) {
        this.selectedTasks = selectedTasks;
    }

    private ArrayList<String> selectedTasks = new ArrayList<>();

    public void addOrRemoveSelectedTask(String taskId) {
        if (selectedTasks.contains(taskId)) {
            selectedTasks.remove(taskId);
        } else {
            selectedTasks.add(taskId);
        }
    }

    public void addSelectedTasks(ArrayList<String> tasks) {
        selectedTasks.addAll(tasks);
    }

    public ArrayList<TaskTemplateModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<TaskTemplateModel> models) {
        this.models = models;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    private ArrayList<TaskTemplateModel> models;
    private boolean showButton;

    public TasksListAdapter(Context context, ArrayList<TaskTemplateModel> models, boolean showButtons) {
        this.context = context;
        this.models = models;
        this.showButton = showButtons;
        selectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_on);
        unSelectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_off);
        selectedColor = context.getResources().getColor(R.color.color_dfdfeb);
        unSelectedColor = context.getResources().getColor(R.color.color_efeffc);
        textSize = (int) (16 * AppControl.getInstance().getDimensionUtil().dp1);
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
        /*if (Utils.getMaxLinesOfText(taskTemplateModel.getTitle(), maxWidth - 50 * dp1, textSize) > 1) {
            holder.taskViewLayoutBinding.rootLayout.setMinimumHeight((int) (130 * dp1));
        } else {
            holder.taskViewLayoutBinding.rootLayout.setMinimumHeight(0);
        }*/
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TaskViewLayoutBinding taskViewLayoutBinding;

        public ViewHolder(@NonNull TaskViewLayoutBinding binding) {
            super(binding.getRoot());
            this.taskViewLayoutBinding = binding;
        }
    }
}
