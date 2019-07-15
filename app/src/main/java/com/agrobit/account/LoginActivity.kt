package com.agrobit.account

import android.animation.Animator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.agrobit.R
import com.agrobit.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.forgot_password.*
import android.R.id.edit
import android.content.SharedPreferences
import com.agrobit.BuildConfig



class LoginActivity : AppCompatActivity() {

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

    //For the
    lateinit var dialog:Dialog
    lateinit var logInFailTitle: TextView
    lateinit var logInFailDescription:TextView

    lateinit var logInFailClose: ImageView
    lateinit var logInFailAccept:Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
        initMethods()

        when (getFirstTimeRun()) {
            0 -> loginWelcome.setText("Bienvenido")
            1 -> loginWelcome.setText("Bienvenido de nuevo")
            2 -> loginWelcome.setText("Bienvenido de nuevo")
        }
    }

    private fun initComponents(){
        loginSignup=findViewById(R.id.loginSignup)
        loginButton=findViewById(R.id.loginButton)
        loginEmail=findViewById(R.id.loginEmail)
        loginPassword=findViewById(R.id.loginPassword)
        loginProgressBar=findViewById(R.id.loginProgressBar)
        loginRecover=findViewById(R.id.loginRecover)
        loginWelcome=findViewById(R.id.loginWelcome)

        dialog= Dialog(this)
        dialogPassword=Dialog(this)

        auth= FirebaseAuth.getInstance()
    }
    private fun initMethods(){
        loginSignup.setOnClickListener(View.OnClickListener {
            signup()
        })
        loginButton.setOnClickListener(View.OnClickListener {
            login()
        })
        loginRecover.setOnClickListener(View.OnClickListener {
            recoverPassword()
        })
    }

    private fun signup(){
        startActivity(Intent(this,SignupActivity::class.java))
    }
    private fun login(){
        val email=loginEmail.text.toString()
        val pass=loginPassword.text.toString()

        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
            loginProgressBar.visibility=View.VISIBLE

            auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this){
                    task->
                    if(task.isSuccessful){
                        val user = auth.getCurrentUser()
                        if (user != null) {
                            if (!user.isEmailVerified()) {
                                loginProgressBar.visibility=View.GONE
                                showFailPopup()
                            } else {
                                loginProgressBar.visibility=View.GONE
                                success()
                            }
                        }
                    }else
                    {
                        Toast.makeText(this,"Error de autentificación",Toast.LENGTH_LONG).show()
                        loginProgressBar.visibility=View.GONE
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
        startActivity(Intent(this,MainActivity::class.java))
        finish()
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
        //Set text
        //logInFailAccept.setText("Reenviar correo")
        //logInFailTitle.setText("¡Inicio de sesión fallido!")
        //logInFailDescription.setText("No se ha confirmado el correo electrónico. Por favor verifícalo para poder iniciar sesión.")

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
    private fun getFirstTimeRun(): Int {
        val sp = getSharedPreferences("MYAPP", 0)
        val result: Int
        val currentVersionCode = BuildConfig.VERSION_CODE
        val lastVersionCode = sp.getInt("FIRSTTIMERUN", -1)
        if (lastVersionCode == -1)
            result = 0
        else
            result = if (lastVersionCode == currentVersionCode) 1 else 2
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply()
        return result
    }
}