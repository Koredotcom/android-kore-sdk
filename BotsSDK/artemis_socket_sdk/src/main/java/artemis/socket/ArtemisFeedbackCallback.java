package artemis.socket;

/** Receives the server acknowledgement for a submitted feedback rating. */
public interface ArtemisFeedbackCallback {
    /** Called when the runtime accepts the feedback and returns its identifier. */
    void onSuccess(String feedbackId);

    /** Called when the runtime rejects the feedback or no acknowledgement arrives in time. */
    void onFailure(String code, String message);
}
