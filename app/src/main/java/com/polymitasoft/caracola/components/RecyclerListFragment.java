package com.polymitasoft.caracola.components;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;

import io.requery.android.QueryRecyclerAdapter;

import static butterknife.ButterKnife.findById;

/**
 * @author rainermf
 * @since 27/2/2017
 */

public abstract class RecyclerListFragment<T> extends Fragment {

    private OnListInteractionListener<T> mListener;
    private QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter;

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
        if (context instanceof OnListInteractionListener) {
            mListener = (OnListInteractionListener<T>) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListInteractionListener<T> {
        void onClientListInteraction(T item);
    }
}
