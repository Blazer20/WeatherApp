package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.adapters.CitySearchAdapter;
import com.example.weatherapp.adapters.ViewPagerAdapter;
import com.example.weatherapp.api.ApiClient;
import com.example.weatherapp.customViews.CustomViewPager;
import com.example.weatherapp.models.cities.Cities;
import com.example.weatherapp.roomDB.weatherData.HourlyDataDao;
import com.example.weatherapp.roomDB.weatherData.WeatherDataBase;
import com.example.weatherapp.roomDB.weatherData.WeatherDataDao;
import com.example.weatherapp.viewmodels.StateViewModel;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements CitySearchAdapter.OnItemClickListener, View.OnClickListener {

    private static final String API_KEY = "0ea9812ad11649cdbfa144808202010";
    private static final String TAG = "MainActivity";

    private StateViewModel stateViewModel;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private CustomViewPager myViewPager;
    private ViewPagerAdapter myViewPagerAdapter;
    private WeatherDataBase database;
    private WeatherDataDao weatherDataDao;
    private HourlyDataDao hourlyDataDao;
    private SearchView searchView;
    private ImageButton imageButton;

    private RecyclerView recyclerViewCitySearch;
    private CitySearchAdapter citySearchAdapter;
    private List<String> fullLocationName = new ArrayList<>();
    private List<String> lat_lonList = new ArrayList<>();
    private String queryCity = null;
    private boolean myFlag, cityListFlag;
    private int cityListPosition;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        stateViewModel = new ViewModelProvider(this).get(StateViewModel.class);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imageButton = findViewById(R.id.cityList);
        database = WeatherDataBase.getInstance(getApplicationContext());
        weatherDataDao = database.weatherDataDao();
        hourlyDataDao = database.hourlyDataDao();
//        weatherDataDao.deleteTable();
//        hourlyDataDao.deleteTable();
        tabLayout = findViewById(R.id.dots);
        myViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), weatherDataDao.getAllData().size());
        myViewPager = findViewById(R.id.view_pager);
        myViewPager.setAdapter(myViewPagerAdapter);
        boolean first = false;
        if (weatherDataDao.getAllData().size() == 0) {
            myViewPagerAdapter.addPage(MainFragment.newInstance());
        } else {
            for (int i = 0; i < weatherDataDao.getAllData().size(); i++) {
                if (!first) {
                    myViewPagerAdapter.addPage(MainFragment.newInstance());
                    first = true;
                } else {
                    myViewPagerAdapter.addPage(MainFragment.newInstance(null, null, i + 1));
                    MainFragment.getCurrentPage(myViewPager.getCurrentItem());
                    myViewPager.setOffscreenPageLimit(myViewPagerAdapter.getCount());
                }
            }
            myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    MainFragment.getCurrentPage(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
        myViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(myViewPager);

        recyclerViewCitySearch = findViewById(R.id.city_search_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewCitySearch.setLayoutManager(layoutManager);
        recyclerViewCitySearch.setHasFixedSize(true);
        citySearchAdapter = new CitySearchAdapter();
        recyclerViewCitySearch.setAdapter(citySearchAdapter);

        String mCurrentDate = DateFormat.getTimeInstance(DateFormat.SHORT).
                format(Calendar.getInstance().getTime());
        if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 9
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 12) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.morning_toolbar)));
            recyclerViewCitySearch.setBackground(new ColorDrawable(getResources().getColor(R.color.morning_toolbar)));
            imageButton.setBackgroundResource(R.drawable.ic_baseline_list_24);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 12
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 15) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.day_toolbar)));
            recyclerViewCitySearch.setBackground(new ColorDrawable(getResources().getColor(R.color.day_toolbar)));
            imageButton.setBackgroundResource(R.drawable.ic_baseline_list_24);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 15
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 18) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.evening_toolbar)));
            recyclerViewCitySearch.setBackground(new ColorDrawable(getResources().getColor(R.color.evening_toolbar)));
            imageButton.setBackgroundResource(R.drawable.ic_baseline_list__24_white);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 18
                && Integer.parseInt(mCurrentDate.substring(0, 2)) > 0) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.prenight_toolbar)));
            recyclerViewCitySearch.setBackground(new ColorDrawable(getResources().getColor(R.color.prenight_toolbar)));
            imageButton.setBackgroundResource(R.drawable.ic_baseline_list__24_white);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 0
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 5) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fullnight_toolbar)));
            recyclerViewCitySearch.setBackground(new ColorDrawable(getResources().getColor(R.color.fullnight_toolbar)));
            imageButton.setBackgroundResource(R.drawable.ic_baseline_list__24_white);
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 5
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 9) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.premorning_toolbar)));
            recyclerViewCitySearch.setBackground(new ColorDrawable(getResources().getColor(R.color.premorning_toolbar)));
            imageButton.setBackgroundResource(R.drawable.ic_baseline_list__24_white);
        }

        imageButton.setOnClickListener(this);
        citySearchAdapter.setOnItemClickListener(this);

        listenViewState();
    }

    private void listenViewState() {
        stateViewModel.getMotionState().observe(this, new Observer<StateViewModel.MotionState>() {
            @Override
            public void onChanged(StateViewModel.MotionState motionState) {
                switch (motionState) {
                    case START:
                        myViewPager.setPageScrollEnabled(false);
                        break;
                    case IDLE:
                        myViewPager.setPageScrollEnabled(true);
                        break;
                }
                Log.e("listenViewState", motionState.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetwork() == null)
            return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        cityListFlag = data.getBooleanExtra("cityList", false);
        cityListPosition = data.getIntExtra("position", -1);
        myViewPager.setCurrentItem(cityListPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_elements, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        String mCurrentDate = DateFormat.getTimeInstance(DateFormat.SHORT).
                format(Calendar.getInstance().getTime());
        if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 9
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 12) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24));
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 12
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 15) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24));
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 15
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 18) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24_white));
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 18
                && Integer.parseInt(mCurrentDate.substring(0, 2)) > 0) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24_white));
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 0
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 5) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24_white));
        } else if (Integer.parseInt(mCurrentDate.substring(0, 2)) >= 5
                && Integer.parseInt(mCurrentDate.substring(0, 2)) < 9) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_baseline_search_24_white));
        }
        searchView = (SearchView) menuItem.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onQueryTextSubmit(String s) {
                myFlag = true;
                if (s != null && (s.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$") || s.matches("^\\p{L}*$"))) {
                    queryCity = s;
                    String language = Locale.getDefault().getLanguage();
                    LoadJsonC(s, language);
                    recyclerViewCitySearch.setVisibility(View.VISIBLE);
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong entry city!" + "\n" + "Try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onQueryTextChange(String s) {
                if (s != null && (s.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$") || s.matches("^\\p{L}*$"))) {
                    myFlag = false;
                    queryCity = s;
                    String language = Locale.getDefault().getLanguage();
                    LoadJsonC(s, language);
                    if (recyclerViewCitySearch.getVisibility() == View.GONE) {
                        recyclerViewCitySearch.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void LoadJsonC(String city, String lang) {

        if (isOnline()) {
            ApiClient.getInstance()
                    .getApiInterface()
                    .getCities(API_KEY, city, lang)
                    .enqueue(new Callback<List<Cities>>() {
                        @Override
                        public void onResponse(Call<List<Cities>> call, Response<List<Cities>> response) {
                            if (response.isSuccessful()
                                    && response.body() != null
                                    && !response.body().isEmpty()) {
                                for (Cities iterator : response.body()) {
                                    fullLocationName.add(iterator.getCitiesName());
                                    lat_lonList.add(iterator.getLat());
                                    lat_lonList.add(iterator.getLon());
                                }

                                if (fullLocationName.size() != 0) {
                                    citySearchAdapter.setItems(fullLocationName, lat_lonList, queryCity);
                                    fullLocationName.clear();
                                    lat_lonList.clear();
                                } else {
                                    citySearchAdapter.addItems(fullLocationName, lat_lonList, queryCity);
                                }
                            } else {
                                citySearchAdapter.setItems(fullLocationName, lat_lonList, queryCity);
                                fullLocationName.clear();
                                if (myFlag)
                                    Toast.makeText(getApplicationContext(), "No Result! \n" + "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Cities>> call, Throwable t) {

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Error! No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemClick(String lat, String lon, boolean add) {
        String mLat = lat;
        String mLon = lon;
        String ql = mLat + "," + mLon;

        if (cityListFlag || weatherDataDao.findExistence(ql)) {
            myViewPager.setCurrentItem(weatherDataDao.getCurrentPage(ql) - 1);
            searchView.setIconified(true);
            searchView.clearFocus();
        } else {
            MainFragment newFragment = MainFragment.newInstance(mLat, mLon, myViewPagerAdapter.getCount() + 1);
            myViewPagerAdapter.addPage(newFragment);
            myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    MainFragment.getCurrentPage(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            myViewPager.setOffscreenPageLimit(myViewPagerAdapter.getCount());
            myViewPager.setCurrentItem(myViewPagerAdapter.getCount());
            searchView.setIconified(true);
            searchView.clearFocus();
        }
        citySearchAdapter.setItems(fullLocationName, lat_lonList, queryCity);
        fullLocationName.clear();
        lat_lonList.clear();
        recyclerViewCitySearch.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cityList:
                Intent intent = new Intent(getApplicationContext(), CitiesList.class);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
