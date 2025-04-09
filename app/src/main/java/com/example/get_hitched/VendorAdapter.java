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

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder> {

    private List<Vendor> vendorList;
    private Context context;

    public VendorAdapter(List<Vendor> vendorList, Context context) {
        this.vendorList = vendorList;
        this.context = context;
    }

    @NonNull
    @Override
    public VendorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vendor_item , parent, false);
        return new VendorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewHolder holder, int position) {
        Vendor vendor = vendorList.get(position);
        holder.name.setText(vendor.name);
        holder.type.setText(vendor.type);
        holder.city.setText(vendor.city);
        holder.rating.setRating((float) vendor.rating);
        Glide.with(context).load(vendor.image_url).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }

    static class VendorViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, city;
        ImageView image;
        RatingBar rating;

        public VendorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_vendor_name);
            type = itemView.findViewById(R.id.tv_vendor_type);
            city = itemView.findViewById(R.id.tv_vendor_city);
            image = itemView.findViewById(R.id.iv_vendor);
            rating = itemView.findViewById(R.id.rating_vendor);
        }
    }
}
