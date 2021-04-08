package kore.botssdk.models;

public class WorkHoursModel {


    public int getStart() {
        return s;
    }

    public void setStart(int s) {
        this.s = s;
    }

    public int getEnd() {
        return e;
    }

    public void setEnd(int e) {
        this.e = e;
    }

    public boolean isUa() {
        return a;
    }

    public void setUa(boolean a) {
        this.a = a;
    }

    private int s;
    private int e;
    private boolean a;

    //{"a":false,"s":570,"e":1110,"ua":false,"start":"0930","end":"1830"}
    private int start;
    private int end;
    private boolean ua;

    @Override
    public String toString() {
        return "{" +
                "\"s\":" + s +
                ", \"e\":" + e +
                ", \"a\":" + a +
                ", \"start\":" + start +
                ",\"end\":" + end +
                ", \"ua\":" + ua +
                '}';
    }
}
