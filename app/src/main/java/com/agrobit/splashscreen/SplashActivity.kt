package com.agrobit.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.agrobit.account.AccountActivity
import com.agrobit.account.LoginActivity
import com.agrobit.activities.MainActivity
import com.agrobit.activities.OrchardActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        //get current user
        val currentUser = auth.currentUser

        val intent = intent
        intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        if (currentUser != null) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

}
