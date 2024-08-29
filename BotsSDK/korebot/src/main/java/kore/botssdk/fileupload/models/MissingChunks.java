package kore.botssdk.fileupload.models;


import java.util.ArrayList;
import java.util.List;

public class MissingChunks {

    private List<UploadError> errors = new ArrayList<UploadError>();
    /**
     * @return
     * The errors
     */
    public List<UploadError> getErrors() {
        return errors;
    }
    /**
     * @param errors
     * The errors
     */
    public void setErrors(List<UploadError> errors) {
        this.errors = errors;
    }
}

