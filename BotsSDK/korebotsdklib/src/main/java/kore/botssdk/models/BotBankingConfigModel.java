package kore.botssdk.models;

public class BotBankingConfigModel
{
    private String id;
    private String header_title;
    private String header_color;
    private String back_img;
    private String top_left_icon;
//    private String left_icon;


    public void setId(String id) {
        this.id = id;
    }

    public void setBack_img(String back_img) {
        this.back_img = back_img;
    }

    public void setHeader_color(String header_color) {
        this.header_color = header_color;
    }

    public void setHeader_title(String header_title) {
        this.header_title = header_title;
    }

    public void setTop_left_icon(String top_left_icon) {
        this.top_left_icon = top_left_icon;
    }

    public String getId() {
        return id;
    }

    public String getBack_img() {
        return back_img;
    }

    public String getHeader_color() {
        return header_color;
    }

    public String getHeader_title() {
        return header_title;
    }

    public String getTop_left_icon() {
        return top_left_icon;
    }

}
