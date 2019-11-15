package com.iamrajendra.googlekeep.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.iamrajendra.googlekeep.Model;
import com.iamrajendra.googlekeep.R;
import com.iamrajendra.googlekeep.adapter.draganddrop.ItemTouchHelperAdapter;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnCustomerListChangedListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnStartDragListener;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<CardViewHolder> implements ItemTouchHelperAdapter {
    public List<Model> list;
    public MutableLiveData<List<Model>> selectedItem = new MutableLiveData<>();
    public OnStartDragListener mDragStartListener;
    public OnCustomerListChangedListener mListChangedListener;
    public  static boolean isSelection;
    public  static boolean itemMoved;

    public Adapter(List<Model> list, OnStartDragListener mDragStartListener, OnCustomerListChangedListener mListChangedListener) {
        this.list = list;
        this.mDragStartListener = mDragStartListener;
        this.mListChangedListener = mListChangedListener;
    }



    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,null));
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        holder.bind(position,Adapter.this);


    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.gird_view;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemMove( int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        itemMoved =true;
    }

    @Override
    public void onItemDismiss(int position) {
//        mListChangedListener.onNoteListChanged(list);
    }

    public void resetData(List<Model> customers) {
        list=reset(customers);
        notifyDataSetChanged();
    }

    private List<Model> reset(List<Model> customers) {
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setSelected(false);
        }
        return customers;
    }
}
