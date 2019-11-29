package com.iamrajendra.googlekeep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser

import com.iamrajendra.googlekeep.firebase.GoogleAuthentication
import kotlinx.android.synthetic.main.activity_google_login.*
import java.util.*

class GoogleLoginActivity : BaseActivity() {
    internal lateinit var googleAuthentication: GoogleAuthentication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_login)
        googleAuthentication = GoogleAuthentication.getInstance(this)
        sign_in_button.setOnClickListener {
            googleAuthentication.signIn()
        }
        googleAuthentication.user.observe(this, Observer {

            if (it!=null){
                var intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

    }

    override fun onStart() {
        super.onStart()
        googleAuthentication.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleAuthentication.onActivityResult(requestCode,requestCode,data)

    }
}
