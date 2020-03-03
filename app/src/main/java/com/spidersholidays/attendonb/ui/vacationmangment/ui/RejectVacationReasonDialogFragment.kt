package com.spidersholidays.attendonb.ui.vacationmangment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.ui.vacationmangment.viewmodel.ManageVacationViewModel
import kotlinx.android.synthetic.main.fragment_reject_reason.*

class RejectVacationReasonDialogFragment : DialogFragment() {

    private var manageVacationViewModel: ManageVacationViewModel? = null
    private var vacationId: String? = null

    companion object {
        val TAG = RejectVacationReasonDialogFragment::javaClass.name
        private var INSTANCE: RejectVacationReasonDialogFragment? = null

        fun getInstance(vacationId: String, parentFragment: ManageVacationViewModel): RejectVacationReasonDialogFragment {

            if (INSTANCE == null) {
                INSTANCE = RejectVacationReasonDialogFragment()
//                INSTANCE?.manageVacationViewModel = ManageVacationViewModel.getInstance(parentFragment)
                INSTANCE?.manageVacationViewModel = parentFragment
                INSTANCE?.vacationId = vacationId
             }
            return INSTANCE!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reject_reason, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }


    fun initListener() {
        btn_vacation_reason_send.setOnClickListener {

            if (vacation_reason_val.text.toString().isEmpty()) {
                vacation_reason_val.error = resources.getString(R.string.field_required)
            } else {
                vacation_reason_val.error = null
                manageVacationViewModel?.rejectVacation(vacationId!!, vacation_reason_val.text.toString())
            }

        }
        btn_vacation_reason_cancel.setOnClickListener {
            dismiss()
        }
    }
}