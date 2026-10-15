package kore.botssdk.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kore.botssdk.utils.LogUtils;

public class PayloadInner {
    private String template_type;
    private String label;
    private String text;
    private String pie_type;
    private List<String> X_axis;
    private String direction;
    private boolean stacked;
    private List<FeedbackRatingModel> numbersArrays;
    private List<FeedbackThumbsModel> thumpsUpDownArrays;
    private String carousel_type;
    private String layout;
    private String heading;
    private String title;
    private String endDate;
    private String startDate;
    private String format;
    private String seeMoreTitle;
    private int moreCount;
    private String subtitle;
    private String image_url;
    private boolean is_end;
    private int emojiPosition = -1;
    private String view;
    private String messageTodisplay;
    private boolean sliderView;
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
    private String name;
    private boolean isSortEnabled;
    private boolean isSearchEnabled;
    private List<RadioOptionModel> radioOptions;
    private static final Gson gson = new Gson();
    private boolean hideComposeBar;
    private String placeholder;
    private List<List<String>> columns;
    private ArrayList<BotTableDataModel> tableDataModel = null;
    private ArrayList<BotButtonModel> buttons;
    private transient boolean buttonTemplateSubmitted;
    private ArrayList<QuickReplyTemplate> quick_replies;
    private ArrayList<BotFormTemplateModel> formFields;
    private ArrayList<FeedbackSmileyModel> smileyArrays;
    private ArrayList<FeedbackStarModel> starArrays;
    private ArrayList<AdvancedMultiSelectModel> advancedMultiSelectModels;
    private int limit;
    private Object cards;
    private ArrayList<AdvancedListModel> listItems;
    private ArrayList<BotBeneficiaryModel> botBeneficiaryModels;
    private ArrayList<BotMultiSelectElementModel> multiSelectModels;
    private ArrayList<BotCarouselModel> carouselElements;
    private ArrayList<BotCarouselStackModel> carouselStackElements;
    private ArrayList<BotListModel> listElements;
    private ArrayList<BotLineChartDataModel> lineChartDataModels;
    private ArrayList<BotTableListModel> tableListElements;
    private ArrayList<WidgetListElementModel> widgetlistElements;
    private ArrayList<DropDownElementsModel> dropDownElementsModels;
    private SearchGraphAnswerModel graph_answer;
    private boolean dialogCancel;
    private int listItemDisplayCount;
    private int checkedPosition = -1;
    private ArrayList<String> headers;
    private ArrayList<BotBarChartDataModel> barChartDataModels;
    private ArrayList<PdfDownloadModel> pdfDownloadModels;
    private HashMap<String, AllSearchResultsDataModel> results;
    private String table_design;
    private Object elements = null;
    private BotListViewMoreDataModel moreData;
    private String color;
    private String speech_hint;
    private ArrayList<BotPieChartElementModel> pieChartElements;
    private BotTableDataModel data;
    private ArrayList<BotMiniTableModel> miniTableDataModels;
    private boolean fullWidth;
    private boolean stackedButtons;
    private String variation;

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public List<FeedbackRatingModel> getNumbersArrays() {
        return numbersArrays;
    }

    public List<FeedbackThumbsModel> getThumpsUpDownArrays() {
        return thumpsUpDownArrays;
    }

    public List<RadioOptionModel> getRadioOptions() {
        return radioOptions;
    }

    public boolean isSortEnabled() {
        return isSortEnabled;
    }

    public boolean isSearchEnabled() {
        return isSearchEnabled;
    }

    public String getName() {
        return name;
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

    public boolean getSliderView() {
        return sliderView;
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

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
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

    public String getPlaceholder() {
        return placeholder;
    }

    public ArrayList<AdvancedMultiSelectModel> getAdvancedMultiSelectModels() {
        return advancedMultiSelectModels;
    }

    public int getLimit() {
        return limit;
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

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
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

    public ArrayList<WidgetListElementModel> getWidgetlistElements() {
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

    public String getMessageTodisplay() {
        return messageTodisplay;
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

    public String getCarousel_type() {
        return carousel_type;
    }

    public String getText() {
        return text;
    }

    public boolean isFullWidth() {
        return fullWidth;
    }

    public boolean isStackedButtons() {
        return stackedButtons;
    }

    public String getVariation() {
        return variation;
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

    public boolean isButtonTemplateSubmitted() {
        return buttonTemplateSubmitted;
    }

    public void setButtonTemplateSubmitted(boolean buttonTemplateSubmitted) {
        this.buttonTemplateSubmitted = buttonTemplateSubmitted;
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
        return color;
    }

    public String getSpeech_hint() {
        return speech_hint;
    }

    public BotListViewMoreDataModel getMoreData() {
        return moreData;
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

    public void setX_axis(List<String> x_axis) {
        X_axis = x_axis;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPie_type(String pie_type) {
        this.pie_type = pie_type;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public void setCarousel_type(String carousel_type) {
        this.carousel_type = carousel_type;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSeeMoreTitle(String seeMoreTitle) {
        this.seeMoreTitle = seeMoreTitle;
    }

    public void setMoreCount(int moreCount) {
        this.moreCount = moreCount;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setMessageTodisplay(String messageTodisplay) {
        this.messageTodisplay = messageTodisplay;
    }

    public void setSliderView(boolean sliderView) {
        this.sliderView = sliderView;
    }

    public void setHeaderOptions(Object headerOptions) {
        this.headerOptions = headerOptions;
    }

    public void setFieldButton(BotFormFieldButtonModel fieldButton) {
        this.fieldButton = fieldButton;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void setVideoCurrentPosition(double videoCurrentPosition) {
        this.videoCurrentPosition = videoCurrentPosition;
    }

    public void setExperienceContent(ArrayList<FeedbackExperienceContentModel> experienceContent) {
        this.experienceContent = experienceContent;
    }

    public void setFeedbackList(ArrayList<FeedbackListModel> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public void setText_message(String text_message) {
        this.text_message = text_message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortEnabled(boolean sortEnabled) {
        isSortEnabled = sortEnabled;
    }

    public void setSearchEnabled(boolean searchEnabled) {
        isSearchEnabled = searchEnabled;
    }

    public void setRadioOptions(List<RadioOptionModel> radioOptions) {
        this.radioOptions = radioOptions;
    }

    public boolean isHideComposeBar() {
        return hideComposeBar;
    }

    public void setHideComposeBar(boolean hideComposeBar) {
        this.hideComposeBar = hideComposeBar;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setColumns(List<List<String>> columns) {
        this.columns = columns;
    }

    public void setQuick_replies(ArrayList<QuickReplyTemplate> quick_replies) {
        this.quick_replies = quick_replies;
    }

    public ArrayList<FeedbackSmileyModel> getSmileyArrays() {
        return smileyArrays;
    }

    public void setSmileyArrays(ArrayList<FeedbackSmileyModel> smileyArrays) {
        this.smileyArrays = smileyArrays;
    }

    public void setStarArrays(ArrayList<FeedbackStarModel> starArrays) {
        this.starArrays = starArrays;
    }

    public ArrayList<FeedbackStarModel> getStarArrays() {
        return starArrays;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setCards(Object cards) {
        this.cards = cards;
    }

    public void setListItems(ArrayList<AdvancedListModel> listItems) {
        this.listItems = listItems;
    }

    public void setBotBeneficiaryModels(ArrayList<BotBeneficiaryModel> botBeneficiaryModels) {
        this.botBeneficiaryModels = botBeneficiaryModels;
    }

    public void setGraph_answer(SearchGraphAnswerModel graph_answer) {
        this.graph_answer = graph_answer;
    }

    public void setListItemDisplayCount(int listItemDisplayCount) {
        this.listItemDisplayCount = listItemDisplayCount;
    }

    public void setPdfDownloadModels(ArrayList<PdfDownloadModel> pdfDownloadModels) {
        this.pdfDownloadModels = pdfDownloadModels;
    }

    public void setMoreData(BotListViewMoreDataModel moreData) {
        this.moreData = moreData;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTable_design(String table_design) {
        this.table_design = table_design;
    }

    public void setSpeech_hint(String speech_hint) {
        this.speech_hint = speech_hint;
    }

    public ArrayList<ContactTemplateModel> getContactCardModel() {
        if (getCards() != null && getCards() instanceof ArrayList<?>) {
            String cardsAsString = gson.toJson(getCards());
            Type carouselType = new TypeToken<ArrayList<ContactTemplateModel>>() {
            }.getType();
            return gson.fromJson(cardsAsString, carouselType);
        }
        return null;
    }

    public ArrayList<CardTemplateModel> getCardsModel() {
        if (getCards() != null && getCards() instanceof ArrayList<?>) {
            String cardsAsString = gson.toJson(getCards());
            Type carouselType = new TypeToken<ArrayList<CardTemplateModel>>() {
            }.getType();
            return gson.fromJson(cardsAsString, carouselType);
        }
        return null;
    }

    public void convertElementToAppropriate() {
        try {
            if (elements != null) {
                String elementsAsString = gson.toJson(elements);
                if (!BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH.equals(template_type)) {
                    if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equalsIgnoreCase(template_type) || BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equalsIgnoreCase(template_type)) {
                        if (carousel_type != null && carousel_type.equals(BotResponse.STACKED)) {
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
                    } else if (BotResponse.TEMPLATE_TYPE_MULTI_SELECT.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotMultiSelectElementModel>>() {
                        }.getType();
                        multiSelectModels = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_FORM.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotFormTemplateModel>>() {
                        }.getType();
                        formFields = gson.fromJson(elementsAsString, listType);
                    } else if (BotResponse.TEMPLATE_TYPE_FEEDBACK.equals(template_type)) {
                        switch (view) {
                            case BotResponse.VIEW_NPS:
                                Type listType1 = new TypeToken<ArrayList<FeedbackRatingModel>>() {
                                }.getType();
                                numbersArrays = gson.fromJson(elementsAsString, listType1);
                                break;
                            case BotResponse.VIEW_STAR:
                                Type listType2 = new TypeToken<ArrayList<FeedbackStarModel>>() {
                                }.getType();
                                starArrays = gson.fromJson(elementsAsString, listType2);
                                break;
                            case BotResponse.VIEW_THUMBS_UP_DOWN:
                                Type listType3 = new TypeToken<ArrayList<FeedbackThumbsModel>>() {
                                }.getType();
                                thumpsUpDownArrays = gson.fromJson(elementsAsString, listType3);
                                break;
                            case BotResponse.VIEW_CSAT:
                                Type listType4 = new TypeToken<ArrayList<FeedbackSmileyModel>>() {
                                }.getType();
                                smileyArrays = gson.fromJson(elementsAsString, listType4);
                                break;
                            default:
                                Type listType5 = new TypeToken<ArrayList<BotFormTemplateModel>>() {
                                }.getType();
                                formFields = gson.fromJson(elementsAsString, listType5);

                        }
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
                    else if (BotResponse.TEMPLATE_BUTTON_LINK.equals(template_type)) {
                        Type listType = new TypeToken<ArrayList<BotButtonModel>>() {
                        }.getType();
                        buttons = gson.fromJson(elementsAsString, listType);
                    }
                } else {
                    //Special case where we are getting multiple types of template responses in a single template(knowledge retrieval or universal search)
                    Type listType = new TypeToken<ArrayList<KoraUniversalSearchModel>>() {
                    }.getType();
                    ArrayList<KoraUniversalSearchModel> universalSearchModels = gson.fromJson(elementsAsString, listType);
                    if (universalSearchModels != null && !universalSearchModels.isEmpty()) {
                        for (int index = 0; index < universalSearchModels.size(); index++) {
                            if (universalSearchModels.get(index) != null) {
                                String elementStr = gson.toJson(universalSearchModels.get(index).getElements());
                                if (universalSearchModels.get(index).getType().equalsIgnoreCase("Email")) {
                                    Type subListType = new TypeToken<ArrayList<EmailModel>>() {
                                    }.getType();
                                    universalSearchModels.get(index).setEmails(gson.fromJson(elementStr, subListType));
                                } else if (universalSearchModels.get(index).getType().equalsIgnoreCase("Article")) {
                                    Type subListType = new TypeToken<ArrayList<KnowledgeDetailModel>>() {
                                    }.getType();
                                    universalSearchModels.get(index).setKnowledge(gson.fromJson(elementStr, subListType));
                                } else if (universalSearchModels.get(index).getType().equalsIgnoreCase("Files")) {
                                    Type subListType = new TypeToken<ArrayList<KaFileLookupModel>>() {
                                    }.getType();
                                    universalSearchModels.get(index).setFiles(gson.fromJson(elementStr, subListType));
                                } else if (universalSearchModels.get(index).getType().equalsIgnoreCase("MeetingNotes")) {
                                    Type subListType = new TypeToken<ArrayList<CalEventsTemplateModel>>() {
                                    }.getType();
                                    universalSearchModels.get(index).setMeetingNotes(gson.fromJson(elementStr, subListType));
                                } else if (universalSearchModels.get(index).getType().equalsIgnoreCase("KnowledgeCollection")) {
                                    Type subListType = new TypeToken<KnowledgeCollectionModel.Elements>() {
                                    }.getType();
                                    universalSearchModels.get(index).setKnowledgeCollection(gson.fromJson(elementStr, subListType));
                                } else if (universalSearchModels.get(index).getType().equalsIgnoreCase("Skill")) {
                                    Type subListType = new TypeToken<ArrayList<UniversalSearchSkillModel>>() {
                                    }.getType();
                                    universalSearchModels.get(index).setKnowledgeCollection(gson.fromJson(elementStr, subListType));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("Error at convertElementToAppropriate", e+"");
        }
    }
}
