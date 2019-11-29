package com.iamrajendra.googlekeep;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iamrajendra.googlekeep.adapter.Adapter;
import com.iamrajendra.googlekeep.adapter.ItemDecorationAlbumColumns;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnCustomerListChangedListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnItemClickListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.OnStartDragListener;
import com.iamrajendra.googlekeep.adapter.draganddrop.SimpleItemTouchHelperCallback;
import com.iamrajendra.googlekeep.firebase.DatabaseManger;
import com.iamrajendra.googlekeep.firebase.GoogleAuthentication;
import com.iamrajendra.googlekeep.model.Model;
import com.iamrajendra.googlekeep.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnCustomerListChangedListener,
        OnStartDragListener, View.OnClickListener, OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
private TextView textViewCounter;
private List<Todo> selectedItems;
    private SimpleItemTouchHelperCallback simpleItemTouchHelperCallback;
    private Adapter adapter;
    private DatabaseManger manger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedItems = new ArrayList<>();
        GoogleAuthentication.getInstance(this).getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                manger=    new DatabaseManger(user);
                manger.getLiveTodoList().observe(MainActivity.this, new Observer<List<Todo>>() {
                    @Override
                    public void onChanged(List<Todo> todos) {
                        adapter.resetData(todos);
                    }
                });
            }
        });
        recyclerView = findViewById(R.id.gridView);
        textViewCounter = findViewById(R.id.number);
        findViewById(R.id.reset).setOnClickListener(this);
        findViewById(R.id.addNotes).setOnClickListener(this);
        findViewById(R.id.delete_tv).setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new Adapter(new ArrayList<Todo>() {
        }, this, this);
        adapter.setItemClickListener(this);

         simpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        adapter.selectedItem.observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> models) {
                selectedItems.clear();
                Log.i(MainActivity.class.getSimpleName(), "onChanged: ");

                for (Todo model:models
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
    public void onNoteListChanged(List<Todo> customers) {
        adapter.resetData(customers);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onBackPressed() {
        if (selectedItems.isEmpty())
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset:
//                adapter.resetData(new);
                break;
            case R.id.addNotes:
                Intent intent = new Intent(this, AddNotesActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_tv:
                if (selectedItems.size()==1 && selectedItems.size()!=0)
                manger.delete(selectedItems.get(0).getKey());
                else Toast.makeText(getApplicationContext()," Deleting collections from an Android client is not recommended. ",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(int position, Object o) {
        Todo todo = (Todo) o;
        Log.i(TAG, "onItemClick: "+position);
        Intent intent =  new Intent(this,UpdateNoteActivity.class);
        intent.putExtra("key",todo.getKey());
        startActivity(intent);
    }
}
