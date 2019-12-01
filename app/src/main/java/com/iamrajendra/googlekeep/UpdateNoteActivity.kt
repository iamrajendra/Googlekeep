package com.iamrajendra.googlekeep

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.palette.graphics.Palette

import com.google.firebase.auth.FirebaseUser
import com.iamrajendra.googlekeep.firebase.DatabaseManger
import com.iamrajendra.googlekeep.firebase.GoogleAuthentication
import com.iamrajendra.googlekeep.model.Todo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.activity_add_notes.view.*
import kotlinx.android.synthetic.main.gird_view.*
import android.R.attr.bitmap
import android.app.Activity
import android.content.BroadcastReceiver
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.iamrajendra.googlekeep.firebase.ImageUploadService


class UpdateNoteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBroadcastReceiver: BroadcastReceiver
    private var manger: DatabaseManger? = null
    private lateinit var todo:Todo
    private  var change:Int?=0;
    private  var mediaUri: Uri?=null
    companion object{
        val TAG:String =UpdateNoteActivity.javaClass.simpleName
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        setSupportActionBar(toolbar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        GoogleAuthentication.getInstance(this).user.observe(this, Observer { user ->
            manger = DatabaseManger(user)
            manger?.populateSingleValue(intent.getStringExtra("key"))
            manger?.liveTodo?.observe(this@UpdateNoteActivity, Observer { todo -> init(todo) })
        })

       uploadImage.setOnClickListener(this)

        mBroadcastReceiver = object : BroadcastReceiver() {


            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG, "onReceive:$intent")

                when (intent.action) {
                    ImageUploadService.ACTION_UPLOAD -> Log.i(TAG, "upload image: ")
                    ImageUploadService.UPLOAD_COMPLETED -> {
                        Log.i(TAG, "upload is completed: ")
                        progressBar.setVisibility(View.GONE)
                        val mDownloadUrl = Uri.parse(intent.getStringExtra(ImageUploadService.EXTRA_DOWNLOAD_URL))
                        mediaUri = mDownloadUrl
                        val imageView = findViewById(R.id.media) as ImageView
                        Picasso.get().load(mediaUri).resize(600, 600)
                                .centerInside()
                                .into(imageView)
                        imageView.visibility = View.VISIBLE
                    }
                    ImageUploadService.UPLOAD_ERROR -> {
                    }
                }
            }
        }
    }

    private fun init(todo: Todo) {
        this.todo =todo
        change =todo.hashCode();
        notes_title.text = Editable.Factory.getInstance().newEditable(todo.title);
        description.text = Editable.Factory.getInstance().newEditable(todo.description)
        Picasso.get().load(todo.photo).resize(600,600)
                .centerInside().into(media)
        media.visibility= View.VISIBLE
        if (todo?.color!=0){
            root.setBackgroundColor(resources.getColor(todo?.color!!))
            toolbar.setBackgroundColor(resources.getColor(todo?.color!!))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.add_notes, menu)
        val item = menu.findItem(R.id.upload_image)
        item.isVisible = false
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveDetails()
    }

    private fun saveDetails() {
        todo.title = notes_title.text.toString()
        todo.description = description.text.toString()
        if (mediaUri != null) {
            todo.photo = mediaUri.toString()
        }

        if(change!=todo.hashCode()){
            Log.i(TAG,"change dedected")

        }

        manger?.update(todo)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onClick(v: View) {
        val i = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.type = "image/*"

        i.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivityForResult(i, 1001)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data!!.data

                selectedImage?.let { uploadFromUri(it) } ?: Log.w(TAG, "File URI is null")
            } else {
                Toast.makeText(this, "Taking picture failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadFromUri(fileUri: Uri) {


        progressBar.setVisibility(View.VISIBLE)
        Log.d(TAG, "uploadFromUri:src:$fileUri")

        // Save the File URI

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(Intent(this, ImageUploadService::class.java)
                .putExtra(ImageUploadService.EXTRA_FILE_URI, fileUri)
                .setAction(ImageUploadService.ACTION_UPLOAD))


    }

    public override fun onStart() {
        super.onStart()

        // Register receiver for uploads and downloads
        val manager = LocalBroadcastManager.getInstance(this)
        manager.registerReceiver(mBroadcastReceiver, ImageUploadService.getIntentFilter())
    }


}
