package com.horseracingtips.ui.channels

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horseracingtips.CheckNetworkConnection
import com.horseracingtips.data.db.entity.HorseVideo
import com.horseracingtips.data.network.response.HorseRaceResponse
import com.horseracingtips.databinding.FragmentGalleryBinding
import com.horseracingtips.utils.*
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

private val LAYOUT_COLUMS = 2

class GalleryFragment : Fragment(), GalleryListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val factory:GalleryViewFactory by instance()

    private var groupAdapter: GroupieAdapter? = null
    lateinit var progressVar: ProgressBar
    lateinit var fragmentRecyclerView: RecyclerView
    lateinit var imageViewWifi: ImageView

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

        galleryViewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]

        galleryViewModel.galleryListener = this
        progressVar = binding.progressBarGallery
        fragmentRecyclerView = binding.galleryRecyclerView
        imageViewWifi = binding.noConnection

        progressVar.show()

        groupAdapter = GroupieAdapter()

        groupAdapter!!.setOnItemClickListener(onItemClickListener)

        //update the recyclerview
//        buildUI()
        callNetworkConnection()

    }

    private fun callNetworkConnection() {

        val checkNetworkConnection = activity?.let { CheckNetworkConnection(it.application) }
        checkNetworkConnection?.observe(viewLifecycleOwner, { isConnected ->
            if(isConnected){
                buildUI()
                imageViewWifi.hide()
                fragmentRecyclerView.show()
                
            }else{
                fragmentRecyclerView.hide()
                progressVar.hide()
                imageViewWifi.show()
            }
        })
    }

    private val onItemClickListener: OnItemClickListener =
        OnItemClickListener { item, _ ->
            if (item is HorseItem) {
                val cardItem: HorseItem = item as HorseItem
                if (!TextUtils.isEmpty(cardItem.toString())) {
                    isClickable(cardItem)
                }

            }
    }

    private fun isClickable(cardItem: HorseItem) = Coroutines.main {
        galleryViewModel.horseIsActive.await().observe(viewLifecycleOwner, {
            if( it == true) {
                fragmentRecyclerView.hide()
                progressVar.show()
                activity?.getIpAddres()?.let {
                    galleryViewModel.getGliveLink(
                        cardItem.getText().toString(),
                        it
                    )
                }
            }else{
                root_layout.snackbar("Coming soon!!!")
            }

        })
    }

    private fun buildUI() = Coroutines.main {
       galleryViewModel.horseData.await().observe(viewLifecycleOwner, Observer {
            initRecyclerView(it.toHorseItem())
        })
    }

    private fun initRecyclerView(horseItem: List<HorseItem>) {
        progressVar.hide()
        groupAdapter?.apply {
            addAll(horseItem)
        }
        fragmentRecyclerView.apply {
            layoutManager = GridLayoutManager(activity, LAYOUT_COLUMS)
            adapter = groupAdapter
        }

    }

    private fun List<HorseVideo>.toHorseItem() : List<HorseItem> {
        return  this.map {
            HorseItem(it,requireActivity())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStarted(message: String, from: String) {
        activity?.popUpAds(requireActivity(), message)
        if(FROM_GLIVE == from){
            progressVar.hide()
            fragmentRecyclerView.show()
        }
    }

    override fun onSuccess(loginResponse: HorseRaceResponse) {
        progressVar.hide()
    }

    override fun onFailure(message: String) {
        root_layout.snackbar(message)
        progressVar.hide()
    }

    override fun onLoading() {
        progressVar.show()
    }
}