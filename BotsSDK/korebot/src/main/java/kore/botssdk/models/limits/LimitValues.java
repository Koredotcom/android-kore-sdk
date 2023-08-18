package kore.botssdk.models.limits;

public class LimitValues {
    public final int limitTotal,usedTotal,limit;


    public LimitValues(int limitTotal, int usedTotal,int limit) {
        this.limit = limit;
        this.usedTotal = usedTotal;
        this.limitTotal=limitTotal;
    }
}
