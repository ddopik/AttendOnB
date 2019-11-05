package com.attendance735.attendonb.ui.vacation.pending

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.BaseFragment
import com.attendance735.attendonb.base.commonModel.Vacation
import com.attendance735.attendonb.ui.vacation.VacationAdapter
import kotlinx.android.synthetic.main.fragment_pending.*

class PendingFragment : BaseFragment() {


    companion object {
        val TAG = PendingFragment::javaClass.name
        fun getInstance(): PendingFragment {
            return PendingFragment()
        }
    }


    lateinit var pendingViewModel: PendingViewModel
    lateinit var pendingVacationAdapter: VacationAdapter
    var pendingVacationList: MutableList<Vacation> = mutableListOf()

    val pendingProgressChange: Unit by lazy {
        pendingViewModel.onPendingProgressChange().observe(viewLifecycleOwner, Observer {
            if (it) {
                pr_pending_vacation.visibility = View.VISIBLE
            } else {
                pr_pending_vacation.visibility = View.GONE
            }


        })
    }
    val pendingListChange: Unit by lazy {
        pendingViewModel.onPendingVacationChange().observe(this, Observer {
            Log.e(TAG, "onPendingVacationChange ----> called()")
            pendingVacationList.clear()
            pendingVacationList.addAll(it)
            pendingVacationAdapter.notifyDataSetChanged()
            if (pendingVacationList.size > 0) {
                no_pending_vacation_stats_msg.visibility = View.GONE
            } else {
                no_pending_vacation_stats_msg.visibility = View.VISIBLE

            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        return layoutInflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiView()
        initObservers()


    }


    override fun intiView() {
        pendingVacationAdapter = VacationAdapter(pendingVacationList, VacationAdapter.VacationType.PENDING)
        rv_pending_vacation.adapter = pendingVacationAdapter

        Log.e(TAG, "getPendingVacations ----> called()")
    }

    override fun initObservers() {
        pendingViewModel = PendingViewModel.getInstance(this)!!
        pendingListChange
        pendingProgressChange
        pendingViewModel.getPendingVacations()

    }

}