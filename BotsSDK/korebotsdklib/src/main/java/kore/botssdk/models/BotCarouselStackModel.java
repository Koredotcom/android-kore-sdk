package kore.botssdk.models;

import java.util.ArrayList;

public class BotCarouselStackModel {
    private ArrayList<BotCaourselButtonModel> buttons;
    private TopSection topSection;
    private MiddleSection middleSection;
    private BottomSection bottomSection;

    public ArrayList<BotCaourselButtonModel> getButtons() {
        return buttons;
    }

    public TopSection getTopSection() {
        return topSection;
    }

    public MiddleSection getMiddleSection() {
        return middleSection;
    }

    public BottomSection getBottomSection() {
        return bottomSection;
    }

    public static class TopSection {
        public String title;
    }

    public static class MiddleSection {
        public String description;
    }

    public static class BottomSection {
        public String title;
        public String description;
    }
}
