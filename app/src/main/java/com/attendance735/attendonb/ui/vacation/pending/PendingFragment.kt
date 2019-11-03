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
import kotlinx.android.synthetic.main.fragment_pending.*

class PendingFragment : BaseFragment() {


    companion object {
        val TAG = PendingFragment::javaClass.name
        fun getInstance(): PendingFragment {
            return PendingFragment()
        }
    }


    var pendingViewModel: PendingViewModel? = null
    var pedingVacationAdapter: VacationAdapter? = null
    var pendingVacationList: MutableList<Vacation> = mutableListOf()

    val vacationList: MutableList<Vacation> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true


        return layoutInflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intiView()
        initObservers()

        pendingViewModel?.getPendingVacations()

    }

    override fun intiView() {
        pendingViewModel = PendingViewModel.getInstance(this)
        pedingVacationAdapter = VacationAdapter(pendingVacationList, VacationAdapter.VacationType.PENDING)
        rv_pending_vacation.adapter = pedingVacationAdapter

    }

    override fun initObservers() {

        pendingViewModel?.onPendingProgressChange()?.observe(this, Observer {
            if (it) {
                Log.e(TAG, "ProgreesBar On")
                pr_pending_vacation.visibility = View.VISIBLE
            } else {
                Log.e(TAG, "ProgreesBar Off")

                pr_pending_vacation.visibility = View.GONE
            }


            pendingViewModel?.onPendingVacationChange()?.observe(this, Observer {
                pendingVacationList.clear()
                pendingVacationList.addAll(it)
                pedingVacationAdapter?.notifyDataSetChanged()
            })

        })
    }

}