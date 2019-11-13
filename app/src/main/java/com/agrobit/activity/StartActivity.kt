package com.agrobit.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.agrobit.R
import com.agrobit.account.LoginActivity
import com.agrobit.account.SignupActivity

class StartActivity : AppCompatActivity(){

    private lateinit var sbStart:Button
    private lateinit var lbStart:Button
    private lateinit var gbStart:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        sbStart=findViewById<Button>(R.id.sbStart)
        lbStart=findViewById<Button>(R.id.lbStart)
        gbStart=findViewById<Button>(R.id.gbStart)

        sbStart.setOnClickListener{view->
            val newIntent = Intent(this@StartActivity, SignupActivity::class.java)
            startActivity(newIntent)
        }
        lbStart.setOnClickListener{view->
            val newIntent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(newIntent)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
