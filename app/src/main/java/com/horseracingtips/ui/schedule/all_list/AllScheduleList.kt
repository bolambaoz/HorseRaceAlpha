package com.horseracingtips.ui.schedule.all_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horseracingtips.R
import com.horseracingtips.data.db.entity.Schedules
import com.horseracingtips.databinding.ActivityAllScheduleListBinding
import com.horseracingtips.ui.adapters.RecyclerMutliAdapter
import com.horseracingtips.ui.schedule.ScheduleFactory
import com.horseracingtips.ui.schedule.SchedulePage
import com.horseracingtips.ui.schedule.ScheduleVM
import com.horseracingtips.ui.videostream.VideoStreamActivity
import com.horseracingtips.utils.SCHEDULE_PAGE_PARAMS
import com.horseracingtips.utils.setSafeOnClickListener
import com.horseracingtips.utils.snackBar
import com.horseracingtips.utils.toast
import kotlinx.coroutines.flow.collect
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class AllScheduleList : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ScheduleFactory by instance()
    private lateinit var viewModel: ScheduleVM

    private lateinit var _binding: ActivityAllScheduleListBinding
    private val binding get() = _binding

    private lateinit var _btnBack: ImageButton
    private lateinit var _adapter: RecyclerMutliAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAllScheduleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,factory)[ScheduleVM::class.java]
        setupBinding()
        initRV()

        lifecycleScope.launchWhenStarted {
            viewModel.allSchedule().collect {
                _adapter.submitScheduleList(it)
                _adapter.notifyDataSetChanged()
            }
        }

        _btnBack.setSafeOnClickListener {
            super.onBackPressed()
        }

    }

    private fun setupBinding(){
        binding.apply {
            _btnBack = btnBack
        }
    }

    private fun initRV(){
        _adapter = RecyclerMutliAdapter(
            this@AllScheduleList,
            null,
            null,
            null,
            null,
            null,
            null,
            RecyclerMutliAdapter.VIEW_TYPE_ALL_SCHED
        ) { channel, title, content, iframe, list ->

            var sched = list as Schedules
            if (sched.horse_race_card?.count() != 0){
                startActivity(Intent(this@AllScheduleList, SchedulePage::class.java).apply {
                    this.putExtra(SCHEDULE_PAGE_PARAMS, sched)
                })
            }else{
                this@AllScheduleList.snackBar("TBD",this@AllScheduleList,true, R.id.unang_layout)
            }
        }
        binding.allSchedRv.apply {
            layoutManager = LinearLayoutManager(
                this@AllScheduleList,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = _adapter
        }
    }
}