package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class LiveSearchResultsModel implements Serializable
{
    private String contentId;
    private String contentType;
    private String score;
    private ArrayList<String> keywords;
    private String question;
    private String answer;
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

    public String getContentId() {
        return contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getQuestion() {
        return question;
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

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFeedback(LiveSearchFeedbackModel feedback) {
        this.feedback = feedback;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public void setQuestion(String question) {
        this.question = question;
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
}
