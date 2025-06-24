package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class PromotionalContentModel implements Serializable {
    private Boolean show;
    private ArrayList<PromotionsModel> promotions;

    public PromotionalContentModel updateWith(PromotionalContentModel configModel) {
        show = configModel.show != null ? configModel.show : show;
        promotions = configModel.promotions != null && !configModel.promotions.isEmpty() ? configModel.promotions : promotions;
        return this;
    }

    public ArrayList<PromotionsModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(@NonNull ArrayList<PromotionsModel> promotions) {
        this.promotions = promotions;
    }

    public boolean isShow() {
        return show != null ? show : false;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
