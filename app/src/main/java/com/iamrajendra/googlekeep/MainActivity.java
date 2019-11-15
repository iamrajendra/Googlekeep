package com.iamrajendra.googlekeep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iamrajendra.googlekeep.adapter.Adapter;
import com.iamrajendra.googlekeep.adapter.ItemDecorationAlbumColumns;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnCustomerListChangedListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnStartDragListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnCustomerListChangedListener,
        OnStartDragListener, View.OnClickListener {
private RecyclerView recyclerView;
private TextView textViewCounter;
private List<Model> selectedItems;
    private SimpleItemTouchHelperCallback simpleItemTouchHelperCallback;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedItems = new ArrayList<>();

        recyclerView = findViewById(R.id.gridView);
        textViewCounter = findViewById(R.id.number);
        findViewById(R.id.reset).setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.addItemDecoration(new ItemDecorationAlbumColumns(10,4));
        adapter = new Adapter(FakeData.getItems(),this,this);

         simpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        adapter.selectedItem.observe(this, new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> models) {
                selectedItems.clear();
                Log.i(MainActivity.class.getSimpleName(), "onChanged: ");

                for (Model model:models
                     ) {
                    if (model.isSelected()){
                        selectedItems.add(model);
                    }else {
                        selectedItems.remove(model);
                    }

                }

                if (selectedItems.size()>1){
                    simpleItemTouchHelperCallback.setLongPressed(false);
                }else {
                    simpleItemTouchHelperCallback.setLongPressed(true);
                }
                if (selectedItems.size()<=0){
                    Adapter.isSelection = false;
                     findViewById(R.id.select_layout).setVisibility(View.GONE);
                    findViewById(R.id.un_select_layout).setVisibility(View.VISIBLE);
                }else {
                    Adapter.isSelection = true;
                    findViewById(R.id.select_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.un_select_layout).setVisibility(View.GONE);
                }
                textViewCounter.setText(""+selectedItems.size());
            }

        });

    }

    @Override
    public void onNoteListChanged(List<Model> customers) {
        adapter.resetData(customers);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onClick(View v) {
        adapter.resetData(FakeData.getItems());

    }
}
