package kore.botssdk.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import kore.botssdk.R;
import kore.botssdk.databinding.TaskViewLayoutBinding;
import kore.botssdk.models.TaskTemplateModel;
import kore.botssdk.utils.KaFontUtils;

public class TasksListAdapter extends BaseAdapter {
    private Context context;

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
    }
    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewLayoutBinding taskViewLayoutBinding;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.task_view_layout, null);
            taskViewLayoutBinding = DataBindingUtil.bind(convertView);
            convertView.setTag(taskViewLayoutBinding);
            KaFontUtils.applyCustomFont(context,convertView);
        }else {
            taskViewLayoutBinding = (TaskViewLayoutBinding) convertView.getTag();
        }
        taskViewLayoutBinding.setTask(models.get(position));
        taskViewLayoutBinding.setCreateMode(showButton);
        return taskViewLayoutBinding.getRoot();
    }
}
