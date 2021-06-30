package com.example.weatherapp.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.models.hourly.DataH;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapterH extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataH> data = new ArrayList();
    /* access modifiers changed from: private */
    public int index = 0;
    /* access modifiers changed from: private */
    public WeatherDataDao weatherDataDaoH;
    private List<WeatherData> weatherList;
    private List<WeatherData> weatherOutputList;
    /* access modifiers changed from: private */
    public ArrayList<String> weather_icons = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<Integer> weather_icons_images = new ArrayList<>();

    static /* synthetic */ int access$108(RecyclerViewAdapterH recyclerViewAdapterH) {
        int i = recyclerViewAdapterH.index;
        recyclerViewAdapterH.index = i + 1;
        return i;
    }

    public RecyclerViewAdapterH(WeatherDataDao weatherDataDao, MainActivity mainActivity) {
        this.context = mainActivity;
        this.weatherDataDaoH = weatherDataDao;
        this.weatherList = new ArrayList();
        this.weatherOutputList = new ArrayList();
    }

    public void setItems(List<DataH> list) {
        this.data.clear();
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(List<DataH> list) {
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    public void addOfflineItems(List<WeatherData> list) {
        this.weatherList.addAll(list);
        this.weatherOutputList.addAll(this.weatherDataDaoH.getAllByIndH());
        notifyDataSetChanged();
    }

    public void setOfflineItems(List<WeatherData> list) {
        this.weatherList.clear();
        this.weatherOutputList.clear();
        this.weatherList.addAll(list);
        this.weatherOutputList.addAll(this.weatherDataDaoH.getAllByIndH());
        notifyDataSetChanged();
    }

    public void outPutList() {
        for (int i = 0; i < this.weatherList.size(); i++) {
            if (this.weatherList.get(i).ind == 1) {
                this.weatherOutputList.add(this.weatherList.get(i));
            }
        }
    }

    public boolean isOnline(Context context2) {
        return ((ConnectivityManager) context2.getApplicationContext().getSystemService("connectivity")).getActiveNetwork() != null;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item2, viewGroup, false));
    }

    public int getItemCount() {
        if (isOnline(this.context)) {
            return this.data.size();
        }
        return this.weatherOutputList.size();
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if (isOnline(this.context)) {
            myViewHolder.bindView(this.data.get(i));
        } else {
            myViewHolder.loadData(this.weatherOutputList.get(i));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText date;
        EditText degree;
        ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            this.date = (EditText) view.findViewById(R.id.hour_time);
            this.degree = (EditText) view.findViewById(R.id.hour_current_degree);
            this.icon = (ImageView) view.findViewById(R.id.day2);
        }

        public void bindView(DataH dataH) {
            this.date.setText(getDate(dataH.getDate()));
            EditText editText = this.degree;
            editText.setText(((int) dataH.getTemp()) + "℃");
            saveData(dataH);
            imageDefiner(dataH);
        }

        private String getDate(long j) {
            Calendar instance = Calendar.getInstance(Locale.getDefault());
            instance.setTimeInMillis(j * 1000);
            return DateFormat.format("h:mm", instance).toString();
        }

        public void saveData(DataH dataH) {
            long j;
            if (RecyclerViewAdapterH.this.weatherDataDaoH.getCountH() <= 12) {
                WeatherData weatherData = new WeatherData();
                weatherData.dbHourlyTime = this.date.getText().toString();
                weatherData.dbHourlyIcon = dataH.getWeather().getIcon();
                weatherData.dbHourlyDegree = this.degree.getText().toString();
                weatherData.ind = 1;
                Log.e("hInsert", "Data: " + weatherData.dbHourlyTime + ", " + weatherData.dbHourlyDegree + ", " + weatherData.dbHourlyIcon + ", " + RecyclerViewAdapterH.this.weatherDataDaoH.insert(weatherData));
                return;
            }
            List<WeatherData> allData = RecyclerViewAdapterH.this.weatherDataDaoH.getAllData();
            List<Long> allIdH = RecyclerViewAdapterH.this.weatherDataDaoH.getAllIdH();
            int i = 0;
            while (true) {
                if (i >= allData.size()) {
                    j = 0;
                    break;
                }
                if (RecyclerViewAdapterH.this.index >= 12) {
                    int unused = RecyclerViewAdapterH.this.index = 0;
                } else if (allIdH.get(RecyclerViewAdapterH.this.index).longValue() == allData.get(i).id) {
                    j = allIdH.get(RecyclerViewAdapterH.this.index).longValue();
                    RecyclerViewAdapterH.access$108(RecyclerViewAdapterH.this);
                    break;
                }
                i++;
            }
            if (j != 0) {
                WeatherData byId = RecyclerViewAdapterH.this.weatherDataDaoH.getById(allData.get(RecyclerViewAdapterH.this.index).id);
                byId.dbHourlyTime = this.date.getText().toString();
                byId.dbHourlyIcon = dataH.getWeather().getIcon();
                byId.dbHourlyDegree = this.degree.getText().toString();
                RecyclerViewAdapterH.this.weatherDataDaoH.update(byId);
                Log.e("hUpdate", "Data: " + byId.dbHourlyTime + ", " + byId.dbHourlyDegree + ", " + byId.dbHourlyIcon);
            }
        }

        public void loadData(WeatherData weatherData) {
            this.date.setText(weatherData.dbHourlyTime);
            this.degree.setText(weatherData.dbHourlyDegree);
            addOfflineWeatherIcons(weatherData);
        }

        public void addOfflineWeatherIcons(WeatherData weatherData) {
            addWeatherIcons();
            String str = weatherData.dbHourlyIcon;
            for (int i = 0; i < RecyclerViewAdapterH.this.weather_icons_images.size(); i++) {
                if (str != null && str.equals(RecyclerViewAdapterH.this.weather_icons.get(i))) {
                    this.icon.setImageResource(((Integer) RecyclerViewAdapterH.this.weather_icons_images.get(i)).intValue());
                }
            }
        }

        public void addWeatherIcons() {
            RecyclerViewAdapterH.this.weather_icons.add("a01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a01d));
            RecyclerViewAdapterH.this.weather_icons.add("a01n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a01n));
            RecyclerViewAdapterH.this.weather_icons.add("a02d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a02d));
            RecyclerViewAdapterH.this.weather_icons.add("a02n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a02n));
            RecyclerViewAdapterH.this.weather_icons.add("a03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a03d));
            RecyclerViewAdapterH.this.weather_icons.add("a03n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a03n));
            RecyclerViewAdapterH.this.weather_icons.add("a04d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a04d));
            RecyclerViewAdapterH.this.weather_icons.add("a04n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a04n));
            RecyclerViewAdapterH.this.weather_icons.add("a05d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a05d));
            RecyclerViewAdapterH.this.weather_icons.add("a05n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a05n));
            RecyclerViewAdapterH.this.weather_icons.add("a06d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a06d));
            RecyclerViewAdapterH.this.weather_icons.add("a06n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.a06n));
            RecyclerViewAdapterH.this.weather_icons.add("c01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c01d));
            RecyclerViewAdapterH.this.weather_icons.add("c01n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c01n));
            RecyclerViewAdapterH.this.weather_icons.add("c02d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c02d));
            RecyclerViewAdapterH.this.weather_icons.add("c02n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c02n));
            RecyclerViewAdapterH.this.weather_icons.add("c03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c03d));
            RecyclerViewAdapterH.this.weather_icons.add("c03n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c03n));
            RecyclerViewAdapterH.this.weather_icons.add("c03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c04d));
            RecyclerViewAdapterH.this.weather_icons.add("c04n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.c04n));
            RecyclerViewAdapterH.this.weather_icons.add("d01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.d01d));
            RecyclerViewAdapterH.this.weather_icons.add("d01n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.d01n));
            RecyclerViewAdapterH.this.weather_icons.add("d02d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.d02d));
            RecyclerViewAdapterH.this.weather_icons.add("d02n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.d02n));
            RecyclerViewAdapterH.this.weather_icons.add("d03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.d03d));
            RecyclerViewAdapterH.this.weather_icons.add("d03n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.d03n));
            RecyclerViewAdapterH.this.weather_icons.add("f01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.f01d));
            RecyclerViewAdapterH.this.weather_icons.add("f01n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.f01n));
            RecyclerViewAdapterH.this.weather_icons.add("r01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r01d));
            RecyclerViewAdapterH.this.weather_icons.add("r01n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r01n));
            RecyclerViewAdapterH.this.weather_icons.add("r02d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r02d));
            RecyclerViewAdapterH.this.weather_icons.add("r02n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r02n));
            RecyclerViewAdapterH.this.weather_icons.add("r03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r03d));
            RecyclerViewAdapterH.this.weather_icons.add("r03n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r03n));
            RecyclerViewAdapterH.this.weather_icons.add("r04d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r04d));
            RecyclerViewAdapterH.this.weather_icons.add("r04d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r04n));
            RecyclerViewAdapterH.this.weather_icons.add("r05d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r05d));
            RecyclerViewAdapterH.this.weather_icons.add("r05n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r05n));
            RecyclerViewAdapterH.this.weather_icons.add("r04d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r06d));
            RecyclerViewAdapterH.this.weather_icons.add("r06n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.r06n));
            RecyclerViewAdapterH.this.weather_icons.add("s01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s01d));
            RecyclerViewAdapterH.this.weather_icons.add("s01n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s01n));
            RecyclerViewAdapterH.this.weather_icons.add("s02d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s02d));
            RecyclerViewAdapterH.this.weather_icons.add("s02n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s02n));
            RecyclerViewAdapterH.this.weather_icons.add("s03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s03d));
            RecyclerViewAdapterH.this.weather_icons.add("s03n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s03n));
            RecyclerViewAdapterH.this.weather_icons.add("s04d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s04d));
            RecyclerViewAdapterH.this.weather_icons.add("s04n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s04n));
            RecyclerViewAdapterH.this.weather_icons.add("s05d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s05d));
            RecyclerViewAdapterH.this.weather_icons.add("s05n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s05n));
            RecyclerViewAdapterH.this.weather_icons.add("s06d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s06d));
            RecyclerViewAdapterH.this.weather_icons.add("s06n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.s06n));
            RecyclerViewAdapterH.this.weather_icons.add("t01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t01d));
            RecyclerViewAdapterH.this.weather_icons.add("t01d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t01n));
            RecyclerViewAdapterH.this.weather_icons.add("t02d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t02d));
            RecyclerViewAdapterH.this.weather_icons.add("t02n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t02n));
            RecyclerViewAdapterH.this.weather_icons.add("t03d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t03d));
            RecyclerViewAdapterH.this.weather_icons.add("t03n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t03n));
            RecyclerViewAdapterH.this.weather_icons.add("t04d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t04d));
            RecyclerViewAdapterH.this.weather_icons.add("t04n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t04n));
            RecyclerViewAdapterH.this.weather_icons.add("t05d");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t05d));
            RecyclerViewAdapterH.this.weather_icons.add("t05n");
            RecyclerViewAdapterH.this.weather_icons_images.add(Integer.valueOf(R.drawable.t05n));
        }

        public void imageDefiner(DataH dataH) {
            addWeatherIcons();
            String icon2 = dataH.getWeather().getIcon();
            for (int i = 0; i < RecyclerViewAdapterH.this.weather_icons_images.size(); i++) {
                if (icon2.equals(RecyclerViewAdapterH.this.weather_icons.get(i))) {
                    this.icon.setImageResource(((Integer) RecyclerViewAdapterH.this.weather_icons_images.get(i)).intValue());
                }
            }
        }
    }
}
