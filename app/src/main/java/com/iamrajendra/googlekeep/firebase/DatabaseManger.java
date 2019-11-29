package com.iamrajendra.googlekeep.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iamrajendra.googlekeep.model.Todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManger {
    private String TAG = DatabaseManger.class.getSimpleName();
    private FirebaseUser user;

  private   FirebaseFirestore db = FirebaseFirestore.getInstance();
  private MutableLiveData<List<Todo>> liveTodoList  = new MutableLiveData<>();
  private MutableLiveData<Todo> liveTodo  = new MutableLiveData<>();

  private   List<Todo> list;

    public MutableLiveData<Todo> getLiveTodo() {
        return liveTodo;
    }

    public MutableLiveData<List<Todo>> getLiveTodoList() {
        return liveTodoList;
    }

    public List<Todo> getTodo() {
        return list;
    }

    public DatabaseManger(FirebaseUser user) {
        this.user =user;

        db.collection(user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                list  = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Todo todo = new Todo(doc);
                    list.add(todo);
                }
                liveTodoList.setValue(list);
            }
        });



    }

    public void delete(String key) {
        db.collection(user.getUid()).document(key).delete();
    }

    public void populateSingleValue(String key) {

        DocumentReference docRef = db.collection(user.getUid()).document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Todo todo   =  document.toObject(Todo.class);
                        todo.setKey(document.getId());
                        liveTodo.setValue(todo);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void update(Todo todo){
        DocumentReference docRef = db.collection(user.getUid()).document(todo.getKey());

// Remove the 'capital' field from the document

        Map<String,Object> updates = new HashMap<>();
        updates.put("title",todo.getTitle());
        updates.put("description",todo.getDescription());
        docRef.update(updates);
    }
}
