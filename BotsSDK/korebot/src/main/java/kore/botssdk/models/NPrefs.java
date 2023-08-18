package kore.botssdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NPrefs implements Serializable,Cloneable {

@SerializedName("sound")
@Expose
private String sound;
@SerializedName("dndTill")
@Expose
private long dndTill;
@SerializedName("MEETING_REQ")
@Expose
private Boolean mEETINGREQ;
@SerializedName("MEETING_REQ_RESPOND")
@Expose
private Boolean mEETINGREQRESPOND;
@SerializedName("MEETING_NOTES_SHARE")
@Expose
private Boolean mEETINGNOTESSHARE;
@SerializedName("KNOWLEDGE_SHARE")
@Expose
private Boolean kNOWLEDGESHARE;
@SerializedName("KNOWLEDGE_UPDATE")
@Expose
private Boolean kNOWLEDGEUPDATE;
@SerializedName("KNOWLEDGE_COMMENT")
@Expose
private Boolean kNOWLEDGECOMMENT;
@SerializedName("ANNOUNCEMENT_COMMENT")
@Expose
private Boolean aNNOUNCEMENTCOMMENT;
@SerializedName("TEAM_MEM_ADD")
@Expose
private Boolean tEAMMEMADD;
@SerializedName("TEAM_MEM_LEFT")
@Expose
private Boolean tEAMMEMLEFT;
@SerializedName("BUDDY_JOIN")
@Expose
private Boolean bUDDYJOIN;

public String getSound() {
return sound;
}

public void setSound(String sound) {
this.sound = sound;
}

public long getDndTill() {
return dndTill;
}

public void setDndTill(long dndTill) {
this.dndTill = dndTill;
}

public Boolean getMEETINGREQ() {
return mEETINGREQ;
}

public void setMEETINGREQ(Boolean mEETINGREQ) {
this.mEETINGREQ = mEETINGREQ;
}

public Boolean getMEETINGREQRESPOND() {
return mEETINGREQRESPOND;
}

public void setMEETINGREQRESPOND(Boolean mEETINGREQRESPOND) {
this.mEETINGREQRESPOND = mEETINGREQRESPOND;
}

public Boolean getMEETINGNOTESSHARE() {
return mEETINGNOTESSHARE;
}

public void setMEETINGNOTESSHARE(Boolean mEETINGNOTESSHARE) {
this.mEETINGNOTESSHARE = mEETINGNOTESSHARE;
}

public Boolean getKNOWLEDGESHARE() {
return kNOWLEDGESHARE;
}

public void setKNOWLEDGESHARE(Boolean kNOWLEDGESHARE) {
this.kNOWLEDGESHARE = kNOWLEDGESHARE;
}

public Boolean getKNOWLEDGEUPDATE() {
return kNOWLEDGEUPDATE;
}

public void setKNOWLEDGEUPDATE(Boolean kNOWLEDGEUPDATE) {
this.kNOWLEDGEUPDATE = kNOWLEDGEUPDATE;
}

public Boolean getKNOWLEDGECOMMENT() {
return kNOWLEDGECOMMENT;
}

public void setKNOWLEDGECOMMENT(Boolean kNOWLEDGECOMMENT) {
this.kNOWLEDGECOMMENT = kNOWLEDGECOMMENT;
}

public Boolean getANNOUNCEMENTCOMMENT() {
return aNNOUNCEMENTCOMMENT;
}

public void setANNOUNCEMENTCOMMENT(Boolean aNNOUNCEMENTCOMMENT) {
this.aNNOUNCEMENTCOMMENT = aNNOUNCEMENTCOMMENT;
}

public Boolean getTEAMMEMADD() {
return tEAMMEMADD;
}

public void setTEAMMEMADD(Boolean tEAMMEMADD) {
this.tEAMMEMADD = tEAMMEMADD;
}

public Boolean getTEAMMEMLEFT() {
return tEAMMEMLEFT;
}

public void setTEAMMEMLEFT(Boolean tEAMMEMLEFT) {
this.tEAMMEMLEFT = tEAMMEMLEFT;
}

public Boolean getBUDDYJOIN() {
return bUDDYJOIN;
}

public void setBUDDYJOIN(Boolean bUDDYJOIN) {
this.bUDDYJOIN = bUDDYJOIN;
}

    @Override
    protected NPrefs clone() throws CloneNotSupportedException {
        return (NPrefs)super.clone();
    }
}