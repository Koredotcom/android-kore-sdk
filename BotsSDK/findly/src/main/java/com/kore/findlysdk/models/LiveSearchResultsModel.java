package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveSearchResultsModel implements Serializable
{
    private String contentId;
    private String score;
    private ArrayList<String> keywords;
    private String faqQuestion;
    private String answer;
    private String faqAnswer;
    private LiveSearchConfigModel config;
    private LiveSearchFeedbackModel feedback;
    private String title;
    private String url;
    private String imageUrl;
    private String searchResultPreview;
    private String taskName;
    private LiveSearchPostBackPayloadModel postBackPayload;
    private String childBotId;
    private String childBotName;
    private String taskId;
    private String name;
    private String text;
    private String titleText;
    private String payload;
    private String externalFileUrl;
    private String pageTitle;
    private String pageSearchResultPreview;
    private String pagePreview;
    private String pageBody;
    private String pageImageUrl;
    private String pageUrl;
    private ResultsViewAppearance appearance;
    private String sysContentType;
    private String fileTitle;
    private String fileUrl;
    private String fileContent;
    private String fileImageUrl;
    private String fileContentType;
    private String sysSourceName;
    private String filePreview;
    private String contentType;
    private String product;
    private String category;
    private String default_pipeline;
    private String extractionSourceId;
    private String docId;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public LiveSearchConfigModel getConfig() {
        return config;
    }

    public LiveSearchFeedbackModel getFeedback() {
        return feedback;
    }

    public String getAnswer() {
        return answer;
    }

    public String getFaqAnswer() {
        return faqAnswer;
    }

    public void setFaqAnswer(String faqAnswer) {
        this.faqAnswer = faqAnswer;
    }

    public String getContentId() {
        return contentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFaqQuestion() {
        return faqQuestion;
    }

    public String getScore() {
        return score;
    }

    public String getSearchResultPreview() {
        return searchResultPreview;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setConfig(LiveSearchConfigModel config) {
        this.config = config;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setSysContentType(String sysContentType) {
        this.sysContentType = sysContentType;
    }

    public void setFeedback(LiveSearchFeedbackModel feedback) {
        this.feedback = feedback;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public void setFaqQuestion(String question) {
        this.faqQuestion = question;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setSearchResultPreview(String searchResultPreview) {
        this.searchResultPreview = searchResultPreview;
    }

    public LiveSearchPostBackPayloadModel getPostBackPayload() {
        return postBackPayload;
    }

    public void setPostBackPayload(LiveSearchPostBackPayloadModel postBackPayload) {
        this.postBackPayload = postBackPayload;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setChildBotId(String childBotId) {
        this.childBotId = childBotId;
    }

    public void setChildBotName(String childBotName) {
        this.childBotName = childBotName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getText() {
        return text;
    }

    public String getPayload() {
        return payload;
    }

    public String getName() {
        return name;
    }

    public String getChildBotId() {
        return childBotId;
    }

    public String getChildBotName() {
        return childBotName;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getExternalFileUrl() {
        return externalFileUrl;
    }

    public void setExternalFileUrl(String externalFileUrl) {
        this.externalFileUrl = externalFileUrl;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public void setPageImageUrl(String pageImageUrl) {
        this.pageImageUrl = pageImageUrl;
    }

    public void setPageSearchResultPreview(String pageSearchResultPreview) {
        this.pageSearchResultPreview = pageSearchResultPreview;
    }

    public String getPageImageUrl() {
        return pageImageUrl;
    }

    public String getPageSearchResultPreview() {
        return pageSearchResultPreview;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setAppearance(ResultsViewAppearance appearance) {
        this.appearance = appearance;
    }

    public ResultsViewAppearance getAppearance() {
        return appearance;
    }

    public String getSysContentType() {
        return sysContentType;
    }

    public void setPageBody(String pageBody) {
        this.pageBody = pageBody;
    }

    public void setPagePreview(String pagePreview) {
        this.pagePreview = pagePreview;
    }

    public String getPageBody() {
        return pageBody;
    }

    public String getPagePreview() {
        return pagePreview;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileImageUrl(String fileImageUrl) {
        this.fileImageUrl = fileImageUrl;
    }

    public void setFilePreview(String filePreview) {
        this.filePreview = filePreview;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setSysSourceName(String sysSourceName) {
        this.sysSourceName = sysSourceName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public String getFileImageUrl() {
        return fileImageUrl;
    }

    public String getFilePreview() {
        return filePreview;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getSysSourceName() {
        return sysSourceName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDefault_pipeline(String default_pipeline) {
        this.default_pipeline = default_pipeline;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setExtractionSourceId(String extractionSourceId) {
        this.extractionSourceId = extractionSourceId;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public String getDefault_pipeline() {
        return default_pipeline;
    }

    public String getDocId() {
        return docId;
    }

    public String getExtractionSourceId() {
        return extractionSourceId;
    }

    public String getProduct() {
        return product;
    }
}
