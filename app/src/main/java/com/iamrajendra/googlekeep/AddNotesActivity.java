package com.iamrajendra.googlekeep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamrajendra.googlekeep.firebase.GoogleAuthentication;
import com.iamrajendra.googlekeep.firebase.ImageUploadService;
import com.iamrajendra.googlekeep.model.Todo;
import com.squareup.picasso.Picasso;


public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AddNotesActivity.class.getSimpleName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    EditText editTextTitle, editTextDescription;
    private BroadcastReceiver mBroadcastReceiver;
    private  Uri mediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG, "onCreate: ");
        user = GoogleAuthentication.getInstance(this).getCurrentUser();
        editTextTitle = findViewById(R.id.notes_title);
        editTextDescription = findViewById(R.id.description);
        findViewById(R.id.uploadImage).setOnClickListener(this);

        // Create a new user with a first and last name

        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);

                switch (intent.getAction()) {
                    case ImageUploadService.ACTION_UPLOAD:
                        Log.i(TAG, "upload image: ");
                        break;
                    case ImageUploadService.UPLOAD_COMPLETED:
                        Log.i(TAG, "upload is completed: ");
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                      Uri mDownloadUrl = Uri.parse(intent.getStringExtra(ImageUploadService.EXTRA_DOWNLOAD_URL));
                        mediaUri=mDownloadUrl;
                        ImageView imageView = (ImageView) findViewById(R.id.media);
                        Picasso.get().load(mediaUri).resize(600,600)
                                .centerInside()
                                .into(imageView);
                        imageView.setVisibility(View.VISIBLE);
                        break;
                    case ImageUploadService.UPLOAD_ERROR:

                        break;
                }
            }
        };

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (editTextTitle.getText().toString().isEmpty()){
            return;
        }
        if (editTextDescription.getText().toString().isEmpty()){
            return;
        }
        Todo todo = new Todo(editTextTitle.getText().toString());
        todo.setUid(user.getUid());
        if (mediaUri!=null){
            todo.setPhoto(mediaUri.toString());
        }
        todo.setAutherPhoto(user.getPhotoUrl().toString());
        todo.setDescription(editTextDescription.getText().toString());
        todo.setAddedBy(user.getDisplayName());
        db.collection(this.user.getUid())
                .add(todo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error adding document", e);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_notes, menu);
        MenuItem item = menu.findItem(R.id.upload_image);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.upload_image:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");

                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                startActivityForResult(i, 1001);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001){
            if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();

                if (selectedImage != null) {
                    uploadFromUri(selectedImage);
                } else {
                    Log.w(TAG, "File URI is null");
                }
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFromUri(Uri fileUri) {


        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, ImageUploadService.class)
                .putExtra(ImageUploadService.EXTRA_FILE_URI, fileUri)
                .setAction(ImageUploadService.ACTION_UPLOAD));


    }


    @Override
    public void onStart() {
        super.onStart();

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, ImageUploadService.getIntentFilter());
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");

        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(i, 1001);

    }
}
