package com.horseracingtips.ui.channels

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuth
import com.horseracingtips.CheckNetworkConnection
import com.horseracingtips.R
import com.horseracingtips.data.db.entity.HorseVideo
import com.horseracingtips.data.network.response.HorseRaceResponse
import com.horseracingtips.databinding.FragmentGalleryBinding
import com.horseracingtips.ui.adapters.RecyclerMutliAdapter
import com.horseracingtips.ui.videostream.VideoStreamActivity
import com.horseracingtips.utils.*
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.popup_dialog.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.lang.Exception


class GalleryFragment : Fragment(), GalleryListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val factory: GalleryViewFactory by instance()

    private var groupAdapter: GroupieAdapter? = null
    lateinit var progressVar: ProgressBar
    lateinit var fragmentRecyclerView: RecyclerView
    lateinit var imageViewWifi: ImageView

    private lateinit var mAuth: FirebaseAuth
    var isLogin: Boolean = false

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        galleryViewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]

        galleryViewModel.galleryListener = this
        progressVar = binding.progressBarGallery
        fragmentRecyclerView = binding.galleryRecyclerView
        imageViewWifi = binding.noConnection

        progressVar.show()
        groupAdapter = GroupieAdapter()
        fragmentRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        try {
            callNetworkConnection()
        } catch (e: Exception) {
            Log.d(TAG, "Error")
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        isLogin = currentUser != null
    }

    private fun callNetworkConnection() {

        val checkNetworkConnection =
            requireActivity().let { CheckNetworkConnection(it.application) }

        checkNetworkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    buildUI()
                }

                imageViewWifi.hide()
                fragmentRecyclerView.show()

            } else {
                fragmentRecyclerView.hide()
                progressVar.hide()
                imageViewWifi.show()
            }
        }
    }

    private fun isClickable(channel: String) = Coroutines.main {
        galleryViewModel.horseIsActive.await().observe(viewLifecycleOwner) {
            if (it == true) {
                fragmentRecyclerView.hide()
                progressVar.show()
                startActivity(Intent(activity, VideoStreamActivity::class.java).apply {
                    this.putExtra("link", channel)
                    this.putExtra("isLogin", isLogin)
                })
            } else {
                popupAds(requireContext())
            }

        }
    }

    private fun buildUI() = Coroutines.main {
        try {
            galleryViewModel.horseData.await().observe(viewLifecycleOwner, Observer {
                initRecyclerView(it)
            })
        } catch (e: Exception) {
            Log.d(TAG, "Error $e")
        }
    }

    private fun initRecyclerView(horseItem: List<HorseVideo>) {//List<HorseItem>) {

        val titleCH = resources.getStringArray(R.array.array_horse_title_ch)

        progressVar.hide()
        fragmentRecyclerView.apply {
            adapter = RecyclerMutliAdapter(
                requireActivity(),
                null,
                horseItem,
                titleCH,
                RecyclerMutliAdapter.VIEW_TYPE_TWO
            ) { channel ->
                isClickable(channel)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStarted(message: String, from: String) {
        if (FROM_GLIVE == from) {
            progressVar.hide()
        }
    }

    override fun onSuccess(loginResponse: HorseRaceResponse) {
        progressVar.hide()
    }

    override fun onFailure(message: String) {
        root_layout.snackbar(message)
        fragmentRecyclerView.show()
        progressVar.hide()
    }

    override fun onLoading() {
        progressVar.show()
    }

    private fun popupAds(context: Context) {
        val url = URL_FB_HORSE
        val openURL = Intent(Intent.ACTION_VIEW)
        val imgs = resources.obtainTypedArray(R.array.pop_random_)
        val dialog = Dialog(context)
        openURL.data = Uri.parse(url)

        dialog.setContentView(R.layout.popup_dialog)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.background.background =
            ResourcesCompat.getDrawable(resources, imgs.getResourceId(0, -1), null)

        dialog.img_exit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_clickhere.setOnClickListener {
            startActivity(openURL)
        }

        dialog.show()
    }
}