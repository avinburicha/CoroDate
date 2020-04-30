package com.example.android.corodate;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.corodate.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private String countryCoronaData;
    private TextView coronaCountryDataTextView;

    private ActivityDetailBinding coronaDetailBinding;

    private static final String SHARE_DESCRIPTION = "#CORODATE - Live COVID-19 Updates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coronaDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                countryCoronaData = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                String[] coronaData = countryCoronaData.split(" ");

                String country = coronaData[0];
                String cases = coronaData[1];
                String deaths = coronaData[2];
                String recovered = coronaData[3];
                String active = coronaData[4];
                String casesToday = coronaData[5];
                String deathsToday = coronaData[6];
                String flag = coronaData[7];

                coronaDetailBinding.primaryInfo.primaryInfoCountry.setText(country);
                coronaDetailBinding.extraInfo.casesCount.setText(cases);
                coronaDetailBinding.extraInfo.deathsCount.setText(deaths);
                coronaDetailBinding.extraInfo.recoveredCount.setText(recovered);
                coronaDetailBinding.extraInfo.activeCount.setText(active);
                coronaDetailBinding.extraInfo.casesTodayCount.setText(casesToday);
                coronaDetailBinding.extraInfo.deathsTodayCount.setText(deathsToday);
                ImageView imageView = coronaDetailBinding.primaryInfo.countryFlag;
                Glide.with(this).load(flag).into(imageView);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareCoronaDataIntent());
        return true;
    }

    private Intent createShareCoronaDataIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("Country : " + coronaDetailBinding.primaryInfo.primaryInfoCountry.getText() + "\n"
                        + "Cases : " +  coronaDetailBinding.extraInfo.casesCount.getText() + "\n"
                        + "Deaths : " +  coronaDetailBinding.extraInfo.deathsCount.getText() + "\n"
                        + "Recovered : " +  coronaDetailBinding.extraInfo.recoveredCount.getText() + "\n"
                        + "Cases Today : " +  coronaDetailBinding.extraInfo.casesTodayCount.getText() + "\n"
                        + "Deaths Today : " +  coronaDetailBinding.extraInfo.deathsTodayCount.getText() + "\n"
                        + "Active : " +  coronaDetailBinding.extraInfo.activeCount.getText() + "\n\n"
                        + SHARE_DESCRIPTION)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

}

