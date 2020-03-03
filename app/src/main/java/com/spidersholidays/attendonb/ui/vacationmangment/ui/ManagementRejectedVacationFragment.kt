package com.spidersholidays.attendonb.ui.vacationmangment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.ui.vacationmangment.viewmodel.ManageVacationViewModel
import com.spidersholidays.attendonb.utilites.PrefUtil
import kotlinx.android.synthetic.main.fragment_rejected_management_vacation.*

class ManagementRejectedVacationFragment : BaseFragment() {

    companion object {
        val TAG = ManagementRejectedVacationFragment::javaClass.name
        private var INSTANCE: ManagementRejectedVacationFragment? = null

        fun getInstance(): ManagementRejectedVacationFragment {
            if (INSTANCE == null) {
                INSTANCE = ManagementRejectedVacationFragment()
            }
            return INSTANCE!!
        }
    }

    private val TAG = ManagementRejectedVacationFragment::javaClass.name
    private var manageVacationViewModel: ManageVacationViewModel? = null
    private var managementVacationAdapter: ManagmeentVacationAdapter? = null
    private var managementRejectedList = mutableListOf<Vacation>()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rejected_management_vacation, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiView()
        initObservers()
    }

    override fun intiView() {
        manageVacationViewModel = ManageVacationViewModel.getInstance(this)



        managementVacationAdapter = ManagmeentVacationAdapter(managementRejectedList, ManagmeentVacationAdapter.VacationType.REJECTED, null)
        rv_management_rejected_vacation.adapter = managementVacationAdapter

        manageVacationViewModel?.getRejectedManagementVacation(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!))

    }

    override fun initObservers() {

        manageVacationViewModel?.onVacationProgressChanged()?.observe(viewLifecycleOwner, Observer {
            if (it) {
                pb_management_rejected.visibility = View.VISIBLE
            } else {
                pb_management_rejected.visibility = View.GONE
            }

            if (managementRejectedList.size > 0) {
                no_management_pending_vacation_stats_msg.visibility = View.GONE
            } else {
                no_management_pending_vacation_stats_msg.visibility = View.VISIBLE

            }

        })


        manageVacationViewModel?.onRejectedVacationListChanged()?.observe(viewLifecycleOwner, Observer {
            managementRejectedList.clear()
            managementRejectedList.addAll(it)
            managementVacationAdapter?.notifyDataSetChanged()
            if (it.size > 0) {
                no_management_pending_vacation_stats_msg.visibility = View.GONE
            } else {
                no_management_pending_vacation_stats_msg.visibility = View.VISIBLE

            }
        })

    }
}