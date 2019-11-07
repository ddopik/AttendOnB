package com.spidersholidays.attendonb.ui.vacation.pending

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.ui.vacation.VacationAdapter
import com.spidersholidays.attendonb.ui.vacation.VacationAdapter.OnPendingVacationClick
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        return layoutInflater.inflate(R.layout.fragment_pending, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intiView()
        initObservers()
        pendingViewModel.getPendingVacations ()
    }


    override fun intiView() {

        val deleteAction = object : OnPendingVacationClick {
            override fun onVacationDeleteClicked(vacationId: String) {
                pendingViewModel.deleteVacation(vacationId)
            }
        }
        pendingVacationAdapter = VacationAdapter(pendingVacationList, VacationAdapter.VacationType.PENDING, deleteAction)
        rv_pending_vacation.adapter = pendingVacationAdapter

    }



    override fun initObservers() {
        pendingViewModel = PendingViewModel.getInstance(this)!!
        pendingViewModel.onPendingProgressChange().observe(viewLifecycleOwner, Observer {
            if (it) {
                pr_pending_vacation.visibility = View.VISIBLE
            } else {
                pr_pending_vacation.visibility = View.GONE
            }
        })


        pendingViewModel.onPendingVacationChange().observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "onPendingVacationChange ----> called()")
            pendingVacationList.clear()
            it?.let {
                pendingVacationList.addAll(it)
            }
             pendingVacationAdapter.notifyDataSetChanged()
            if (pendingVacationList.size > 0) {
                no_pending_vacation_stats_msg.visibility = View.GONE
            } else {
                no_pending_vacation_stats_msg.visibility = View.VISIBLE
            }
        })


        pendingViewModel.onPendingVacationDeleted().observe(viewLifecycleOwner, Observer {

            val vacationDeleteStats = it.getBoolean(PendingViewModel.VACATION_STATS)
            val vacationId = it.getString(PendingViewModel.VACATION_ID)
            if (vacationDeleteStats) {


                for (index in 0 until pendingVacationList.size) {
                    if (pendingVacationList[index].id.equals(vacationId)) {
                        pendingVacationList.removeAt(index)
                        pendingVacationAdapter.notifyDataSetChanged()
                        break
                    }
                }

            }else{
                showToast("Vacation not Deleted")
            }
        })
    }

}