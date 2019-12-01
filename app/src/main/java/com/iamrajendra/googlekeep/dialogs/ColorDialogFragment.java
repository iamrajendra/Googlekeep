package com.iamrajendra.googlekeep.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iamrajendra.googlekeep.Colors;
import com.iamrajendra.googlekeep.R;
import com.iamrajendra.googlekeep.adapter.ColorAdapter;
import com.iamrajendra.googlekeep.model.Color;

import java.util.List;

public class ColorDialogFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private ColorAdapter colorAdapter;
    private MutableLiveData<Color> liveColor =  new MutableLiveData<>();

    public MutableLiveData<Color> getLiveColor() {
        return liveColor;
    }

    public ColorAdapter getColorAdapter() {
        return colorAdapter;
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.color_dialog,container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.color_list);
        colorAdapter = new ColorAdapter(Colors.getColors());
        colorAdapter.getLiveData().observe(getActivity(), new Observer<Color>() {
            @Override
            public void onChanged(Color colors) {
              liveColor.setValue(colors);
              dismiss();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setAdapter(colorAdapter);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
               getDialog().getWindow().setLayout(400, 400);

            }

        });



    }


}
