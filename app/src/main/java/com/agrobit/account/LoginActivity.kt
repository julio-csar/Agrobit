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
import com.agrobit.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.provider.ContactsContract
import android.widget.TextView
import android.widget.ProgressBar
import com.agrobit.classes.CProgressDialog
import com.agrobit.classes.User
import com.agrobit.framework.shareddata.UserSharedPreference
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import pl.droidsonroids.gif.GifImageView


class LoginActivity : AppCompatActivity(){


    private lateinit var loginSignup: TextView
    private lateinit var loginButton:Button
    private lateinit var loginEmail:EditText
    private lateinit var loginPassword:EditText
    private lateinit var loginProgressBar:ProgressBar
    private lateinit var loginRecover:TextView
    private lateinit var loginWelcome:TextView

    lateinit var dialogPassword: Dialog
    lateinit var dialogPasswordAccept:Button
    lateinit var dialogPasswordClose:ImageView
    lateinit var dialogPasswordEmail:EditText
    lateinit var dialogForgotPB:ProgressBar

    lateinit var dialogProgress:Dialog

    //For the
    lateinit var dialog:Dialog
    lateinit var logInFailTitle: TextView
    lateinit var logInFailDescription:TextView

    lateinit var logInFailClose: ImageView
    lateinit var logInFailAccept:Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private lateinit var pb: GifImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
        initMethods()
/*
        when (getFirstTimeRun()) {
            0 -> loginWelcome.setText("Bienvenido")
            1 -> loginWelcome.setText("Bienvenido de nuevo")
            2 -> loginWelcome.setText("Bienvenido de nuevo")
        }*/
    }

    private fun initComponents(){
        //loginSignup=findViewById(R.id.loginSignup)
        loginButton=findViewById(R.id.loginButton)
        loginEmail=findViewById(R.id.loginEmail)
        loginPassword=findViewById(R.id.loginPassword)
        loginProgressBar=findViewById(R.id.loginProgressBar)
        loginRecover=findViewById(R.id.loginRecover)
        pb=findViewById(R.id.pb)
        //loginWelcome=findViewById(R.id.loginWelcome)

        dialog= Dialog(this)
        dialogPassword=Dialog(this)
        dialogProgress= Dialog(this)

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()
        dbReference=database.reference.child("users")
    }
    private fun initMethods(){
        loginButton.setOnClickListener(View.OnClickListener {
            login()
        })
        loginRecover.setOnClickListener(View.OnClickListener {
            recoverPassword()
        })
        loginEmail.setOnFocusChangeListener { view,hasfocus ->
            if(hasfocus){

            }else{

            }
        }
    }

    private fun login(){
        val email=loginEmail.text.toString()
        val pass=loginPassword.text.toString()
        //VeryLongAsyncTask(this).execute()



        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
            showProgressDialog()
            //pb.visibility=View.VISIBLE
            //loginProgressBar.visibility=View.VISIBLE

            auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this){
                    task->
                    if(task.isSuccessful){
                        val user = auth.getCurrentUser()
                        if (user != null) {
                            if (!user.isEmailVerified()) {
                                //loginProgressBar.visibility=View.GONE
                                deleteProgressDialog()
                                //pb.visibility=View.INVISIBLE
                                showFailPopup()
                            } else {
                                //pb.visibility=View.INVISIBLE
                                deleteProgressDialog()
                                //loginProgressBar.visibility=View.GONE
                                success()
                            }
                        }
                    }else
                    {
                        Toast.makeText(this,"Error de autentificación",Toast.LENGTH_LONG).show()
                        //pb.visibility=View.INVISIBLE
                        deleteProgressDialog()
                        //loginProgressBar.visibility=View.GONE
                    }
                }
        }else{
            if(TextUtils.isEmpty(email))
                loginEmail.setError("Requerido")
            else
                loginEmail.setError(null)

            if(TextUtils.isEmpty(pass))
                loginPassword.setError("Requerido")
            else
                loginPassword.setError(null)
        }
    }

    fun showProgressDialog(){
        dialogProgress.setContentView(R.layout.progress_dialog)
        dialogProgress.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogProgress.setCanceledOnTouchOutside(false)
        dialogProgress.setCancelable(false)
        dialogProgress.show()
    }
    fun deleteProgressDialog(){
        dialogProgress.dismiss()
    }

    fun showFailPopup() {
        dialog.setContentView(R.layout.message_login_fail)
        logInFailClose = dialog.findViewById(R.id.logInFailClose) as ImageView
        logInFailAccept = dialog.findViewById(R.id.logInFailAccept) as Button
        logInFailTitle = dialog.findViewById(R.id.logInFailTitle) as TextView
        logInFailDescription = dialog.findViewById(R.id.logInFailDescription) as TextView

        //Config
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        //Set text
        logInFailAccept.setText("Reenviar correo")
        logInFailTitle.setText("¡Inicio de sesión fallido!")
        logInFailDescription.setText("No se ha confirmado el correo electrónico. Por favor verifícalo para poder iniciar sesión.")

        logInFailClose.setOnClickListener(View.OnClickListener {
            auth.signOut()
            dialog.dismiss()
        })

        logInFailAccept.setOnClickListener(View.OnClickListener {
            sendEmailVerification()
            auth.signOut()
            dialog.dismiss()
        })
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        val user = auth.getCurrentUser()
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task->
                if(task.isSuccessful){
                    Toast.makeText(this,"Correo enviado",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"Error al enviar correo",Toast.LENGTH_LONG).show()
                }
            }
        // [END send_email_verification]
    }

    private fun success(){
        dbReference!!.child(auth.currentUser?.uid.toString()).
            addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user:User= dataSnapshot.getValue(User::class.java)!!
                UserSharedPreference(applicationContext).saveOrUpdateUser(user)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
        val newIntent = Intent(this@LoginActivity, MainActivity::class.java)
        finishAffinity()
        startActivity(newIntent)
        this.finish()
    }
    private fun error(){

    }
    private fun recoverPassword(){
        dialogPassword.setContentView(R.layout.forgot_password)
        dialogPasswordClose = dialogPassword.findViewById(R.id.forgotClose) as ImageView
        dialogPasswordAccept = dialogPassword.findViewById(R.id.forgotButton) as Button
        dialogPasswordEmail=dialogPassword.findViewById(R.id.forgotEmail) as EditText
        dialogForgotPB=dialogPassword.findViewById(R.id.forgotPB) as ProgressBar

        //Config
        dialogPassword.setCanceledOnTouchOutside(true)
        dialogPassword.setCancelable(true)
        dialogPasswordClose.setOnClickListener(View.OnClickListener {
            auth.signOut()
            dialogPassword.dismiss()
        })

        dialogPasswordAccept.setOnClickListener(View.OnClickListener {
            var email:String=dialogPasswordEmail.text.toString()
            if(!TextUtils.isEmpty(email)){
                dialogForgotPB.visibility=View.VISIBLE
                dialogPasswordEmail.setError(null)
                sendEmailPassword(email)

                auth.signOut()
                dialogForgotPB.visibility=View.INVISIBLE
                dialogPassword.dismiss()
            }else{
                dialogPasswordEmail.setError("Requerido")
            }

        })
        dialogPassword.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogPassword.show()
    }
    private fun sendEmailPassword(email:String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this){
                task->
                if(task.isSuccessful){
                    Toast.makeText(this,"Correo enviado",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Error al enviar",Toast.LENGTH_LONG).show()
                }
            }
    }

    internal inner class VeryLongAsyncTask(ctx: Context) : AsyncTask<Void, Void, Void>() {
        private val progressDialog: ProgressDialog

        init {
            progressDialog = CProgressDialog.ctor(ctx)
        }

        override fun onPreExecute() {
            super.onPreExecute()

            progressDialog.show()
        }

        override fun doInBackground(vararg params: Void): Void? {
            // sleep for 5 seconds
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: Void) {
            super.onPostExecute(result)

            progressDialog.hide()
        }
    }
}