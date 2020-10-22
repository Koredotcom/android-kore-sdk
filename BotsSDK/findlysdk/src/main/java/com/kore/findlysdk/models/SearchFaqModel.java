package com.kore.findlysdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchFaqModel implements Serializable
{
    private String contentId;
    private String contentType;
    private String score;
    private ArrayList<String> keywords;
    private String question;
    private String answer;
    private LiveSearchConfigModel config;
    private LiveSearchFeedbackModel feedback;
    private int fuzzyScore;

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

    public String getQuestion() {
        return question;
    }

    public String getScore() {
        return score;
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

    public void setFuzzyScore(int fuzzyScore) {
        this.fuzzyScore = fuzzyScore;
    }

    public int getFuzzyScore() {
        return fuzzyScore;
    }
}
