package com.example.smartwastemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<WasteRequest> requestList;

    public RequestAdapter(List<WasteRequest> requestList) {
        this.requestList = requestList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        WasteRequest request = requestList.get(position);
        holder.wasteTypeText.setText(request.getWasteType());
        holder.addressText.setText(request.getAddress());
        holder.statusText.setText(request.getStatus());
        holder.timestampText.setText(request.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView wasteTypeText, addressText, statusText, timestampText;

        public RequestViewHolder(View itemView) {
            super(itemView);
            wasteTypeText = itemView.findViewById(R.id.wasteTypeText);
            addressText = itemView.findViewById(R.id.addressText);
            statusText = itemView.findViewById(R.id.statusText);
            timestampText = itemView.findViewById(R.id.timestampText);
        }
    }
}
