package com.example.weatherapp;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.api.ApiClient;
import com.example.weatherapp.models.current.Current;
import com.example.weatherapp.models.current.DataC;
import com.example.weatherapp.models.daily.Forecast;
import com.example.weatherapp.models.hourly.Hourly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LocationListener {
    public static final String API_KEY = "e889e7670a9344f49e10d214d8c03a00";
    private Button choose;
    private List<Address> cities;
    private String city;
    /* access modifiers changed from: private */
    public EditText current_degree;
    /* access modifiers changed from: private */
    public EditText current_location;
    private ImageView current_weather_icon;
    private WeatherDataBase database;
    private boolean enter = false;
    private boolean isEnabled_GPS = false;
    private boolean isEnabled_Network = false;
    private Double lat;
    private LocationManager locationManager;
    private Double lon;
    private BroadcastReceiver receiver;
    /* access modifiers changed from: private */
    public RecyclerViewAdapterD recyclerViewAdapterD;
    /* access modifiers changed from: private */
    public RecyclerViewAdapterH recyclerViewAdapterH;
    private RecyclerView recyclerView_day;
    private RecyclerView recyclerView_hour;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;
    private WeatherData weatherData;
    private WeatherDataDao weatherDataDao;
    private ArrayList<String> weather_icons;
    private ArrayList<Integer> weather_icons_images;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        WeatherDataBase instance = WeatherDataBase.getInstance(getApplicationContext());
        this.database = instance;
        this.weatherDataDao = instance.weatherDataDao();
        LocationManager locationManager2 = (LocationManager) getSystemService("location");
        this.locationManager = locationManager2;
        this.isEnabled_GPS = locationManager2.isProviderEnabled("gps");
        this.isEnabled_Network = this.locationManager.isProviderEnabled("network");
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.locationManager.requestLocationUpdates("gps", 1000, 0.0f, this);
            SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            this.swipeRefreshLayout = swipeRefreshLayout2;
            swipeRefreshLayout2.setOnRefreshListener(this);
            this.swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.every_day);
            this.recyclerView_day = recyclerView;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            this.recyclerView_day.setLayoutManager(new LinearLayoutManager(this, 0, false));
            this.recyclerView_day.setHasFixedSize(true);
            RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.every_hour);
            this.recyclerView_hour = recyclerView2;
            recyclerView2.setItemAnimator(new DefaultItemAnimator());
            this.recyclerView_hour.setLayoutManager(new LinearLayoutManager(this, 0, false));
            this.recyclerView_hour.setHasFixedSize(true);
            RecyclerViewAdapterD recyclerViewAdapterD2 = new RecyclerViewAdapterD(this.weatherDataDao, this);
            this.recyclerViewAdapterD = recyclerViewAdapterD2;
            this.recyclerView_day.setAdapter(recyclerViewAdapterD2);
            RecyclerViewAdapterH recyclerViewAdapterH2 = new RecyclerViewAdapterH(this.weatherDataDao, this);
            this.recyclerViewAdapterH = recyclerViewAdapterH2;
            this.recyclerView_hour.setAdapter(recyclerViewAdapterH2);
            this.current_location = (EditText) findViewById(R.id.city);
            this.current_degree = (EditText) findViewById(R.id.current_degree);
            this.current_weather_icon = (ImageView) findViewById(R.id.current_weather_icon);
            Button button = (Button) findViewById(R.id.choose);
            this.choose = button;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MainActivity.this.startActivityForResult(new Intent(MainActivity.this.getBaseContext(), Countries.class), 1);
                }
            });
            this.weather_icons_images = new ArrayList<>();
            this.weather_icons = new ArrayList<>();
            return;
        }
        requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.INTERNET"}, 10);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (intent != null) {
            this.city = intent.getStringExtra("city");
            Log.e("City3", "" + this.city);
            LoadJson(true, this.city);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.isEnabled_GPS && this.isEnabled_Network) {
            LoadJson(true, this.city);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) "GPS is settings");
        builder.setMessage((CharSequence) "GPS is not enabled. Do you want to go to settings menu?");
        builder.setPositiveButton((CharSequence) "Settings", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void onLocationChanged(Location location) {
        if (location != null && location.getProvider().equals("gps")) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            this.lat = Double.valueOf(location.getLatitude());
            this.lon = Double.valueOf(location.getLongitude());
            try {
                List<Address> fromLocation = geocoder.getFromLocation(this.lat.doubleValue(), this.lon.doubleValue(), 1);
                this.cities = fromLocation;
                this.city = fromLocation.get(0).getLocality();
                Log.e("City", "" + this.city);
                if (!this.enter) {
                    LoadJson(true, this.city);
                    this.enter = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onProviderDisabled(String str) {
        if (!this.locationManager.isProviderEnabled("gps")) {
            showSettingsAlert();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isOnline() {
        return ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetwork() != null;
    }

    public void LoadJson(final boolean z, String str) {
        this.swipeRefreshLayout.setRefreshing(true);
        Toast.makeText(this, "Location: " + this.lat + this.lon, 0).show();
        if (isOnline()) {
            ApiClient.getInstance().getApiInterface().getCurrent(API_KEY, str).enqueue(new Callback<Current>() {
                public void onResponse(Call<Current> call, Response<Current> response) {
                    if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                        MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                    Iterator<DataC> it = response.body().getData().iterator();
                    if (it.hasNext()) {
                        DataC next = it.next();
                        MainActivity.this.current_location.setText(next.getCityName());
                        EditText access$100 = MainActivity.this.current_degree;
                        access$100.setText(((int) next.getTemp()) + "℃");
                        MainActivity.this.saveData(next);
                    }
                    MainActivity.this.weatherIconDef(response);
                    MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                }

                public void onFailure(Call<Current> call, Throwable th) {
                    MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                    Log.e("Error!", th.getMessage());
                }
            });
            ApiClient.getInstance().getApiInterface().getHourly(API_KEY, str, 12).enqueue(new Callback<Hourly>() {
                public void onResponse(Call<Hourly> call, Response<Hourly> response) {
                    if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                        MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                    if (z) {
                        MainActivity.this.recyclerViewAdapterH.setItems(response.body().getData());
                    } else {
                        MainActivity.this.recyclerViewAdapterH.addItems(response.body().getData());
                    }
                    MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                }

                public void onFailure(Call<Hourly> call, Throwable th) {
                    MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                    Log.e("Error!", th.getMessage());
                }
            });
            ApiClient.getInstance().getApiInterface().getForecast(7, str, API_KEY).enqueue(new Callback<Forecast>() {
                public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                    if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                        MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                    if (z) {
                        MainActivity.this.recyclerViewAdapterD.setItems(response.body().getData());
                    } else {
                        MainActivity.this.recyclerViewAdapterD.addItems(response.body().getData());
                    }
                    MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                }

                public void onFailure(Call<Forecast> call, Throwable th) {
                    MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                    Log.e("Error!", th.getMessage());
                }
            });
            return;
        }
        loadData(z);
        Toast.makeText(this, "Error! No internet connection", 1).show();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    public void saveData(DataC dataC) {
        if (this.weatherDataDao.getAllData().size() == 0) {
            WeatherData weatherData2 = new WeatherData();
            this.weatherData = weatherData2;
            weatherData2.dbCurrentLocation = this.current_location.getText().toString();
            this.weatherData.dbCurrentDegree = this.current_degree.getText().toString();
            this.weatherData.dbCurrentWeatherIcon = dataC.getWeather().getIcon();
            this.weatherDataDao.insert(this.weatherData);
            Log.e("Insert", "Data: " + this.weatherData.dbCurrentLocation + ", " + this.weatherData.dbCurrentDegree + " " + this.weatherData.dbCurrentWeatherIcon + ", ");
        } else {
            WeatherData weatherData3 = this.weatherDataDao.getAllData().get(0);
            weatherData3.dbCurrentLocation = this.current_location.getText().toString();
            weatherData3.dbCurrentDegree = this.current_degree.getText().toString();
            weatherData3.dbCurrentWeatherIcon = dataC.getWeather().getIcon();
            this.weatherDataDao.update(weatherData3);
        }
        for (WeatherData next : this.weatherDataDao.getAllData()) {
            Log.e("LoadAllData", "Data: " + next.dbCurrentLocation + ", " + next.dbCurrentDegree + " " + next.dbCurrentWeatherIcon + "\n" + next.dbHourlyTime + ", " + next.dbHourlyDegree + ", " + next.dbHourlyIcon + "\n" + next.dbDailyWeekday + ", " + next.dbDailyMaxDegree + ", " + next.dbDailyMinDegree + ", " + next.dbDailyDayIcon + ", " + next.dbDailyNightIcon);
        }
    }

    public void loadData(boolean z) {
        List<WeatherData> allData = this.weatherDataDao.getAllData();
        Iterator<WeatherData> it = allData.iterator();
        if (it.hasNext()) {
            WeatherData next = it.next();
            this.current_location.setText(next.dbCurrentLocation);
            this.current_degree.setText(next.dbCurrentDegree);
            addOfflineWeatherIcons(next);
            Log.e("LoadAllData", "Data: " + next.dbCurrentLocation + ", " + next.dbCurrentDegree + " " + next.dbCurrentWeatherIcon + "\n" + next.dbHourlyTime + ", " + next.dbHourlyDegree + ", " + next.dbHourlyIcon + "\n" + next.dbDailyWeekday + ", " + next.dbDailyMaxDegree + ", " + next.dbDailyMinDegree + ", " + next.dbDailyDayIcon + ", " + next.dbDailyNightIcon);
        }
        if (z) {
            this.recyclerViewAdapterH.setOfflineItems(allData);
            this.recyclerViewAdapterD.setOfflineItems(allData);
            return;
        }
        this.recyclerViewAdapterH.addOfflineItems(allData);
        this.recyclerViewAdapterD.addOfflineItems(allData);
    }

    public void addWeatherIcons() {
        this.weather_icons.add("a01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a01d));
        this.weather_icons.add("a01n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a01n));
        this.weather_icons.add("a02d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a02d));
        this.weather_icons.add("a02n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a02n));
        this.weather_icons.add("a03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a03d));
        this.weather_icons.add("a03n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a03n));
        this.weather_icons.add("a04d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a04d));
        this.weather_icons.add("a04n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a04n));
        this.weather_icons.add("a05d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a05d));
        this.weather_icons.add("a05n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a05n));
        this.weather_icons.add("a06d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a06d));
        this.weather_icons.add("a06n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.a06n));
        this.weather_icons.add("c01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c01d));
        this.weather_icons.add("c01n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c01n));
        this.weather_icons.add("c02d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c02d));
        this.weather_icons.add("c02n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c02n));
        this.weather_icons.add("c03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c03d));
        this.weather_icons.add("c03n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c03n));
        this.weather_icons.add("c03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c04d));
        this.weather_icons.add("c04n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.c04n));
        this.weather_icons.add("d01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.d01d));
        this.weather_icons.add("d01n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.d01n));
        this.weather_icons.add("d02d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.d02d));
        this.weather_icons.add("d02n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.d02n));
        this.weather_icons.add("d03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.d03d));
        this.weather_icons.add("d03n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.d03n));
        this.weather_icons.add("f01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.f01d));
        this.weather_icons.add("f01n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.f01n));
        this.weather_icons.add("r01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r01d));
        this.weather_icons.add("r01n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r01n));
        this.weather_icons.add("r02d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r02d));
        this.weather_icons.add("r02n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r02n));
        this.weather_icons.add("r03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r03d));
        this.weather_icons.add("r03n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r03n));
        this.weather_icons.add("r04d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r04d));
        this.weather_icons.add("r04d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r04n));
        this.weather_icons.add("r05d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r05d));
        this.weather_icons.add("r05n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r05n));
        this.weather_icons.add("r04d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r06d));
        this.weather_icons.add("r06n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.r06n));
        this.weather_icons.add("s01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s01d));
        this.weather_icons.add("s01n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s01n));
        this.weather_icons.add("s02d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s02d));
        this.weather_icons.add("s02n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s02n));
        this.weather_icons.add("s03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s03d));
        this.weather_icons.add("s03n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s03n));
        this.weather_icons.add("s04d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s04d));
        this.weather_icons.add("s04n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s04n));
        this.weather_icons.add("s05d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s05d));
        this.weather_icons.add("s05n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s05n));
        this.weather_icons.add("s06d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s06d));
        this.weather_icons.add("s06n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.s06n));
        this.weather_icons.add("t01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t01d));
        this.weather_icons.add("t01d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t01n));
        this.weather_icons.add("t02d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t02d));
        this.weather_icons.add("t02n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t02n));
        this.weather_icons.add("t03d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t03d));
        this.weather_icons.add("t03n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t03n));
        this.weather_icons.add("t04d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t04d));
        this.weather_icons.add("t04n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t04n));
        this.weather_icons.add("t05d");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t05d));
        this.weather_icons.add("t05n");
        this.weather_icons_images.add(Integer.valueOf(R.drawable.t05n));
    }

    public void addOfflineWeatherIcons(WeatherData weatherData2) {
        addWeatherIcons();
        String str = weatherData2.dbCurrentWeatherIcon;
        for (int i = 0; i < this.weather_icons_images.size(); i++) {
            if (str.equals(this.weather_icons.get(i))) {
                this.current_weather_icon.setImageResource(this.weather_icons_images.get(i).intValue());
            }
        }
    }

    public void weatherIconDef(Response<Current> response) {
        addWeatherIcons();
        for (DataC weather : response.body().getData()) {
            String icon = weather.getWeather().getIcon();
            for (int i = 0; i < this.weather_icons_images.size(); i++) {
                if (icon.equals(this.weather_icons.get(i))) {
                    this.current_weather_icon.setImageResource(this.weather_icons_images.get(i).intValue());
                }
            }
        }
    }

    public void onRefresh() {
        LoadJson(true, this.city);
    }
}
