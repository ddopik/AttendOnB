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
import kotlinx.android.synthetic.main.fragment_approved_management_vacation.*

class ManagementApprovedVacationFragment : BaseFragment() {


    private val TAG = ManagementApprovedVacationFragment::javaClass.name
    private var manageVacationViewModel: ManageVacationViewModel? = null
    private var managementVacationAdapter: ManagmeentVacationAdapter? = null
    private var managementApprovedList = mutableListOf<Vacation>()


    companion object {
        val TAG = ManagementApprovedVacationFragment::javaClass.name
        var INSTANCE: ManagementApprovedVacationFragment? = null

        fun getInstance(): ManagementApprovedVacationFragment {
            if (INSTANCE == null) {
                INSTANCE = ManagementApprovedVacationFragment()
            }
            return INSTANCE!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_approved_management_vacation, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiView()
        initObservers()
    }

    override fun intiView() {
        manageVacationViewModel = ManageVacationViewModel.getInstance(this)



        managementVacationAdapter = ManagmeentVacationAdapter(managementApprovedList, ManagmeentVacationAdapter.VacationType.APPROVED, null)
        rv_management_approved_vacation.adapter = managementVacationAdapter

        manageVacationViewModel?.getApprovedManagementVacation(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!))

    }

    override fun initObservers() {

        manageVacationViewModel?.onVacationProgressChanged()?.observe(viewLifecycleOwner, Observer {
            if (it) {
                management_approved_pb_approved.visibility = View.VISIBLE
            } else {
                management_approved_pb_approved.visibility = View.GONE
            }
            if (managementApprovedList.size > 0) {
                no_management_approved_vacation_stats_msg.visibility = View.GONE
            } else {
                no_management_approved_vacation_stats_msg.visibility = View.VISIBLE

            }
        })


        manageVacationViewModel?.onApprovedVacationListChanged()?.observe(viewLifecycleOwner, Observer {
            managementApprovedList.clear()
            managementApprovedList.addAll(it)
            managementVacationAdapter?.notifyDataSetChanged()
            if (it.size > 0) {
                no_management_approved_vacation_stats_msg.visibility = View.GONE
            } else {
                no_management_approved_vacation_stats_msg.visibility = View.VISIBLE

            }
        })

    }
}