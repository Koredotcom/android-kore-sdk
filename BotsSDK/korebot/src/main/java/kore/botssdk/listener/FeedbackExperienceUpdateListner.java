package kore.botssdk.listener;

import java.util.ArrayList;

import kore.botssdk.models.FeedbackExperienceContentModel;
import kore.botssdk.models.FeedbackListModel;

public interface FeedbackExperienceUpdateListner
{
   void updateExperienceList(int position, ArrayList<FeedbackExperienceContentModel> feedbackExperienceContentModels);
   void sendFeedback();
   void updateFeedbackList(ArrayList<FeedbackListModel> feedbackListModels);
}
