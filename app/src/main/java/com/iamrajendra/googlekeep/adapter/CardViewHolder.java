package com.iamrajendra.googlekeep.adapter;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.iamrajendra.googlekeep.MainActivity;
import com.iamrajendra.googlekeep.R;
import com.iamrajendra.googlekeep.adapter.draganddrop.ItemTouchHelperViewHolder;
import com.iamrajendra.googlekeep.model.Color;
import com.iamrajendra.googlekeep.model.Model;

import java.util.List;

import static com.squareup.picasso.Picasso.get;

public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,
        ItemTouchHelperViewHolder, View.OnClickListener {
    private TextView title, supporting_text;
//    private ImageView avatar_image;
        private ImageView   media_image;
    private Model model;
    private Adapter adapter;
    private int position;

    public CardViewHolder(@NonNull final View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_text);
//        subtitle_text = itemView.findViewById(R.id.subtitle_text);
        supporting_text = itemView.findViewById(R.id.supporting_text);
//        avatar_image = itemView.findViewById(R.id.avatar_image);
        media_image = itemView.findViewById(R.id.media_image);
        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void bind(int position, final Adapter adapter) {
        this.adapter = adapter;
        this.position = position;

        if (adapter.list.get(position).getColor()!=0){
            CardView cardView = (CardView) itemView;
            cardView.setCardBackgroundColor(itemView.getResources().getColor(adapter.list.get(position).getColor()));

        }
//        if (adapter.list.get(position).getAutherPhoto() != null)
//            get().load(Uri.parse(adapter.list.get(position).getAutherPhoto())).centerInside().resize(100, 100).into(avatar_image);
        if (adapter.list.get(position).getPhoto() != null) {
            get().load(Uri.parse(adapter.list.get(position).getPhoto())).centerInside().resize(600, 600).into(media_image);
            media_image.setVisibility(View.VISIBLE);
        }else {
            media_image.setVisibility(View.GONE);
        }
        title.setText(adapter.list.get(position).getTitle());
//        subtitle_text.setText(adapter.list.get(position).getAddedBy());
        supporting_text.setText(adapter.list.get(position).getDescription());
        title.setText(adapter.list.get(position).getTitle());
        adapter.selectedItem.setValue(adapter.list);



       final CardView carView = (CardView) itemView;

        if (adapter.list.get(position).isSelected()) {
            carView.setCardElevation(8);

        } else {
            carView.setCardElevation(0);
        }

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


        if (Adapter.itemMoved) {
            adapter.mListChangedListener.onNoteListChanged(adapter.list);
            Adapter.itemMoved = false;

        }
    }

    @Override
    public void onClick(View v) {
        if (Adapter.isSelection) {
            adapter.list.get(position).setSelected(adapter.list.get(position).isSelected() ? false : true);
            adapter.notifyItemChanged(position);
        } else {

            if (adapter.itemClickListener != null)
                adapter.itemClickListener.onItemClick(position, adapter.list.get(position));
        }
    }
}
