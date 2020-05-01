package com.example.android.corodate;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.corodate.model.Corona;
import com.example.android.corodate.utilities.CoronaDataJsonUtils;
import com.example.android.corodate.utilities.NetworkUtils;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CoronaAdapter.CoronaAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Corona>> {

    private static final int CORONA_COUNTRY_DATA_LOADER_ID = 1;

    public static final String novel_covi_url = "https://github.com/NovelCOVID/API";

    private RecyclerView coronaRecyclerView;
    private CoronaAdapter coronaAdapter;
    private TextView coronaErrorTextView;
    private ImageView coronaErrorImageView;
    private ProgressBar coronaProgressBar;
    private SearchView searchView;

    public static final String sort_by_country = "country_sort";
    public static final String sort_by_cases = "cases_sort";
    public static final String sort_by_deaths = "deaths_sort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coronaRecyclerView = findViewById(R.id.corona_recyclerview);
        coronaErrorTextView = findViewById(R.id.corona_error);
        coronaErrorImageView = findViewById(R.id.corona_error_image);
        coronaErrorImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        coronaProgressBar = findViewById(R.id.corona_progress_bar);
        coronaProgressBar.setIndeterminateDrawable(new DoubleBounce());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        coronaRecyclerView.setLayoutManager(layoutManager);
        coronaRecyclerView.setHasFixedSize(true);

        coronaAdapter = new CoronaAdapter(this, this, Glide.with(this));
        coronaRecyclerView.setAdapter(coronaAdapter);

        LoaderManager.LoaderCallbacks<ArrayList<Corona>> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(CORONA_COUNTRY_DATA_LOADER_ID, bundleForLoader, callback);

    }

    @Override
    public void onClick(Corona coronaData) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, CoronaDataJsonUtils.gson.toJson(coronaData));
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Corona>> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new AsyncTaskLoader<ArrayList<Corona>>(this) {

            ArrayList<Corona> coronaCountryData = null;

            @Override
            protected void onStartLoading() {
                if (coronaCountryData != null) {
                    deliverResult(coronaCountryData);
                } else {
                    coronaProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Corona> loadInBackground() {
                URL coronaCountryDataRequestUrl = NetworkUtils.buildUrl(null);

                try {
                    String jsonCoronaCountryDataResponse = NetworkUtils
                            .getResponseFromHttpUrl(coronaCountryDataRequestUrl);

                    ArrayList<Corona> coronaCountryData = CoronaDataJsonUtils
                            .getSimpleCoronaCountryDataStringsFromJson(jsonCoronaCountryDataResponse);

                    return coronaCountryData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable ArrayList<Corona> data) {
                coronaCountryData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Corona>> loader, ArrayList<Corona> data) {
        coronaProgressBar.setVisibility(View.INVISIBLE);
        coronaAdapter.setCoronaCountryData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Corona>> loader) {

    }

    private void showErrorMessage() {
        coronaErrorTextView.setVisibility(View.VISIBLE);
        coronaErrorImageView.setVisibility(View.VISIBLE);
        coronaRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showJsonDataView() {
        coronaRecyclerView.setVisibility(View.VISIBLE);
        coronaErrorTextView.setVisibility(View.INVISIBLE);
        coronaErrorImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.corona_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                coronaAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                coronaAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (R.id.action_search == itemId) {
            return true;
        } else if(R.id.action_refresh == itemId) {
            coronaErrorTextView.setVisibility(View.INVISIBLE);
            coronaErrorImageView.setVisibility(View.INVISIBLE);
            if(isConnected()) {
                invalidateData();
                getSupportLoaderManager().restartLoader(CORONA_COUNTRY_DATA_LOADER_ID, null, this);
            } else {
                showErrorMessage();
            }
            return true;
        } else if(R.id.action_sort_country_asc == itemId) {
            coronaAdapter.sortCoronaData(true, sort_by_country);
        } else if(R.id.action_sort_country_desc == itemId) {
            coronaAdapter.sortCoronaData(false, sort_by_country);
        } else if(R.id.action_sort_cases_asc == itemId) {
            coronaAdapter.sortCoronaData(true, sort_by_cases);
        } else if(R.id.action_sort_cases_desc == itemId) {
            coronaAdapter.sortCoronaData(false, sort_by_cases);
        } else if(R.id.action_sort_deaths_asc == itemId) {
            coronaAdapter.sortCoronaData(true, sort_by_deaths);
        } else if(R.id.action_sort_deaths_desc == itemId) {
            coronaAdapter.sortCoronaData(false, sort_by_deaths);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void invalidateData() {
        coronaAdapter.setCoronaCountryData(null);
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

}