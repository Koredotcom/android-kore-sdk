package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class PromotionalContentModel implements Serializable {
    private boolean show;
    private ArrayList<PromotionsModel> promotions;

    public ArrayList<PromotionsModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(@NonNull ArrayList<PromotionsModel> promotions) {
        this.promotions = promotions;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
