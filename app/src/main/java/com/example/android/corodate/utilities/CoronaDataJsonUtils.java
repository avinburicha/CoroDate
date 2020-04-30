package com.example.android.corodate.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.corodate.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class CoronaDataJsonUtils {

    private static final String CORONA_JSON_COUNTRY = "country";
    private static final String CORONA_JSON_CASES = "cases";
    private static final String CORONA_JSON_DEATHS = "deaths";
    private static final String CORONA_JSON_RECOVERED = "recovered";
    private static final String CORONA_JSON_ACTIVE = "active";
    private static final String CORONA_JSON_CASES_TODAY = "todayCases";
    private static final String CORONA_JSON_DEATHS_TODAY = "todayDeaths";

    private static final String CORONA_JSON_COUNTRY_INFO = "countryInfo";
    private static final String CORONA_JSON_FLAG = "flag";

    public static ArrayList<String>[] getSimpleCoronaCountryDataStringsFromJson(String jsonCoronaCountryDataStr)
            throws JSONException {

        ArrayList<String>[] parsedCoronaData = null;

        if(jsonCoronaCountryDataStr != null) {
            JSONArray coronaCountryDataArray = new JSONArray(jsonCoronaCountryDataStr);

            parsedCoronaData = new ArrayList[coronaCountryDataArray.length()];

            for (int i = 0; i < coronaCountryDataArray.length(); i++) {
                String country;
                int cases;
                int deaths;
                int recovered;
                int active;
                int casesToday;
                int deathsToday;
                String flag;

                JSONObject coronaData = coronaCountryDataArray.getJSONObject(i);

                country = coronaData.getString(CORONA_JSON_COUNTRY);
                cases = coronaData.getInt(CORONA_JSON_CASES);
                deaths = coronaData.getInt(CORONA_JSON_DEATHS);
                recovered = coronaData.getInt(CORONA_JSON_RECOVERED);
                active = coronaData.getInt(CORONA_JSON_ACTIVE);
                casesToday = coronaData.getInt(CORONA_JSON_CASES_TODAY);
                deathsToday = coronaData.getInt(CORONA_JSON_DEATHS_TODAY);
                flag = coronaData.getJSONObject(CORONA_JSON_COUNTRY_INFO).getString(CORONA_JSON_FLAG);

                ArrayList<String> coronaDataList = new ArrayList<>();
                coronaDataList.add(country);
                coronaDataList.add(String.valueOf(cases));
                coronaDataList.add(String.valueOf(deaths));
                coronaDataList.add(String.valueOf(recovered));
                coronaDataList.add(String.valueOf(active));
                coronaDataList.add(String.valueOf(casesToday));
                coronaDataList.add(String.valueOf(deathsToday));
                coronaDataList.add(flag);

                parsedCoronaData[i] = coronaDataList;
            }
        }

        return parsedCoronaData;
    }

}
