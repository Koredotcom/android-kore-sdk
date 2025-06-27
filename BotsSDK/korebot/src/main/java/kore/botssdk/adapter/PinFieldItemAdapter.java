package kore.botssdk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kore.botssdk.R;
import kore.botssdk.utils.KaFontUtils;

public class PinFieldItemAdapter extends RecyclerView.Adapter<PinFieldItemAdapter.ViewHolder> {
    private int pinLength;
    private int parentWidth;

    public PinFieldItemAdapter(int pinLength, int parentWidth) {
        this.pinLength = pinLength;
        this.parentWidth = parentWidth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_field_item, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.etPinField.getLayoutParams();
        params.width = parentWidth / pinLength - 10;
        holder.etPinField.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return pinLength;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etPinField;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            etPinField = itemView.findViewById(R.id.otp_field);
        }
    }
}
