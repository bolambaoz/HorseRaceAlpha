package com.horseracingtips.ui.otp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horseracingtips.MainActivity
import com.horseracingtips.R
import com.horseracingtips.data.User
import com.horseracingtips.data.UserTwolio
import com.horseracingtips.data.network.response.VerifyResponse
import com.horseracingtips.databinding.ActivityOtpactivityBinding
import com.horseracingtips.utils.convertEmailToValidString
import com.horseracingtips.utils.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat
import java.text.NumberFormat


class OtpActivity : AppCompatActivity(), KodeinAware, OtpListener{

    override val kodein by kodein()

    private val factory: OtpFactory by instance()

    private lateinit var otpViewModel: OtpViewModel

    private lateinit var binding: ActivityOtpactivityBinding

    private lateinit var database: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    private lateinit var userData: UserTwolio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otpViewModel = ViewModelProvider(this, factory)[OtpViewModel::class.java]

        otpViewModel.otpListener = this
        userData = intent.getParcelableExtra("userData")!!

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        binding.apply {
            otpOneTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (otpOneTxt.text.toString().length == 1) {
                        otpOneTxt.clearFocus()
                        otpTwoTxt.requestFocus()
                        otpTwoTxt.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (otpOneTxt.text.toString().isEmpty()) {
                        otpOneTxt.requestFocus()
                    }
                }

            })

            otpTwoTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (otpTwoTxt.text.toString().length == 1) {
                        otpTwoTxt.clearFocus()
                        otpThreeTxt.requestFocus()
                        otpThreeTxt.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (otpTwoTxt.text.toString().isEmpty()) {
                        otpTwoTxt.requestFocus()
                    }
                }

            })

            otpThreeTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (otpThreeTxt.text.toString().length == 1) {
                        otpThreeTxt.clearFocus()
                        otpFourTxt.requestFocus()
                        otpFourTxt.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (otpThreeTxt.text.toString().isEmpty()) {
                        otpThreeTxt.requestFocus()
                    }
                }
            })

            otpFourTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (otpFourTxt.text.toString().length == 1) {
                        otpFourTxt.clearFocus()
                        otpFiveTxt.requestFocus()
                        otpFiveTxt.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (otpFourTxt.text.toString().isEmpty()) {
                        otpFourTxt.requestFocus()
                    }
                }
            })

            otpFiveTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (otpFiveTxt.text.toString().length == 1) {
                        otpFiveTxt.clearFocus()
                        otpSixTxt.requestFocus()
                        otpSixTxt.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (otpFiveTxt.text.toString().isEmpty()) {
                        otpFiveTxt.requestFocus()
                    }
                }
            })

            otpSixTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (otpSixTxt.text.toString().length == 1) {
                        otpSixTxt.requestFocus()
                        otpSixTxt.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            sendBtn.setOnClickListener {
                if (userData != null){
                    binding.progressBar2.visibility = View.VISIBLE
                    verifyCode()
                }
            }

        }

        object : CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.otpCountDown.text = "${f.format(min)}:${f.format(sec)}"
            }
            override fun onFinish() {
                binding.otpCountDown.text = "OTP Expired"
                binding.sendBtn.isEnabled = false
            }
        }.start()

    }

    private fun verifyCode(){
        val one = binding.otpOneTxt.text.trim()
        val two = binding.otpTwoTxt.text.trim()
        val three = binding.otpThreeTxt.text.trim()
        val four = binding.otpFourTxt.text.trim()
        val five = binding.otpFiveTxt.text.trim()
        val six = binding.otpSixTxt.text.trim()

        when {
            TextUtils.isEmpty(one) -> {
                binding.otpOneTxt.requestFocus()
            }
            TextUtils.isEmpty(two) -> {
                binding.otpOneTxt.requestFocus()
            }
            TextUtils.isEmpty(three) -> {
                binding.otpOneTxt.requestFocus()
            }
            TextUtils.isEmpty(four) -> {
                binding.otpOneTxt.requestFocus()
            }
            TextUtils.isEmpty(five) -> {
                binding.otpOneTxt.requestFocus()
            }
            TextUtils.isEmpty(six) -> {
                binding.otpOneTxt.requestFocus()
            }
            else -> {
                val code = "$one$two$three$four$five$six"
                otpViewModel.codeVerification(code,"${userData.countryCode}${userData.phone}")
            }
        }
    }

    private fun registerUser(user: UserTwolio){
            mAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "CreateUserWithEmail:success")
                        binding.progressBar2.visibility = View.GONE
                        val database = Firebase.database
                        val myRef = database.getReference("${convertEmailToValidString(user.email)}")

                        val userData = User(user.username,user.password,user.countryCode,user.phone,user.email, user.date)

                        myRef.setValue(userData)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    startActivity(
                                        Intent(
                                            this,
                                            MainActivity::class.java
                                        ).also {
                                            finish()
                                        })
                                }
                            }

                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed. ${user.email}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }

    override fun onSuccess(response: VerifyResponse) {
        if (response.isApproved){
            registerUser(userData)
        }
    }

    override fun onFailure(message: String) {
        binding.progressBar2.visibility = View.GONE
        toast(message)
    }
}