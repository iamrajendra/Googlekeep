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

import com.google.firebase.auth.FirebaseUser
import com.iamrajendra.googlekeep.firebase.DatabaseManger
import com.iamrajendra.googlekeep.firebase.GoogleAuthentication
import com.iamrajendra.googlekeep.model.Todo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.activity_add_notes.view.*
import kotlinx.android.synthetic.main.gird_view.*

class UpdateNoteActivity : AppCompatActivity() {

    private var manger: DatabaseManger? = null
    private lateinit var todo:Todo
    private  var change:Int?=0;

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


    }

    private fun init(todo: Todo) {
        this.todo =todo
        change =todo.hashCode();
        notes_title.text = Editable.Factory.getInstance().newEditable(todo.title);
        description.text = Editable.Factory.getInstance().newEditable(todo.description)
        Picasso.get().load(todo.photo).centerCrop().resize(400,200).into(media)
        media.visibility= View.VISIBLE

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.add_notes, menu)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveDetails()
    }

    private fun saveDetails() {
        todo.title = notes_title.text.toString()
        todo.description = description.text.toString()

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

}
