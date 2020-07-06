package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;


public class KoraUniversalSearchModel {

    private String type;
    private String title;
    private int count;
    private String id;
    private ArrayList<KnowledgeDetailModel> knowledge;
    private ArrayList<EmailModel> emails;
    private ArrayList<KaFileLookupModel> files;
    private ArrayList<CalEventsTemplateModel> meetingNotes;
    private KnowledgeCollectionModel.Elements knowledgeCollection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ArrayList<UniversalSearchSkillModel> getSkillModel() {
        return skillModel;
    }

    public void setSkillModel(ArrayList<UniversalSearchSkillModel> skillModel) {
        this.skillModel = skillModel;
    }

    private ArrayList<UniversalSearchSkillModel> skillModel;

    public ArrayList<CalEventsTemplateModel> getMeetingNotes() {
        return meetingNotes;
    }

    public void setMeetingNotes(ArrayList<CalEventsTemplateModel> meetingNotes) {
        this.meetingNotes = meetingNotes;
    }

    public KnowledgeCollectionModel.Elements getKnowledgeCollection() {
        return knowledgeCollection;
    }

    public void setKnowledgeCollection(KnowledgeCollectionModel.Elements knowledgeCollection) {
        this.knowledgeCollection = knowledgeCollection;
    }

    public Object getElements() {
        return elements;
    }

    public String getExpiryMsg() {
        return expiryMsg;
    }


    public PayloadInner.AskExpert askExpert;

    public PayloadInner.AskExpert getAskExpert() {
        return askExpert;
    }

    public void setAskExpert(PayloadInner.AskExpert askExpert) {
        this.askExpert = askExpert;
    }

    public void setExpiryMsg(String expiryMsg) {
        this.expiryMsg = expiryMsg;
    }

    public boolean isAuthRequired() {
        return isAuthRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        isAuthRequired = authRequired;
    }

    private String expiryMsg;
    private boolean isAuthRequired;


    public void setElements(Object elements) {
        this.elements = elements;
    }

    private Object elements = null;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<EmailModel> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<EmailModel> emails) {
        this.emails = emails;
    }

    public ArrayList<KnowledgeDetailModel> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(ArrayList<KnowledgeDetailModel> knowledge) {
        this.knowledge = knowledge;
    }

    public ArrayList<KaFileLookupModel> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<KaFileLookupModel> files) {
        this.files = files;
    }


}

