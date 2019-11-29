package com.iamrajendra.googlekeep.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.iamrajendra.googlekeep.adapter.draganddrop.OnItemClickListener;
import com.iamrajendra.googlekeep.model.Model;
import com.iamrajendra.googlekeep.R;
import com.iamrajendra.googlekeep.adapter.draganddrop.ItemTouchHelperAdapter;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnCustomerListChangedListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnStartDragListener;
import com.iamrajendra.googlekeep.model.Todo;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<CardViewHolder> implements ItemTouchHelperAdapter {
    public List<Todo> list;
    public MutableLiveData<List<Todo>> selectedItem = new MutableLiveData<>();
    public OnStartDragListener mDragStartListener;
    public OnCustomerListChangedListener mListChangedListener;
    public  static boolean isSelection;
    public  static boolean itemMoved;
    public OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public Adapter(List<Todo> list, OnStartDragListener mDragStartListener, OnCustomerListChangedListener mListChangedListener) {
        this.list = list;
        this.mDragStartListener = mDragStartListener;
        this.mListChangedListener = mListChangedListener;
    }



    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        holder.bind(position,Adapter.this);


    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.row;
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

    public void resetData(List<Todo> customers) {
        list=reset(customers);
        notifyDataSetChanged();
    }

    private List<Todo> reset(List<Todo> customers) {
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setSelected(false);
        }
        return customers;
    }
}
