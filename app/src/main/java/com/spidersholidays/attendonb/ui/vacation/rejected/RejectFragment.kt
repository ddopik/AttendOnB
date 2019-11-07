package com.spidersholidays.attendonb.ui.vacation.rejected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.ui.vacation.VacationAdapter
import kotlinx.android.synthetic.main.fragment_rejected.*

class RejectFragment : BaseFragment() {

    companion object {
        val TAG = RejectFragment::javaClass.name
        fun getInstance(): RejectFragment {
            return RejectFragment()
        }
    }


    lateinit var rejectedViewModel: RejectedViewModel
    lateinit var rejectedAdapter: VacationAdapter
    var rejectedVacationList: MutableList<Vacation> = mutableListOf()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        return layoutInflater.inflate(R.layout.fragment_rejected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intiView()
        initObservers()

        rejectedViewModel.getRejectedVacations()

    }

    override fun intiView() {
        rejectedAdapter = VacationAdapter(rejectedVacationList, VacationAdapter.VacationType.REJECTED)
        rv_rejected_vacation.adapter = rejectedAdapter

    }

    override fun initObservers() {
        rejectedViewModel = RejectedViewModel.getInstance(this)!!


        rejectedViewModel.onRejectedProgressChange().observe(this, Observer {
            if (it) {
                pr_rejected_vacation.visibility = View.VISIBLE
            } else {
                pr_rejected_vacation.visibility = View.GONE
            }
        })


        rejectedViewModel.onRejectedVacationChange().observe(this, Observer {
            rejectedVacationList.clear()
            it?.let {
                rejectedVacationList.addAll(it)
            }
            rejectedAdapter.notifyDataSetChanged()
            if (rejectedVacationList.size > 0) {
                no_rejected_vacation_stats_msg.visibility = View.GONE
            } else {
                no_rejected_vacation_stats_msg.visibility = View.VISIBLE

            }
        })
    }

}