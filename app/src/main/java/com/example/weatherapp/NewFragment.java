package com.example.weatherapp;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.adapters.RecyclerViewAdapterHoursCollapseNew;
import com.example.weatherapp.api.ApiClient;
import com.example.weatherapp.models.forecast.Current;
import com.example.weatherapp.models.forecast.Data;
import com.example.weatherapp.models.forecast.Hour;
import com.example.weatherapp.roomDB.weatherData.HourlyData;
import com.example.weatherapp.roomDB.weatherData.HourlyDataDao;
import com.example.weatherapp.roomDB.weatherData.WeatherData;
import com.example.weatherapp.roomDB.weatherData.WeatherDataBase;
import com.example.weatherapp.roomDB.weatherData.WeatherDataDao;
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

public class NewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, View.OnTouchListener, MotionLayout.TransitionListener {

    public static final String API_KEY = "0ea9812ad11649cdbfa144808202010";
    public static final String TAG = "NewFragment";

    private SwipeRefreshLayout swipeRefreshLayoutNew;
    private ScrollView expandedView;
    private ConstraintLayout cardView, newFragment;
    private MotionLayout motionLayout;
    private CardView cardDraw;
    private Button collapseButton, collapseButtonBack;
    private ImageView currentWeatherIcon, currentWeatherIconLow, collapseCurrentWeatherIcon, imageView, longLine, up, down;
    private TextView currentDegree, currentLocation, currentWeatherDescription, currentDegreeDay,
            currentDegreeNight, currentDate, currentWeatherDegreeLow, currentTime, sunriseTime,
            sunsetTime, windValue, tomorrow, nextFewDays, collapseCurrentDescription, collapseCurrentWeatherDegree,
            collapseCurrentDegreeDay, collapseCurrentDegreeNight, collapseSunriseTime, collapseWindValue,
            collapseSunsetTime, collapseTomorrow, collapseNextFewDays, collapseCurrentDate, collapseCurrentLocation, collapseHumidityValue,
            collapsePrecipitationValue, collapsePressureValue, collapseCloudValue, collapseUvValue, collapseVisionValue, currentDay, currentNight,
            degreeSymbolD, degreeSymbolN, currentCelsius;

    private WeatherDataBase database;
    private WeatherDataDao weatherDataDao;
    private HourlyDataDao hourlyDataDao;
    private WeatherData weatherData;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterHoursCollapseNew recyclerViewAdapterHoursCollapse;
    private ArrayList<Point> points;

    private String mLat, mLon, q;
    private int myCountPage, cardViewHeight, distance = 0, oldDistance = 0;
    private static int myCurrentPage;
    private String language;
    private boolean isMove = false;
    private float dX, dY, newX, newY, currentX, currentY;


    public static NewFragment newInstance(String lat, String lon, int countPage) {
        NewFragment newFragment = new NewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("lon", lon);
        bundle.putInt("countPage", countPage);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLat = getArguments().getString("lat");
        mLon = getArguments().getString("lon");
        myCountPage = getArguments().getInt("countPage");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new, container, false);
        newFragment = view.findViewById(R.id.newFragment);
        motionLayout = view.findViewById(R.id.motionCardNew);
        database = WeatherDataBase.getInstance(view.getContext());
        weatherDataDao = database.weatherDataDao();
        hourlyDataDao = database.hourlyDataDao();

        swipeRefreshLayoutNew = view.findViewById(R.id.swipe_refresh_layout_new);
        swipeRefreshLayoutNew.setOnRefreshListener(this);
        language = Locale.getDefault().getLanguage();

        expandedView = view.findViewById(R.id.expandedViewNew);
        cardDraw = view.findViewById(R.id.cardDrawNew);
        cardDraw.setBackgroundResource(R.drawable.card_view_style);
        cardView = view.findViewById(R.id.lowerDrawNew);
        currentLocation = view.findViewById(R.id.currentLocationNew);
        currentDate = view.findViewById(R.id.currentDateNew);
        currentDegree = view.findViewById(R.id.currentDegreeNew);
        currentWeatherDescription = view.findViewById(R.id.currentDescriptionNew);
        currentWeatherIcon = view.findViewById(R.id.currentWeatherIconNew);
        currentDegreeDay = view.findViewById(R.id.currentDegreeDayNew);
        currentDegreeNight = view.findViewById(R.id.currentDegreeNightNew);
        currentDay = view.findViewById(R.id.currentDayNew);
        currentNight = view.findViewById(R.id.currentNightNew);
        degreeSymbolD = view.findViewById(R.id.degreeSymbolDNew);
        degreeSymbolN = view.findViewById(R.id.degreeSymbolNNew);
        currentCelsius = view.findViewById(R.id.currentCelsiusNew);
        up = view.findViewById(R.id.upNew);
        down = view.findViewById(R.id.downNew);

        currentWeatherIconLow = view.findViewById(R.id.currentWeatherIconLowNew);
        currentWeatherDegreeLow = view.findViewById(R.id.currentDegreeLowNew);
        currentTime = view.findViewById(R.id.timeLowNew);
        sunriseTime = view.findViewById(R.id.sunriseTimeNew);
        windValue = view.findViewById(R.id.windValueNew);
        sunsetTime = view.findViewById(R.id.sunsetTimeNew);
        imageView = view.findViewById(R.id.imageViewNew);
        longLine = view.findViewById(R.id.longLineNew);
        tomorrow = view.findViewById(R.id.tomorrowNew);
        nextFewDays = view.findViewById(R.id.nextFewDaysNew);

        collapseButton = view.findViewById(R.id.collapseButtonNew);
        collapseButtonBack = view.findViewById(R.id.collapseButtonBackNew);

        collapseTomorrow = view.findViewById(R.id.collapseTomorrowNew);
        collapseNextFewDays = view.findViewById(R.id.collapseNextFewDaysNew);
        collapseCurrentWeatherIcon = view.findViewById(R.id.collapseCurrentWeatherIconNew);
        collapseCurrentWeatherDegree = view.findViewById(R.id.collapseCurrentDegreeNew);
        collapseCurrentDegreeDay = view.findViewById(R.id.collapseCurrentDegreeDayNew);
        collapseCurrentDegreeNight = view.findViewById(R.id.collapseCurrentDegreeNightNew);
        collapseSunriseTime = view.findViewById(R.id.collapseSunriseTimeNew);
        collapseWindValue = view.findViewById(R.id.collapseWindValueNew);
        collapseSunsetTime = view.findViewById(R.id.collapseSunsetTimeNew);
        collapseCurrentDate = view.findViewById(R.id.collapseCurrentDateNew);
        collapseCurrentLocation = view.findViewById(R.id.collapseCurrentLocationNew);
        collapseCurrentDescription = view.findViewById(R.id.collapseCurrentDescriptionNew);

        recyclerView = view.findViewById(R.id.collapseHourlyNew);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapterHoursCollapse = new RecyclerViewAdapterHoursCollapseNew(hourlyDataDao, this);
        recyclerView.setAdapter(recyclerViewAdapterHoursCollapse);

        collapseHumidityValue = view.findViewById(R.id.collapseHumidityValueNew);
        collapsePrecipitationValue = view.findViewById(R.id.collapsePrecipitationValueNew);
        collapsePressureValue = view.findViewById(R.id.collapsePressureValueNew);
        collapseCloudValue = view.findViewById(R.id.collapseCloudValueNew);
        collapseUvValue = view.findViewById(R.id.collapseUvValueNew);
        collapseVisionValue = view.findViewById(R.id.collapseVisionValueNew);
        points = new ArrayList<>();

        tomorrow.setOnClickListener(this);
        nextFewDays.setOnClickListener(this);
        collapseButton.setOnClickListener(this);
        collapseButtonBack.setOnClickListener(this);
        cardView.setOnTouchListener(this);
        expandedView.setOnTouchListener(this);
        motionLayout.setTransitionListener(this);

        if (mLat == null && mLon == null && weatherDataDao.getAllData().size() > 1) {
            q = weatherDataDao.getCurrentLocation(myCountPage);
        } else {
            q = mLat + "," + mLon;
        }

        LoadJson(q, language);
        setRetainInstance(true);
        addPoints();

        //loadBackground();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetwork() == null)
            return false;
        else
            return true;
    }

    public String timeFormatter(String jsonTime) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"),
                simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        String string1 = null;
        try {
            Date date = simpleDateFormat1.parse(jsonTime);
            string1 = simpleDateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string1;
    }

    public void loadBackground(){

        String mCurrentDate = DateFormat.getTimeInstance(DateFormat.SHORT).
                format(Calendar.getInstance().getTime());

        if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 9
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 12) {
            newFragment.setBackgroundResource(R.drawable.background_morning);
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
            newFragment.setBackgroundResource(R.drawable.background_day);
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
            newFragment.setBackgroundResource(R.drawable.background_evening);
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
            newFragment.setBackgroundResource(R.drawable.background_prenight);
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
            newFragment.setBackgroundResource(R.drawable.background_fullnight);
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
            newFragment.setBackgroundResource(R.drawable.background_premorning);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void LoadJson(String q, String lang){
        swipeRefreshLayoutNew.setRefreshing(true);

        if (isOnline()) {
            Toast.makeText(getActivity().getApplicationContext(), "Location: " + q, Toast.LENGTH_SHORT).show();

            ApiClient.getInstance()
                    .getApiInterface()
                    .getForecast(API_KEY, 6, q, lang)
                    .enqueue(new Callback<Data>() {
                        @Override
                        public void onResponse(Call<Data> call, Response<Data> response) {
                            if (response.isSuccessful()
                                    && response.body() != null
                                    && response.body().getMyCurrent() != null
                                    && response.body().getMyForecast() != null
                                    && response.body().getMyLocation() != null) {
                                DateFormat formatter = new SimpleDateFormat("EEE, d MMM, yy");
                                currentDate.setText(formatter.format(response.body().getMyForecast().getForecastDay().get(0).getDate()));
                                currentLocation.setText(response.body().getMyLocation().getCityName());
                                currentDegreeDay.setText((int) response.body().getMyForecast().getForecastDay().get(0).getDay().getMaxTemp() + "");
                                currentDegreeNight.setText((int) response.body().getMyForecast().getForecastDay().get(0).getDay().getMinTemp() + "");

                                Picasso.get()
                                        .load("https:" + response.body().getMyCurrent().getCondition().getUrlToImage())
                                        .fit()
                                        .centerInside()
                                        .into(currentWeatherIcon);
                                currentDegree.setText((int) response.body().getMyCurrent().getTemp() + "");
                                currentWeatherDescription.setText(response.body().getMyCurrent().getCondition().getText());

                                Picasso.get()
                                        .load("https:" + response.body().getMyCurrent().getCondition().getUrlToImage())
                                        .fit()
                                        .centerInside()
                                        .into(currentWeatherIconLow);
                                currentWeatherDegreeLow.setText((int) response.body().getMyCurrent().getTemp() + "\u2103");
                                currentTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).
                                        format(Calendar.getInstance().getTime()));
                                sunriseTime.setText(response.body().getMyForecast().getForecastDay().get(0).getAstro().getSunrise() + "");
                                windValue.setText(response.body().getMyCurrent().getWind() + "");
                                sunsetTime.setText(response.body().getMyForecast().getForecastDay().get(0).getAstro().getSunset());
                                saveData(response.body().getMyCurrent());
                                List<Hour> hours = response.body().getMyForecast().getForecastDay().get(0).getHour();
                                for (Hour iterator: hours){
                                    if (hourlyDataDao.getAllByIndH(myCountPage).size() < 24){
                                        HourlyData hourlyData = new HourlyData();
                                        hourlyData.urlIcon = iterator.getCondition().getUrlToImage();
                                        hourlyData.hourlyDegree = iterator.getTemp();
                                        hourlyData.hourlyTime = timeFormatter(iterator.getTime());
                                        hourlyData.ind = myCountPage;
                                        hourlyDataDao.insert(hourlyData);
                                    } else {
                                        List<HourlyData> tempList = hourlyDataDao.getAllData();
                                        List<Long> idList = hourlyDataDao.getAllIdH(myCountPage);
                                        long tempId = 0;
                                        int index = 0;
                                        for (int i = 0; i < tempList.size(); i++) {
                                            if (index < 12) {
                                                if (idList.get(index) == tempList.get(i).id) {
                                                    tempId = idList.get(index);
                                                    index++;
                                                    break;
                                                }
                                            } else
                                                index = 0;
                                        }

                                        if (tempId != 0){
                                            HourlyData temporary = hourlyDataDao.getByIdH(tempList.get(index).id);
                                            temporary.urlIcon = iterator.getCondition().getUrlToImage();
                                            temporary.hourlyDegree = iterator.getTemp();
                                            temporary.hourlyTime = timeFormatter(iterator.getTime());
                                            hourlyDataDao.update(temporary);
                                        }
                                    }
                                }
//                                hourlyDataDao.deleteTable();
                                swipeRefreshLayoutNew.setRefreshing(false);
                            } else {
                                swipeRefreshLayoutNew.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<Data> call, Throwable t) {
                            swipeRefreshLayoutNew.setRefreshing(false);
                        }
                    });
        } else {
            loadData();
            Toast.makeText(getActivity().getApplicationContext(), "Error! No internet connection", Toast.LENGTH_LONG).show();
            swipeRefreshLayoutNew.setRefreshing(false);
        }
    }

    public void addDataToUI() {
        List<WeatherData> weatherDataList = weatherDataDao.getAllByInd(myCountPage);
        WeatherData iterator = weatherDataList.get(0);
        collapseCurrentLocation.setText(iterator.dbCurrentLocation);
        collapseCurrentWeatherDegree.setText(iterator.dbCurrentDegree);
        collapseCurrentDate.setText(iterator.dbCurrentDate);
        Picasso.get()
                .load("https:" + iterator.dbCurrentWeatherIcon)
                .fit()
                .centerInside()
                .into(collapseCurrentWeatherIcon);
        collapseCurrentDegreeDay.setText(iterator.dbCurrentDegreeDay);
        collapseCurrentDegreeNight.setText(iterator.dbCurrentDegreeNight);
        collapseCurrentDescription.setText(iterator.dbCurrentWeatherDescription);
        collapseSunriseTime.setText(iterator.dbSunriseTime);
        collapseSunsetTime.setText(iterator.dbSunsetTime);
        collapseWindValue.setText(iterator.dbWindValue);
        List<HourlyData> hourlyDataList = hourlyDataDao.getAllByIndH(myCountPage);
        recyclerViewAdapterHoursCollapse.addItems(hourlyDataList);
        collapseHumidityValue.setText(iterator.dbHumidity + " %");
        collapsePrecipitationValue.setText(iterator.dbPrecipitation + " mb");
        collapsePressureValue.setText(iterator.dbPressure + " mm");
        collapseCloudValue.setText(iterator.dbCloud + " %");
        collapseUvValue.setText(iterator.dbUv + "");
        collapseVisionValue.setText(iterator.dbVision + " km");
    }

    public void saveData(Current temp) {
        //weatherDataDao.deleteTable();
        if (weatherDataDao.getAllByInd(myCountPage).size() == 0 && !weatherDataDao.findExistence(q)){
            weatherData = new WeatherData();
            weatherData.dbCurrentLocation = currentLocation.getText().toString();
            weatherData.dbCurrentDegree = currentDegree.getText().toString();
            weatherData.dbCurrentDate = currentDate.getText().toString();
            weatherData.dbCurrentWeatherIcon = temp.getCondition().getUrlToImage();
            weatherData.dbCurrentDegreeDay = currentDegreeDay.getText().toString();
            weatherData.dbCurrentDegreeNight = currentDegreeNight.getText().toString();
            weatherData.dbCurrentTime = currentTime.getText().toString();
            weatherData.dbCurrentWeatherDescription = currentWeatherDescription.getText().toString();
            weatherData.dbCurrentWeatherIconLow = temp.getCondition().getUrlToImage();
            weatherData.dbCurrentWeatherDegreeLow = currentWeatherDegreeLow.getText().toString();
            weatherData.dbSunriseTime = sunriseTime.getText().toString();
            weatherData.dbSunsetTime = sunsetTime.getText().toString();
            weatherData.dbWindValue = windValue.getText().toString();
            weatherData.dbHumidity = temp.getHumidity();
            weatherData.dbPrecipitation = temp.getPrecipitation();
            weatherData.dbPressure = temp.getPressure();
            weatherData.dbCloud = temp.getCloud();
            weatherData.dbUv = temp.getUv();
            weatherData.dbVision = temp.getVision();
            weatherData.cityLocation = q;
            weatherData.ind = myCountPage;
            weatherDataDao.insert(weatherData);
        } else {
            List<WeatherData> weatherDataList = weatherDataDao.getAllByInd(myCountPage);
            WeatherData iterator = weatherDataList.get(0);
            iterator.dbCurrentLocation = currentLocation.getText().toString();
            iterator.dbCurrentDegree = currentDegree.getText().toString();
            iterator.dbCurrentDate = currentDate.getText().toString();
            iterator.dbCurrentWeatherIcon = temp.getCondition().getUrlToImage();
            iterator.dbCurrentDegreeDay = currentDegreeDay.getText().toString();
            iterator.dbCurrentDegreeNight = currentDegreeNight.getText().toString();
            iterator.dbCurrentTime = currentTime.getText().toString();
            iterator.dbCurrentWeatherDescription = currentWeatherDescription.getText().toString();
            iterator.dbCurrentWeatherIconLow = temp.getCondition().getUrlToImage();
            iterator.dbCurrentWeatherDegreeLow = currentWeatherDegreeLow.getText().toString();
            iterator.dbSunriseTime = sunriseTime.getText().toString();
            iterator.dbSunsetTime = sunsetTime.getText().toString();
            iterator.dbWindValue = windValue.getText().toString();
            iterator.dbHumidity = temp.getHumidity();
            iterator.dbPrecipitation = temp.getPrecipitation();
            iterator.dbPressure = temp.getPressure();
            iterator.dbCloud = temp.getCloud();
            iterator.dbUv = temp.getUv();
            iterator.dbVision = temp.getVision();
            weatherDataDao.update(iterator);
        }
    }

    public void loadData()  {
        List<WeatherData> weatherDataList = weatherDataDao.getCity(q);
        WeatherData iterator = weatherDataList.get(0);
        currentLocation.setText(iterator.dbCurrentLocation);
        currentDegree.setText(iterator.dbCurrentDegree);
        currentDate.setText(iterator.dbCurrentDate);
        Picasso.get()
                .load("https:" + iterator.dbCurrentWeatherIcon)
                .fit()
                .centerInside()
                .into(currentWeatherIcon);
        currentDegreeDay.setText(iterator.dbCurrentDegreeDay);
        currentDegreeNight.setText(iterator.dbCurrentDegreeNight);
        currentTime.setText(iterator.dbCurrentTime);
        currentWeatherDescription.setText(iterator.dbCurrentWeatherDescription);
        Picasso.get()
                .load("https:" + iterator.dbCurrentWeatherIconLow)
                .fit()
                .centerInside()
                .into(currentWeatherIconLow);
        currentWeatherDegreeLow.setText(iterator.dbCurrentWeatherDegreeLow);
        sunriseTime.setText(iterator.dbSunriseTime);
        sunsetTime.setText(iterator.dbSunsetTime);
        windValue.setText(iterator.dbWindValue);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRefresh() {
        q = weatherDataDao.getCurrentLocation(myCurrentPage);
        LoadJson(q, language);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.collapseButtonNew:
                if (expandedView.getVisibility() == View.GONE){
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
                    collapse(cardView, 700, displayMetrics.heightPixels - 350);
                    view.setVisibility(View.GONE);
                    cardDraw.setVisibility(View.GONE);
                    expandedView.setVisibility(View.VISIBLE);
                    tomorrow.setVisibility(View.GONE);
                    nextFewDays.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    longLine.setVisibility(View.GONE);
                    addDataToUI();
                    cardViewHeight = cardView.getHeight();
                }
                break;
            case R.id.collapseButtonBackNew:
                if (expandedView.getVisibility() == View.VISIBLE){
                    collapse(cardView, 700, cardViewHeight);
                    view.setVisibility(View.VISIBLE);
                    collapseButton.setVisibility(View.VISIBLE);
                    cardDraw.setVisibility(View.VISIBLE);
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

        int prevHeight  = myView.getHeight();

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addPoints() {
        points.add(new Point(5 , 197));
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
        }else if (x > (float) points.get(2).x && (float) points.get(3).x > x) {
            distance = 2;
        }else if (x > (float) points.get(3).x && (float) points.get(4).x > x) {
            distance = 3;
        }else if (x > (float) points.get(4).x && (float) points.get(5).x > x) {
            distance = 4;
        }else if (x > (float) points.get(5).x && (float) points.get(6).x > x) {
            distance = 5;
        }else if (x > (float) points.get(6).x && (float) points.get(7).x > x) {
            distance = 6;
        }else if (x > (float) points.get(7).x && (float) points.get(8).x > x) {
            distance = 7;
        }
        return distance;
    }

    @SuppressLint("SetTextI18n")
    public void loadCardData() {
        int srTimeTemp = Integer.parseInt(sunriseTime.getText().subSequence(0, 2).toString()),
                stTimeTemp = Integer.parseInt(sunsetTime.getText().subSequence(0, 2).toString());

        List<HourlyData> hourlyData = hourlyDataDao.getAllData();
        if (distance == 7) {
            for (int i = 0; i < hourlyData.size(); i++) {
                int tmp = Integer.parseInt(hourlyData.get(i).hourlyTime.substring(0, 2));
                if (tmp == stTimeTemp) {
                    HourlyData temp = hourlyDataDao.getHourData(hourlyData.get(i).hourlyTime);

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
            for (int i = 0; i < hourlyData.size(); i++) {
                int tmp1 = Integer.parseInt(hourlyData.get(i).hourlyTime.substring(0, 2));
                if (tmp1 == srTimeTemp) {
                    HourlyData temp = hourlyDataDao.getHourData(hourlyData.get(i).hourlyTime);

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
                for (int i = 0; i < hourlyData.size(); i++) {
                    if (nextHour == Integer.parseInt(hourlyData.get(i).hourlyTime.substring(0, 2))) {
                        HourlyData temp = hourlyDataDao.getHourData(hourlyData.get(i).hourlyTime);

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
            } else if (Integer.parseInt(timeTemp.substring(0, 2)) == 0) {
                nextHour = 23;
                for (int i = 0; i < hourlyData.size(); i++) {
                    if (nextHour == Integer.parseInt(hourlyData.get(i).hourlyTime.substring(0, 2))) {
                        HourlyData temp = hourlyDataDao.getHourData(hourlyData.get(i).hourlyTime);
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
                for (int i = 0; i < hourlyData.size(); i++) {
                    if (nextHour == Integer.parseInt(hourlyData.get(i).hourlyTime.substring(0, 2))) {
                        HourlyData temp = hourlyDataDao.getHourData(hourlyData.get(i).hourlyTime);

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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                switch (view.getId()){
                    case R.id.lowerDrawNew:
                        isMove = false;
                        dY = motionEvent.getRawY();
                        break;
                    case R.id.expandedViewNew:
                        dY = motionEvent.getRawY();
                        isMove = false;
                        break;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                switch (view.getId()){
                    case R.id.lowerDrawNew:
                        newX = motionEvent.getRawX();
                        newY = motionEvent.getRawY();
                        if (expandedView.getVisibility() == View.GONE && newY < dY) {
                            float parentHeight = swipeRefreshLayoutNew.getHeight();
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

                    case R.id.expandedViewNew:
                        newX = motionEvent.getRawX();
                        newY = motionEvent.getRawY();
                        if (expandedView.getVisibility() == View.VISIBLE && newY > dY) {
                            collapse(cardView, 500, cardViewHeight);
                            expandedView.setVisibility(View.GONE);
                            collapseButton.setVisibility(View.VISIBLE);
                            motionLayout.setVisibility(View.VISIBLE);
                            tomorrow.setVisibility(View.VISIBLE);
                            nextFewDays.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            longLine.setVisibility(View.VISIBLE);
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
    public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

    }
}
