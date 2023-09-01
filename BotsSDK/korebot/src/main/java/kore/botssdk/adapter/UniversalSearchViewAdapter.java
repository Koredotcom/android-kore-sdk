package kore.botssdk.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.RecyclerViewDataAccessor;
import kore.botssdk.listener.VerticalListViewActionHelper;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.CalEventsTemplateModel;
import kore.botssdk.models.EmailModel;
import kore.botssdk.models.KaFileLookupModel;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.KnowledgeDetailModel;
import kore.botssdk.models.KoraUniversalSearchModel;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.DateUtils;
import kore.botssdk.utils.StringUtils;
import kore.botssdk.utils.Utility;
import kore.botssdk.view.UniversalSearchView;
import kore.botssdk.view.viewHolder.KnowledgeCollectionViewHolder;
import kore.botssdk.view.viewHolder.MeetingNotesViewHolder;
import kore.botssdk.view.viewUtils.FileUtils;

public class UniversalSearchViewAdapter extends RecyclerView.Adapter implements RecyclerViewDataAccessor {

    private final int MEETING_NOTES = 0;
    private final int EMAIl = 1;
    private final int KNOWLEDGE = 2;
    private final int FILES = 3;
    private final int KN_COLLECTION = 4;

    VerticalListViewActionHelper verticalListViewActionHelper;
    ArrayList<KoraUniversalSearchModel> koraUniversalSearchModel;

    final Context context;
    final UniversalSearchView universalSearchViewContext;


    public UniversalSearchViewAdapter(UniversalSearchView universalSearchView) {
        context = universalSearchView.getContext();
        universalSearchViewContext = universalSearchView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case EMAIl:
                view = LayoutInflater.from(context).inflate(R.layout.us_email_layout, parent, false);
                return new EmailViewHolder(view);
            case FILES:
                view = LayoutInflater.from(context).inflate(R.layout.us_files_layout, parent, false);
                return new FilesViewHolder(view);
            case KNOWLEDGE:
                view = LayoutInflater.from(context).inflate(R.layout.us_knowledge_article_layout, parent, false);
                return new KnowledgeViewHolder(view);
            case KN_COLLECTION:
                view = LayoutInflater.from(context).inflate(R.layout.us_knowledge_collection_layout, parent, false);
                return new KnowledgeCollectionViewHolder(view);
            case MEETING_NOTES:
                view = LayoutInflater.from(context).inflate(R.layout.us_meeting_layout, parent, false);
                return new MeetingNotesViewHolder(view);
            default:
                view = LayoutInflater.from(context).inflate(R.layout.us_email_layout, parent, false);
                return new EmailViewHolder(view);
        }
    }

    private void bindKnowledgeCollection(KnowledgeCollectionViewHolder viewHolder, final int position) {

        final KnowledgeCollectionModel.DataElements model = koraUniversalSearchModel.get(position).getKnowledgeCollection().getCombinedData().get(0);
        String title = koraUniversalSearchModel.get(position).getTitle();
        int count = koraUniversalSearchModel.get(position).getCount();
        viewHolder.divider.setVisibility(View.GONE);
        viewHolder.icon_view.setTypeface(Utility.getTypeFaceObj(context));
        viewHolder.icon_view.setBackground(Utility.changeColorOfDrawable(context, R.color.color_f98140));
        viewHolder.peopleicon.setTypeface(Utility.getTypeFaceObj(context));
//        viewHolder.staricon.setTypeface(Utility.getTypeFaceObj(context));
        viewHolder.root_title_view.setText(title);
        viewHolder.view_suggest.setVisibility(model.isSuggestive() ? View.VISIBLE : View.GONE);
        viewHolder.count_view.setVisibility(count > 1 ? View.VISIBLE : View.GONE);
        if (count > 1) {
            String str = (count - 1) + " more";
            viewHolder.count_view.setText(str);
        }


        viewHolder.view_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verticalListViewActionHelper != null) {
                    verticalListViewActionHelper.knowledgeCollectionItemClick(model, "");
                }
            }
        });
        viewHolder.count_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalSearchViewContext.itemClickPosition(position);
            }
        });

        viewHolder.title_view.setText(model.getQuestion());
        viewHolder.search_view.setText(model.getName());
        String str = model.getScore() + "% Match";
        viewHolder.percent_view.setText(str);
        viewHolder.sub_view.setText(model.getAnswerPayload().get(0).getText());
    }


    private void bindMeetingNotes(MeetingNotesViewHolder holder, final int position) {
        final CalEventsTemplateModel model = koraUniversalSearchModel.get(position).getMeetingNotes().get(0);
        String title = koraUniversalSearchModel.get(position).getTitle();
        int count = koraUniversalSearchModel.get(position).getCount();


        holder.icon_view.setTypeface(Utility.getTypeFaceObj(context));
        holder.icon_view.setBackground(Utility.changeColorOfDrawable(context, R.color.color_4e74f0));
        holder.root_title_view.setText(title);

        holder.count_view.setVisibility(count > 1 ? View.VISIBLE : View.GONE);
        if (count > 1) {
            String str = (count - 1) + " more";
            holder.count_view.setText(str);
        }
        holder.date_view.setText(DateUtils.getDateMMMDDYYYY(model.getDuration().getStart(), model.getDuration().getEnd()));
        String text = Utility.getFormatedAttendiesFromList(model.getAttendees());
        holder.creator_view.setText(text);
        holder.title_view.setText(model.getTitle());

        holder.click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalListViewActionHelper.meetingNotesNavigation(context, model.getEventId(), model.getMeetingNoteId());
            }
        });

        holder.count_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalSearchViewContext.itemClickPosition(position);
            }
        });
    }

    public void bindEmailData(EmailViewHolder holder, final int position) {
        final EmailModel model = koraUniversalSearchModel.get(position).getEmails().get(0);
        String title = koraUniversalSearchModel.get(position).getTitle();
        int count = koraUniversalSearchModel.get(position).getCount();


        holder.icon_view.setTypeface(Utility.getTypeFaceObj(context));
        holder.icon_view.setBackground(Utility.changeColorOfDrawable(context, R.color.color_2ad082));
        holder.count_view.setVisibility(count > 1 ? View.VISIBLE : View.GONE);
        if (count > 1) {
            String str = (count - 1) + " more";
            holder.count_view.setText(str);
        }
        holder.root_title_view.setText(title);
        holder.title.setText(model.getSubject() != null && !TextUtils.isEmpty(model.getSubject()) ? model.getSubject() : "(No Subject)");
        holder.date_view.setText(model.getDateFormat());
        holder.from.setText(StringEscapeUtils.unescapeHtml4(model.getFrom()));
        holder.image.setVisibility(model.getAttachments() != null && model.getAttachments().length != 0 ? View.VISIBLE : View.GONE);
        holder.body.setText(model.getDesc() != null && !TextUtils.isEmpty(model.getDesc()) ? model.getDesc() : "(No Body)");
        holder.count_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalSearchViewContext.itemClickPosition(position);
            }
        });

        holder.click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model.getButtons() == null || model.getButtons().size() == 0) return;
                BotCaourselButtonModel botCaourselButtonModel = model.getButtons().get(0);
                verticalListViewActionHelper.emailItemClicked(botCaourselButtonModel.getAction(), botCaourselButtonModel.getCustomData());
            }
        });
    }

    public void bindFilesData(FilesViewHolder holder, final int position) {


        final KaFileLookupModel model = koraUniversalSearchModel.get(position).getFiles().get(0);
        String title = koraUniversalSearchModel.get(position).getTitle();
        int count = koraUniversalSearchModel.get(position).getCount();


        holder.icon_view.setTypeface(Utility.getTypeFaceObj(context));
        holder.icon_view.setBackground(Utility.changeColorOfDrawable(context, R.color.color_38c9e1));
        holder.count_view.setVisibility(count > 1 ? View.VISIBLE : View.GONE);
        if (count > 1) {
            String str = count - 1 + " more";
            holder.count_view.setText(str);
        }
        holder.root_title_view.setText(title);
        holder.title.setText(model.getFileName());
        String type = model.getFileType();
        holder.image.setImageResource(FileUtils.getDrawableByExt(!StringUtils.isNullOrEmptyWithTrim(type) ? type.toLowerCase() : ""));

        holder.sharedBy.setVisibility(model.getSharedByDetails() != null && !TextUtils.isEmpty(model.getSharedByDetails()) ? View.VISIBLE : View.GONE);
        if (model.getSharedByDetails() != null && !TextUtils.isEmpty(model.getSharedByDetails())) {
            holder.sharedBy.setText(model.getSharedByDetails());
        }

        holder.last_edited.setText(model.getLastModifiedDate());
        holder.count_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalSearchViewContext.itemClickPosition(position);
            }
        });

        holder.click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getButtons() != null && model.getButtons().size() > 0) {
                    verticalListViewActionHelper.driveItemClicked(model.getButtons().get(0));
                } else if (model.getWebViewLink() != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getWebViewLink()));
                    context.startActivity(browserIntent);
                }

            }
        });

    }

    public void bindKnowledgeData(KnowledgeViewHolder holder, final int position) {


        final KnowledgeDetailModel model = koraUniversalSearchModel.get(position).getKnowledge().get(0);
        String title = koraUniversalSearchModel.get(position).getTitle();
        int count = koraUniversalSearchModel.get(position).getCount();


        holder.icon_view.setTypeface(Utility.getTypeFaceObj(context));
        holder.icon_view.setBackground(Utility.changeColorOfDrawable(context, R.color.color_f98140));
        holder.count_view.setVisibility(count > 1 ? View.VISIBLE : View.GONE);

        if (model!=null&&model.getImageUrl() != null && !model.getImageUrl().isEmpty()) {
            holder.link_image.setVisibility(View.VISIBLE);
            Picasso.get().load(model.getImageUrl()).into(holder.link_image);
        } else {
            holder.link_image.setVisibility(View.GONE);
        }

        if (count > 1) {
            String str = count - 1 + " more";
            holder.count_view.setText(str);
        }
        holder.root_title_view.setText(title);
        holder.title.setText(model.getTitle());
        holder.time.setText(model.getLastModifiedDate());

        if (StringUtils.isNullOrEmptyWithTrim(model.getDesc())) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(model.getSpannedString());
        }

        if (model.getNViews() > 0) {
            holder.eye_count.setVisibility(View.VISIBLE);
            holder.eye_count.setText(String.valueOf(model.getNViews()));
        } else {
            holder.eye_count.setVisibility(View.GONE);
        }

        if (model.getNShares() > 0) {
            holder.chat_count.setVisibility(View.VISIBLE);
            holder.chat_count.setText(String.valueOf(model.getNComments()));
        } else {
            holder.chat_count.setVisibility(View.GONE);
        }

        if (model.getNShares() > 0) {
            holder.like_count.setVisibility(View.VISIBLE);
            holder.like_count.setText(String.valueOf(model.getNUpVotes()));
        } else {
            holder.like_count.setVisibility(View.GONE);
        }

        if (model.getNShares() > 0) {
            holder.downvote_count.setVisibility(View.VISIBLE);
            holder.downvote_count.setText(String.valueOf(model.getNDownVotes()));
        } else {
            holder.downvote_count.setVisibility(View.GONE);
        }


        holder.click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString(BundleConstants.KNOWLEDGE_ID, model.getId());
                verticalListViewActionHelper.knowledgeItemClicked(extras, true);
            }
        });

        holder.count_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universalSearchViewContext.itemClickPosition(position);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case KN_COLLECTION:
                bindKnowledgeCollection((KnowledgeCollectionViewHolder) holder, position);


                break;

            case MEETING_NOTES:
                bindMeetingNotes((MeetingNotesViewHolder) holder, position);


                break;
            case EMAIl:
                bindEmailData((EmailViewHolder) holder, position);
                break;

            case FILES:

                bindFilesData((FilesViewHolder) holder, position);
                break;

            case KNOWLEDGE:

                bindKnowledgeData((KnowledgeViewHolder) holder, position);


                break;
        }


    }

    @Override
    public int getItemCount() {

        return koraUniversalSearchModel != null ? koraUniversalSearchModel.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        switch (koraUniversalSearchModel.get(position).getType()) {
            case BotResponse.US_EMAIL_TYPE:
                return EMAIl;
            case BotResponse.US_FILES_TYPE:
                return FILES;
            case BotResponse.US_KNOWLEDGE_TYPE:
                return KNOWLEDGE;
            case BotResponse.US_KNOWLEDGE_COLLECTION_TYPE:
                return KN_COLLECTION;
            case BotResponse.US_MEETING_NOTES_TYPE:
                return MEETING_NOTES;
        }

        return super.getItemViewType(position);
    }


    public ArrayList<KoraUniversalSearchModel> getData() {
        return koraUniversalSearchModel;
    }

    @Override
    public void setData(ArrayList koraUniversalSearchModel) {
        this.koraUniversalSearchModel = koraUniversalSearchModel;
        if(koraUniversalSearchModel != null && koraUniversalSearchModel.size() - 1 > 0)
            notifyItemRangeChanged(0, koraUniversalSearchModel.size() - 1);
    }

    @Override
    public void setExpanded(boolean isExpanded) {

    }

    @Override
    public void setVerticalListViewActionHelper(VerticalListViewActionHelper verticalListViewActionHelper) {
        this.verticalListViewActionHelper = verticalListViewActionHelper;
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView date_view;
        final TextView from;
        final TextView body;
        final TextView icon_view;
        final TextView root_title_view;
        final TextView count_view;
        final ImageView image;
        final View click_view;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);

            click_view=itemView.findViewById(R.id.click_view);
            title = itemView.findViewById(R.id.title);
            date_view = itemView.findViewById(R.id.date_view);
            from = itemView.findViewById(R.id.from);
            body = itemView.findViewById(R.id.body);
            image = itemView.findViewById(R.id.image);
            icon_view = itemView.findViewById(R.id.icon_view);
            root_title_view = itemView.findViewById(R.id.root_title_view);
            count_view = itemView.findViewById(R.id.count_view);
        }
    }


    public static class FilesViewHolder extends RecyclerView.ViewHolder {

        final TextView icon_view;
        final TextView root_title_view;
        final TextView count_view;
        final TextView title;
        final TextView sharedBy;
        final TextView last_edited;
        final ImageView image;
        final View click_view;

        public FilesViewHolder(@NonNull View itemView) {
            super(itemView);
            click_view=itemView.findViewById(R.id.click_view);
            icon_view = itemView.findViewById(R.id.icon_view);
            root_title_view = itemView.findViewById(R.id.root_title_view);
            count_view = itemView.findViewById(R.id.count_view);
            title = itemView.findViewById(R.id.title);
            sharedBy = itemView.findViewById(R.id.sharedBy);
            last_edited = itemView.findViewById(R.id.last_edited);
            image = itemView.findViewById(R.id.image);


        }
    }

    public static class KnowledgeViewHolder extends RecyclerView.ViewHolder {

        final TextView icon_view;
        final TextView root_title_view;
        final TextView count_view;
        final TextView title;
        final TextView time;
        final TextView description;
        final TextView eye_count;
        final TextView chat_count;
        final TextView like_count;
        final TextView downvote_count;
        final ImageView link_image;
        final View click_view;

        public KnowledgeViewHolder(@NonNull View itemView) {
            super(itemView);
            click_view=itemView.findViewById(R.id.click_view);
            link_image = itemView.findViewById(R.id.link_image);
            icon_view = itemView.findViewById(R.id.icon_view);
            root_title_view = itemView.findViewById(R.id.root_title_view);
            count_view = itemView.findViewById(R.id.count_view);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
            eye_count = itemView.findViewById(R.id.eye_count);
            chat_count = itemView.findViewById(R.id.chat_count);
            like_count = itemView.findViewById(R.id.like_count);
            downvote_count = itemView.findViewById(R.id.downvote_count);

        }
    }

}
