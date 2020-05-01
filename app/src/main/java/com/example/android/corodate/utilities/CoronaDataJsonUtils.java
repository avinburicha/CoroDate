package com.example.android.corodate.utilities;

import com.example.android.corodate.model.Corona;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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

    public static Gson gson = new Gson();

    public static ArrayList<Corona> getSimpleCoronaCountryDataStringsFromJson(String jsonCoronaCountryDataStr)
            throws JSONException {

        ArrayList<Corona> parsedCoronaData = null;

        if(jsonCoronaCountryDataStr != null) {
            try {
                parsedCoronaData = new ArrayList<>();
                parsedCoronaData = gson.fromJson(jsonCoronaCountryDataStr.toString(),
                        new TypeToken<List<Corona>>() {}.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return parsedCoronaData;
    }

}
