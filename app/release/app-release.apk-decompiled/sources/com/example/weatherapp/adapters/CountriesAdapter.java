package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.R;
import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<String> mCountryList = new ArrayList();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String str);
    }

    public void addItems(List<String> list) {
        this.mCountryList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item3, viewGroup, false), this.mOnItemClickListener);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.bindView(this.mCountryList.get(i));
    }

    public int getItemCount() {
        return this.mCountryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String city;
        TextView countries;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View view, OnItemClickListener onItemClickListener2) {
            super(view);
            this.countries = (TextView) view.findViewById(R.id.country);
            view.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener2;
        }

        public void bindView(String str) {
            this.countries.setText(str);
            this.city = str;
        }

        public void onClick(View view) {
            this.onItemClickListener.onItemClick(this.city);
        }
    }
}
