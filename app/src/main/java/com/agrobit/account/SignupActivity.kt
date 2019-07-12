package com.agrobit.account

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.agrobit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var signupName:EditText
    private lateinit var signupEmail:EditText
    private lateinit var signupPassword:EditText
    private lateinit var signupProgressBar:ProgressBar
    private lateinit var signupButton:Button

    private lateinit var dialog: Dialog
    private lateinit var sigInCorrectTitle: TextView
    private lateinit var sigInCorrectDescription:TextView
    private lateinit var sigInCorrectClose: ImageView
    private lateinit var sigInFailClose:ImageView
    private lateinit var sigInCorrectAccept: Button
    private lateinit var sigInFailAccept:Button

    private lateinit var dbReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initComponents()
        initMethods()

    }

    fun initComponents(){
        signupName=findViewById(R.id.signupName)
        signupEmail=findViewById(R.id.signupEmail)
        signupPassword=findViewById(R.id.signupPassword)
        signupProgressBar=findViewById(R.id.signupProgressBar)
        signupButton=findViewById(R.id.signupButton)

        //Dialog
        dialog = Dialog(this)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Registrarse"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        dbReference=database.reference.child("User")
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun initMethods(){
        signupButton.setOnClickListener(View.OnClickListener {
            createNewAccount()
        })
    }

    private fun createNewAccount(){
        val name = signupName.text.toString()
        val email=signupEmail.text.toString()
        val password=signupPassword.text.toString()

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            signupProgressBar.visibility=View.VISIBLE
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task->

                    if(task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verifyEmail(user)

                        val userBD=dbReference.child(user?.uid.toString())
                        userBD.child("Name").setValue(name)
                        succes()
                    }
                }
        }else{
            if(TextUtils.isEmpty(name))
                signupName.setError("Requerido")
            else
                signupName.setError(null)

            if(TextUtils.isEmpty(email))
                signupEmail.setError("Requerido")
            else
                signupEmail.setError(null)

            if(TextUtils.isEmpty(password))
                signupPassword.setError("Requerido")
            else
                signupPassword.setError(null)
        }
    }
    private fun succes(){
        showCorrectPupup()
        //startActivity(Intent(this,LoginActivity::class.java))
        //finish()
    }
    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task->

                if(task.isComplete){
                    //Toast.makeText(this,"Correo enviado",Toast.LENGTH_LONG).show()
                }else{
                    //Toast.makeText(this,"Correo no enviado",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showCorrectPupup() {
        dialog.setContentView(R.layout.message_sigin_correct)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        sigInCorrectClose = dialog.findViewById(R.id.sigInCorrectClose) as ImageView
        sigInCorrectAccept = dialog.findViewById(R.id.sigInCorrecAccept) as Button

        sigInCorrectAccept.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        })
        sigInCorrectClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            auth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        })
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        signupProgressBar.visibility=View.INVISIBLE
        dialog.show()
    }
}
