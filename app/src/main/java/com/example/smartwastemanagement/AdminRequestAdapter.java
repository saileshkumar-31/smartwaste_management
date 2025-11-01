package com.example.smartwastemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.AdminViewHolder> {

    private List<WasteRequest> requestList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WasteRequest request);
    }

    public AdminRequestAdapter(List<WasteRequest> requestList, OnItemClickListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @Override
    public AdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_request, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminViewHolder holder, int position) {
        WasteRequest request = requestList.get(position);
        holder.wasteTypeText.setText(request.getWasteType());
        holder.addressText.setText(request.getAddress());
        holder.statusText.setText(request.getStatus());
        holder.timestampText.setText(request.getTimestamp());

        holder.updateStatusButton.setOnClickListener(v -> listener.onItemClick(request));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView wasteTypeText, addressText, statusText, timestampText;
        Button updateStatusButton;

        public AdminViewHolder(View itemView) {
            super(itemView);
            wasteTypeText = itemView.findViewById(R.id.adminWasteTypeText);
            addressText = itemView.findViewById(R.id.adminAddressText);
            statusText = itemView.findViewById(R.id.adminStatusText);
            timestampText = itemView.findViewById(R.id.adminTimestampText);
            updateStatusButton = itemView.findViewById(R.id.updateStatusButton);
        }
    }
}
