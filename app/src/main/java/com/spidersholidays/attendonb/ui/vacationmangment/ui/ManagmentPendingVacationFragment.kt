package com.spidersholidays.attendonb.ui.vacationmangment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.ui.vacationmangment.model.VacationStatsChange
import com.spidersholidays.attendonb.ui.vacationmangment.viewmodel.ManageVacationViewModel
import com.spidersholidays.attendonb.utilites.Constants.Companion.PENDING_VACATION_UNDER_REVISION
import kotlinx.android.synthetic.main.fragment_pending_management_vacation.*

class ManagmentPendingVacationFragment : BaseFragment() {


    companion object {
        val TAG = ManagmentPendingVacationFragment::javaClass.name
        fun getInstance(): ManagmentPendingVacationFragment {
            return ManagmentPendingVacationFragment()
        }
    }

    private lateinit var manageVacationViewModel: ManageVacationViewModel
    private lateinit var pendingManagementVacationAdapter: ManagmeentVacationAdapter
    private var pendingVacationList: MutableList<Vacation> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        return layoutInflater.inflate(R.layout.fragment_pending_management_vacation, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intiView()
        initObservers()
    }


    override fun onResume() {
        super.onResume()
        manageVacationViewModel.getPendingManagementVacation()
    }

    override fun intiView() {

        val vacationClickAction = object : ManagmeentVacationAdapter.PendingManagementsVacationClickListener {
            override fun onVacationRejectClicked(vacationId: String) {

                RejectVacationReasonDialogFragment.getInstance(vacationId,manageVacationViewModel).show(childFragmentManager, RejectVacationReasonDialogFragment.TAG)
             }

            override fun onVacationApproveClicked(vacationId: String) {
                manageVacationViewModel.approveVacation(vacationId)
            }
        }
        pendingManagementVacationAdapter = ManagmeentVacationAdapter(pendingVacationList, ManagmeentVacationAdapter.VacationType.PENDING, vacationClickAction)
        rv_management_pending_vacation.adapter = pendingManagementVacationAdapter

    }


    override fun initObservers() {
        manageVacationViewModel = ManageVacationViewModel.getInstance(this)
        manageVacationViewModel.onVacationProgressChanged().observe(viewLifecycleOwner, Observer {
            if (it) {
                pb_management_pending.visibility = View.VISIBLE
            } else {
                pb_management_pending.visibility = View.GONE
            }
        })


        manageVacationViewModel.onPendingVacationListChanged().observe(viewLifecycleOwner, Observer {
            pendingVacationList.clear()
            it?.let {
                pendingVacationList.addAll(it)
            }
            pendingManagementVacationAdapter.notifyDataSetChanged()

            if (pendingVacationList.size > 0) {
                no_management_pending_vacation_stats_msg.visibility = View.GONE
            } else {
                no_management_pending_vacation_stats_msg.visibility = View.VISIBLE
            }
        })


        manageVacationViewModel.onPendingItemChange().observe(viewLifecycleOwner, Observer { vacationStatsChange: VacationStatsChange ->
            pendingVacationList.forEach {
                if (vacationStatsChange.id == it.id) {

                    when (vacationStatsChange.vacationStatsType) {
                        VacationStatsChange.VacationStatsType.APPROVED -> {
                            it.requestStatus = PENDING_VACATION_UNDER_REVISION
                        }
                        VacationStatsChange.VacationStatsType.REJECTED -> {
                            pendingVacationList.remove(it)
                            childFragmentManager.findFragmentByTag(RejectVacationReasonDialogFragment.TAG)?.let {
                                if (it.isVisible){
                                    (it as RejectVacationReasonDialogFragment).dismiss()
                                }
                            }
                        }
                    }
                }
                pendingManagementVacationAdapter.notifyDataSetChanged()
            }
        })


    }
}