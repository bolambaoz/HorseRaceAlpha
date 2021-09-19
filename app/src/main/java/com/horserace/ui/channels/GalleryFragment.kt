package com.horserace.ui.channels

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.horserace.data.network.response.HorseRaceResponse
import com.horserace.databinding.FragmentGalleryBinding
import com.horserace.utils.hide
import com.horserace.utils.show
import com.horserace.utils.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class GalleryFragment : Fragment(), GalleryListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val factory:GalleryViewFactory by instance()

    lateinit var progressVar: ProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val root: View = binding.root
        galleryViewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]
        galleryViewModel.galleryListener = this


        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        progressVar = binding.progressBarGallery


//            .observe(requireActivity(), Observer { arrayVideo ->
//            if(arrayVideo != null){
////                activity?.toast(arrayVideo.toString())
//                activity?.toast("DATA NOT NULL $arrayVideo")
//            }else {
//                activity?.toast("Data is null $arrayVideo")
//                galleryViewModel.fetchVideo().also {
//                    progressVar.show()
//                }
//            }
//        })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStarted(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

    }

    override fun onSuccess(loginResponse: HorseRaceResponse) {
        progressVar.hide()
    }

    override fun onFailure(message: String) {
        progressVar.hide()
    }
}