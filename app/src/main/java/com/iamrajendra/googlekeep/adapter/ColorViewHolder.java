package com.iamrajendra.googlekeep.adapter;



import com.iamrajendra.googlekeep.R;
import com.iamrajendra.googlekeep.model.Color;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private int pos;
    private ColorAdapter adapter;

    public ColorViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        invertList(adapter.colors,pos);

        adapter.getLiveData().setValue(adapter.colors.get(pos));
        adapter.notifyDataSetChanged();

    }

    private void invertList(List<Color> colors, int pos) {
        for (int i = 0; i < colors.size(); i++) {

            if (i==pos) {
                colors.get(i).setSelected(true);
            }else {
                colors.get(i).setSelected(false);

            }
        }
    }

    public void bind(int position, ColorAdapter colorAdapter) {
        pos = position;
        adapter = colorAdapter;
        GradientDrawable drawable = (GradientDrawable) itemView.getBackground().mutate();
        if (colorAdapter.colors.get(position).isSelected()) drawable.setStroke(5, itemView.getResources().getColor(R.color.md_black_1000));
        else drawable.setStroke(5, itemView.getResources().getColor(R.color.transparent));
        drawable.setColor(itemView.getContext().getResources().getColor(colorAdapter.colors.get(position).getCode()));
    }
}
