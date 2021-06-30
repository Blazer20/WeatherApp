package com.example.weatherapp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.icu.util.LocaleData;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.adapters.RecyclerViewAdapterHoursCollapse;
import com.example.weatherapp.api.ApiClient;
import com.example.weatherapp.customViews.TouchableMotionLayout;
import com.example.weatherapp.models.forecast.Current;
import com.example.weatherapp.models.forecast.Data;
import com.example.weatherapp.models.forecast.Hour;
import com.example.weatherapp.roomDB.weatherData.HourlyData;
import com.example.weatherapp.roomDB.weatherData.HourlyDataDao;
import com.example.weatherapp.roomDB.weatherData.WeatherData;
import com.example.weatherapp.roomDB.weatherData.WeatherDataBase;
import com.example.weatherapp.roomDB.weatherData.WeatherDataDao;
import com.example.weatherapp.viewmodels.StateViewModel;
import com.example.weatherapp.viewmodels.WeatherViewModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LocationListener, View.OnClickListener, View.OnTouchListener, MotionLayout.TransitionListener, TouchableMotionLayout.OnMotionEventListener {

    public static final String TAG = "MainFragment";

    private StateViewModel stateViewModel;
    private WeatherViewModel weatherViewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;
    private TouchableMotionLayout motionLayout;
    private CardView cardDraw;
    private ConstraintLayout cardView;
    private ScrollView expandedView;
    private Button collapseButton, collapseButtonBack;
    private ImageView currentWeatherIcon, currentWeatherIconLow, collapseCurrentWeatherIcon, imageView, longLine, up, down, thermometer, arc;
    private TextView currentDegree, currentLocation, currentWeatherDescription, currentDegreeDay,
            currentDegreeNight, currentDate, currentWeatherDegreeLow, currentTime, sunriseTime,
            sunsetTime, windValue, tomorrow, nextFewDays, collapseCurrentDescription, collapseCurrentWeatherDegree,
            collapseCurrentDegreeDay, collapseCurrentDegreeNight, collapseSunriseTime, collapseWindValue, collapseSunsetTime,
            collapseTomorrow, collapseNextFewDays, collapseCurrentDate, collapseCurrentLocation, collapseHumidityValue,
            collapsePrecipitationValue, collapsePressureValue, collapseCloudValue, collapseUvValue, collapseVisionValue, currentDay,
            currentNight, degreeSymbolD, degreeSymbolN, currentCelsius;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterHoursCollapse recyclerViewAdapterHoursCollapse;

    private WeatherData weatherData;
    private LocationManager locationManager;
    private ArrayList<Point> points;

    private String mLat, mLon, q, language, lat, lon;
    private boolean enter = false, checker = false;
    private int cardViewHeight, distance = 0, oldDistance = 1, myCountPage = 1;
    private static int myCurrentPage;
    private static boolean isHere = false;
    private float dX, dY, newX, newY, currentX, currentY;

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        myCurrentPage = 1;
        return mainFragment;
    }

    public static MainFragment newInstance(String lat, String lon, int countPage) {
        MainFragment newFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("lon", lon);
        bundle.putInt("countPage", countPage);
        newFragment.setArguments(bundle);
        isHere = true;
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLat = getArguments().getString("lat");
            mLon = getArguments().getString("lon");
            myCountPage = getArguments().getInt("countPage");
            Log.e(TAG, String.valueOf(myCountPage));
        }
        stateViewModel = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        observeForecast();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setRetainInstance(true);
        return view;
    }

    public void loadBackgrounds(@NonNull View view) {
        String mCurrentDate = DateFormat.getTimeInstance(DateFormat.SHORT).
                format(Calendar.getInstance().getTime());
        if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 9
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 12) {
            view.setBackgroundResource(R.drawable.background_morning);
            currentLocation.setTextColor(Color.parseColor("#333333"));
            currentDate.setTextColor(Color.parseColor("#333333"));
            currentDegree.setTextColor(Color.parseColor("#333333"));
            currentWeatherDescription.setTextColor(Color.parseColor("#333333"));
            currentDegreeDay.setTextColor(Color.parseColor("#333333"));
            currentDegreeNight.setTextColor(Color.parseColor("#333333"));
            currentDay.setTextColor(Color.parseColor("#333333"));
            currentNight.setTextColor(Color.parseColor("#333333"));
            degreeSymbolD.setTextColor(Color.parseColor("#333333"));
            degreeSymbolN.setTextColor(Color.parseColor("#333333"));
            currentCelsius.setTextColor(Color.parseColor("#333333"));
            up.setBackgroundResource(R.drawable.ic_up);
            down.setBackgroundResource(R.drawable.ic_down);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 12
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 15) {
            view.setBackgroundResource(R.drawable.background_day);
            currentLocation.setTextColor(Color.parseColor("#333333"));
            currentDate.setTextColor(Color.parseColor("#333333"));
            currentDegree.setTextColor(Color.parseColor("#333333"));
            currentWeatherDescription.setTextColor(Color.parseColor("#333333"));
            currentDegreeDay.setTextColor(Color.parseColor("#333333"));
            currentDegreeNight.setTextColor(Color.parseColor("#333333"));
            currentDay.setTextColor(Color.parseColor("#333333"));
            currentNight.setTextColor(Color.parseColor("#333333"));
            degreeSymbolD.setTextColor(Color.parseColor("#333333"));
            degreeSymbolN.setTextColor(Color.parseColor("#333333"));
            currentCelsius.setTextColor(Color.parseColor("#333333"));
            up.setBackgroundResource(R.drawable.ic_up);
            down.setBackgroundResource(R.drawable.ic_down);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 15
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 18) {
            view.setBackgroundResource(R.drawable.background_evening);
            currentLocation.setTextColor(Color.parseColor("#FFFFFF"));
            currentDate.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegree.setTextColor(Color.parseColor("#FFFFFF"));
            currentWeatherDescription.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeNight.setTextColor(Color.parseColor("#FFFFFF"));
            currentDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentNight.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolD.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolN.setTextColor(Color.parseColor("#FFFFFF"));
            currentCelsius.setTextColor(Color.parseColor("#FFFFFF"));
            up.setBackgroundResource(R.drawable.ic_up_white);
            down.setBackgroundResource(R.drawable.ic_down_white);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 18
                && Integer.parseInt(mCurrentDate.substring(0, 2)) > 0) {
            view.setBackgroundResource(R.drawable.background_prenight);
            currentLocation.setTextColor(Color.parseColor("#FFFFFF"));
            currentDate.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegree.setTextColor(Color.parseColor("#FFFFFF"));
            currentWeatherDescription.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeNight.setTextColor(Color.parseColor("#FFFFFF"));
            currentDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentNight.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolD.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolN.setTextColor(Color.parseColor("#FFFFFF"));
            currentCelsius.setTextColor(Color.parseColor("#FFFFFF"));
            up.setBackgroundResource(R.drawable.ic_up_white);
            down.setBackgroundResource(R.drawable.ic_down_white);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 0
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 5) {
            view.setBackgroundResource(R.drawable.background_fullnight);
            currentLocation.setTextColor(Color.parseColor("#FFFFFF"));
            currentDate.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegree.setTextColor(Color.parseColor("#FFFFFF"));
            currentWeatherDescription.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeNight.setTextColor(Color.parseColor("#FFFFFF"));
            currentDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentNight.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolD.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolN.setTextColor(Color.parseColor("#FFFFFF"));
            currentCelsius.setTextColor(Color.parseColor("#FFFFFF"));
            up.setBackgroundResource(R.drawable.ic_up_white);
            down.setBackgroundResource(R.drawable.ic_down_white);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 5
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 9) {
            view.setBackgroundResource(R.drawable.background_premorning);
            currentLocation.setTextColor(Color.parseColor("#FFFFFF"));
            currentDate.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegree.setTextColor(Color.parseColor("#FFFFFF"));
            currentWeatherDescription.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentDegreeNight.setTextColor(Color.parseColor("#FFFFFF"));
            currentDay.setTextColor(Color.parseColor("#FFFFFF"));
            currentNight.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolD.setTextColor(Color.parseColor("#FFFFFF"));
            degreeSymbolN.setTextColor(Color.parseColor("#FFFFFF"));
            currentCelsius.setTextColor(Color.parseColor("#FFFFFF"));
            up.setBackgroundResource(R.drawable.ic_up_white);
            down.setBackgroundResource(R.drawable.ic_down_white);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupViews(view);
        loadBackgrounds(view);
        addPoints();
        motionLayout.setOnMotionEventListener(this);
        motionLayout.setTransitionListener(this);
        tomorrow.setOnClickListener(this);
        nextFewDays.setOnClickListener(this);
        collapseButton.setOnClickListener(this);
        collapseButtonBack.setOnClickListener(this);
        collapseTomorrow.setOnClickListener(this);
        collapseNextFewDays.setOnClickListener(this);
        cardView.setOnTouchListener(this);
        expandedView.setOnTouchListener(this);

    }

    private void initViews(@NonNull View view) {
        constraintLayout = view.findViewById(R.id.mainFragment);
        motionLayout = view.findViewById(R.id.motionCard);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_main);

        cardView = view.findViewById(R.id.lowerDraw);
        cardDraw = view.findViewById(R.id.cardDraw);
        cardDraw.setBackgroundResource(R.drawable.card_view_style);
        expandedView = view.findViewById(R.id.expandedView);
        currentLocation = view.findViewById(R.id.currentLocation);
        currentDate = view.findViewById(R.id.currentDate);
        currentDegree = view.findViewById(R.id.currentDegree);
        currentWeatherDescription = view.findViewById(R.id.currentDescription);
        currentWeatherIcon = view.findViewById(R.id.currentWeatherIcon);
        currentDegreeDay = view.findViewById(R.id.currentDegreeDay);
        currentDegreeNight = view.findViewById(R.id.currentDegreeNight);
        currentDay = view.findViewById(R.id.currentDay);
        currentNight = view.findViewById(R.id.currentNight);
        degreeSymbolD = view.findViewById(R.id.degreeSymbolD);
        degreeSymbolN = view.findViewById(R.id.degreeSymbolN);
        currentCelsius = view.findViewById(R.id.currentCelsius);
        up = view.findViewById(R.id.up);
        down = view.findViewById(R.id.down);
        thermometer = view.findViewById(R.id.thermometer);
        arc = view.findViewById(R.id.arc);
        currentWeatherIconLow = view.findViewById(R.id.currentWeatherIconLow);
        currentWeatherDegreeLow = view.findViewById(R.id.currentDegreeLow);
        currentTime = view.findViewById(R.id.timeLow);
        sunriseTime = view.findViewById(R.id.sunriseTime);
        windValue = view.findViewById(R.id.windValue);
        sunsetTime = view.findViewById(R.id.sunsetTime);
        imageView = view.findViewById(R.id.imageView);
        longLine = view.findViewById(R.id.longLine);

        collapseButton = view.findViewById(R.id.collapseButton);
        collapseButtonBack = view.findViewById(R.id.collapseButtonBack);

        collapseTomorrow = view.findViewById(R.id.collapseTomorrow);
        collapseNextFewDays = view.findViewById(R.id.collapseNextFewDays);
        collapseCurrentWeatherIcon = view.findViewById(R.id.collapseCurrentWeatherIcon);
        collapseCurrentWeatherDegree = view.findViewById(R.id.collapseCurrentDegree);
        collapseCurrentDegreeDay = view.findViewById(R.id.collapseCurrentDegreeDay);
        collapseCurrentDegreeNight = view.findViewById(R.id.collapseCurrentDegreeNight);
        collapseSunriseTime = view.findViewById(R.id.collapseSunriseTime);
        collapseWindValue = view.findViewById(R.id.collapseWindValue);
        collapseSunsetTime = view.findViewById(R.id.collapseSunsetTime);
        collapseCurrentDate = view.findViewById(R.id.collapseCurrentDate);
        collapseCurrentLocation = view.findViewById(R.id.collapseCurrentLocation);
        collapseCurrentDescription = view.findViewById(R.id.collapseCurrentDescription);
        tomorrow = view.findViewById(R.id.tomorrow);
        nextFewDays = view.findViewById(R.id.nextFewDays);

        recyclerView = view.findViewById(R.id.collapseHourly);

        collapseHumidityValue = view.findViewById(R.id.collapseHumidityValue);
        collapsePrecipitationValue = view.findViewById(R.id.collapsePrecipitationValue);
        collapsePressureValue = view.findViewById(R.id.collapsePressureValue);
        collapseCloudValue = view.findViewById(R.id.collapseCloudValue);
        collapseUvValue = view.findViewById(R.id.collapseUvValue);
        collapseVisionValue = view.findViewById(R.id.collapseVisionValue);
    }

    private void setupViews(@NonNull View view) {

        swipeRefreshLayout.setOnRefreshListener(this);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        language = Locale.getDefault().getLanguage();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapterHoursCollapse = new RecyclerViewAdapterHoursCollapse();
        recyclerView.setAdapter(recyclerViewAdapterHoursCollapse);

        points = new ArrayList<>();
    }

    public static void getCurrentPage(int position) {
        myCurrentPage = position + 1;
        Log.e(TAG, "position " + myCurrentPage);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(myCurrentPage == 1 && !isHere) {
            if (weatherViewModel.getWeatherDataDao().getCurrentLocation(1) != null) {
                q = weatherViewModel.getWeatherDataDao().getCurrentLocation(1);
                loadJson(q, language);
            } else {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET}, 10);
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }
        } else {
            if (mLat == null && mLon == null && weatherViewModel.getWeatherDataDao().getAllData().size() > 1) {
                q = weatherViewModel.getWeatherDataDao().getCurrentLocation(myCountPage);
            } else {
                q = mLat + "," + mLon;
            }
            loadJson(q, language);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                q = lat + "," + lon;
                if (!enter) {
                    loadJson(q, language);
                }
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showSettingsAlert();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null && !connectivityManager.getActiveNetworkInfo().isConnected())
            return false;
        else
            return true;
    }

    private void observeForecast() {
        weatherViewModel.getWeatherLiveData().observe(this, new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                swipeRefreshLayout.setRefreshing(false);
                if (data != null) {
                    DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yy");
                    currentDate.setText(formatter.format(data.getMyForecast().getForecastDay().get(0).getDate()));
                    currentLocation.setText(data.getMyLocation().getCityName());
                    currentDegreeDay.setText((int) data.getMyForecast().getForecastDay().get(0).getDay().getMaxTemp() + "");
                    currentDegreeNight.setText((int) data.getMyForecast().getForecastDay().get(0).getDay().getMinTemp() + "");

                    Picasso.get()
                            .load("https:" + data.getMyCurrent().getCondition().getUrlToImage())
                            .fit()
                            .centerInside()
                            .into(currentWeatherIcon);
                    currentDegree.setText((int) data.getMyCurrent().getTemp() + "");
                    currentWeatherDescription.setText(data.getMyCurrent().getCondition().getText());

                    Picasso.get()
                            .load("https:" + data.getMyCurrent().getCondition().getUrlToImage())
                            .fit()
                            .centerInside()
                            .into(currentWeatherIconLow);
                    currentWeatherDegreeLow.setText((int) data.getMyCurrent().getTemp() + "\u2103");
                    currentTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).
                            format(Calendar.getInstance().getTime()));
                    sunriseTime.setText(data.getMyForecast().getForecastDay().get(0).getAstro().getSunrise());
                    windValue.setText(data.getMyCurrent().getWind() + "");
                    sunsetTime.setText(data.getMyForecast().getForecastDay().get(0).getAstro().getSunset());
                }
            }
        });

        weatherViewModel.getDbWeatherLiveData().observe(this, new Observer<Pair<List<WeatherData>, List<HourlyData>>>() {
            @Override
            public void onChanged(Pair<List<WeatherData>, List<HourlyData>> listListPair) {
                List<WeatherData> weatherDataList = listListPair.first;
                List<HourlyData> hourlyDataList = listListPair.second;

                WeatherData weatherData = weatherDataList.get(0);
                DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yy");
                try {
                    Date date = formatter.parse(weatherData.dbCurrentDate);
                    collapseCurrentDate.setText(formatter.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                collapseCurrentLocation.setText(weatherData.dbCurrentLocation);
                collapseCurrentWeatherDegree.setText(weatherData.dbCurrentDegree);
                Picasso.get()
                        .load("https:" + weatherData.dbCurrentWeatherIcon)
                        .resize(100, 100)
                        .centerInside()
                        .into(collapseCurrentWeatherIcon);
                collapseCurrentDegreeDay.setText(weatherData.dbCurrentDegreeDay);
                collapseCurrentDegreeNight.setText(weatherData.dbCurrentDegreeNight);
                collapseCurrentDescription.setText(weatherData.dbCurrentWeatherDescription);
                collapseSunriseTime.setText(weatherData.dbSunriseTime);
                collapseSunsetTime.setText(weatherData.dbSunsetTime);
                collapseWindValue.setText(weatherData.dbWindValue);
                recyclerViewAdapterHoursCollapse.setItems(hourlyDataList);
                collapseHumidityValue.setText(weatherData.dbHumidity + " %");
                collapsePrecipitationValue.setText(weatherData.dbPrecipitation + " mb");
                collapsePressureValue.setText(weatherData.dbPressure + " mm");
                collapseCloudValue.setText(weatherData.dbCloud + " %");
                collapseUvValue.setText(weatherData.dbUv + "");
                collapseVisionValue.setText(weatherData.dbVision + " km");
            }
        });
    }

    private void loadJson(String q, String lang) {
        if (isOnline()) {
            swipeRefreshLayout.setRefreshing(true);
            enter = true;
            Toast.makeText(getActivity(), "Location: " + q, Toast.LENGTH_SHORT).show();
            weatherViewModel.loadJson(q, lang, myCountPage);
        }
        else {
            swipeRefreshLayout.setRefreshing(true);
            loadData();
            Toast.makeText(getActivity().getApplicationContext(), "Error! No internet connection", Toast.LENGTH_LONG).show();
        }

    }

    public void addDataToUI() {
        weatherViewModel.updateFromDb(myCurrentPage);
    }

    public void loadData() {
        observeForecast();
    }

    @Override
    public void onRefresh() {
        if (myCurrentPage == 1) {
            loadJson(q, language);
            if (checker)
                addDataToUI();
        } else {
            List<WeatherData> weatherDataList = weatherViewModel.getWeatherDataDao().getAllByInd(myCurrentPage);
            for (WeatherData iterator: weatherDataList) {
                Log.e(TAG, "myCurrentPage " + iterator.cityLocation + ", " + iterator.dbCurrentDegree);
            }
            q = weatherViewModel.getWeatherDataDao().getCurrentLocation(myCurrentPage);
            loadJson(q, language);
        }

    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.collapseButton:
                if (expandedView.getVisibility() == View.GONE) {
                    collapse(cardView, 700, swipeRefreshLayout.getHeight());
                    expandedView.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    motionLayout.setVisibility(View.GONE);
                    tomorrow.setVisibility(View.GONE);
                    nextFewDays.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    longLine.setVisibility(View.GONE);
                    cardViewHeight = cardView.getHeight();
                    addDataToUI();
                }
                break;
            case R.id.collapseButtonBack:
                if (expandedView.getVisibility() == View.VISIBLE) {
                    collapse(cardView, 700, cardViewHeight);
                    view.setVisibility(View.VISIBLE);
                    collapseButton.setVisibility(View.VISIBLE);
                    motionLayout.setVisibility(View.VISIBLE);
                    expandedView.setVisibility(View.GONE);
                    tomorrow.setVisibility(View.VISIBLE);
                    nextFewDays.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    longLine.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public static void collapse(final View myView, int duration, int targetHeight) {

        int prevHeight = myView.getHeight();

        myView.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                myView.getLayoutParams().height = (int) animation.getAnimatedValue();
                myView.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    @SuppressLint("SetTextI18n")
    public void loadCardData() {
        int srTimeTemp = Integer.parseInt(sunriseTime.getText().subSequence(0, 2).toString()),
                stTimeTemp = Integer.parseInt(sunsetTime.getText().subSequence(0, 2).toString());
        List<HourlyData> hourlyDataList = weatherViewModel.getDbWeatherLiveData().getValue().second;

        if (distance == 7) {
            for (int i = 0; i < hourlyDataList.size(); i++) {
                int tmp = Integer.parseInt(hourlyDataList.get(i).hourlyTime.substring(0, 2));
                if (tmp == stTimeTemp) {
                    HourlyData temp = weatherViewModel.getHourlyDataDao().getHourData(hourlyDataList.get(i).hourlyTime);

                    Picasso.get()
                            .load("https:" + temp.urlIcon)
                            .fit()
                            .centerInside()
                            .into(currentWeatherIconLow);

                    currentWeatherDegreeLow.setText((int) temp.hourlyDegree + "\u2103");
                    currentTime.setText(temp.hourlyTime);
                    break;
                }
            }
        } else if (distance == 0) {
            for (int i = 0; i < hourlyDataList.size(); i++) {
                int tmp1 = Integer.parseInt(hourlyDataList.get(i).hourlyTime.substring(0, 2));
                if (tmp1 == srTimeTemp) {
                    HourlyData temp = weatherViewModel.getHourlyDataDao().getHourData(hourlyDataList.get(i).hourlyTime);

                    Picasso.get()
                            .load("https:" + temp.urlIcon)
                            .fit()
                            .centerInside()
                            .into(currentWeatherIconLow);

                    currentWeatherDegreeLow.setText((int) temp.hourlyDegree + "\u2103");
                    currentTime.setText(temp.hourlyTime);
                    break;
                }
            }
        } else {
            String timeTemp;
            int nextHour;
            if (currentTime.getText().length() == 4) {
                timeTemp = "0" + currentTime.getText().toString();
            } else {
                timeTemp = currentTime.getText().toString();
            }

            if (Integer.parseInt(timeTemp.substring(0, 2)) == 23) {
                nextHour = 1;
                for (int i = 0; i < hourlyDataList.size(); i++) {
                    if (nextHour == Integer.parseInt(hourlyDataList.get(i).hourlyTime.substring(0, 2))) {
                        HourlyData temp = weatherViewModel.getHourlyDataDao().getHourData(hourlyDataList.get(i).hourlyTime);

                        Picasso.get()
                                .load("https:" + temp.urlIcon)
                                .fit()
                                .centerInside()
                                .into(currentWeatherIconLow);

                        currentWeatherDegreeLow.setText((int) temp.hourlyDegree + "\u2103");
                        break;
                    }
                }
            } else if (Integer.parseInt(timeTemp.substring(0, 2)) == 0) {
                nextHour = 23;
                for (int i = 0; i < hourlyDataList.size(); i++) {
                    if (nextHour == Integer.parseInt(hourlyDataList.get(i).hourlyTime.substring(0, 2))) {
                        HourlyData temp = weatherViewModel.getHourlyDataDao().getHourData(hourlyDataList.get(i).hourlyTime);
                        Picasso.get()
                                .load("https:" + temp.urlIcon)
                                .fit()
                                .centerInside()
                                .into(currentWeatherIconLow);

                        currentWeatherDegreeLow.setText((int) temp.hourlyDegree + "\u2103");
                        currentTime.setText(temp.hourlyTime);
                        break;
                    }
                }
            } else {
                if (Integer.parseInt(timeTemp.substring(0, 2)) >= 0) {
                    nextHour = Integer.parseInt(timeTemp.substring(0, 2)) + 1;
                } else {
                    nextHour = Integer.parseInt(timeTemp.substring(0, 2)) - 1;
                }
                for (int i = 0; i < hourlyDataList.size(); i++) {
                    if (nextHour == Integer.parseInt(hourlyDataList.get(i).hourlyTime.substring(0, 2))) {
                        HourlyData temp = weatherViewModel.getHourlyDataDao().getHourData(hourlyDataList.get(i).hourlyTime);

                        Picasso.get()
                                .load("https:" + temp.urlIcon)
                                .fit()
                                .centerInside()
                                .into(currentWeatherIconLow);

                        currentWeatherDegreeLow.setText((int) temp.hourlyDegree + "\u2103");
                        currentTime.setText(temp.hourlyTime);
                        break;
                    }
                }
            }
        }
    }

    public void addPoints() {
        points.add(new Point(5, 197));
        points.add(new Point(65, 161));
        points.add(new Point(130, 127));
        points.add(new Point(190, 103));
        points.add(new Point(245, 93));
        points.add(new Point(305, 103));
        points.add(new Point(365, 127));
        points.add(new Point(430, 161));
        points.add(new Point(491, 197));
    }

    public int getCurrentPosition(float x) {
        oldDistance = distance;
        if (x > (float) points.get(0).x && (float) points.get(1).x > x) {
            distance = 0;
        } else if (x > (float) points.get(1).x && (float) points.get(2).x > x) {
            distance = 1;
        } else if (x > (float) points.get(2).x && (float) points.get(3).x > x) {
            distance = 2;
        } else if (x > (float) points.get(3).x && (float) points.get(4).x > x) {
            distance = 3;
        } else if (x > (float) points.get(4).x && (float) points.get(5).x > x) {
            distance = 4;
        } else if (x > (float) points.get(5).x && (float) points.get(6).x > x) {
            distance = 5;
        } else if (x > (float) points.get(6).x && (float) points.get(7).x > x) {
            distance = 6;
        } else if (x > (float) points.get(7).x && (float) points.get(8).x > x) {
            distance = 7;
        }
        return distance;
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()) {
                    case R.id.lowerDraw:
                        dY = motionEvent.getRawY();
                        break;
                    case R.id.expandedView:
                        dY = motionEvent.getRawY();
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (view.getId()) {
                    case R.id.lowerDraw:
                        newX = motionEvent.getRawX();
                        newY = motionEvent.getRawY();
                        if (expandedView.getVisibility() == View.GONE && newY < dY) {
                            float parentHeight = swipeRefreshLayout.getHeight();
                            collapse(cardView, 500, (int) parentHeight);
                            expandedView.setVisibility(View.VISIBLE);
                            collapseButton.setVisibility(View.GONE);
                            motionLayout.setVisibility(View.GONE);
                            tomorrow.setVisibility(View.GONE);
                            nextFewDays.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                            longLine.setVisibility(View.GONE);
                            cardViewHeight = cardView.getHeight();
                            addDataToUI();
                        }
                        break;

                    case R.id.expandedView:
                        newX = motionEvent.getRawX();
                        newY = motionEvent.getRawY();
                        if (expandedView.getVisibility() == View.VISIBLE && newY > dY) {
                            collapse(cardView, 500, cardViewHeight);
                            expandedView.setVisibility(View.GONE);
                            collapseButton.setVisibility(View.VISIBLE);
                            tomorrow.setVisibility(View.VISIBLE);
                            nextFewDays.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            longLine.setVisibility(View.VISIBLE);
                            motionLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

    }

    @Override
    public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

        getCurrentPosition(cardDraw.getX());
        if (oldDistance != distance) {
            loadCardData();
        }
    }

    @Override
    public void onTransitionCompleted(MotionLayout motionLayout, int i) {

    }

    @Override
    public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
    }

    @Override
    public void onMotionEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stateViewModel.setMotionState(StateViewModel.MotionState.START);
                break;
            case MotionEvent.ACTION_UP:
                stateViewModel.setMotionState(StateViewModel.MotionState.IDLE);
                break;
        }
    }
}
