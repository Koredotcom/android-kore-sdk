package kore.botssdk.models;

import java.util.ArrayList;

public class KnowledgeCollectionModel {

    private String type;
    private String title;
    private ArrayList<KCElement> elements;

    public ArrayList<KCElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<KCElement> elements) {
        this.elements = elements;
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

    public class KCElement{
        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        private String question;
        private String answer;
    }
}
