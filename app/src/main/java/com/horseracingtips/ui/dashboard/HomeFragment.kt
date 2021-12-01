package com.horseracingtips.ui.dashboard

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horseracingtips.CheckNetworkConnection
import com.horseracingtips.data.db.entity.HorseNews
import com.horseracingtips.data.network.response.HorseRaceResponse
import com.horseracingtips.databinding.FragmentHomeBinding
import com.horseracingtips.ui.adapters.RecyclerMutliAdapter
import com.horseracingtips.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class HomeFragment : Fragment(), HomeListener, KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewFactory by instance()

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    lateinit var progressVar: ProgressBar
    lateinit var fragmentRecyclerView: RecyclerView
    lateinit var imageViewWifi: ImageView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        homeViewModel.homeListener = this
        progressVar = binding.progressBarGallery
        imageViewWifi = binding.noConnection
        progressVar.show()
        fragmentRecyclerView = binding.homeRecyclerView
        fragmentRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL,
                false
            )
        }

        callNetworkConnection()

        setHasOptionsMenu(true)
    }


    private fun callNetworkConnection() {

        val checkNetworkConnection = requireActivity().let { CheckNetworkConnection(it.application) }
        checkNetworkConnection?.observe(viewLifecycleOwner, { isConnected ->
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
        })
    }

    private fun buildUI() = Coroutines.main {
        homeViewModel.horseNewsData.await().observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
    }

    private fun initRecyclerView(horseNewsItem: List<HorseNews>) {
        progressVar.hide()
        fragmentRecyclerView.apply {
            adapter = RecyclerMutliAdapter(
                requireActivity(),
                horseNewsItem,
                null,
                RecyclerMutliAdapter.VIEW_TYPE_ONE,
                onItemClick = {

                }
            )

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStarted(message: String, from: String) {
        activity?.toast(message)
    }

    override fun onSuccess(loginResponse: HorseRaceResponse) {
    }

    override fun onFailure(message: String) {
        activity?.toast(message)
    }

    override fun onLoading() {
    }
}