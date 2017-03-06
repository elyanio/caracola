package com.polymitasoft.caracola.components;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Country;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.layout.simple_dropdown_item_1line;
import static com.polymitasoft.caracola.datamodel.Country.getCountries;
import static com.polymitasoft.caracola.util.Comparators.ignoreCaseToStringComparator;

/**
 * @author rainermf
 * @since 6/3/2017
 */

public class CountryListDialog {

    private AlertDialog.Builder builder;
    @BindView(R.id.countryList) RecyclerView countryList;
    @BindView(R.id.searchBox) AutoCompleteTextView searchBox;

    public CountryListDialog(Context context) {
        this.builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.country_list_view, null);
        ButterKnife.bind(this, view);
        countryList.setLayoutManager(new LinearLayoutManager(context));
        countryList.setAdapter(new CountryListAdapter());

        final List<Country> countries = getCountries();
        final Comparator<Country> countryComparator = ignoreCaseToStringComparator();
        Collections.sort(countries, countryComparator);
        final ArrayAdapter<Country> adapter = new ArrayAdapter<>(context, simple_dropdown_item_1line, countries);
        searchBox.setAdapter(adapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country country = adapter.getItem(position);
                int listPosition = Collections.binarySearch(countries, country, countryComparator);
                countryList.smoothScrollToPosition(listPosition);
            }
        });

        builder.setView(view);
    }

    public void show() {
        builder.show();
    }
}
