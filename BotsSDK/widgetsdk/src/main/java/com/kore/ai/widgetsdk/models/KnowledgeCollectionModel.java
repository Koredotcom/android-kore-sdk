package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeCollectionModel {

    private String type;
    private String title;
    private Elements elements;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
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


    public static class Elements {

        @SerializedName("definitive")
        @Expose
        private ArrayList<DataElements> definitive = null;
        @SerializedName("suggestive")
        @Expose
        private ArrayList<DataElements> suggestive = null;

        public ArrayList<DataElements> getDefinitive() {
            return definitive;
        }

        public void setDefinitive(ArrayList<DataElements> definitive) {
            this.definitive = definitive;
        }

        public ArrayList<DataElements> getSuggestive() {
            return suggestive;
        }

        public void setSuggestive(ArrayList<DataElements> suggestive) {
            this.suggestive = suggestive;
        }


        public ArrayList<DataElements> getCombinedData() {
            ArrayList<DataElements> list = new ArrayList<>();
            if (getDefinitive() != null) {
                list.addAll(getDefinitive());
            }
            if (getSuggestive() != null) {
                for (DataElements elements : getSuggestive()) {
                    elements.setIsSuggestive(true);
                    list.add(elements);
                }
            }

            return list;
        }


    }


    public static class DataElements implements Serializable {

        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("answerPayload")
        @Expose
        private List<AnswerPayload> answerPayload = null;
        @SerializedName("score")
        @Expose
        private Double score;
        @SerializedName("name")
        @Expose
        private String name;

        boolean isSuggestive;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public List<AnswerPayload> getAnswerPayload() {
            if (answerPayload != null) {
                for (AnswerPayload payload : answerPayload) {
                    try {
                        payload.text = URLDecoder.decode(payload.text, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return answerPayload;
        }

        public void setAnswerPayload(List<AnswerPayload> answerPayload) {
            this.answerPayload = answerPayload;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSuggestive() {
            return isSuggestive;
        }

        public void setIsSuggestive(boolean isSuggestive) {
            this.isSuggestive = isSuggestive;
        }
    }

    public static class AnswerPayload implements Serializable {

        @SerializedName("text")
        @Expose
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


    }

/*    public class SuggestiveDefinative {

        public ArrayList<DataElements> getDataElements() {
            return dataElements;
        }

        public void setDataElements(ArrayList<DataElements> dataElements) {
            this.dataElements = dataElements;
        }

        ArrayList<DataElements> dataElements;

    }*/


}
