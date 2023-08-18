package com.kore.ai.widgetsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FeedbackDataResponse implements  Serializable {

@SerializedName("features")
@Expose
private List<Feature> features = null;
@SerializedName("ratings")
@Expose
private List<Rating> ratings = null;

public List<Feature> getFeatures() {
return features;
}

public void setFeatures(List<Feature> features) {
this.features = features;
}

public List<Rating> getRatings() {
return ratings;
}

public void setRatings(List<Rating> ratings) {
this.ratings = ratings;
}

    public static class Feedback implements  Serializable{

        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("options")
        @Expose
        private List<Option> options = null;

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Option> getOptions() {
            return options;
        }

        public void setOptions(List<Option> options) {
            this.options = options;
        }

    }

    public static class Feature implements  Serializable{

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("feedbacks")
        @Expose
        private List<Feedback> feedbacks = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Feedback> getFeedbacks() {
            return feedbacks;
        }

        public void setFeedbacks(List<Feedback> feedbacks) {
            this.feedbacks = feedbacks;
        }

    }

    public static class Option implements  Serializable{

        private boolean userAction;

        private String placeholder;

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public boolean isUserAction() {
            return userAction;
        }

        public void setUserAction(boolean userAction) {
            this.userAction = userAction;
        }


        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        private String userAnswer;


        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("action")
        @Expose
        private String action;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

    }

    public static class Rating implements  Serializable{


        @Override
        protected FeedbackDataResponse.Rating clone() throws CloneNotSupportedException {
            return (FeedbackDataResponse.Rating)super.clone();
        }
        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("title")
        @Expose
        private String title;

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}