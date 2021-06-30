package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.weatherapp.adapters.AddedCitiesAdapter;
import com.example.weatherapp.roomDB.weatherData.WeatherDataBase;
import com.example.weatherapp.roomDB.weatherData.WeatherDataDao;

import java.text.DateFormat;
import java.util.Calendar;

public class CitiesList extends AppCompatActivity implements AddedCitiesAdapter.OnItemClickListener {

    private RecyclerView recyclerViewAddedCities;
    private AddedCitiesAdapter addedCitiesAdapter;
    private TextView degreeChanger;
    private ConstraintLayout constraintLayout;

    private WeatherDataBase database;
    private WeatherDataDao weatherDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);
        database = WeatherDataBase.getInstance(getApplicationContext());
        weatherDataDao = database.weatherDataDao();
        constraintLayout = findViewById(R.id.cityListActivity);

        recyclerViewAddedCities = findViewById(R.id.added_cities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewAddedCities.setLayoutManager(linearLayoutManager);
        recyclerViewAddedCities.setHasFixedSize(true);
        addedCitiesAdapter = new AddedCitiesAdapter(weatherDataDao.getAllData(), Calendar.getInstance().getTime());
        recyclerViewAddedCities.setAdapter(addedCitiesAdapter);
        addedCitiesAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addedCitiesAdapter.setItems(weatherDataDao.getAllData());
    }

    @Override
    public void onItemClick(int mPosition, boolean flg) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("position", mPosition);
        intent.putExtra("cityList", flg);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}