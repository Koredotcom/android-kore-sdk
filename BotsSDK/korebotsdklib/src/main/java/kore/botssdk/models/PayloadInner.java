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
    static final Gson gson = new Gson();

    List<List<String>> columns = null;
    ArrayList<BotTableDataModel> tableDataModel = null;
    ArrayList<BotButtonModel> buttons;
    ArrayList<QuickReplyTemplate> quick_replies;
    ArrayList<NarratorTextModel> narratorTextModel;
    ArrayList<BotFormTemplateModel> formFields;
    ArrayList<FeedbackThumbsModel> thumpsUpDownArrays;
    ArrayList<AdvancedListModel> listItems;
    ArrayList<FeedbackRatingModel> numbersArrays;
    List<RadioOptionModel> radioOptions;
    ArrayList<BotBeneficiaryModel> botBeneficiaryModels;
    HashMap<String, AllSearchResultsDataModel> results;
    ArrayList<BotMultiSelectElementModel> multiSelectModels;
    ArrayList<BotCarouselModel> carouselElements;
    ArrayList<BotCarouselStackModel> carouselStackElements;
    ArrayList<ContactTemplateModel> contactTemplateModels;
    ArrayList<CardTemplateModel> cardTemplateModels;
    ArrayList<BotListModel> listElements;
    ArrayList<BotLineChartDataModel> lineChartDataModels;
    ArrayList<BotTableListModel> tableListElements;
    ArrayList<WidgetListElementModel> widgetlistElements;
    ArrayList<DropDownElementsModel> dropDownElementsModels;
    ArrayList<AdvancedMultiSelectModel> advancedMultiSelectModels;
    ArrayList<String> headers;
    ArrayList<BotBarChartDataModel> barChartDataModels;
    ArrayList<QuickReplyTemplate> pickerTemplateModels;
    ArrayList<PdfDownloadModel> pdfDownloadModels;
    ArrayList<BotMiniTableModel> miniTableDataModels;
    ArrayList<BotPieChartElementModel> pieChartElements;
    ArrayList<FeedbackExperienceContentModel> experienceContent;
    ArrayList<FeedbackListModel> feedbackList;
    List<String> X_axis;
    private boolean isSortEnabled;
    private boolean isSearchEnabled;
    boolean dialogCancel;
    int listItemDisplayCount;
    Object cards;
    SearchGraphAnswerModel graph_answer;
    BotTableDataModel data;
    String table_design;
    Object elements = null;
    BotListViewMoreDataModel moreData;
    String speech_hint;
    String template_type;
    String label;
    String text;
    String pie_type;
    String direction;
    boolean stacked;
    String layout;
    String heading;
    String title;
    String format;
    String seeMoreTitle;
    int moreCount = 0;
    String subtitle;
    String image_url;
    boolean is_end;
    int emojiPosition = -1;
    String view;
    boolean sliderView;
    String description;
    Object headerOptions;
    BotFormFieldButtonModel fieldButton;
    String url;
    String videoUrl;
    String audioUrl;
    double videoCurrentPosition;
    String text_message;
    String fileName;
    int limit;
    String placeholder;
    String endDate;
    String startDate;
    String carousel_type;
    String messageTodisplay;

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getLimit() {
        return limit;
    }

    public String getPlaceholder() {
        return placeholder;
    }

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

    public ArrayList<FeedbackRatingModel> getNumbersArrays() {
        return numbersArrays;
    }

    public List<RadioOptionModel> getRadioOptions() {
        return radioOptions;
    }

    public ArrayList<FeedbackThumbsModel> getThumpsUpDownArrays() {
        return thumpsUpDownArrays;
    }

    public ArrayList<BotBeneficiaryModel> getBotBeneficiaryModels() {
        return botBeneficiaryModels;
    }

    public SearchGraphAnswerModel getGraph_answer() {
        return graph_answer;
    }

    public HashMap<String, AllSearchResultsDataModel> getResults() {
        return results;
    }

    public void setResults(HashMap<String, AllSearchResultsDataModel> results) {
        this.results = results;
    }

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

    public void setNarratorTextModel(ArrayList<NarratorTextModel> narratorTextModel) {
        this.narratorTextModel = narratorTextModel;
    }

    public ArrayList<AdvancedMultiSelectModel> getAdvancedMultiSelectModels() {
        return advancedMultiSelectModels;
    }

    public ArrayList<WidgetListElementModel> getWidgetListElements() {
        return widgetlistElements;
    }

    public ArrayList<DropDownElementsModel> getDropDownElementsModels() {
        return dropDownElementsModels;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public ArrayList<BotMiniTableModel> getMiniTableDataModels() {
        return miniTableDataModels;
    }

    public Object getCards() {
        return cards;
    }

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

    public Object getElements() {
        return elements;
    }

    public void setElements(Object elements) {
        this.elements = elements;
    }

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
                    } else if (BotResponse.TEMPLATE_TYPE_PIE_CHART.equalsIgnoreCase(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotPieChartElementModel>>() {
                        }.getType();
                        pieChartElements = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_LINE_CHART.equalsIgnoreCase(template_type)) {
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
                    } else if (BotResponse.TEMPLATE_TYPE_MINI_TABLE.equalsIgnoreCase(template_type)) {
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
                        DropDownElementsModel model = new DropDownElementsModel();
                        model.setTitle(placeholder);
                        model.setValue(placeholder);
                        dropDownElementsModels.add(0, model);
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

    public boolean isSortEnabled() {
        return isSortEnabled;
    }

    public void setSortEnabled(boolean sortEnabled) {
        isSortEnabled = sortEnabled;
    }

    public boolean isSearchEnabled() {
        return isSearchEnabled;
    }

    public void setSearchEnabled(boolean searchEnabled) {
        isSearchEnabled = searchEnabled;
    }

    public void setMessageTodisplay(String messageTodisplay) {
        this.messageTodisplay = messageTodisplay;
    }

    public String getMessageTodisplay() {
        return messageTodisplay;
    }

    public static class Skill {
        String id;
        String name;
        String color;
        String icon;
        String trigger;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTrigger() {
            return trigger;
        }

        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }
    }
}
