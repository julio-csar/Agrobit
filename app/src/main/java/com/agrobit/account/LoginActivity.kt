package com.agrobit.account

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.agrobit.R
import com.agrobit.activities.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginSignup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
        initMethods()
    }

    private fun initComponents(){
        loginSignup=findViewById(R.id.loginSignup)
    }
    private fun initMethods(){
        loginSignup.setOnClickListener(View.OnClickListener {
            signup()
        })
    }

    private fun signup(){
        startActivity(Intent(this,SignupActivity::class.java))
    }
}

