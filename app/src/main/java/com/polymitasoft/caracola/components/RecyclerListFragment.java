package com.polymitasoft.caracola.components;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;

import java.util.EnumSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.android.QueryRecyclerAdapter;

import static butterknife.ButterKnife.findById;

/**
 * @author rainermf
 * @since 27/2/2017
 */

public abstract class RecyclerListFragment<T, L> extends Fragment {

    private L mListener;
    private QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter;
    @BindView(R.id.fab) FloatingActionButton fab;
    private EnumSet<RecyclerListActivity.Options> options;

    protected abstract QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> createAdapter();

    @Override
    public void onResume() {
        adapter.queryAsync();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_items, container, false);
        ButterKnife.bind(this, view);

        if (getOptions().contains(RecyclerListActivity.Options.ADD_MENU)) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionPlusMenu();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        Context context = view.getContext();
        RecyclerView recyclerView = findById(view, R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = createAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (L) context;
        } catch (ClassCastException e){
            throw new RuntimeException(context.toString()
                    + " must implement OnListInteractionListener");
        }
    }

    protected void onActionPlusMenu() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected EnumSet<RecyclerListActivity.Options> removedDefaults() {
        return EnumSet.noneOf(RecyclerListActivity.Options.class);
    }

    private EnumSet<RecyclerListActivity.Options> getOptions() {
        if (options == null) {
            options = EnumSet.complementOf(removedDefaults());
        }
        return options;
    }

    public enum Options {
        ADD_MENU
    }
}
