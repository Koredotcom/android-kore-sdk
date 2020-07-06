package com.kore.ai.widgetsdk.models;

import java.util.ArrayList;

public class FeatureUtteranceModel {
    private String title;

    public String getTitle() {
        return title;
    }

    public ArrayList<FeatureModel> getResources() {
        return resources;
    }

    private ArrayList<FeatureModel> resources;

    public class FeatureModel {
        private String id;
        private String icon;
        private String title;
        private ArrayList<String> utterances;
        private ArrayList<FilterModel> filters;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<String> getUtterances() {
            return utterances;
        }

        public void setUtterances(ArrayList<String> utterances) {
            this.utterances = utterances;
        }

        public ArrayList<FilterModel> getFilters() {
            return filters;
        }

        public void setFilters(ArrayList<FilterModel> filters) {
            this.filters = filters;
        }
    }

    public class FilterModel{
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<String> getUtterances() {
            return utterances;
        }

        public void setUtterances(ArrayList<String> utterances) {
            this.utterances = utterances;
        }

        private ArrayList<String> utterances;
    }
}


