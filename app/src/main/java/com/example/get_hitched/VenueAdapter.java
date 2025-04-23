package com.example.get_hitched;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {

    private List<Venue> venueList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Venue venue);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VenueAdapter(List<Venue> venueList, Context context) {
        this.venueList = venueList;
        this.context = context;
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.venue_item, parent, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        Venue venue = venueList.get(position);
        holder.name.setText(venue.getName());
        holder.location.setText(venue.getLocation());
        holder.capacity.setText("Capacity: " + venue.getCapacity());
        holder.price.setText("₹" + venue.getPrice_per_plate() + "/plate");
        holder.rating.setRating((float) venue.getRating());
        Glide.with(context).load(venue.getImage_url()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(venueList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    class VenueViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, capacity, price;
        ImageView image;
        RatingBar rating;

        public VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_venue_name);
            location = itemView.findViewById(R.id.tv_venue_location);
            capacity = itemView.findViewById(R.id.tv_venue_capacity);
            price = itemView.findViewById(R.id.tv_venue_price);
            image = itemView.findViewById(R.id.iv_venue);
            rating = itemView.findViewById(R.id.rating_venue);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(venueList.get(position));
                }
            });
        }
    }
}
