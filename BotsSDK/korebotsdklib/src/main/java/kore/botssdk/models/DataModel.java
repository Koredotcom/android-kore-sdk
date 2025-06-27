package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataModel {
    @SerializedName("snippet_title")
    private String snippetTitle;
    @SerializedName("snippet_content")
    private ArrayList<SnippetContentModel> snippetContents;
    @SerializedName("snippet_type")
    private String snippetType;
    @SerializedName("snippet_model_name")
    private String snippetModelName;
    private String timeTaken;
    private String message;
    private boolean isPresentedAnswer;
    private String score;

    public String getSnippetTitle() {
        return snippetTitle;
    }

    public ArrayList<SnippetContentModel> getSnippetContents() {
        return snippetContents;
    }

    public String getSnippetType() {
        return snippetType;
    }

    public String getSnippetModelName() {
        return snippetModelName;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPresentedAnswer() {
        return isPresentedAnswer;
    }

    public String getScore() {
        return score;
    }
}
