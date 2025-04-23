package com.example.get_hitched;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private Context context;

    public BookingAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Set values to the views
        holder.textName.setText(booking.name);
        holder.textLocation.setText(booking.location);
        holder.textDate.setText(booking.date);
        holder.textAdditionalInfo.setText(booking.price); // Assuming price is the additional info
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textLocation, textDate, textAdditionalInfo;

        public BookingViewHolder(View itemView) {
            super(itemView);

            // Initialize the TextViews
            textName = itemView.findViewById(R.id.textName);
            textLocation = itemView.findViewById(R.id.textLocation);
            textDate = itemView.findViewById(R.id.textDate);
            textAdditionalInfo = itemView.findViewById(R.id.textAdditionalInfo);  // Added missing view
        }
    }
}
