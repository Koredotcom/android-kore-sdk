package kore.botssdk.models;

import java.util.ArrayList;

@SuppressWarnings("UnKnownNullness")
public class PayloadHeaderModel {
    private String type;
    private PayloadHeaderInner payload;
    private String text;

    public String getType() {
        return type;
    }

    public PayloadHeaderInner getPayload() {
        return payload;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(PayloadHeaderInner payload) {
        this.payload = payload;
    }

    public static class PayloadHeaderInner {
        private String template_type;

        public void setText(String text) {
            this.text = text;
        }

        private String text;
        private boolean stacked;
        private String layout;
        private String title;
        private String format;
        private String subtitle;
        private String image_url;
        private String view;
        private String description;
        private String url;

        public String getLayout() {
            return layout;
        }

        public void setLayout(String layout) {
            this.layout = layout;
        }

        public boolean isStacked() {
            return stacked;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setFormat(String endDate) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

        private ArrayList<BotButtonModel> buttons;

        public void setView(String view) {
            this.view = view;
        }

        public String getView() {
            return view;
        }

        public String getDescription() {
            return description;
        }

        public BotTableDataModel getData() {
            return data;
        }

        public void setData(BotTableDataModel data) {
            this.data = data;
        }

        private BotTableDataModel data;

        private final String color = "#000000";

        public String getTemplate_type() {
            return template_type;
        }

        public String getText() {
            return text;
        }

        public ArrayList<BotButtonModel> getButtons() {
            return buttons;
        }

        public void setButtons(ArrayList<BotButtonModel> buttons) {
            this.buttons = buttons;
        }

        public String getColor() {
            return color;
        }

        public static class Skill {
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            private String name;
            private String color;
            private String icon;
            private String trigger;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String id;

            public String getTrigger() {
                return trigger;
            }

            public void setTrigger(String trigger) {
                this.trigger = trigger;
            }
        }
    }
}