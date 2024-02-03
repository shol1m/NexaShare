package com.example.nexashare.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexashare.EventPassengersActivity;
import com.example.nexashare.Models.CreatedData;
import com.example.nexashare.R;
import com.example.nexashare.SingleRidePassengersActivity;

import java.util.List;

public class CreatedAdapter extends RecyclerView.Adapter<CreatedAdapter.ViewHolder> {
        private List<CreatedData> itemList;
        private Context context;

        public CreatedAdapter(List<CreatedData> itemList, Context context) {
            this.itemList = itemList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.created_item, parent, false);
            return new ViewHolder(view);
        }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreatedData item = itemList.get(position);
            // Set data based on the type (event or ride)
            holder.bindData(item);

            // Handle item click
        if (item.getType() == "event"){
            holder.itemView.setOnClickListener(view -> {
                // Pass the document ID to the next activity using Intent
                Intent intent = new Intent(context, EventPassengersActivity.class);
                intent.putExtra("documentId", item.getDocumentId());
                intent.putExtra("type", item.getType());
                context.startActivity(intent);
            });
        }
        else{
            holder.itemView.setOnClickListener(view -> {
                // Pass the document ID to the next activity using Intent
                Intent intent = new Intent(context, SingleRidePassengersActivity.class);
                intent.putExtra("documentId", item.getDocumentId());
                intent.putExtra("type", item.getType());
                context.startActivity(intent);
            });
        }
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTextView;
            private TextView typeTextView;
            private TextView locationOrSourceTextView;
            private TextView phoneNumberOrDestinationTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                locationOrSourceTextView = itemView.findViewById(R.id.locationOrSourceTextView);
                phoneNumberOrDestinationTextView = itemView.findViewById(R.id.phoneNumberOrDestinationTextView);
                typeTextView = itemView.findViewById(R.id.typeTextView);
            }

            public void bindData(CreatedData item) {
                // Set data based on the type (event or ride)
                if(item.getType() == "event") {
                    nameTextView.setText(item.getName());
                    typeTextView.setText(item.getType());
                    locationOrSourceTextView.setText(item.getLocationOrSource());
                    phoneNumberOrDestinationTextView.setText(item.getPhoneNumberOrDestination());
                }else{
                    typeTextView.setText(item.getType());
                    locationOrSourceTextView.setText("From: "+item.getLocationOrSource());
                    phoneNumberOrDestinationTextView.setText("To: "+item.getPhoneNumberOrDestination());
                }
            }
        }

}
