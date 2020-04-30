package com.example.android.corodate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.Map;

public class CoronaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private ArrayList<String>[] coronaCountryData;
    private Context context;
    private RequestManager manager;

    private final CoronaAdapterOnClickHandler coronaItemClickHandler;

    public CoronaAdapter(Context context, CoronaAdapterOnClickHandler coronaItemClickHandler, RequestManager manager) {
        this.context = context;
        this.coronaItemClickHandler = coronaItemClickHandler;
        this.manager = manager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == HEADER_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.corona_data_item_header, viewGroup, false);
            view.setFocusable(true);
            return new HeaderViewHolder(view);
        } else if (viewType == ITEM_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.corona_data_item, viewGroup, false);
            view.setFocusable(true);
            return new CoronaAdapterViewHolder(view);
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof CoronaAdapterViewHolder) {
            CoronaAdapterViewHolder coronaAdapterViewHolder = (CoronaAdapterViewHolder) viewHolder;

            ArrayList<String> coronaData = coronaCountryData[position-1];

            String country = coronaData.get(0);
            String cases = coronaData.get(1);
            String deaths = coronaData.get(2);
            String recovered = coronaData.get(3);
            String active = coronaData.get(4);
            String casesToday = coronaData.get(5);
            String deathsToday = coronaData.get(6);
            String flag = coronaData.get(7);

            manager.load(flag).into(coronaAdapterViewHolder.countryImage);
            coronaAdapterViewHolder.country.setText(country);
            coronaAdapterViewHolder.cases.setText(cases);
            coronaAdapterViewHolder.deaths.setText(deaths);
            coronaAdapterViewHolder.recovered.setText(recovered);

        } else if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

            headerViewHolder.countryImage.setText(R.string.country_flag);
            headerViewHolder.country.setText(R.string.pref_country_label);
            headerViewHolder.casesImage.setImageResource(R.drawable.ic_cases);
            headerViewHolder.deathsImage.setImageResource(R.drawable.ic_deaths);
            headerViewHolder.recoveredImage.setImageResource(R.drawable.ic_recovered);

        }
    }

    @Override
    public int getItemCount() {
        if (null == coronaCountryData)
            return 0;
        return coronaCountryData.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW;
        }
        return ITEM_VIEW;
    }

    public interface CoronaAdapterOnClickHandler {
        void onClick(ArrayList<String> coronaData);
    }

    public class CoronaAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView countryImage;
        public final TextView country;
        public final TextView cases;
        public final TextView deaths;
        public final TextView recovered;

        public CoronaAdapterViewHolder(View view) {
            super(view);

            countryImage = view.findViewById(R.id.countryImage);
            country = view.findViewById(R.id.country_name);
            cases = view.findViewById(R.id.cases_count);
            deaths = view.findViewById(R.id.deaths_count);
            recovered = view.findViewById(R.id.recovered_count);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ArrayList<String> coronaData = coronaCountryData[adapterPosition-1];
            coronaItemClickHandler.onClick(coronaData);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public final TextView countryImage;
        public final TextView country;
        public final ImageView casesImage;
        public final ImageView deathsImage;
        public final ImageView recoveredImage;

        public HeaderViewHolder(View view) {
            super(view);

            countryImage = view.findViewById(R.id.countryImage);
            country = view.findViewById(R.id.country_name);
            casesImage = view.findViewById(R.id.image_cases);
            deathsImage = view.findViewById(R.id.image_deaths);
            recoveredImage = view.findViewById(R.id.image_recovered);
        }
    }

    public void setCoronaCountryData(ArrayList<String>[] coronaCountryData) {
        this.coronaCountryData = coronaCountryData;
        notifyDataSetChanged();
    }
}