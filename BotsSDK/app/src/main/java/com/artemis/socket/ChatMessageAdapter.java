package com.artemis.socket;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** Flutter-example-style chat bubbles for the Android verification app. */
public final class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {
    private final List<ChatMessage> messages = new ArrayList<>();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private int typingPosition = RecyclerView.NO_POSITION;

    public void addMessage(String sender, String message, boolean userMessage) {
        messages.add(new ChatMessage(sender, message, userMessage, new Date()));
        notifyItemInserted(messages.size() - 1);
    }

    public void showTyping() {
        removeTyping();
        typingPosition = messages.size();
        messages.add(new ChatMessage("Artemis", "Typing…", false, new Date()));
        notifyItemInserted(typingPosition);
    }

    public void removeTyping() {
        if (typingPosition != RecyclerView.NO_POSITION && typingPosition < messages.size()) {
            messages.remove(typingPosition);
            notifyItemRemoved(typingPosition);
        }
        typingPosition = RecyclerView.NO_POSITION;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout bubble = new LinearLayout(parent.getContext());
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(dp(parent, 16), dp(parent, 12), dp(parent, 16), dp(parent, 10));

        TextView message = new TextView(parent.getContext());
        message.setId(View.generateViewId());
        message.setTextSize(16);
        message.setLineSpacing(0, 1.05f);
        message.setTextColor(Color.BLACK);
        bubble.addView(message, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView timestamp = new TextView(parent.getContext());
        timestamp.setId(View.generateViewId());
        timestamp.setTextSize(11);
        timestamp.setPadding(0, dp(parent, 4), 0, 0);
        bubble.addView(timestamp, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        FrameLayout container = new FrameLayout(parent.getContext());
        container.setPadding(dp(parent, 16), 0, dp(parent, 16), dp(parent, 12));
        container.addView(bubble);
        return new MessageViewHolder(container, bubble, message, timestamp);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage item = messages.get(position);
        holder.message.setText(item.message);
        holder.timestamp.setText(timeFormat.format(item.timestamp));

        int bubbleWidth = (int) (holder.container.getResources().getDisplayMetrics().widthPixels * 0.72f);
        FrameLayout.LayoutParams bubbleParams = new FrameLayout.LayoutParams(
                bubbleWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        bubbleParams.gravity = item.userMessage ? Gravity.END : Gravity.START;
        holder.bubble.setLayoutParams(bubbleParams);

        holder.bubble.setBackgroundResource(item.userMessage
                ? R.drawable.chat_bubble_user
                : R.drawable.chat_bubble_assistant);
        holder.message.setTextColor(ContextCompat.getColor(holder.container.getContext(),
                item.userMessage ? R.color.user_message_text : R.color.assistant_message_text));
        holder.timestamp.setTextColor(ContextCompat.getColor(holder.container.getContext(),
                item.userMessage ? R.color.user_message_timestamp : R.color.assistant_message_timestamp));
        holder.timestamp.setGravity(item.userMessage ? Gravity.END : Gravity.START);
        holder.message.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private int dp(ViewGroup parent, int value) {
        return Math.round(value * parent.getResources().getDisplayMetrics().density);
    }

    static final class MessageViewHolder extends RecyclerView.ViewHolder {
        private final FrameLayout container;
        private final LinearLayout bubble;
        private final TextView message;
        private final TextView timestamp;

        MessageViewHolder(FrameLayout container, LinearLayout bubble, TextView message, TextView timestamp) {
            super(container);
            this.container = container;
            this.bubble = bubble;
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    private static final class ChatMessage {
        private final String sender;
        private final String message;
        private final boolean userMessage;
        private final Date timestamp;

        private ChatMessage(String sender, String message, boolean userMessage, Date timestamp) {
            this.sender = sender;
            this.message = message;
            this.userMessage = userMessage;
            this.timestamp = timestamp;
        }
    }
}
