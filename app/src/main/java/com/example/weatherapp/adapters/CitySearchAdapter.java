package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

import java.util.ArrayList;
import java.util.List;

public class CitySearchAdapter extends RecyclerView.Adapter<CitySearchAdapter.MyViewHolder> {

    private List<String> mCountryList = new ArrayList<>();
    private List<String> mQueryLatLon = new ArrayList<>();
    private String mQueryCity = null;
    private OnItemClickListener mOnItemClickListener;

    public void setItems(List<String> items, List<String> queryLatLon, String queryCity) {
        mCountryList.clear();
        mQueryLatLon.clear();
        mCountryList.addAll(items);
        mQueryLatLon.addAll(queryLatLon);
        mQueryCity = queryCity;
        notifyDataSetChanged();
    }

    public void addItems(List<String> items, List<String> queryLatLon, String queryCity) {
        mCountryList.addAll(items);
        mQueryLatLon.addAll(queryLatLon);
        mQueryCity = queryCity;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String lat, String lon, boolean add);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, parent, false);
        return new MyViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String city = mCountryList.get(position);
        String lat = mQueryLatLon.get(position), lon = mQueryLatLon.get(position + 1);
        holder.bindView(city, mQueryCity, lat, lon, true);
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView countries, fullNameOfLocation;
        OnItemClickListener onItemClickListener;
        String mLat, mLon;
        boolean add;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            countries = itemView.findViewById(R.id.country);
            fullNameOfLocation = itemView.findViewById(R.id.full_name);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        public void bindView(String text, String mQueryCity, String lat, String lon, boolean flag){
            countries.setText(mQueryCity);
            fullNameOfLocation.setHint(text);
            mLat = lat;
            mLon = lon;
            add = flag;
        }
        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(mLat, mLon, add);
        }
    }
}
