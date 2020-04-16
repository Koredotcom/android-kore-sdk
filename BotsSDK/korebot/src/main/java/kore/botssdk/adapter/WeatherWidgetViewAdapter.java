/*
package kore.botssdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.models.WeatherWidgetModel;
import kore.botssdk.utils.StringUtils;

public class WeatherWidgetViewAdapter extends RecyclerView.Adapter<WeatherWidgetViewAdapter.WeatherViewHolder> {

    Context context;
    WeatherWidgetModel itemsList ;

    public WeatherWidgetViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_adapter_layout, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {

        holder.tv_item.setText(itemsList.getElements().get(position).getTitle());
        holder.tv_icon.setTypeface(ResourcesCompat.getFont(context, R.font.icomoon));
        switch (itemsList.getElements().get(position).getIconId()) {
            case "meeting":
                holder.tv_icon.setText(context.getResources().getString(R.string.icon_2d));
                break;
            case "upcoming_tasks":
            case "overdue":
                holder.tv_icon.setText(context.getResources().getString(R.string.icon_e96c));
                break;
            case "email":
                holder.tv_icon.setText(context.getResources().getString(R.string.icon_e915));
                break;
            case "knowledge":
                holder.tv_icon.setText(context.getResources().getString(R.string.icon_e959));
                break;
            case "announcement":
                holder.tv_icon.setText(context.getResources().getString(R.string.icon_33));
                break;
            default:
                holder.tv_icon.setText(context.getResources().getString(R.string.icon_e94f));
                break;
        }

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!StringUtils.isNullOrEmpty(itemsList.getElements().get(position).getType()) && itemsList.getElements().get(position).getType().equals("postback") && !StringUtils.isNullOrEmpty(itemsList.getElements().get(position).getPayload())){
                    EntityEditEvent event = new EntityEditEvent();
                    event.setMessage(itemsList.getElements().get(position).getPayload());
                    event.setScrollUpNeeded(true);
                    KoreEventCenter.post(event);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList!=null&&itemsList.getElements()!=null?itemsList.getElements().size():0;
    }

    public void setData(WeatherWidgetModel itemsList) {
        this.itemsList=itemsList;
        notifyDataSetChanged();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tv_icon, tv_item;
        RelativeLayout rl;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_icon=itemView.findViewById(R.id.tv_icon);
            tv_item=itemView.findViewById(R.id.tv_item);

            rl=itemView.findViewById(R.id.root_layout);

        }
    }
}
*/
