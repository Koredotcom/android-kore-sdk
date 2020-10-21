package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.On;
import kore.botssdk.R;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.DropDownElementsModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.KaFontUtils;

public class BotDropDownTemplateView extends LinearLayout {
    private float dp1;
    private TextView tvDropDownTitle;
    private Spinner snrSplitList;
    private LinearLayout llSpinner;
    private Context mContext;
    private List<String> categories = new ArrayList<String>();
    private int selectionCurrent;
    private PopupWindow popupWindow;
    private View popUpView;


    public BotDropDownTemplateView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BotDropDownTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public BotDropDownTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bot_dropdown_template, this, true);
        KaFontUtils.applyCustomFont(getContext(), view);
        tvDropDownTitle = view.findViewById(R.id.tvDropDownTitle);
        snrSplitList = view.findViewById(R.id.snrSplitList);
        llSpinner = view.findViewById(R.id.llSpinner);

        dp1 = (int) AppControl.getInstance().getDimensionUtil().dp1;
        selectionCurrent = snrSplitList.getSelectedItemPosition();
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popUpView = LayoutInflater.from(mContext).inflate(R.layout.theme_change_layout, null);
        popupWindow = new PopupWindow(popUpView, width, height, focusable);

    }

    public void populateData(final PayloadInner payloadInner) {

        if (payloadInner != null)
        {
            tvDropDownTitle.setText(payloadInner.getHeading());

            for(int i = 0; i < payloadInner.getDropDownElementsModels().size(); i++)
            {
                categories.add(payloadInner.getDropDownElementsModels().get(i).getTitle());
            }

            // Creating adapter for spinner
            SpinnerAdapter dataAdapter = new SpinnerAdapter(mContext, categories);

            // Drop down layout style - list view with radio button
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            snrSplitList.setAdapter(dataAdapter);
            snrSplitList.setPrompt("Select");
            snrSplitList.setSelection(selectionCurrent);
        }
    }

    public class SpinnerAdapter extends BaseAdapter
    {
        private LayoutInflater inflater = null;
        private List<String> arrDropDownElementsModels;
        private Context context;

        public SpinnerAdapter(Context context, List<String> arrDropDownElementsModels) {
            this.context = context;
            inflater = LayoutInflater.from(mContext);
            this.arrDropDownElementsModels = arrDropDownElementsModels;
        }

        @Override
        public int getCount()
        {
            return arrDropDownElementsModels.size();
        }

        @Override
        public Object getItem(int position)
        {
            return arrDropDownElementsModels.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                convertView = inflater.inflate(R.layout.bot_drop_down_item_cell, null);
                holder = new ViewHolder();
                holder.more_txt_view = (TextView) convertView.findViewById(R.id.more_txt_view);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.more_txt_view.setText(arrDropDownElementsModels.get(position));

            holder.more_txt_view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    composeFooterInterface.copyMessageToComposer(arrDropDownElementsModels.get(position), false);
                    selectionCurrent = position;
                    hideSpinnerDropDown(snrSplitList);
                    snrSplitList.setSelection(position);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public TextView more_txt_view;
        }
    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
