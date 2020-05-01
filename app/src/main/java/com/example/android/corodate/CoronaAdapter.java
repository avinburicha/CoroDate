package com.example.android.corodate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.android.corodate.model.Corona;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CoronaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int HEADER_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private ArrayList<Corona> coronaCountryData;
    private ArrayList<Corona> coronaCountryDataFiltered;
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

            Corona coronaData = coronaCountryDataFiltered.get(position-1);

            String country = coronaData.getCountry();
            String cases = String.valueOf(coronaData.getCases());
            String deaths = String.valueOf(coronaData.getDeaths());
            String recovered = String.valueOf(coronaData.getRecovered());
            String flag = coronaData.getCountryInfo().getFlag();

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
        if (null == coronaCountryDataFiltered)
            return 0;
        return coronaCountryDataFiltered.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW;
        }
        return ITEM_VIEW;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    coronaCountryDataFiltered = coronaCountryData;
                } else {
                    ArrayList<Corona> coronaList = coronaCountryData;
                    ArrayList<Corona> filteredList = new ArrayList<>();
                    for (Corona coronaData : coronaList) {

                        if (coronaData.getCountry().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(coronaData);
                        }
                    }

                    coronaCountryDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = coronaCountryDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                coronaCountryDataFiltered = (ArrayList<Corona>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CoronaAdapterOnClickHandler {
        void onClick(Corona coronaData);
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
            Corona coronaData = coronaCountryDataFiltered.get(adapterPosition-1);
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

    public void setCoronaCountryData(ArrayList<Corona> coronaCountryData) {
        this.coronaCountryData = coronaCountryData;
        this.coronaCountryDataFiltered = coronaCountryData;
        notifyDataSetChanged();
    }

    public void sortCoronaData(boolean isAsc, String sortOn) {

        if (MainActivity.sort_by_country.equals(sortOn)) {
            if(isAsc)
                Collections.sort(coronaCountryDataFiltered, countriesAsc);
            else
                Collections.sort(coronaCountryDataFiltered, countriesDesc);
        } else if (MainActivity.sort_by_cases.equals(sortOn)) {
            if(isAsc)
                Collections.sort(coronaCountryDataFiltered, casesAsc);
            else
                Collections.sort(coronaCountryDataFiltered, casesDesc);
        } else if (MainActivity.sort_by_deaths.equals(sortOn)) {
            if(isAsc)
                Collections.sort(coronaCountryDataFiltered, deathsAsc);
            else
                Collections.sort(coronaCountryDataFiltered, deathsDesc);
        }

        notifyDataSetChanged();
    }

    Comparator<Corona> countriesAsc = new Comparator<Corona>() {
        @Override
        public int compare(Corona c1, Corona c2) {
            return c1.getCountry().compareTo(c2.getCountry());
        }
    };

    Comparator<Corona> countriesDesc = new Comparator<Corona>() {
        @Override
        public int compare(Corona c1, Corona c2) {
            return c2.getCountry().compareTo(c1.getCountry());
        }
    };

    Comparator<Corona> casesAsc = new Comparator<Corona>() {
        @Override
        public int compare(Corona c1, Corona c2) {
            if(c1.getCases() == c2.getCases()) {
                return c1.getCountry().compareTo(c2.getCountry());
            } else {
                if(c1.getCases() > c2.getCases())
                    return 1;
                else
                    return -1;
            }
        }
    };

    Comparator<Corona> casesDesc = new Comparator<Corona>() {
        @Override
        public int compare(Corona c1, Corona c2) {
            if(c1.getCases() == c2.getCases()) {
                return c1.getCountry().compareTo(c2.getCountry());
            } else {
                if(c1.getCases() < c2.getCases())
                    return 1;
                else
                    return -1;
            }
        }
    };

    Comparator<Corona> deathsAsc = new Comparator<Corona>() {
        @Override
        public int compare(Corona c1, Corona c2) {
            if(c1.getDeaths() == c2.getDeaths()) {
                return c1.getCountry().compareTo(c2.getCountry());
            } else {
                if(c1.getDeaths() > c2.getDeaths())
                    return 1;
                else
                    return -1;
            }
        }
    };

    Comparator<Corona> deathsDesc = new Comparator<Corona>() {
        @Override
        public int compare(Corona c1, Corona c2) {
            if(c1.getDeaths() == c2.getDeaths()) {
                return c1.getCountry().compareTo(c2.getCountry());
            } else {
                if(c1.getDeaths() < c2.getDeaths())
                    return 1;
                else
                    return -1;
            }
        }
    };

}