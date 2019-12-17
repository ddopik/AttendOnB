package com.attendance735.attendonb.ui.vacationmangment.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attendance735.attendonb.R
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.commonModel.Vacation
import com.attendance735.attendonb.utilites.Constants.Companion.PENDING_VACATION_UNDER_REVISION

class ManagmeentVacationAdapter(val vacationList: MutableList<Vacation>, val vacationType: VacationType) : RecyclerView.Adapter<ManagmeentVacationAdapter.VacationViewHolder>() {

    var onPendingVacationClick: PendingManagementsVacationClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
        return VacationViewHolder(layoutInflater.inflate(R.layout.view_holder_vacation_managmeent, parent, false))
    }

    constructor(pendingVacationList: MutableList<Vacation>, vacationType: VacationType, action: PendingManagementsVacationClickListener?) : this(pendingVacationList, vacationType) {

        this.onPendingVacationClick = action
    }

    override fun getItemCount(): Int {
        return vacationList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        holder.vacationControlView.visibility = View.GONE

        when (vacationType) {
            VacationType.PENDING -> {
                if (vacationList[position].requestStatus == PENDING_VACATION_UNDER_REVISION) {
                    holder.vacationControlView.visibility = View.GONE
                    holder.vacationUnderRevisiionIcon.visibility = View.VISIBLE

                }else{
                    /**
                     * vacation get approved by manger and waiting for hr second approval
                     */
                    holder.vacationControlView.visibility = View.VISIBLE
                    holder.vacationUnderRevisiionIcon.visibility = View.GONE
                    holder.vacationRejectBtn.setOnClickListener {
                        onPendingVacationClick?.onVacationRejectClicked(vacationList[position].id)
                    }

                    holder.vacationApproveBtn.setOnClickListener {
                        onPendingVacationClick?.onVacationApproveClicked(vacationList[position].id)
                    }
                }
                holder.vacationIcon.setImageResource(R.drawable.ic_pending)

            }

            VacationType.APPROVED -> {
                holder.vacationIcon.setImageResource(R.drawable.ic_approved)
            }


            VacationType.REJECTED -> {
                holder.vacationIcon.setImageResource(R.drawable.ic_rejected)

            }
        }


        holder.vacationUserName.text = vacationList[position].userName
        holder.vacationDepartment.text = vacationList[position].userDepartment
        holder.vacationReason.text = vacationList[position].reason
        holder.vacationStartDate.text = vacationList[position].startDate
        holder.vacationEndDate.text = vacationList[position].endDate
        holder.vacationDaysLeft.text = vacationList[position].totalDays + " " + AttendOnBApp.app?.baseContext?.resources?.getString(R.string.day)
        holder.vacationCreatedDate.text = vacationList[position].requestDate

    }


    class VacationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vacationControlView = view.findViewById<View>(R.id.vacation_control_container)
        val vacationUnderRevisiionIcon = view.findViewById<ImageView>(R.id.img_vacation_vacation_under_revision)
        val vacationIcon = view.findViewById<ImageView>(R.id.vacation_icon)
        val vacationUserName: TextView = view.findViewById(R.id.employee_name)
        val vacationDepartment: TextView = view.findViewById(R.id.employee_department)
        val vacationReason: TextView = view.findViewById(R.id.vacation_reason_val)
        var vacationStartDate: TextView = view.findViewById(R.id.vacation_start_date)
        var vacationEndDate: TextView = view.findViewById(R.id.vacation_end_date)
        var vacationDaysLeft: TextView = view.findViewById(R.id.vacation_day_count)
        var vacationCreatedDate: TextView = view.findViewById(R.id.created_date_val)
        var vacationApproveBtn: ImageButton = view.findViewById(R.id.btn_approve_pending_vacation)
        var vacationRejectBtn: ImageButton = view.findViewById(R.id.btn_reject_vacation)
    }


    interface PendingManagementsVacationClickListener {
        fun onVacationRejectClicked(vacationId: String)
        fun onVacationApproveClicked(vacationId: String)
    }

    enum class VacationType {
        PENDING, REJECTED, APPROVED
    }
}