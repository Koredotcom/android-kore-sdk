package kore.botssdk.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import kore.botssdk.R;
import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.EntityEditEvent;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.Widget;
import kore.botssdk.utils.StringUtils;

public class ListWidgetButtonAdapter extends RecyclerView.Adapter<ListWidgetButtonAdapter.ButtonViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<Widget.Button> buttons;
    private final Context mContext;
    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;
    private String skillName;
    private final String trigger;
    private BottomSheetDialog bottomSheetDialog;

    public ListWidgetButtonAdapter(Context context, ArrayList<Widget.Button> buttons, String trigger) {
        this.buttons = buttons;
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.trigger = trigger;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ButtonViewHolder(inflater.inflate(R.layout.widget_button_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int i) {

        Widget.Button btn = buttons.get(i);
        holder.tv.setText(btn.getTitle());

        holder.tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                if(!StringUtils.isNullOrEmpty(btn.getPayload())){
                    composeFooterInterface.onSendClick(btn.getPayload(), true);
                }
            }
        });
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setBottomSheet(BottomSheetDialog bottomSheetDialog) {
        this.bottomSheetDialog = bottomSheetDialog;
    }

    @Override
    public int getItemCount() {
        return buttons !=null?buttons.size():0;
        /*if(buttons != null){
            if(buttons.size()<3)
                buttons.size();
            else return 3;
        }
            return 0;
*/    }

    boolean isFullView;
    public void setIsFromFullView(boolean isFullView) {
        this.isFullView=isFullView;
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
//        private LinearLayout ll;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.buttonTV);
//            ll = itemView.findViewById(R.id.buttonsLayout);

        }
    }


    public void buttonAction(String utterance, boolean appendUtterance){
        if(utterance !=null && (utterance.startsWith("tel:") || utterance.startsWith("mailto:"))){
            if(utterance.startsWith("tel:")){
                launchDialer(mContext,utterance);
            }else if(utterance.startsWith("mailto:")){
                showEmailIntent((Activity) mContext,utterance.split(":")[1]);
            }
            return;
        }
        EntityEditEvent event = new EntityEditEvent();
        StringBuffer msg = new StringBuffer();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("refresh", Boolean.TRUE);
        if(appendUtterance && trigger!= null)
            msg = msg.append(trigger).append(" ");
        msg.append(utterance);
        event.setMessage(msg.toString());
        event.setPayLoad(new Gson().toJson(hashMap));
        event.setScrollUpNeeded(true);
        KoreEventCenter.post(event);

        try {


            if (isFullView) {
                ((Activity) mContext).finish();
            }
        } catch (Exception e) {

        }
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public static void showEmailIntent(Activity activity, String recepientEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recepientEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");

        try {
            activity.startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Error while launching email intent!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public static void launchDialer(Context context, String url) {
        try {
            Intent intent = new Intent(hasPermission(context, Manifest.permission.CALL_PHONE) ? Intent.ACTION_CALL : Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasPermission(Context context,String... permission) {
        boolean shouldShowRequestPermissionRationale = true;
        int permissionLength = permission.length;
        for (int i=0;i<permissionLength;i++) {
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale &&
                    ActivityCompat.checkSelfPermission(context, permission[i]) == PackageManager.PERMISSION_GRANTED;
        }
        return shouldShowRequestPermissionRationale;
    }


}

