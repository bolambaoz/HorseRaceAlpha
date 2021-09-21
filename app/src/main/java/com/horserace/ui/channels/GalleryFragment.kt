package com.horserace.ui.channels

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horserace.data.db.entity.HorseVideo
import com.horserace.data.network.response.HorseRaceResponse
import com.horserace.databinding.FragmentGalleryBinding
import com.horserace.utils.*
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
        progressVar.show()
        fragmentRecyclerView = binding.galleryRecyclerView

        groupAdapter = GroupieAdapter()

        groupAdapter!!.setOnItemClickListener(onItemClickListener)

        //update the recyclerview
        buildUI()
    }

    private val onItemClickListener: OnItemClickListener =
        OnItemClickListener { item, _ ->
            if (item is HorseItem) {
                val cardItem: HorseItem = item as HorseItem
                if (!TextUtils.isEmpty(cardItem.toString())) {

                    if(galleryViewModel.isActive == true) {
                        fragmentRecyclerView.hide()
                        progressVar.show()
                        activity?.getIpAddres()?.let {
                            galleryViewModel.getGliveLink(
                                cardItem.getText().toString(),
                                it
                            )
                        }
                    }else{
                        root_layout.snackbar("Welcome")
                    }
                }

            }
        }

    private fun buildUI() = Coroutines.main {
       galleryViewModel.horseData.await().observe(viewLifecycleOwner, Observer {
            initRecyclerView(it.toHorseItem())
        })
    }

    fun initRecyclerView(horseItem: List<HorseItem>) {
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
        progressVar.hide()
    }

    override fun onLoading() {
        progressVar.show()
    }
}