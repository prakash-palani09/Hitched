package com.example.get_hitched;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<SpinnerItem> itemList;
    private OnItemClickListener listener;
    private int selectedPosition = -1; // Track selected item

    public interface OnItemClickListener {
        void onItemClick(SpinnerItem item);
    }

    public CustomAdapter(Context context, List<SpinnerItem> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpinnerItem item = itemList.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewCost.setText("$" + item.getCost());

        // Change background color if selected
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Highlight selected item
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Default color
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position; // Update selected position
            notifyDataSetChanged(); // Refresh UI
            listener.onItemClick(item); // Callback for selected item
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.itemName);
            textViewCost = itemView.findViewById(R.id.itemCost);
        }
    }
}
