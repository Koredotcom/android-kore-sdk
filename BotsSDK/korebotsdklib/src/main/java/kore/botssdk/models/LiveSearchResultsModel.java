package kore.botssdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveSearchResultsModel implements Serializable
{
    private String contentId;
    private String score;
    private ArrayList<String> keywords;
    private String faq_question;
    private String question;
    private String answer;
    private String faq_answer;
    private LiveSearchConfigModel config;
    private LiveSearchFeedbackModel feedback;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String searchResultPreview;
    private LiveSearchPostBackPayloadModel postBackPayload;
    private String childBotId;
    private String childBotName;
    private String taskId;
    private String name;
    private String text;
    private String titleText;
    private String payload;
    private String externalFileUrl;
    private String page_title;
    private String pageSearchResultPreview;
    private String page_preview;
    private String page_body;
    private String page_image_url;
    private String page_url;
    private String sys_content_type;
    private String file_title;
    private String file_url;
    private String file_content;
    private String file_image_url;
    private String file_content_type;
    private String sys_source_name;
    private String file_preview;
    private String contentType;
    private String product;
    private String category;
    private String default_pipeline;
    private String extractionSourceId;
    private String doc_id;
    private ArrayList<String> page_sections;
    private String addedResult;
    private ArrayList<String> sys_racl;
    private ArrayList<String> faq_alt_questions;
    private ArrayList<String> faq_cond_answers;
    private String fuzzyScore;
    private boolean isExpanded;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setChildBotId(String childBotId) {
        this.childBotId = childBotId;
    }

    public void setConfig(LiveSearchConfigModel config) {
        this.config = config;
    }

    public void setChildBotName(String childBotName) {
        this.childBotName = childBotName;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setExternalFileUrl(String externalFileUrl) {
        this.externalFileUrl = externalFileUrl;
    }

    public void setFaq_answer(String faq_answer) {
        this.faq_answer = faq_answer;
    }

    public void setFaq_question(String faq_question) {
        this.faq_question = faq_question;
    }

    public void setFeedback(LiveSearchFeedbackModel feedback) {
        this.feedback = feedback;
    }

    public void setFile_content(String file_content) {
        this.file_content = file_content;
    }

    public void setFile_title(String file_title) {
        this.file_title = file_title;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public void setFile_content_type(String file_content_type) {
        this.file_content_type = file_content_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFile_preview(String file_preview) {
        this.file_preview = file_preview;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public void setPage_body(String page_body) {
        this.page_body = page_body;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFile_image_url(String fileImageUrl) {
        this.file_image_url = fileImageUrl;
    }

    public void setPage_image_url(String page_image_url) {
        this.page_image_url = page_image_url;
    }

    public void setPage_preview(String page_preview) {
        this.page_preview = page_preview;
    }

    public void setPage_url(String page_url) {
        this.page_url = page_url;
    }

    public void setExtractionSourceId(String extractionSourceId) {
        this.extractionSourceId = extractionSourceId;
    }

    public void setPageSearchResultPreview(String pageSearchResultPreview) {
        this.pageSearchResultPreview = pageSearchResultPreview;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setPostBackPayload(LiveSearchPostBackPayloadModel postBackPayload) {
        this.postBackPayload = postBackPayload;
    }

    public void setDefault_pipeline(String default_pipeline) {
        this.default_pipeline = default_pipeline;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSearchResultPreview(String searchResultPreview) {
        this.searchResultPreview = searchResultPreview;
    }

    public void setSys_content_type(String sys_content_type) {
        this.sys_content_type = sys_content_type;
    }

    public void setSys_source_name(String sys_source_name) {
        this.sys_source_name = sys_source_name;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setAddedResult(String addedResult) {
        this.addedResult = addedResult;
    }

    public void setFaq_alt_questions(ArrayList<String> faq_alt_questions) {
        this.faq_alt_questions = faq_alt_questions;
    }

    public void setFaq_cond_answers(ArrayList<String> faq_cond_answers) {
        this.faq_cond_answers = faq_cond_answers;
    }

    public void setFuzzyScore(String fuzzyScore) {
        this.fuzzyScore = fuzzyScore;
    }

    public void setPage_sections(ArrayList<String> page_sections) {
        this.page_sections = page_sections;
    }

    public void setSys_racl(ArrayList<String> sys_racl) {
        this.sys_racl = sys_racl;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean getExpanded() {
        return isExpanded;
    }

    public String getQuestion() {
        return question;
    }

    public String getUrl() {
        return url;
    }

    public String getProduct() {
        return product;
    }

    public String getExtractionSourceId() {
        return extractionSourceId;
    }

    public String getDefault_pipeline() {
        return default_pipeline;
    }

    public String getContentType() {
        return contentType;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public String getCategory() {
        return category;
    }

    public LiveSearchConfigModel getConfig() {
        return config;
    }

    public LiveSearchFeedbackModel getFeedback() {
        return feedback;
    }

    public LiveSearchPostBackPayloadModel getPostBackPayload() {
        return postBackPayload;
    }

    public String getAnswer() {
        return answer;
    }

    public String getChildBotId() {
        return childBotId;
    }

    public String getChildBotName() {
        return childBotName;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public String getContentId() {
        return contentId;
    }

    public String getExternalFileUrl() {
        return externalFileUrl;
    }

    public String getFaq_answer() {
        return faq_answer;
    }

    public String getFaq_question() {
        return faq_question;
    }

    public String getFile_content() {
        return file_content;
    }

    public String getFile_content_type() {
        return file_content_type;
    }

    public String getFile_preview() {
        return file_preview;
    }

    public String getFile_title() {
        return file_title;
    }

    public String getFile_url() {
        return file_url;
    }

    public String getFile_image_url() {
        return file_image_url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPage_body() {
        return page_body;
    }

    public String getPage_image_url() {
        return page_image_url;
    }

    public String getPage_preview() {
        return page_preview;
    }

    public String getPage_title() {
        return page_title;
    }

    public String getPage_url() {
        return page_url;
    }

    public String getPageSearchResultPreview() {
        return pageSearchResultPreview;
    }

    public String getPayload() {
        return payload;
    }

    public ArrayList<String> getPage_sections() {
        return page_sections;
    }

    public String getAddedResult() {
        return addedResult;
    }

    public String getScore() {
        return score;
    }

    public String getSearchResultPreview() {
        return searchResultPreview;
    }

    public String getSys_content_type() {
        return sys_content_type;
    }

    public String getSys_source_name() {
        return sys_source_name;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleText() {
        return titleText;
    }

//    public ArrayList<String> getFaq_alt_questions() {
//        return faq_alt_questions;
//    }
//
//    public ArrayList<String> getFaq_cond_answers() {
//        return faq_cond_answers;
//    }

//    public ArrayList<String> getSys_racl() {
//        return sys_racl;
//    }

    public String getFuzzyScore() {
        return fuzzyScore;
    }

}
