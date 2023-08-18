package kore.botssdk.models;

import java.util.ArrayList;

public class PdfDownloadBaseModel
{
    private String AccountNumber;
    private String SourceSystemId;
    private String SourceRequestID;
    private String OtherRequestId;
    private String StmtType;
    private String DestinationSystemId;
    private String AccountServiceNumber;
    private String PrimaryCreditCardNumber;
    private String REEmailId;
    private String StatementFlag;
    private String ResponseCode;
    private String ActionStatus;
    private String FailureReason;
    private ArrayList<ResponseData> ResponseData;

    public String getAccountNumber() {
        return AccountNumber;
    }

    public ArrayList<ResponseData> getResponseData() {
        return ResponseData;
    }

    public String getAccountServiceNumber() {
        return AccountServiceNumber;
    }

    public String getActionStatus() {
        return ActionStatus;
    }

    public String getDestinationSystemId() {
        return DestinationSystemId;
    }

    public String getFailureReason() {
        return FailureReason;
    }

    public String getOtherRequestId() {
        return OtherRequestId;
    }

    public String getPrimaryCreditCardNumber() {
        return PrimaryCreditCardNumber;
    }

    public String getREEmailId() {
        return REEmailId;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public String getSourceRequestID() {
        return SourceRequestID;
    }

    public String getSourceSystemId() {
        return SourceSystemId;
    }

    public String getStatementFlag() {
        return StatementFlag;
    }

    public String getStmtType() {
        return StmtType;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public void setAccountServiceNumber(String accountServiceNumber) {
        AccountServiceNumber = accountServiceNumber;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public void setDestinationSystemId(String destinationSystemId) {
        DestinationSystemId = destinationSystemId;
    }

    public void setFailureReason(String failureReason) {
        FailureReason = failureReason;
    }

    public void setOtherRequestId(String otherRequestId) {
        OtherRequestId = otherRequestId;
    }

    public void setPrimaryCreditCardNumber(String primaryCreditCardNumber) {
        PrimaryCreditCardNumber = primaryCreditCardNumber;
    }

    public void setREEmailId(String REEmailId) {
        this.REEmailId = REEmailId;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public void setResponseData(ArrayList<ResponseData> responseData) {
        ResponseData = responseData;
    }

    public void setSourceRequestID(String sourceRequestID) {
        SourceRequestID = sourceRequestID;
    }

    public void setSourceSystemId(String sourceSystemId) {
        SourceSystemId = sourceSystemId;
    }

    public void setStatementFlag(String statementFlag) {
        StatementFlag = statementFlag;
    }

    public void setStmtType(String stmtType) {
        StmtType = stmtType;
    }

    public class ResponseData
    {
        private String Month;
        private String Action;
        private String Status;
        private String FileData;
        private String Comments;

        public void setAction(String action) {
            Action = action;
        }

        public void setComments(String comments) {
            Comments = comments;
        }

        public void setFileData(String fileData) {
            FileData = fileData;
        }

        public void setMonth(String month) {
            Month = month;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getFileData() {
            return FileData;
        }

        public String getAction() {
            return Action;
        }

        public String getComments() {
            return Comments;
        }

        public String getMonth() {
            return Month;
        }

        public String getStatus() {
            return Status;
        }
    }
}
