package com.horseracingtips.ui.registration

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horseracingtips.*
import com.horseracingtips.data.User
import com.horseracingtips.data.UserTwolio
import com.horseracingtips.data.network.response.RegisterResponse
import com.horseracingtips.databinding.ActivityRegistrationPageBinding
import com.horseracingtips.ui.login.LoginPage
import com.horseracingtips.ui.otp.OtpActivity
import com.horseracingtips.utils.*
import kotlinx.android.synthetic.main.activity_registration_page.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RegistrationPage : AppCompatActivity(), KodeinAware, RegisterListener {

    override val kodein by kodein()

    private val factory: RegistrationFactory by instance()
    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var binding: ActivityRegistrationPageBinding

    private lateinit var database: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

//    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
//
//    private lateinit var token: PhoneAuthProvider.ForceResendingToken
//    private lateinit var verifiedToken: String

    private lateinit var username : String
    private lateinit var pass : String
    private lateinit var countryCode : String
    private lateinit var phoneNumber : String
    private lateinit var email : String

//    private val REQ_USER_CONSENT = 200
//    var smsBroadcastReceiver : SmsBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationViewModel = ViewModelProvider(this, factory)[RegistrationViewModel::class.java]

        registrationViewModel.registerListener = this

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

//        startSmartUserConsent()

        btn_register.setOnClickListener {
            registerUser()
        }

//        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                //
//                val code = p0.smsCode
//
//                Log.d(TAG, "REGISTER USER")
//                if (code!=null){
////                    otp.setText(code.toString())
////                    signInWithPhoneAuthCredential(p0)
//                    toast(code.toString())
//                }
//
//            }
//
//            override fun onVerificationFailed(p0: FirebaseException) {
//                //
//                Log.d(TAG, "VERIFICATION FAILED")
//            }
//
//            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(p0, p1)
//
//                verifiedToken = p0
//                token = p1
//                Log.d(TAG, "CODE SEND $verifiedToken $token *****")
//
//            }
//
//            override fun onCodeAutoRetrievalTimeOut(p0: String) {
//                super.onCodeAutoRetrievalTimeOut(p0)
//                Log.d(TAG,"CODE EXPIRED")
//            }
//        }

//        val credential = PhoneAuthProvider.getCredential(verifiedToken,"")


    }

//    private fun startSmartUserConsent() {
//        val client = SmsRetriever.getClient(this)
//        client.startSmsUserConsent(null)
//    }

//    private fun registerBroadcastReceiver(){
//
//        smsBroadcastReceiver = SmsBroadcastReceiver()
//        smsBroadcastReceiver!!.smsBroadcastReceiverListener = object : SmsBroadcastReceiver.SmsBroadcastReceiverListener{
//            override fun onSuccess(intent: Intent?) {
//
//                startActivityForResult(intent, REQ_USER_CONSENT)
//            }
//
//            override fun onFailure() {
//            }
//
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//        registerBroadcastReceiver()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(smsBroadcastReceiver)
//    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = languageSetup(newBase)
        super.attachBaseContext(lang?.let { newBase?.changeLocale(it) })
    }

//    private fun verifyPhoneNumber(pNumber: String){
//        registrationViewModel.validatePhone(pNumber)
//    }

    private fun saveToFirebase(user: UserTwolio){
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "CreateUserWithEmail:success")
                    binding.progressBar.visibility = View.GONE
//                    val database = Firebase.database
//                    val myRef = database.getReference("${convertEmailToValidString(user.email)}")

//                    val userData = User(user.username,user.password,user.countryCode,user.phone,user.email, user.date)


                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        ).also {
                            finish()
                        })

//                    myRef.setValue(userData)
//                        .addOnCompleteListener {
//                            if (it.isSuccessful) {
////                                startActivity(
////                                    Intent(
////                                        this,
////                                        MainActivity::class.java
////                                    ).also {
////                                        finish()
////                                    })
//                            }
//                        }

                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Email already exist",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
    }


    private fun registerUser() {
        username = binding.usernameRegEdt.text.toString().trim()
        pass = binding.passwordRegEdt.text.toString().trim()
        countryCode = binding.phoneCodeRegEdt.text.toString().trim()
        phoneNumber = binding.phoneNumberRegEdt.text.toString().trim()
        email = binding.emailRegEdt.text.toString()

        if (TextUtils.isEmpty(username)) {
            binding.usernameRegEdt.error = getString(R.string.username_error)
            binding.usernameRegEdt.requestFocus()
        } else if (TextUtils.isEmpty(pass)) {
            binding.passwordRegEdt.error = getString(R.string.password_error)
            binding.passwordRegEdt.requestFocus()
        } else if (phoneNumber.isEmpty()) {
            binding.phoneNumberRegEdt.error = getString(R.string.phone_error)
            binding.phoneNumberRegEdt.requestFocus()
        } else if (pass.length < 6) {
            binding.passwordRegEdt.error = getString(R.string.pass_six_char_txt)
            binding.passwordRegEdt.requestFocus()
        } else if (!isValidEmaillId(email)) {
            binding.emailRegEdt.error = getString(R.string.email_not_valid)
            binding.emailRegEdt.requestFocus()
        } else if (TextUtils.isEmpty(email)) {
            binding.emailRegEdt.error = getString(R.string.email_error)
            binding.emailRegEdt.requestFocus()
        } else {

            val userData = UserTwolio(
                username,
                pass,
                countryCode,
                phoneNumber,
                email,
                currentDate()
            )

            binding.progressBar.visibility = View.VISIBLE

            saveToFirebase(userData)
//            if(countryCode == "65"){
//                toast("singapor")
//            }else{
//                val numberInput = "${countryCode}${phoneNumber}"
////                verifyPhoneNumber(numberInput)
//            }

        }
    }

    override fun onStarted(message: String) {
    }

    override fun onSuccess(response: RegisterResponse) {
        val userData = UserTwolio(
            username,
            pass,
            countryCode,
            phoneNumber,
            email,
            currentDate()
        )

        if (response.isSuccess == true){
            startActivity(
                Intent(
                    this@RegistrationPage,
                    OtpActivity::class.java
                ).apply {
                    binding.progressBar.visibility = View.GONE
                    this.putExtra("userData",userData)
                })
        }else {
            toast("Number not validated")
            binding.progressBar.visibility = View.GONE
        }

    }

    override fun onFailure(message: String) {
        binding.progressBar.visibility = View.GONE
        toast(message)
    }

}