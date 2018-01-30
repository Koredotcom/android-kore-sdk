package kore.botssdk.models;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Ramachandra Pradeep on 12/15/2016.
 */
public class PayloadInner {

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    private String template_type;
    private String text;
    private ArrayList<BotButtonModel> buttons;
    private ArrayList<QuickReplyTemplate> quick_replies;

    public void setCarouselElements(ArrayList<BotCarouselModel> carouselElements) {
        this.carouselElements = carouselElements;
    }

    private ArrayList<BotCarouselModel> carouselElements;
    private ArrayList<BotListModel> listElements;
    private ArrayList<BotLineChartDataModel> lineChartDataModels;
    private ArrayList<String> headers;

    public ArrayList<BotPieChartElementModel> getPieChartElements() {
        return pieChartElements;
    }

    public void setPieChartElements(ArrayList<BotPieChartElementModel> pieChartElements) {
        this.pieChartElements = pieChartElements;
    }

    public BotTableDataModel getData() {
        return data;
    }

    public void setData(BotTableDataModel data) {
        this.data = data;
    }

    private ArrayList<BotPieChartElementModel> pieChartElements;


    private BotTableDataModel data;

    public Object getElements() {
        return elements;
    }

    public void setElements(Object elements) {
        this.elements = elements;
    }

    private Object elements = null;
    private String elementsAsString;
    private String color = "#000000";

    private final String INVALID_JSON = "Invalid JSON";
    private String speech_hint;

    public String getTemplate_type() {
        return template_type;
    }

    public String getText() {
        return text;
    }

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public ArrayList<QuickReplyTemplate> getQuick_replies() {
        return quick_replies;
    }

    public ArrayList<BotCarouselModel> getCarouselElements() {
        return carouselElements;
    }

    public ArrayList<BotListModel> getListElements() {
        return listElements;
    }

    public String getColor() {
        return color;
    }

    public String getSpeech_hint() {
        return speech_hint;
    }


    public void convertElementToAppropriate() {
        Gson gson = new Gson();
        if (elements != null) {
            elementsAsString = gson.toJson(elements);
            if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(template_type)) {
                Type carouselType = new TypeToken<ArrayList<BotCarouselModel>>() {
                }.getType();
                carouselElements = gson.fromJson(elementsAsString, carouselType);
            } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(template_type)) {
                Type listType = new TypeToken<ArrayList<BotListModel>>() {
                }.getType();
                listElements = gson.fromJson(elementsAsString, listType);
            } else if (BotResponse.TEMPLATE_TYPE_PIECHART.equalsIgnoreCase(template_type)) {
                Type listType = new TypeToken<ArrayList<BotPieChartElementModel>>() {
                }.getType();
                pieChartElements = gson.fromJson(elementsAsString, listType);
            } else if(BotResponse.TEMPLATE_TYPE_LINECHART.equalsIgnoreCase(template_type)){
                Type listType = new TypeToken<ArrayList<BotLineChartDataModel>>() {
                }.getType();
                lineChartDataModels = gson.fromJson(elementsAsString, listType);

            }
        }
        templateValidator();
    }

    private void templateValidator() throws JsonSyntaxException {
        if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(template_type) || BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(template_type)) {
            if (elements != null) {
                if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(template_type)) {
                    for (int i = 0; i < carouselElements.size(); i++) {
                        BotCarouselModel carouselElement = carouselElements.get(i);
                        if (carouselElement.getTitle() == null || carouselElement.getImage_url() == null) {
                            carouselElements.remove(carouselElement);
                            i--;
                        }
                    }
                    if (carouselElements.isEmpty()) {
                        throw new JsonSyntaxException(INVALID_JSON);
                    }
                } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(template_type)) {
                    for (int i = 0; i < listElements.size(); i++) {
                        BotListModel botListElement = listElements.get(i);
                        if (botListElement.getTitle() == null) {
                            listElements.remove(botListElement);
                            i--;
                        }
                    }
                    if (listElements.isEmpty()) {
                        throw new JsonSyntaxException(INVALID_JSON);
                    }
                }
            } else {
                throw new JsonSyntaxException(INVALID_JSON);
            }
        }
    }

    public ArrayList<ButtonTemplate> convertQuickReplyToButton(ArrayList<QuickReplyTemplate> quick_replies) {
        ArrayList<ButtonTemplate> buttonTemplates = new ArrayList<>();
        if (quick_replies != null && quick_replies.size() > 0) {
            buttonTemplates = new ArrayList<>(quick_replies.size());
            for (QuickReplyTemplate template : quick_replies) {
                ButtonTemplate buttonTemplate = new ButtonTemplate();
                buttonTemplate.setTitle(template.getTitle());
                buttonTemplate.setPayload(template.getPayload());
                buttonTemplate.setType(template.getContent_type());
                buttonTemplates.add(buttonTemplate);
            }

        }
        return buttonTemplates;
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<String> headers) {
        this.headers = headers;
    }

    public ArrayList<BotLineChartDataModel> getLineChartDataModels() {
        return lineChartDataModels;
    }

    public void setLineChartDataModels(ArrayList<BotLineChartDataModel> lineChartDataModels) {
        this.lineChartDataModels = lineChartDataModels;
    }
}
