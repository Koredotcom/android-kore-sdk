package kore.botssdk.models;

import java.util.ArrayList;

public class BrandingDependenciesModel
{
    private ArrayList<BrandingSubEntitiesModel> entities;
    private String[] allowedValues;

    public ArrayList<BrandingSubEntitiesModel> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<BrandingSubEntitiesModel> entities) {
        this.entities = entities;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }
}
