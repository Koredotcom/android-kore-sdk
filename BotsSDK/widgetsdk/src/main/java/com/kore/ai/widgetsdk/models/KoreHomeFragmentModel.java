package com.kore.ai.widgetsdk.models;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

/**
 * Created by Shiva Krishna on 6/24/2016.
 */
public class KoreHomeFragmentModel {
    public enum HomeFragmentStates {
        STATE_INFORMATION,
        STATE_KORA,
        STATE_SPACES
    }

    private Fragment fragment;
    private String title;
    private Drawable resourceId;

    public Drawable getResourceId() {
        return resourceId;
    }

    public void setResourceId(Drawable resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    private String resourceUrl;
    private String sectionTitle;

    public KoreHomeFragmentModel(Fragment fragment, String title, String sectionTitle, Drawable resourceId, String resourceUrl) {
        this.title = title;
        this.fragment = fragment;
        this.resourceId = resourceId;
        this.resourceUrl = resourceUrl;
        this.sectionTitle = sectionTitle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }



    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
}