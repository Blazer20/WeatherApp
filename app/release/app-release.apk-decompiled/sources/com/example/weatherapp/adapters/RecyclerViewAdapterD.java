package com.example.weatherapp.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.models.daily.DataD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterD extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataD> data = new ArrayList();
    /* access modifiers changed from: private */
    public int index;
    /* access modifiers changed from: private */
    public WeatherDataDao weatherDataDaoD;
    private List<WeatherData> weatherList;
    private List<WeatherData> weatherOutputListD;
    /* access modifiers changed from: private */
    public ArrayList<String> weather_icons = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<Integer> weather_icons_images = new ArrayList<>();

    static /* synthetic */ int access$108(RecyclerViewAdapterD recyclerViewAdapterD) {
        int i = recyclerViewAdapterD.index;
        recyclerViewAdapterD.index = i + 1;
        return i;
    }

    public RecyclerViewAdapterD(WeatherDataDao weatherDataDao, MainActivity mainActivity) {
        this.context = mainActivity;
        this.weatherDataDaoD = weatherDataDao;
        this.weatherList = new ArrayList();
        this.weatherOutputListD = new ArrayList();
    }

    public void setItems(List<DataD> list) {
        this.data.clear();
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(List<DataD> list) {
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    public void addOfflineItems(List<WeatherData> list) {
        this.weatherList.addAll(list);
        this.weatherOutputListD.addAll(this.weatherDataDaoD.getAllByIndD());
        notifyDataSetChanged();
    }

    public void setOfflineItems(List<WeatherData> list) {
        this.weatherList.clear();
        this.weatherOutputListD.clear();
        this.weatherList.addAll(list);
        this.weatherOutputListD.addAll(this.weatherDataDaoD.getAllByIndD());
        notifyDataSetChanged();
    }

    public boolean isOnline(Context context2) {
        return ((ConnectivityManager) context2.getApplicationContext().getSystemService("connectivity")).getActiveNetwork() != null;
    }

    public void outPutList() {
        for (int i = 0; i < this.weatherList.size(); i++) {
            if (this.weatherList.get(i).ind == 2) {
                this.weatherOutputListD.add(this.weatherList.get(i));
            }
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item1, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if (isOnline(this.context)) {
            myViewHolder.bindView(this.data.get(i));
        } else {
            myViewHolder.loadData(this.weatherOutputListD.get(i));
        }
    }

    public int getItemCount() {
        if (isOnline(this.context)) {
            return this.data.size();
        }
        return this.weatherOutputListD.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText date;
        ImageView day;
        EditText degreeH;
        EditText degreeL;
        ImageView night;

        public MyViewHolder(View view) {
            super(view);
            this.date = (EditText) view.findViewById(R.id.time);
            this.degreeL = (EditText) view.findViewById(R.id.degreeL);
            this.degreeH = (EditText) view.findViewById(R.id.degreeH);
            this.day = (ImageView) view.findViewById(R.id.day);
            this.night = (ImageView) view.findViewById(R.id.night);
        }

        public void bindView(DataD dataD) {
            this.date.setText(new SimpleDateFormat("EEE, MMM d, ''yy").format(dataD.getDate()));
            EditText editText = this.degreeL;
            editText.setText(((int) dataD.getMin_temp()) + "℃");
            EditText editText2 = this.degreeH;
            editText2.setText(((int) dataD.getMax_temp()) + "℃");
            saveData(dataD);
            imageDefiner(dataD);
        }

        public void saveData(DataD dataD) {
            long j;
            if (RecyclerViewAdapterD.this.weatherDataDaoD.getCountD() < 4) {
                WeatherData weatherData = new WeatherData();
                weatherData.dbDailyWeekday = this.date.getText().toString();
                weatherData.dbDailyDayIcon = dataD.getWeather().getIcon();
                weatherData.dbDailyNightIcon = dataD.getWeather().getIcon();
                weatherData.dbDailyMaxDegree = this.degreeH.getText().toString();
                weatherData.dbDailyMinDegree = this.degreeL.getText().toString();
                weatherData.ind = 2;
                Log.e("dInsert", "Data: " + weatherData.dbDailyWeekday + ", " + weatherData.dbDailyDayIcon + ", " + weatherData.dbDailyNightIcon + ", " + weatherData.dbDailyMinDegree + ", " + weatherData.dbDailyMaxDegree + ", " + RecyclerViewAdapterD.this.weatherDataDaoD.insert(weatherData));
                return;
            }
            List<WeatherData> allData = RecyclerViewAdapterD.this.weatherDataDaoD.getAllData();
            List<Long> allIdD = RecyclerViewAdapterD.this.weatherDataDaoD.getAllIdD();
            int i = 0;
            while (true) {
                if (i >= allData.size()) {
                    j = 0;
                    break;
                }
                if (RecyclerViewAdapterD.this.index >= 4) {
                    int unused = RecyclerViewAdapterD.this.index = 0;
                } else if (allIdD.get(RecyclerViewAdapterD.this.index).longValue() == allData.get(i).id) {
                    j = allIdD.get(RecyclerViewAdapterD.this.index).longValue();
                    RecyclerViewAdapterD.access$108(RecyclerViewAdapterD.this);
                    break;
                }
                i++;
            }
            if (j != 0) {
                WeatherData byId = RecyclerViewAdapterD.this.weatherDataDaoD.getById(allData.get(RecyclerViewAdapterD.this.index).id);
                byId.dbDailyWeekday = this.date.getText().toString();
                byId.dbDailyDayIcon = dataD.getWeather().getIcon();
                byId.dbDailyNightIcon = dataD.getWeather().getIcon();
                byId.dbDailyMaxDegree = this.degreeH.getText().toString();
                byId.dbDailyMinDegree = this.degreeL.getText().toString();
                RecyclerViewAdapterD.this.weatherDataDaoD.update(byId);
                Log.e("dUpdate", "Data: " + byId.dbDailyWeekday + ", " + byId.dbDailyDayIcon + ", " + byId.dbDailyNightIcon + ", " + byId.dbDailyMinDegree + ", " + byId.dbDailyMaxDegree);
            }
        }

        public void loadData(WeatherData weatherData) {
            this.date.setText(weatherData.dbDailyWeekday);
            this.degreeL.setText(weatherData.dbDailyMinDegree);
            this.degreeH.setText(weatherData.dbDailyMaxDegree);
            addOfflineWeatherIcons(weatherData);
        }

        public void addOfflineWeatherIcons(WeatherData weatherData) {
            addWeatherIcons();
            String str = weatherData.dbDailyDayIcon;
            for (int i = 0; i < RecyclerViewAdapterD.this.weather_icons_images.size(); i++) {
                if (str.equals(RecyclerViewAdapterD.this.weather_icons.get(i))) {
                    this.day.setImageResource(((Integer) RecyclerViewAdapterD.this.weather_icons_images.get(i)).intValue());
                    this.night.setImageResource(((Integer) RecyclerViewAdapterD.this.weather_icons_images.get(i + 1)).intValue());
                }
            }
        }

        public void addWeatherIcons() {
            RecyclerViewAdapterD.this.weather_icons.add("a01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a01d));
            RecyclerViewAdapterD.this.weather_icons.add("a01n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a01n));
            RecyclerViewAdapterD.this.weather_icons.add("a02d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a02d));
            RecyclerViewAdapterD.this.weather_icons.add("a02n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a02n));
            RecyclerViewAdapterD.this.weather_icons.add("a03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a03d));
            RecyclerViewAdapterD.this.weather_icons.add("a03n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a03n));
            RecyclerViewAdapterD.this.weather_icons.add("a04d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a04d));
            RecyclerViewAdapterD.this.weather_icons.add("a04n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a04n));
            RecyclerViewAdapterD.this.weather_icons.add("a05d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a05d));
            RecyclerViewAdapterD.this.weather_icons.add("a05n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a05n));
            RecyclerViewAdapterD.this.weather_icons.add("a06d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a06d));
            RecyclerViewAdapterD.this.weather_icons.add("a06n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.a06n));
            RecyclerViewAdapterD.this.weather_icons.add("c01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c01d));
            RecyclerViewAdapterD.this.weather_icons.add("c01n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c01n));
            RecyclerViewAdapterD.this.weather_icons.add("c02d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c02d));
            RecyclerViewAdapterD.this.weather_icons.add("c02n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c02n));
            RecyclerViewAdapterD.this.weather_icons.add("c03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c03d));
            RecyclerViewAdapterD.this.weather_icons.add("c03n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c03n));
            RecyclerViewAdapterD.this.weather_icons.add("c03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c04d));
            RecyclerViewAdapterD.this.weather_icons.add("c04n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.c04n));
            RecyclerViewAdapterD.this.weather_icons.add("d01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.d01d));
            RecyclerViewAdapterD.this.weather_icons.add("d01n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.d01n));
            RecyclerViewAdapterD.this.weather_icons.add("d02d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.d02d));
            RecyclerViewAdapterD.this.weather_icons.add("d02n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.d02n));
            RecyclerViewAdapterD.this.weather_icons.add("d03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.d03d));
            RecyclerViewAdapterD.this.weather_icons.add("d03n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.d03n));
            RecyclerViewAdapterD.this.weather_icons.add("f01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.f01d));
            RecyclerViewAdapterD.this.weather_icons.add("f01n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.f01n));
            RecyclerViewAdapterD.this.weather_icons.add("r01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r01d));
            RecyclerViewAdapterD.this.weather_icons.add("r01n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r01n));
            RecyclerViewAdapterD.this.weather_icons.add("r02d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r02d));
            RecyclerViewAdapterD.this.weather_icons.add("r02n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r02n));
            RecyclerViewAdapterD.this.weather_icons.add("r03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r03d));
            RecyclerViewAdapterD.this.weather_icons.add("r03n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r03n));
            RecyclerViewAdapterD.this.weather_icons.add("r04d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r04d));
            RecyclerViewAdapterD.this.weather_icons.add("r04d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r04n));
            RecyclerViewAdapterD.this.weather_icons.add("r05d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r05d));
            RecyclerViewAdapterD.this.weather_icons.add("r05n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r05n));
            RecyclerViewAdapterD.this.weather_icons.add("r04d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r06d));
            RecyclerViewAdapterD.this.weather_icons.add("r06n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.r06n));
            RecyclerViewAdapterD.this.weather_icons.add("s01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s01d));
            RecyclerViewAdapterD.this.weather_icons.add("s01n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s01n));
            RecyclerViewAdapterD.this.weather_icons.add("s02d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s02d));
            RecyclerViewAdapterD.this.weather_icons.add("s02n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s02n));
            RecyclerViewAdapterD.this.weather_icons.add("s03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s03d));
            RecyclerViewAdapterD.this.weather_icons.add("s03n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s03n));
            RecyclerViewAdapterD.this.weather_icons.add("s04d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s04d));
            RecyclerViewAdapterD.this.weather_icons.add("s04n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s04n));
            RecyclerViewAdapterD.this.weather_icons.add("s05d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s05d));
            RecyclerViewAdapterD.this.weather_icons.add("s05n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s05n));
            RecyclerViewAdapterD.this.weather_icons.add("s06d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s06d));
            RecyclerViewAdapterD.this.weather_icons.add("s06n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.s06n));
            RecyclerViewAdapterD.this.weather_icons.add("t01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t01d));
            RecyclerViewAdapterD.this.weather_icons.add("t01d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t01n));
            RecyclerViewAdapterD.this.weather_icons.add("t02d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t02d));
            RecyclerViewAdapterD.this.weather_icons.add("t02n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t02n));
            RecyclerViewAdapterD.this.weather_icons.add("t03d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t03d));
            RecyclerViewAdapterD.this.weather_icons.add("t03n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t03n));
            RecyclerViewAdapterD.this.weather_icons.add("t04d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t04d));
            RecyclerViewAdapterD.this.weather_icons.add("t04n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t04n));
            RecyclerViewAdapterD.this.weather_icons.add("t05d");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t05d));
            RecyclerViewAdapterD.this.weather_icons.add("t05n");
            RecyclerViewAdapterD.this.weather_icons_images.add(Integer.valueOf(R.drawable.t05n));
        }

        public void imageDefiner(DataD dataD) {
            addWeatherIcons();
            String icon = dataD.getWeather().getIcon();
            for (int i = 0; i < RecyclerViewAdapterD.this.weather_icons_images.size(); i++) {
                if (icon.equals(RecyclerViewAdapterD.this.weather_icons.get(i))) {
                    this.day.setImageResource(((Integer) RecyclerViewAdapterD.this.weather_icons_images.get(i)).intValue());
                    this.night.setImageResource(((Integer) RecyclerViewAdapterD.this.weather_icons_images.get(i + 1)).intValue());
                }
            }
        }
    }
}
