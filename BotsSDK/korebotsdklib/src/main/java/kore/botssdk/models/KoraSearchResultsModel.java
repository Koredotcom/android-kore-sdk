package kore.botssdk.models;

import java.util.ArrayList;

public class KoraSearchResultsModel {
    ArrayList<EmailModel> emails;

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

    ArrayList<KnowledgeDetailModel> knowledge;


}

