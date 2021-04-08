package kore.botssdk.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BrandingDependenciesModel;
import kore.botssdk.models.BrandingEntitiesModel;
import kore.botssdk.models.BrandingSubEntitiesModel;
import kore.botssdk.models.BrandingWidgetThemeModel;
import kore.botssdk.net.SDKConfiguration;

public class BrandingChangeActivity extends BotAppCompactActivity
{
    private Gson gson = new Gson();
    private ListView lvThemeProperties;
    private Spinner spWidgetThemes;
    private BrandingWidgetThemeModel brandingWidgetThemeModel;
    private BrandingEntitiesModel brandingEntitiesModel;
    private String selected_option_id;
    private EntitiesAdapter entitiesAdapter;
    private TextView tvThemeSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_background);

        lvThemeProperties = (ListView)findViewById(R.id.lvThemeProperties);
        spWidgetThemes = (Spinner)findViewById(R.id.spWidgetThemes);
        tvThemeSave = (TextView)findViewById(R.id.tvThemeSave);
        brandingWidgetThemeModel = getDataFromTxt();

        if(brandingWidgetThemeModel != null)
        {
            for (int i = 0; i < brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().size(); i++)
            {
                if(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getType().equalsIgnoreCase("Single-Select"))
                {
                    brandingEntitiesModel = brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i);
                    brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().remove(i);
                    selected_option_id = brandingEntitiesModel.getDef_option_id();
                }
            }

                lvThemeProperties.setAdapter(entitiesAdapter = new EntitiesAdapter(BrandingChangeActivity.this, brandingWidgetThemeModel));
        }

        spWidgetThemes.setAdapter(new SpinnerAdapter(BrandingChangeActivity.this, brandingEntitiesModel.getOptions()));

        spWidgetThemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_option_id = brandingEntitiesModel.getOptions().get(position).getId();
                if(entitiesAdapter != null)
                {
                    entitiesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvThemeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE).edit();

                for(int i=0;i < brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().size();i++)
                {
                    BrandingEntitiesModel brandingEntitiesModel = brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i);

                    for (int j = 0; j < brandingEntitiesModel.getDependencies().size(); j++)
                    {
                        BrandingDependenciesModel brandingDependenciesModel = brandingEntitiesModel.getDependencies().get(j);

                        for (int k = 0; k < brandingDependenciesModel.getEntities().size(); k++)
                        {
                            if(selected_option_id.equalsIgnoreCase(brandingDependenciesModel.getEntities().get(k).getPropertyValue()))
                            {
                                Log.e("Color Value", brandingDependenciesModel.getAllowedValues()[0]);

                                if(BotResponse.BUBBLE_LEFT_BG_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleSelected(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUBBLE_LEFT_TEXT_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUBBLE_RIGHT_BG_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUBBLE_RIGHT_BG_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUBBLE_RIGHT_TEXT_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUTTON_ACTIVE_BG_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUTTON_ACTIVE_BG_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUTTON_ACTIVE_TXT_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUTTON_INACTIVE_BG_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUTTON_INACTIVE_BG_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.BUTTON_INACTIVE_TXT_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.WIDGET_BG_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.WIDGET_BG_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }
                                else if(BotResponse.WIDGET_TXT_COLOR.trim().equalsIgnoreCase(brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(i).getLabel().trim()))
                                {
//                                    SDKConfiguration.BubbleColors.setRightBubbleTextColor(brandingDependenciesModel.getAllowedValues()[0]);
                                    editor.putString(BotResponse.WIDGET_TXT_COLOR, brandingDependenciesModel.getAllowedValues()[0]);
                                }

                                editor.apply();
                            }
                        }
                    }
                }

                finish();
            }
        });

    }

    private class EntitiesAdapter extends BaseAdapter
    {
        private LayoutInflater ownLayoutInflater = null;
        private BrandingWidgetThemeModel brandingWidgetThemeModel;

        public EntitiesAdapter(Context context, BrandingWidgetThemeModel brandingWidgetThemeModel) {
            ownLayoutInflater = LayoutInflater.from(context);
            this.brandingWidgetThemeModel = brandingWidgetThemeModel;
        }

        @Override
        public int getCount()
        {
            if(brandingWidgetThemeModel.getBrandingwidgetdesktop() != null && brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities() != null &&
                    brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().size() > 0)
            return brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().size();
            else
                return 0;
        }

        @Override
        public BrandingEntitiesModel getItem(int position)
        {
            if(brandingWidgetThemeModel.getBrandingwidgetdesktop() != null && brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities() != null &&
                    brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().size() > 0)
                return brandingWidgetThemeModel.getBrandingwidgetdesktop().getEntities().get(position);
            else
                return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ownLayoutInflater.inflate(R.layout.branding_cell, null);
            }

            if (convertView.getTag() == null) {
                initializeViewHolder(convertView);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            populateView(holder, position);

            return convertView;
        }

        private void populateView(ViewHolder holder, int position) {
            BrandingEntitiesModel buttonTemplate = getItem(position);
            holder.tvBrandingName.setText(buttonTemplate.getLabel());

            if(buttonTemplate.getDependencies() != null)
            {
                for (int i = 0; i < buttonTemplate.getDependencies().size(); i++)
                {
                    for (int k = 0; k < buttonTemplate.getDependencies().get(i).getEntities().size(); k++)
                    {
                        if(selected_option_id.equalsIgnoreCase(buttonTemplate.getDependencies().get(i).getEntities().get(k).getPropertyValue()))
                        {
                            holder.edtBrandingValue.setText(buttonTemplate.getDependencies().get(i).getAllowedValues()[0]);
                            GradientDrawable bgShape = (GradientDrawable)holder.ivThemeColor.getBackground();
                            bgShape.setColor(Color.parseColor(buttonTemplate.getDependencies().get(i).getAllowedValues()[0]));
                            break;
                        }
                    }

                }
            }
        }

        public class ViewHolder {
            TextView tvBrandingName;
            EditText edtBrandingValue;
            ImageView ivThemeColor;
        }

        private void initializeViewHolder(View view) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvBrandingName = (TextView) view.findViewById(R.id.tvBrandingName);
            viewHolder.edtBrandingValue = (EditText) view.findViewById(R.id.edtBrandingValue);
            viewHolder.ivThemeColor = (ImageView) view.findViewById(R.id.ivThemeColor);
            view.setTag(viewHolder);
        }
    }

    private class SpinnerAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<BrandingSubEntitiesModel> arrBrandingSubEntitiesModels;

        public SpinnerAdapter(Context context, ArrayList<BrandingSubEntitiesModel> arrBrandingSubEntitiesModels)
        {
            this.context = context;
            this.arrBrandingSubEntitiesModels = arrBrandingSubEntitiesModels;
        }

        @Override
        public int getCount() {
            return arrBrandingSubEntitiesModels.size();
        }

        @Override
        public Object getItem(int position) {
            return arrBrandingSubEntitiesModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = (TextView)LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, null);
            TextView text1 = (TextView)convertView.findViewById(android.R.id.text1);
            text1.setText(arrBrandingSubEntitiesModels.get(position).getValue());
            return convertView;
        }
    }

    public BrandingWidgetThemeModel getDataFromTxt()
    {
        BrandingWidgetThemeModel botOptionsModel = null;

        try
        {
            InputStream is = getResources().openRawResource(R.raw.branding_response);
            Reader reader = new InputStreamReader(is);
            botOptionsModel = gson.fromJson(reader, BrandingWidgetThemeModel.class);
            Log.e("Options Size", botOptionsModel.getBrandingwidgetdesktop().getEntities().size() + "" );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return botOptionsModel;
    }
}
