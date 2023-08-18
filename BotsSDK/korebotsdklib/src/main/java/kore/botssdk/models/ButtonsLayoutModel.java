package kore.botssdk.models;

public class ButtonsLayoutModel
{
    private String buttonAligment;
    private DisplayLimit displayLimit;

    public String getButtonAligment() {
        return buttonAligment;
    }

    public DisplayLimit getDisplayLimit() {
        return displayLimit;
    }

    public class DisplayLimit
    {
        private int count;

        public int getCount() {
            return count;
        }
    }
}
