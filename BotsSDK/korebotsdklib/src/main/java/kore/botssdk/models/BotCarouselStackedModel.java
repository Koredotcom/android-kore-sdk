package kore.botssdk.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;

@SuppressLint("UnknownNullness")
public class BotCarouselStackedModel
{
    CarouselStackedSectionModel topSection;
    CarouselStackedSectionModel middleSection;
    CarouselStackedSectionModel bottomSection;

    ArrayList<BotCaourselButtonModel> buttons;

    public CarouselStackedSectionModel getBottomSection() {
        return bottomSection;
    }

    public CarouselStackedSectionModel getMiddleSection() {
        return middleSection;
    }

    public CarouselStackedSectionModel getTopSection() {
        return topSection;
    }

    public ArrayList<BotCaourselButtonModel> getButtons() {
        return buttons;
    }
}
