package com.kore.findlysdk.models;

public class InteractionsConfigModel
{
    private String welcomeMsg;
    private String welcomeMsgColor;
    private String showSearchesEnabled;
    private String showSearches;
    private String autocompleteOpt;
    private String welcomeMsgEmoji;
    private int querySuggestionsLimit;
    private int liveSearchResultsLimit;
    private FeedbackExperienceModel feedbackExperience;

    public void setAutocompleteOpt(String autocompleteOpt) {
        this.autocompleteOpt = autocompleteOpt;
    }

    public void setFeedbackExperience(FeedbackExperienceModel feedbackExperience) {
        this.feedbackExperience = feedbackExperience;
    }

    public void setLiveSearchResultsLimit(int liveSearchResultsLimit) {
        this.liveSearchResultsLimit = liveSearchResultsLimit;
    }

    public void setQuerySuggestionsLimit(int querySuggestionsLimit) {
        this.querySuggestionsLimit = querySuggestionsLimit;
    }

    public void setShowSearches(String showSearches) {
        this.showSearches = showSearches;
    }

    public void setShowSearchesEnabled(String showSearchesEnabled) {
        this.showSearchesEnabled = showSearchesEnabled;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public void setWelcomeMsgColor(String welcomeMsgColor) {
        this.welcomeMsgColor = welcomeMsgColor;
    }

    public void setWelcomeMsgEmoji(String welcomeMsgEmoji) {
        this.welcomeMsgEmoji = welcomeMsgEmoji;
    }

    public FeedbackExperienceModel getFeedbackExperience() {
        return feedbackExperience;
    }

    public String getAutocompleteOpt() {
        return autocompleteOpt;
    }

    public int getLiveSearchResultsLimit() {
        return liveSearchResultsLimit;
    }

    public int getQuerySuggestionsLimit() {
        return querySuggestionsLimit;
    }

    public String getShowSearches() {
        return showSearches;
    }

    public String getShowSearchesEnabled() {
        return showSearchesEnabled;
    }

    public String getWelcomeMsg() {
        return welcomeMsg;
    }

    public String getWelcomeMsgColor() {
        return welcomeMsgColor;
    }

    public String getWelcomeMsgEmoji() {
        return welcomeMsgEmoji;
    }
}
