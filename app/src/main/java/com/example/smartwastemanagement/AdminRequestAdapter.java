package com.example.smartwastemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.AdminViewHolder> {

    private Context context;
    private List<WasteRequest> requestList;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onUpdateStatus(WasteRequest request);
    }

    public AdminRequestAdapter(Context context, List<WasteRequest> requestList, OnRequestActionListener listener) {
        this.context = context;
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_request, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        WasteRequest request = requestList.get(position);

        // Set request data using correct method names
        holder.requestId.setText(request.getRequestId());
        holder.userEmail.setText(request.getUserId());  // FIXED: Using getUserId()
        holder.wasteTypeText.setText(request.getWasteType());
        holder.addressText.setText(request.getAddress());
        holder.statusText.setText(request.getStatus());
        holder.timestampText.setText(request.getTimestamp());

        // Update Status Button
        holder.updateStatusButton.setOnClickListener(v -> listener.onUpdateStatus(request));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView requestId, userEmail, wasteTypeText, addressText, statusText, timestampText;
        Button updateStatusButton;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            requestId = itemView.findViewById(R.id.adminRequestId);
            userEmail = itemView.findViewById(R.id.adminUserEmail);
            wasteTypeText = itemView.findViewById(R.id.adminWasteTypeText);
            addressText = itemView.findViewById(R.id.adminAddressText);
            statusText = itemView.findViewById(R.id.adminStatusText);
            timestampText = itemView.findViewById(R.id.adminTimestampText);
            updateStatusButton = itemView.findViewById(R.id.updateStatusButton);
        }
    }
}
