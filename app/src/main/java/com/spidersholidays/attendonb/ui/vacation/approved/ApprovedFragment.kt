package com.spidersholidays.attendonb.ui.vacation.approved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.ui.vacation.VacationAdapter
import kotlinx.android.synthetic.main.fragment_approved.*

class ApprovedFragment : BaseFragment() {

    companion object {
        val TAG = ApprovedFragment::javaClass.name
        fun getInstance(): ApprovedFragment {
            return ApprovedFragment()
        }
    }

    lateinit var approvedViewModel: ApprovedViewModel
    lateinit var approvedAdapter: VacationAdapter
    private val approvedList: MutableList<Vacation> = mutableListOf<Vacation>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_approved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intiView()
        initObservers()
        approvedViewModel.getApprovedVacations()


    }


    override fun intiView() {
        approvedViewModel = ApprovedViewModel.getInstance(this)
        approvedAdapter = VacationAdapter(approvedList, VacationAdapter.VacationType.APPROVED)
        rv_approved_vacation.adapter = approvedAdapter

    }

    override fun initObservers() {

        approvedViewModel.onApprovedProgressChange().observe(this, Observer {
            if (it) {
                pb_approved.visibility = View.VISIBLE
            } else {
                pb_approved.visibility = View.GONE
            }
        })

        approvedViewModel.onApprovrdVacationChange().observe(this, Observer {
            approvedList.clear()
            it?.let {
                approvedList.addAll(it)
            }
            approvedAdapter.notifyDataSetChanged()

            if (approvedList.size > 0) {
                no_approved_vacation_stats_msg.visibility = View.GONE
            } else {
                no_approved_vacation_stats_msg.visibility = View.VISIBLE

            }
        })


    }

}