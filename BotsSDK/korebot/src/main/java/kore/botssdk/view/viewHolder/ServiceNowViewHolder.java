package kore.botssdk.view.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;

public class ServiceNowViewHolder extends RecyclerView.ViewHolder {

        public TextView icon_view, root_title_view, count_view, title, body;
        public View header,service_now_click;

        public ServiceNowViewHolder(@NonNull View itemView) {
            super(itemView);
            service_now_click=itemView.findViewById(R.id.service_now_click);
            icon_view = itemView.findViewById(R.id.icon_view);
            root_title_view = itemView.findViewById(R.id.root_title_view);
            count_view = itemView.findViewById(R.id.count_view);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            header = itemView.findViewById(R.id.header);

        }
    }