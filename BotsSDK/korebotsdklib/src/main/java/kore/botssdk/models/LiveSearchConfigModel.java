package kore.botssdk.models;

import java.io.Serializable;

public class LiveSearchConfigModel implements Serializable
{
    private float pinIndex;
    private float boost;
    private boolean visible;

    public float getBoost() {
        return boost;
    }

    public float getPinIndex() {
        return pinIndex;
    }

    public boolean getVisible()
    {
        return visible;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public void setPinIndex(float pinIndex) {
        this.pinIndex = pinIndex;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
