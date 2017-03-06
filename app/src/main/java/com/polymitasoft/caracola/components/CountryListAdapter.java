package com.polymitasoft.caracola.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polymitasoft.caracola.datamodel.Country;
import com.polymitasoft.caracola.util.Comparators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.polymitasoft.caracola.util.Comparators.ignoreCaseToStringComparator;

/**
 * @author rainermf
 * @since 6/3/2017
 */
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {

    private final List<Country> countries;

    public CountryListAdapter() {
        countries = Country.getCountries();
        Collections.sort(countries, ignoreCaseToStringComparator());
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        holder.setCountry(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public CountryViewHolder(TextView itemView) {
            super(itemView);
            text = itemView;
        }

        public CountryViewHolder setCountry(Country country) {
            text.setText(country.getName());
            return this;
        }
    }
}
