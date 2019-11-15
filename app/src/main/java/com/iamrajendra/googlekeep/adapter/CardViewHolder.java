package com.iamrajendra.googlekeep.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iamrajendra.googlekeep.Model;
import com.iamrajendra.googlekeep.R;
import com.iamrajendra.googlekeep.adapter.draganddrop.ItemTouchHelperViewHolder;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,
        ItemTouchHelperViewHolder, View.OnClickListener {
    private TextView textView;
    private ImageView imageView;
    private Model model;
    private Adapter adapter;
    private int position;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.name);
        imageView = itemView.findViewById(R.id.image);
        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void bind(int position, final Adapter adapter) {
        this.adapter = adapter;
        this.position = position;
        textView.setText(adapter.list.get(position).getTitle());
        adapter.selectedItem.setValue(adapter.list);


        Drawable drawable=null;

        if (adapter.list.get(position).isSelected()){

            drawable = itemView.getContext().getDrawable(R.drawable.select);
        }else {
            drawable = itemView.getContext().getDrawable(R.drawable.un_select);
        }

        imageView.setImageDrawable(drawable);


        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    adapter.mDragStartListener.onStartDrag(CardViewHolder.this);
                }
                return false;
            }
        });
}

    @Override
    public boolean onLongClick(View v) {
if (!Adapter.isSelection) {
    adapter.list.get(position).setSelected(adapter.list.get(position).isSelected() ? false : true);
    adapter.notifyItemChanged(position);
}
        return false;
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {
        Log.i("rajendra", "onItemClear: ");
     if(Adapter.itemMoved) {
         adapter.mListChangedListener.onNoteListChanged(adapter.list);
         Adapter.itemMoved = false;
     }
    }

    @Override
    public void onClick(View v) {
        if (Adapter.isSelection) {
            adapter.list.get(position).setSelected(adapter.list.get(position).isSelected() ? false : true);
            adapter.notifyItemChanged(position);
        }
    }
}
