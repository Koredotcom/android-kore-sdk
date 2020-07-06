package com.kore.ai.widgetsdk.models;

public class FeedbackDataModel {


    String questions;
    boolean answered;
    boolean editTextRequired;
    String answerText;

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isEditTextRequired() {
        return editTextRequired;
    }

    public void setEditTextRequired(boolean editTextRequired) {
        this.editTextRequired = editTextRequired;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    String qId;

    public FeedbackDataModel(String questions, String qId, boolean editTextRequired) {
        this.questions=questions;
        this.qId=qId;
        this.editTextRequired=editTextRequired;
    }

    public String getqId() {
        return qId;
    }

    public String getQuestions() {
        return questions;
    }
}
