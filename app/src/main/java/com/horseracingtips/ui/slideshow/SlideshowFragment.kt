package com.horseracingtips.ui.slideshow

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.horseracingtips.R
import com.horseracingtips.data.User
import com.horseracingtips.data.UserTwolio
import com.horseracingtips.ui.login.LoginPage
import com.horseracingtips.databinding.FragmentSlideshowBinding
import com.horseracingtips.ui.About
import com.horseracingtips.ui.Privacy
import com.horseracingtips.ui.Setting
import com.horseracingtips.utils.convertEmailToValidString

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private lateinit var mAuth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mAuth = FirebaseAuth.getInstance()
        binding.apply {

            userAccountPrivacy.setOnClickListener {
                startActivity(Intent(activity, Privacy::class.java))
            }

            userAccountAbout.setOnClickListener {
                startActivity(Intent(activity, About::class.java))
            }

            userAccountSetting.setOnClickListener {
                startActivity(Intent(activity, Setting::class.java))
            }

            userAccountSignOut.setOnClickListener {
                if (mAuth.currentUser == null){
                    startActivity(Intent(activity, LoginPage::class.java))
                }else{
                    signOut()
                }
            }

        }

        return root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser

        if (currentUser != null){
            binding.apply {
                progressUserLayout.visibility = View.VISIBLE
                userAccountName.visibility = View.GONE
                userAccountEmail.visibility = View.GONE
                userAccountDate.visibility = View.GONE
            }
//            val database = FirebaseDatabase.getInstance()
            Log.d(ContentValues.TAG, "Value is email: " + convertEmailToValidString(currentUser.email))
//            val myRef = database.getReference("${convertEmailToValidString(currentUser.email)}")
            // Read from the database
//            myRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val value = snapshot.getValue<User>()
//                    Log.d(ContentValues.TAG, "Value is: " + value)
//
//                    val emailMask = value?.email.toString()
////                    System.out.println(replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*"));
//                    binding.apply {
//
//                        userAccountName.text = value?.username?.uppercase()
//                        userAccountEmail.text = emailMask.replace(Regex("(?<=.{2}).(?=.*@)"), "*")
//                        userAccountDate.text = value?.date
//                        userAccountSignOut.text = getString(R.string.sign_out_account)
//                        progressUserLayout.visibility = View.GONE
//                        userAccountName.visibility = View.VISIBLE
//                        userAccountEmail.visibility = View.VISIBLE
//                        userAccountDate.visibility = View.VISIBLE
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d(ContentValues.TAG, "Value is: " + error.message)
//                }
//
//            })
        }else {
            binding.apply {
                userAccountSignOut.text = getString(R.string.sign_in_account)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signOut(){
        mAuth.signOut()
        binding.apply {
            userAccountName.text = ""
            userAccountEmail.text = ""
            userAccountDate.text = ""
            userAccountSignOut.text = getString(R.string.sign_in_account)
        }
    }

    private fun privacyMethod(){

    }

    private fun aboutMethod(){

    }

    private fun settingMethod(){

    }

}