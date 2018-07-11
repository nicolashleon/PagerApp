package com.pager.teamapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.pager.teamapp.ui.delegates.DelegateAdapter;
import com.pager.teamapp.ui.models.DelegateUIModel;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Code adapted from https://github.com/sockeqwe/AdapterDelegates
 * http://hannesdorfmann.com/android/adapter-delegates
 */
public class BaseAdapter extends RecyclerView.Adapter {

    protected SparseArray<DelegateAdapter> delegateAdapters = new SparseArray<>();
    protected List<DelegateUIModel> delegateUIModels = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        DelegateAdapter delegateAdapter = delegateAdapters.get(viewType);

        if (delegateAdapter != null) {
            return delegateAdapter.onCreateViewHolder(parent, viewType);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return delegateUIModels.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return delegateUIModels.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DelegateAdapter delegateAdapter = delegateAdapters.get(getItemViewType(position));
        if (delegateAdapter != null) {
            delegateAdapter.onBindViewHolder(holder, delegateUIModels.get(position));
        }
    }

    public void addDelegate(DelegateAdapter delegateAdapter, int viewType) {
        delegateAdapters.put(viewType, delegateAdapter);
    }

    public <T extends DelegateUIModel> T getItem(int pos) {
        return (T) delegateUIModels.get(pos);
    }

    public void removeItemsAndNotify(DelegateUIModel uiModel) {
        int posToRemove = delegateUIModels.indexOf(uiModel);
        if (posToRemove != NO_POSITION) {
            delegateUIModels.remove(posToRemove);
            notifyItemRemoved(posToRemove);
        }
    }

    public void clearItems() {
        delegateUIModels.clear();
    }

    public void addAllItemsAndNotify(List<? extends DelegateUIModel> uiModels) {
        int size = delegateUIModels.size();
        delegateUIModels.addAll(uiModels);
        notifyItemRangeInserted(size, uiModels.size());
    }

    public void addItemAndNotify(DelegateUIModel uiModel) {
        int size = delegateUIModels.size();
        delegateUIModels.add(uiModel);
        notifyItemInserted(size);
    }

    public boolean isEmpty() {
        return delegateUIModels.isEmpty();
    }

    public void clearItemsAndNotify() {
        delegateUIModels.clear();
        notifyDataSetChanged();
    }

}
