package com.example.weatherapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.NewFragment;
import com.example.weatherapp.R;
import com.example.weatherapp.roomDB.weatherData.HourlyData;
import com.example.weatherapp.roomDB.weatherData.HourlyDataDao;
import com.example.weatherapp.roomDB.weatherData.WeatherData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterHoursCollapseNew extends RecyclerView.Adapter<RecyclerViewAdapterHoursCollapseNew.MyViewHolder> {
    private List<HourlyData> data;
    private ArrayList<Integer> weather_icons_images;
    private ArrayList<String> weather_icons;
    private Context context;

    private List<WeatherData> weatherList;
    private List<WeatherData> weatherOutputList;
    private HourlyDataDao myHourlyDataDao;
    private int index = 0;

    public RecyclerViewAdapterHoursCollapseNew(HourlyDataDao adapterHourlyDataDao, NewFragment mainFragment) {
        data = new ArrayList<>();
        weather_icons_images = new ArrayList<>();
        weather_icons = new ArrayList<>();
        context = mainFragment.getActivity().getApplicationContext();
        myHourlyDataDao = adapterHourlyDataDao;
        weatherList = new ArrayList<>();
        weatherOutputList = new ArrayList<>();
    }

    public void setItems(List<HourlyData> items) {
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<HourlyData> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int getItemCount() {
        return data.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HourlyData model = data.get(position);
        holder.bindView(model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView date, degree;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.hour_time);
            degree = itemView.findViewById(R.id.hour_current_degree);
            icon = itemView.findViewById(R.id.day2);
        }

        @SuppressLint("SetTextI18n")
        public void bindView(HourlyData model) {
            Picasso.get()
                    .load("https:" + model.urlIcon)
                    .fit()
                    .centerInside()
                    .into(icon);
            degree.setText(model.hourlyDegree + "\u2103");
            date.setText(model.hourlyTime + "");
        }

//        private String getDate(long time) {
//            Calendar cal = Calendar.getInstance(Locale.getDefault());
//            cal.setTimeInMillis(time * 1000);
//            String date = DateFormat.format("h:mm", cal).toString();
//            return date;
//        }
    }
}
