package kore.botssdk.models;

import java.util.ArrayList;

/**
 * Created by Shiva Krishna on 2/8/2018.
 */

public class KoraSearchResultsModel {
    ArrayList<EmailModel> emails;

    public ArrayList<EmailModel> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<EmailModel> emails) {
        this.emails = emails;
    }

    public ArrayList<KnowledgeDetailModel> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(ArrayList<KnowledgeDetailModel> knowledges) {
        this.knowledges = knowledges;
    }

    ArrayList<KnowledgeDetailModel> knowledges;


}

