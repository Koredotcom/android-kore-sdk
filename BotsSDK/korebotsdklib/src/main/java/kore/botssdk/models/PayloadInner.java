package kore.botssdk.models;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class PayloadInner {

    public PayloadInner() {
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    private String template_type;

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    private String label;
    private String text;
    private String pie_type;
    private List<String> X_axis;
    private String direction;
    private boolean stacked;
    private String layout;
    private Skill skill;
    private String heading;
    private String title;
    private String format;
    private String seeMoreTitle;
    private int moreCount = 0;
    private String subtitle;
    private String image_url;
    private boolean is_end;
    private int emojiPosition = -1;
    private String view;
    private String messageTodisplay;
    private boolean sliderView;
    private boolean showViewMore;
    private String description;
    private Object headerOptions;
    private BotFormFieldButtonModel fieldButton;
    private String url;
    private String videoUrl;
    private String audioUrl;
    private double videoCurrentPosition;
    private ArrayList<FeedbackExperienceContentModel> experienceContent;
    private ArrayList<FeedbackListModel> feedbackList;
    private String text_message;
    private String fileName;
    private int limit;

    public int getLimit() {
        return limit;
    }

    private String carousel_type;

    public String getCarousel_type() {
        return carousel_type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getText_message() {
        return text_message;
    }

    public BotFormFieldButtonModel getFieldButton() {
        return fieldButton;
    }

    public boolean isIs_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }

    private static final Gson gson = new Gson();

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public boolean isStacked() {
        return stacked;
    }

    public int getMoreCount() {
        return moreCount;
    }

    public String getSeeMoreTitle() {
        return seeMoreTitle;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getxAxis() {
        return X_axis;
    }

    public String getTableDesign() {
        return table_design;
    }

    public String getHeading() {
        return heading;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setFormat(String endDate) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public int getEmojiPosition() {
        return emojiPosition;
    }

    public void setEmojiPosition(int emojiPosition) {
        this.emojiPosition = emojiPosition;
    }

    public String getPie_type() {
        return pie_type;
    }

    private String placeholder_text;

    private List<List<String>> columns = null;
    private ArrayList<BotTableDataModel> tableDataModel = null;

    private ArrayList<BotButtonModel> buttons;
    private ArrayList<QuickReplyTemplate> quick_replies;
    private ArrayList<FormActionTemplate> form_actions;
    private ArrayList<NarratorTextModel> narratorTextModel;
    private ArrayList<BotFormTemplateModel> formFields;
    private ArrayList<FeedbackSmileyModel> smileyArrays;
    private ArrayList<FeedbackStarModel> starArrays;
    private Object cards;
    private ArrayList<AdvancedListModel> listItems;
    private ArrayList<FeedbackRatingModel> numbersArrays;

    private List<RadioOptionModel> radioOptions;

    public ArrayList<FeedbackRatingModel> getNumbersArrays() {
        return numbersArrays;
    }

    public List<RadioOptionModel> getRadioOptions() {
        return radioOptions;
    }

    private ArrayList<BotBeneficiaryModel> botBeneficiaryModels;

    public ArrayList<BotBeneficiaryModel> getBotBeneficiaryModels() {
        return botBeneficiaryModels;
    }

    private SearchGraphAnswerModel graph_answer;

    public SearchGraphAnswerModel getGraph_answer() {
        return graph_answer;
    }

    private HashMap<String, AllSearchResultsDataModel> results;

    public HashMap<String, AllSearchResultsDataModel> getResults() {
        return results;
    }

    public void setResults(HashMap<String, AllSearchResultsDataModel> results) {
        this.results = results;
    }

    private boolean dialogCancel;
    private int listItemDisplayCount;

    public int getListItemDisplayCount() {
        return listItemDisplayCount;
    }

    public ArrayList<AdvancedListModel> getListItems() {
        return listItems;
    }

    public ArrayList<BotMultiSelectElementModel> getMultiSelectModels() {
        return multiSelectModels;
    }

    public ArrayList<BotFormTemplateModel> getBotFormTemplateModels() {
        return formFields;
    }

    private ArrayList<BotMultiSelectElementModel> multiSelectModels;

    public void setNarratorTextModel(ArrayList<NarratorTextModel> narratorTextModel) {
        this.narratorTextModel = narratorTextModel;
    }

    private ArrayList<BotListWidgetModel> listWidgetModels;
    private ArrayList<BotCarouselModel> carouselElements;
    private ArrayList<BotCarouselStackModel> carouselStackElements;
    private ArrayList<ContactTemplateModel> contactTemplateModels;
    private ArrayList<CardTemplateModel> cardTemplateModels;
    private ArrayList<BotListModel> listElements;
    private ArrayList<BotLineChartDataModel> lineChartDataModels;
    private ArrayList<BotTableListModel> tableListElements;
    private ArrayList<WidgetListElementModel> widgetlistElements;
    private ArrayList<DropDownElementsModel> dropDownElementsModels;
    private ArrayList<AdvancedMultiSelectModel> advancedMultiSelectModels;

    public ArrayList<AdvancedMultiSelectModel> getAdvancedMultiSelectModels() {
        return advancedMultiSelectModels;
    }

    public ArrayList<WidgetListElementModel> getWidgetlistElements() {
        return widgetlistElements;
    }

    public ArrayList<DropDownElementsModel> getDropDownElementsModels() {
        return dropDownElementsModels;
    }

    private ArrayList<String> headers;
    private ArrayList<BotBarChartDataModel> barChartDataModels;
    private ArrayList<QuickReplyTemplate> pickerTemplateModels;
    private ArrayList<PdfDownloadModel> pdfDownloadModels;

    public void setView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public String getMessageTodisplay() {
        return messageTodisplay;
    }

    public ArrayList<BotMiniTableModel> getMiniTableDataModels() {
        return miniTableDataModels;
    }

    public Object getCards() {
        return cards;
    }

    private ArrayList<BotMiniTableModel> miniTableDataModels;

    public ArrayList<BotBarChartDataModel> getBarChartDataModels() {
        return barChartDataModels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getHeaderOptions() {
        return headerOptions;
    }

    public ArrayList<BotPieChartElementModel> getPieChartElements() {
        return pieChartElements;
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

    private String table_design;
    private Object elements = null;
    private BotListViewMoreDataModel moreData;

    private String speech_hint;

    public String getTemplate_type() {
        return template_type;
    }

    public String getText() {
        return text;
    }

    public ArrayList<PdfDownloadModel> getPdfDownloadModels() {
        return pdfDownloadModels;
    }

    public ArrayList<BotButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<BotButtonModel> buttons) {
        this.buttons = buttons;
    }

    public ArrayList<QuickReplyTemplate> getQuick_replies() {
        return quick_replies;
    }

    public ArrayList<BotCarouselModel> getCarouselElements() {
        return carouselElements;
    }

    public ArrayList<BotCarouselStackModel> getCarouselStackElements() {
        return carouselStackElements;
    }

    public ArrayList<BotListModel> getListElements() {
        return listElements;
    }

    public ArrayList<BotTableListModel> getTableListElements() {
        return tableListElements;
    }

    public String getColor() {
        return "#000000";
    }

    public String getSpeech_hint() {
        return speech_hint;
    }

    public BotListViewMoreDataModel getMoreData() {
        return moreData;
    }

    public ArrayList<ContactTemplateModel> getContactCardModel() {
        if (getCards() != null && getCards() instanceof ArrayList<?>) {
            String cardsAsString = gson.toJson(getCards());
            Type carouselType = new TypeToken<ArrayList<ContactTemplateModel>>() {
            }.getType();
            contactTemplateModels = gson.fromJson(cardsAsString, carouselType);
            return contactTemplateModels;
        }
        return null;
    }

    public ArrayList<CardTemplateModel> getCardsModel() {
        if (getCards() != null && getCards() instanceof ArrayList<?>) {
            String cardsAsString = gson.toJson(getCards());
            Type carouselType = new TypeToken<ArrayList<CardTemplateModel>>() {
            }.getType();
            cardTemplateModels = gson.fromJson(cardsAsString, carouselType);
            return cardTemplateModels;
        }
        return null;
    }

    public void convertElementToAppropriate() {

        try {
            if (elements != null) {
                //    private Object columns = null;
                String elementsAsString = gson.toJson(elements);
                if (!BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH.equals(template_type)) {
                    if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(template_type) || BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equalsIgnoreCase(template_type)) {
                        if (!StringUtils.isNullOrEmpty(carousel_type) && carousel_type.equalsIgnoreCase(BotResponse.CAROUSEL_STACKED)) {
                            Type carouselType = new TypeToken<ArrayList<BotCarouselStackModel>>() {
                            }.getType();
                            carouselStackElements = gson.fromJson(elementsAsString, carouselType);
                        } else {
                            Type carouselType = new TypeToken<ArrayList<BotCarouselModel>>() {
                            }.getType();
                            carouselElements = gson.fromJson(elementsAsString, carouselType);
                        }
                    } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL_ADV.equalsIgnoreCase(template_type)) {
                        Type carouselType = new TypeToken<ArrayList<BotCarouselModel>>() {
                        }.getType();
                        carouselElements = gson.fromJson(elementsAsString, carouselType);
                    } else if (BotResponse.TEMPLATE_TYPE_LIST.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotListModel>>() {
                        }.getType();
                        listElements = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_LIST_VIEW.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotListModel>>() {
                        }.getType();
                        listElements = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_TABLE_LIST.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotTableListModel>>() {
                        }.getType();
                        tableListElements = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_PIECHART.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotPieChartElementModel>>() {
                        }.getType();
                        pieChartElements = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_LINECHART.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotLineChartDataModel>>() {
                        }.getType();
                        lineChartDataModels = gson.fromJson(elementsAsString, listType);

                    } else if (BotResponse.TEMPLATE_TYPE_BARCHART.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotBarChartDataModel>>() {
                        }.getType();
                        barChartDataModels = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_TABLE.equalsIgnoreCase(template_type)) {
                        Type tableType = new TypeToken<ArrayList<BotTableDataModel>>() {
                        }.getType();
                        tableDataModel = gson.fromJson(elementsAsString, tableType);
                    } else if (BotResponse.TEMPLATE_TYPE_MINITABLE.equalsIgnoreCase(template_type)) {
                        Type tableType = new TypeToken<ArrayList<BotMiniTableModel>>() {
                        }.getType();
                        miniTableDataModels = gson.fromJson(elementsAsString, tableType);
                    } else if (BotResponse.TEMPLATE_TYPE_PICKER.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<QuickReplyTemplate>>() {
                        }.getType();
                        pickerTemplateModels = gson.fromJson(elementsAsString, listType);

                    } else if (BotResponse.NARRATOR_TEXT.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<NarratorTextModel>>() {
                        }.getType();
                        setNarratorTextModel(gson.fromJson(elementsAsString, listType));
                    } else if (BotResponse.TEMPLATE_TYPE_MULTI_SELECT.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotMultiSelectElementModel>>() {
                        }.getType();
                        multiSelectModels = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_FORM.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotFormTemplateModel>>() {
                        }.getType();
                        formFields = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_FEEDBACK.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<NewFeedbackModel>>() {
                        }.getType();
                        formFields = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_LIST_WIDGET_2.equals(template_type) || BotResponse.TEMPLATE_TYPE_LIST_WIDGET.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<WidgetListElementModel>>() {
                        }.getType();
                        widgetlistElements = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_DROPDOWN.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<DropDownElementsModel>>() {
                        }.getType();
                        dropDownElementsModels = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.ADVANCED_MULTI_SELECT_TEMPLATE.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<AdvancedMultiSelectModel>>() {
                        }.getType();
                        advancedMultiSelectModels = gson.fromJson(elementsAsString, listType);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // templateValidator();
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

    public ArrayList<BotTableDataModel> getTable_elements_data() {
        return tableDataModel;
    }

    public List<List<String>> getColumns() {
        return columns;
    }

    public void setDialogCancel(boolean b) {
        dialogCancel = b;
    }

    public boolean getDialogCancel() {
        return dialogCancel;
    }

    public double getVideoCurrentPosition() {
        return videoCurrentPosition;
    }

    public ArrayList<FeedbackExperienceContentModel> getExperienceContent() {
        return experienceContent;
    }

    public ArrayList<FeedbackListModel> getFeedbackList() {
        return feedbackList;
    }

    public boolean getSliderView() {
        return sliderView;
    }

    public static class Skill {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        private String name;
        private String color;
        private String icon;
        private String trigger;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        public String getTrigger() {
            return trigger;
        }

        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }
    }
}
