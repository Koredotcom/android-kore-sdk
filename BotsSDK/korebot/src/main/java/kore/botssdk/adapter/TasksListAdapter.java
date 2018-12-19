package kore.botssdk.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.databinding.TaskViewLayoutBinding;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.utils.KaFontUtils;

public class TasksListAdapter extends BaseAdapter {
    private final Drawable selectedCheck;
    private final Drawable unSelectedCheck;
    private final int selectedColor;
    private final int unSelectedColor;
    private Context context;

    public ArrayList<String> getSelectedTasks() {
        return selectedTasks;
    }

    public void setSelectedTasks(ArrayList<String> selectedTasks) {
        this.selectedTasks = selectedTasks;
    }

    private ArrayList<String> selectedTasks = new ArrayList<>();
    public void addOrRemoveSelectedTask(String taskId){
        if(selectedTasks.contains(taskId)){
            selectedTasks.remove(taskId);
        }else{
            selectedTasks.add(taskId);
        }
    }
    public void addSelectedTasks(ArrayList<String> tasks){
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
    public TasksListAdapter(Context context, ArrayList<TaskTemplateModel> models, boolean showButtons){
        this.context = context;
        this.models = models;
        this.showButton = showButtons;
        selectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_on);
        unSelectedCheck = context.getResources().getDrawable(R.mipmap.checkbox_off);
        selectedColor = context.getResources().getColor(R.color.color_dfdfeb);
        unSelectedColor = context.getResources().getColor(R.color.color_efeffc);
    }
    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public TaskTemplateModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewLayoutBinding taskViewLayoutBinding;
        TaskTemplateModel taskTemplateModel = models.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.task_view_layout, null);
            taskViewLayoutBinding = DataBindingUtil.bind(convertView);
            convertView.setTag(taskViewLayoutBinding);
            KaFontUtils.applyCustomFont(context,taskViewLayoutBinding.getRoot());
        }else {
            taskViewLayoutBinding = (TaskViewLayoutBinding) convertView.getTag();
        }
        boolean isSelected = selectedTasks.contains(taskTemplateModel.getId()) && isShowButton();
        taskViewLayoutBinding.checkbox.setImageDrawable(isSelected ? selectedCheck : unSelectedCheck);
        taskViewLayoutBinding.setTask(taskTemplateModel);
        taskViewLayoutBinding.setCreateMode(!showButton);
      //  ((GradientDrawable)taskViewLayoutBinding.rootLayout.getBackground()).setColor(isSelected ? selectedColor : unSelectedColor);
        return taskViewLayoutBinding.getRoot();
    }
}
