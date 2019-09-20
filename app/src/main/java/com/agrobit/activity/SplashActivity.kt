package com.agrobit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.internal.Intrinsics
import com.agrobit.framework.shareddata.UserSharedPreference
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.common.util.Strings


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!(UserSharedPreference(this).user.uuid).equals("")) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this@SplashActivity, StartActivity::class.java))
            finish()
        }

/*
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
            startActivity(Intent(this@SplashActivity, StartActivity::class.java))
            finish()
        }*/
    }

}
