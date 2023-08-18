package kore.botssdk.charts.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

import kore.botssdk.charts.utils.Utils;

public class Entry extends BaseEntry implements Parcelable {
    private float x = 0.0F;
    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public Entry() {
    }

    public Entry(float x, float y) {
        super(y);
        this.x = x;
    }

    public Entry(float x, float y, Object data) {
        super(y, data);
        this.x = x;
    }

    public Entry(float x, float y, Drawable icon) {
        super(y, icon);
        this.x = x;
    }

    public Entry(float x, float y, Drawable icon, Object data) {
        super(y, icon, data);
        this.x = x;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public kore.botssdk.charts.data.Entry copy() {
        kore.botssdk.charts.data.Entry e = new kore.botssdk.charts.data.Entry(this.x, this.getY(), this.getData());
        return e;
    }

    public boolean equalTo(Entry e) {
        if (e == null) {
            return false;
        } else if (e.getData() != this.getData()) {
            return false;
        } else if (Math.abs(e.x - this.x) > Utils.FLOAT_EPSILON) {
            return false;
        } else {
            return !(Math.abs(e.getY() - this.getY()) > Utils.FLOAT_EPSILON);
        }
    }

    public String toString() {
        return "Entry, x: " + this.x + " y: " + this.getY();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.getY());
        if (this.getData() != null) {
            if (!(this.getData() instanceof Parcelable)) {
                throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
            }

            dest.writeInt(1);
            dest.writeParcelable((Parcelable)this.getData(), flags);
        } else {
            dest.writeInt(0);
        }

    }

    protected Entry(Parcel in) {
        this.x = in.readFloat();
        this.setY(in.readFloat());
        if (in.readInt() == 1) {
            this.setData(in.readParcelable(Object.class.getClassLoader()));
        }

    }
}
