package kore.botssdk.models;

import java.util.ArrayList;

public class CardTemplateModel
{
    private CardHeadingModel cardHeading;
    private ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> cardDescription;
    private HeaderStyles cardContentStyles;
    private HeaderStyles cardStyles;
    private String cardType;
    private ArrayList<CardTemplateButtonModel> buttons;

    public ArrayList<CardTemplateButtonModel> getButtons() {
        return buttons;
    }

    public String getCardType() {
        return cardType;
    }

    public HeaderStyles getCardStyles() {
        return cardStyles;
    }

    public ArrayList<AdvanceListTableModel.AdvanceTableRowDataModel> getCardDescription() {
        return cardDescription;
    }

    public HeaderStyles getCardContentStyles() {
        return cardContentStyles;
    }

    public CardHeadingModel getCardHeading() {
        return cardHeading;
    }

    static class CardContentStyles
    {
        private String border;
    }
}
