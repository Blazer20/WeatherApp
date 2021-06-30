package com.example.weatherapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.roomDB.weatherData.WeatherData;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddedCitiesAdapter extends RecyclerView.Adapter<AddedCitiesAdapter.MyViewHolder> {

    private List<WeatherData> weatherData;
    private OnItemClickListener mOnItemClickListener;
    private Date mTime;

    public AddedCitiesAdapter(List<WeatherData> allData, Date time){
        weatherData = allData;
        mTime = time;
    }

    public void setItems(List<WeatherData> items) {
        weatherData.clear();
        weatherData.addAll(items);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, boolean flag);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item4, parent, false);
        return new MyViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WeatherData wData = weatherData.get(position);
        holder.bindView(wData);
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView addedCity, currentDegree, dayAndNightDegree;
        OnItemClickListener onItemClickListener;
        int position;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            addedCity = itemView.findViewById(R.id.added_city);
            currentDegree = itemView.findViewById(R.id.added_current_degree);
            dayAndNightDegree = itemView.findViewById(R.id.added_degrees);
            String mCurrentDate = DateFormat.getTimeInstance(DateFormat.SHORT).
                    format(mTime);
            if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 9
                    && Integer.parseInt(mCurrentDate.substring(0, 2)) < 12) {
                itemView.setBackgroundResource(R.drawable.background_morning);
                addedCity.setTextColor(Color.parseColor("#333333"));
                currentDegree.setTextColor(Color.parseColor("#333333"));
                dayAndNightDegree.setTextColor(Color.parseColor("#333333"));
            } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 12
                    && Integer.parseInt(mCurrentDate.substring(0, 2)) < 15) {
                itemView.setBackgroundResource(R.drawable.background_day);
                addedCity.setTextColor(Color.parseColor("#333333"));
                currentDegree.setTextColor(Color.parseColor("#333333"));
                dayAndNightDegree.setTextColor(Color.parseColor("#333333"));
            } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 15
                    && Integer.parseInt(mCurrentDate.substring(0, 2)) < 18) {
                itemView.setBackgroundResource(R.drawable.background_evening);
                addedCity.setTextColor(Color.parseColor("#ffffff"));
                currentDegree.setTextColor(Color.parseColor("#ffffff"));
                dayAndNightDegree.setTextColor(Color.parseColor("#ffffff"));
            } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 18
                    && Integer.parseInt(mCurrentDate.substring(0, 2)) > 0) {
                itemView.setBackgroundResource(R.drawable.background_prenight);
                addedCity.setTextColor(Color.parseColor("#ffffff"));
                currentDegree.setTextColor(Color.parseColor("#ffffff"));
                dayAndNightDegree.setTextColor(Color.parseColor("#ffffff"));
            } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 0
                    && Integer.parseInt(mCurrentDate.substring(0, 2)) < 5) {
                itemView.setBackgroundResource(R.drawable.background_fullnight);
                addedCity.setTextColor(Color.parseColor("#ffffff"));
                currentDegree.setTextColor(Color.parseColor("#ffffff"));
                dayAndNightDegree.setTextColor(Color.parseColor("#ffffff"));
            } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 5
                    && Integer.parseInt(mCurrentDate.substring(0, 2)) < 9) {
                itemView.setBackgroundResource(R.drawable.background_premorning);
                addedCity.setTextColor(Color.parseColor("#ffffff"));
                currentDegree.setTextColor(Color.parseColor("#ffffff"));
                dayAndNightDegree.setTextColor(Color.parseColor("#ffffff"));
            }
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        public void bindView(WeatherData wData) {
            addedCity.setText(wData.dbCurrentLocation);
            currentDegree.setText(wData.dbCurrentDegree + "°");
            dayAndNightDegree.setText(wData.dbCurrentDegreeDay + "°/" + wData.dbCurrentDegreeNight + "°");
        }

        @Override
        public void onClick(View view) {
            position = getAdapterPosition();
            onItemClickListener.onItemClick(position, true);
        }
    }
}
