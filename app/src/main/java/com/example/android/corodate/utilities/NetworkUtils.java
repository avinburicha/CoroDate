package com.example.android.corodate.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String CORONA_COUNTRIES_DATA_URL = "https://corona.lmao.ninja/v2/countries";

    public static final String CORONA_SORT_PARAM = "sort";
    public static final String CORONA_DEFAULT_SORT_PARAM = "cases";

    public static URL buildUrl(String sortBy) {

        if(sortBy == null || "".equals(sortBy))
            sortBy = CORONA_DEFAULT_SORT_PARAM;

        Uri builtUri = Uri.parse(CORONA_COUNTRIES_DATA_URL).buildUpon()
                .appendQueryParameter(CORONA_SORT_PARAM, sortBy)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(urlConnection != null)
                    urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
