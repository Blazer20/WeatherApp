package com.example.weatherapp.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Point;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.api.ApiClient;
import com.example.weatherapp.models.forecast.Data;
import com.example.weatherapp.models.forecast.Hour;
import com.example.weatherapp.roomDB.weatherData.HourlyData;
import com.example.weatherapp.roomDB.weatherData.HourlyDataDao;
import com.example.weatherapp.roomDB.weatherData.WeatherData;
import com.example.weatherapp.roomDB.weatherData.WeatherDataBase;
import com.example.weatherapp.roomDB.weatherData.WeatherDataDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {
    public static final String API_KEY = "0ea9812ad11649cdbfa144808202010";

    private final WeatherDataDao weatherDataDao;
    private final HourlyDataDao hourlyDataDao;
    private WeatherData weatherData;

    List<Point> points = new ArrayList<>();

    private final MutableLiveData<Data> weatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<Pair<List<WeatherData>, List<HourlyData>>> dbWeatherLiveData = new MutableLiveData<>();
    private String mQ;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        WeatherDataBase database = WeatherDataBase.getInstance(application);
        weatherDataDao = database.weatherDataDao();
        hourlyDataDao = database.hourlyDataDao();
    }

    public void loadJson(final String q, String lang, final int mCurrentPage) {
        ApiClient.getInstance()
                .getApiInterface()
                .getForecast(API_KEY, 6, q, lang)
                .enqueue(new Callback<Data>() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getMyCurrent() != null
                                && response.body().getMyForecast() != null
                                && response.body().getMyLocation() != null) {

                            weatherLiveData.setValue(response.body());
                            mQ = q;
                            saveData(response.body(), mCurrentPage);

                            List<Hour> hours = response.body().getMyForecast().getForecastDay().get(0).getHour();
                            for (Hour iterator : hours) {
                                if (hourlyDataDao.getAllByIndH(1).size() < 24) {
                                    HourlyData hourlyData = new HourlyData();
                                    hourlyData.urlIcon = iterator.getCondition().getUrlToImage();
                                    hourlyData.hourlyDegree = iterator.getTemp();
                                    hourlyData.hourlyTime = timeFormatter(iterator.getTime());
                                    hourlyData.ind = mCurrentPage;
                                    hourlyDataDao.insert(hourlyData);
                                } else {
                                    List<HourlyData> tempList = hourlyDataDao.getAllData();
                                    List<Long> idList = hourlyDataDao.getAllIdH(1);
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

                                    if (tempId != 0) {
                                        HourlyData temporary = hourlyDataDao.getByIdH(tempList.get(index).id);
                                        temporary.urlIcon = iterator.getCondition().getUrlToImage();
                                        temporary.hourlyDegree = iterator.getTemp();
                                        temporary.hourlyTime = timeFormatter(iterator.getTime());
                                        hourlyDataDao.update(temporary);
                                    }
                                }
                            }
                        } else {
                            weatherLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        weatherLiveData.setValue(null);
                    }
                });
    }

    public LiveData<Data> getWeatherLiveData() {
        return weatherLiveData;
    }

    public LiveData<Pair<List<WeatherData>, List<HourlyData>>> getDbWeatherLiveData() {
        return dbWeatherLiveData;
    }

    public void updateFromDb(int id) {
        List<WeatherData> weatherDataList = weatherDataDao.getAllByInd(id);
        List<HourlyData> hourlyDataList = hourlyDataDao.getAllByIndH(id);
        dbWeatherLiveData.setValue(new Pair<>(weatherDataList, hourlyDataList));
    }

    public HourlyDataDao getHourlyDataDao() {
        return hourlyDataDao;
    }

    public WeatherDataDao getWeatherDataDao() {
        return weatherDataDao;
    }

    public void saveData(Data data, int currentPage) {
        //weatherDataDao.deleteTable();
        if (weatherDataDao.getAllData().size() == 0 || (weatherDataDao.getAllByInd(currentPage).size() == 0 && !weatherDataDao.findExistence(mQ))) {
            weatherData = new WeatherData();
            weatherData.dbCurrentLocation = data.getMyLocation().getCityName();
            weatherData.dbCurrentDegree = data.getMyCurrent().getTemp() + "";
            weatherData.dbCurrentDate = data.getMyForecast().getForecastDay().get(0).getDate().toString();
            weatherData.dbCurrentWeatherIcon = data.getMyCurrent().getCondition().getUrlToImage();
            weatherData.dbCurrentDegreeDay = data.getMyForecast().getForecastDay().get(0).getDay().getMaxTemp() + "";
            weatherData.dbCurrentDegreeNight = data.getMyForecast().getForecastDay().get(0).getDay().getMinTemp() + "";
            weatherData.dbCurrentTime = Calendar.getInstance().getTime().toString();
            weatherData.dbCurrentWeatherDescription = data.getMyCurrent().getCondition().getText();
            weatherData.dbCurrentWeatherIconLow = data.getMyCurrent().getCondition().getUrlToImage();
            weatherData.dbCurrentWeatherDegreeLow = data.getMyCurrent().getTemp() + "";
            weatherData.dbSunriseTime = data.getMyForecast().getForecastDay().get(0).getAstro().getSunrise();
            weatherData.dbSunsetTime = data.getMyForecast().getForecastDay().get(0).getAstro().getSunset();
            weatherData.dbWindValue = data.getMyCurrent().getWind() + "";
            weatherData.dbHumidity = data.getMyCurrent().getHumidity();
            weatherData.dbPrecipitation = data.getMyCurrent().getPrecipitation();
            weatherData.dbPressure = data.getMyCurrent().getPressure();
            weatherData.dbCloud = data.getMyCurrent().getCloud();
            weatherData.dbUv = data.getMyCurrent().getUv();
            weatherData.dbVision = data.getMyCurrent().getVision();
            weatherData.cityLocation = mQ;
            weatherData.currentPage = currentPage;
            weatherData.ind = currentPage;
            weatherDataDao.insert(weatherData);
//            checker = true;
        } else {
            List<WeatherData> weatherDataList = weatherDataDao.getAllByInd(currentPage);
            WeatherData iterator = weatherDataList.get(0);
            iterator.dbCurrentLocation = data.getMyLocation().getCityName();
            iterator.dbCurrentDegree = data.getMyCurrent().getTemp() + "";
            iterator.dbCurrentDate = data.getMyForecast().getForecastDay().get(0).getDate().toString();
            iterator.dbCurrentWeatherIcon = data.getMyCurrent().getCondition().getUrlToImage();
            iterator.dbCurrentDegreeDay = data.getMyForecast().getForecastDay().get(0).getDay().getMaxTemp() + "";
            iterator.dbCurrentDegreeNight = data.getMyForecast().getForecastDay().get(0).getDay().getMinTemp() + "";
            iterator.dbCurrentTime = Calendar.getInstance().getTime().toString();
            iterator.dbCurrentWeatherDescription = data.getMyCurrent().getCondition().getText();
            iterator.dbCurrentWeatherIconLow = data.getMyCurrent().getCondition().getUrlToImage();
            iterator.dbCurrentWeatherDegreeLow = data.getMyCurrent().getTemp() + "";
            iterator.dbSunriseTime = data.getMyForecast().getForecastDay().get(0).getAstro().getSunrise();
            iterator.dbSunsetTime = data.getMyForecast().getForecastDay().get(0).getAstro().getSunset();
            iterator.dbWindValue = data.getMyCurrent().getWind() + "";
            iterator.dbHumidity = data.getMyCurrent().getHumidity();
            iterator.dbPrecipitation = data.getMyCurrent().getPrecipitation();
            iterator.dbPressure = data.getMyCurrent().getPressure();
            iterator.dbCloud = data.getMyCurrent().getCloud();
            iterator.dbUv = data.getMyCurrent().getUv();
            iterator.dbVision = data.getMyCurrent().getVision();
            weatherDataDao.update(iterator);
        }
    }



    public static String timeFormatter(String jsonTime) {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm"),
                simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        String result = null;
        try {
            Date date = simpleDateFormat1.parse(jsonTime);
            result = simpleDateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
