package kore.botssdk.models;

import java.util.ArrayList;

public class SearchGraphAnswerModel
{
    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public class Payload
    {
        private CenterPanel center_panel;

        public void setCenter_panel(CenterPanel center_panel) {
            this.center_panel = center_panel;
        }

        public CenterPanel getCenter_panel() {
            return center_panel;
        }
    }

    public class CenterPanel
    {
        private ArrayList<Data> data;
        private String type;

        public void setType(String type) {
            this.type = type;
        }

        public void setData(ArrayList<Data> data) {
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public ArrayList<Data> getData() {
            return data;
        }
    }

    public class Data
    {
        private String snippet_title;
        private String snippet_content;
        private String url;
        private String score;
        private String source;
        private String snippet_type;
        private String timeTaken;
        private String message;
        private boolean isPresentedAnswer;

        public String getUrl() {
            return url;
        }

        public String getMessage() {
            return message;
        }

        public String getScore() {
            return score;
        }

        public String getSnippet_content() {
            return snippet_content;
        }

        public String getSnippet_title() {
            return snippet_title;
        }

        public String getSnippet_type() {
            return snippet_type;
        }

        public String getSource() {
            return source;
        }

        public String getTimeTaken() {
            return timeTaken;
        }

        public boolean isPresentedAnswer() {
            return isPresentedAnswer;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setPresentedAnswer(boolean presentedAnswer) {
            isPresentedAnswer = presentedAnswer;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public void setSnippet_content(String snippet_content) {
            this.snippet_content = snippet_content;
        }

        public void setSnippet_title(String snippet_title) {
            this.snippet_title = snippet_title;
        }

        public void setSnippet_type(String snippet_type) {
            this.snippet_type = snippet_type;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setTimeTaken(String timeTaken) {
            this.timeTaken = timeTaken;
        }
    }
}
