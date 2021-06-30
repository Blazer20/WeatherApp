package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp.adapters.CountriesAdapter;
import java.util.ArrayList;
import java.util.List;

public class Countries extends AppCompatActivity implements CountriesAdapter.OnItemClickListener {
    private CountriesAdapter adapter;
    private List<String> cityList = new ArrayList();
    private RecyclerView recyclerView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_countries);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.countries);
        this.recyclerView = recyclerView2;
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.recyclerView.setHasFixedSize(true);
        addCountries();
        CountriesAdapter countriesAdapter = new CountriesAdapter();
        this.adapter = countriesAdapter;
        this.recyclerView.setAdapter(countriesAdapter);
        this.adapter.addItems(this.cityList);
        this.adapter.setOnItemClickListener(this);
    }

    public void addCountries() {
        this.cityList.add("Tashkent");
        this.cityList.add("Moscow");
        this.cityList.add("London");
        this.cityList.add("Berlin");
        this.cityList.add("Minsk");
        this.cityList.add("Athens");
        this.cityList.add("Astana");
        this.cityList.add("Helsinki");
        this.cityList.add("Stockholm");
        this.cityList.add("Oslo");
    }

    public void onItemClick(String str) {
        Log.e("Item", "" + str);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("city", str);
        setResult(-1, intent);
        finish();
    }
}
