package com.example.android.corodate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.corodate.utilities.CoronaDataJsonUtils;
import com.example.android.corodate.utilities.NetworkUtils;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CoronaAdapter.CoronaAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<String>[]> {

    private static final int CORONA_COUNTRY_DATA_LOADER_ID = 1;

    public static final String novel_covi_url = "https://github.com/NovelCOVID/API";

    private RecyclerView coronaRecyclerView;
    private CoronaAdapter coronaAdapter;
    private TextView coronaErrorTextView;
    private ImageView coronaErrorImageView;
    private ProgressBar coronaProgressBar;

    public static final String INDEX_CORONA_COUNTRY = "1";
    public static final String INDEX_CORONA_CASES = "2";
    public static final String INDEX_CORONA_DEATHS = "3";
    public static final String INDEX_CORONA_RECOVERED = "4";
    public static final String INDEX_CORONA_FLAG = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        LoaderManager.LoaderCallbacks<ArrayList<String>[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(CORONA_COUNTRY_DATA_LOADER_ID, bundleForLoader, callback);

    }

    @Override
    public void onClick(ArrayList<String> coronaData) {

        Intent intent = new Intent(this, DetailActivity.class);

        StringBuilder builder = new StringBuilder();
        for(String data : coronaData) {
            builder.append(data);
            builder.append(" ");
        }
        builder.setLength(builder.length() - 1);

        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<ArrayList<String>[]> onCreateLoader(int i, @Nullable Bundle bundle) {

        return new AsyncTaskLoader<ArrayList<String>[]>(this) {

            ArrayList<String>[] coronaCountryData = null;

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
            public ArrayList<String>[] loadInBackground() {
                URL coronaCountryDataRequestUrl = NetworkUtils.buildUrl(null);

                try {
                    String jsonCoronaCountryDataResponse = NetworkUtils
                            .getResponseFromHttpUrl(coronaCountryDataRequestUrl);

                    ArrayList<String>[] coronaCountryData = CoronaDataJsonUtils
                            .getSimpleCoronaCountryDataStringsFromJson(jsonCoronaCountryDataResponse);

                    return coronaCountryData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable ArrayList<String>[] data) {
                coronaCountryData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>[]> loader, ArrayList<String>[] data) {
        coronaProgressBar.setVisibility(View.INVISIBLE);
        coronaAdapter.setCoronaCountryData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>[]> loader) {

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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.corona_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if(R.id.action_refresh == itemId) {
            coronaErrorTextView.setVisibility(View.INVISIBLE);
            coronaErrorImageView.setVisibility(View.INVISIBLE);
            if(isConnected()) {
                invalidateData();
                getSupportLoaderManager().restartLoader(CORONA_COUNTRY_DATA_LOADER_ID, null, this);
            } else {
                showErrorMessage();
            }
            return true;
        }  else if (R.id.action_source == itemId) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(novel_covi_url));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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