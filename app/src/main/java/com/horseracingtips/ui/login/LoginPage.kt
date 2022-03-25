package com.horseracingtips.ui.login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import com.horseracingtips.databinding.ActivityLoginPageBinding
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horseracingtips.ENGLISH
import com.horseracingtips.MainActivity
import com.horseracingtips.R
import com.horseracingtips.ui.registration.RegistrationPage
import com.horseracingtips.utils.*
import kotlinx.android.synthetic.main.activity_video_stream.*
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnCompleteListener





class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding

    private lateinit var database: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtRegisterHere = getString(R.string.register_txt_line)

        val spannableString = SpannableString(txtRegisterHere)
        val langCheck = currentLanguage(this)

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginPage, RegistrationPage::class.java).also {
                })
            }
        }

        binding.apply {
            btnLogin.setOnClickListener {
                loginUser()
            }

            textResetPasswordOnclick.setOnClickListener {
                displayUIResetPass()
            }

            btnResetPassword.setOnClickListener {
                resetPassFirebase()
            }

        }

        if (langCheck.toString() == ENGLISH){
            spannableString.setSpan(clickableSpan1, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }else{
            spannableString.setSpan(clickableSpan1, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.registerHere.text = spannableString
        binding.registerHere.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = languageSetup(newBase)
        super.attachBaseContext(lang?.let { newBase?.changeLocale(it) })
    }

    private fun displayUIResetPass(){
        binding.apply {
            textUsernameLayout.visibility = View.GONE
            textPasswordLayout.visibility = View.GONE
            btnLogin.visibility = View.GONE
            linearLayout2.visibility = View.GONE
            textResetPasswordOnclick.visibility = View.GONE
            textInputOldPassword.visibility = View.VISIBLE
            btnResetPassword.visibility = View.VISIBLE
            horseLogo.visibility = View.GONE
        }
    }

    private fun hideUIResetPass(){
        binding.apply {
            textUsernameLayout.visibility = View.VISIBLE
            textPasswordLayout.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
            linearLayout2.visibility = View.VISIBLE
            horseLogo.visibility = View.VISIBLE
            textResetPasswordOnclick.visibility = View.VISIBLE
            textInputOldPassword.visibility = View.GONE
            btnResetPassword.visibility = View.GONE
            binding.progressLogin.visibility = View.GONE
        }
    }

    private fun resetPassFirebase(){

        val sendEmail = binding.sendEmailEdt.text.toString().trim()

        if (TextUtils.isEmpty(sendEmail)) {
            binding.sendEmailEdt.error =  getString(R.string.email_error)
            binding.sendEmailEdt.requestFocus()
        }else{

            mAuth.sendPasswordResetEmail(sendEmail).addOnCompleteListener { task ->
//                loadingBar.dismiss()
                binding.progressLogin.visibility = View.VISIBLE
                if (task.isSuccessful) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(this, "Check email to reset password", Toast.LENGTH_LONG).show()
                    hideUIResetPass()
                } else {
                    Toast.makeText(this, "Unable to send email", Toast.LENGTH_LONG)
                        .show()
                }
            }.addOnFailureListener {
                binding.progressLogin.visibility = View.GONE
                Toast.makeText(this, "Error Failed", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun loginUser() {
        val email = binding.usernameEdt.text.toString().trim()
        val pass = binding.passwordEdt.text.toString().trim()
        binding.loginNotFound.visibility = View.GONE

        if (TextUtils.isEmpty(email)) {
            binding.usernameEdt.error =  getString(R.string.email_error)
            binding.usernameEdt.requestFocus()
        } else if (TextUtils.isEmpty(pass)) {
            binding.passwordEdt.error = getString(R.string.password_error)
            binding.passwordEdt.requestFocus()
        } else if (!isValidEmaillId(email)){
            binding.usernameEdt.error  = getString(R.string.email_not_valid)
            binding.usernameEdt.requestFocus()
        } else {
            binding.progressLogin.visibility = View.VISIBLE

            mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        binding.progressLogin.visibility = View.GONE
                        startActivity(Intent(this, MainActivity::class.java).also {
                            finish()
                        })
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        binding.loginNotFound.visibility = View.VISIBLE
                        binding.progressLogin.visibility = View.GONE
//                        Toast.makeText(
//                            baseContext, "Authentication failed: ${task.exception}",
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                }
        }
    }
}