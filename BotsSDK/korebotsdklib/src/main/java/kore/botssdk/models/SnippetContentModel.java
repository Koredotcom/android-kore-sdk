package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SnippetContentModel {
    @SerializedName("answer_fragment")
    private String answerFragment;
    private ArrayList<SourceModel> sources;

    public String getAnswerFragment() {
        return answerFragment;
    }

    public ArrayList<SourceModel> getSources() {
        return sources;
    }
}
