package kore.botssdk.models;

import java.util.ArrayList;

public class BrandingResponseModel
{
    private ArrayList<BrandingEntitiesModel> entities;

    public void setEntities(ArrayList<BrandingEntitiesModel> entities) {
        this.entities = entities;
    }

    public ArrayList<BrandingEntitiesModel> getEntities() {
        return entities;
    }
}