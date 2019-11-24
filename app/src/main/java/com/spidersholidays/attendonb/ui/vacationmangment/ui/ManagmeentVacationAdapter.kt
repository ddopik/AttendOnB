package com.spidersholidays.attendonb.ui.vacationmangment.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.commonModel.Vacation

class ManagmeentVacationAdapter(val VacationList: MutableList<Vacation>, val vacationType: VacationType) : RecyclerView.Adapter<ManagmeentVacationAdapter.VacationViewHolder>() {

    var onPendingVacationClick: PendingManagementsVacationClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
        return VacationViewHolder(layoutInflater.inflate(R.layout.view_holder_vacation_managmeent, parent, false))
    }

    constructor(pendingVacationList: MutableList<Vacation>, vacationType: VacationType, action: PendingManagementsVacationClickListener?) : this(pendingVacationList, vacationType) {

        this.onPendingVacationClick = action
    }

    override fun getItemCount(): Int {
        return VacationList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        holder.vacationControlView.visibility = View.GONE

        when (vacationType) {
            VacationType.PENDING -> {
                holder.vacationControlView.visibility = View.VISIBLE
                holder.vacationControlView.visibility = View.VISIBLE

                holder.vacationApproveBtn.setOnClickListener {
                    onPendingVacationClick?.onVacationRejectClicked(VacationList[position].id)
                }

                holder.vacationRejectBtn.setOnClickListener {
                    onPendingVacationClick?.onVacationApproveClicked(VacationList[position].id)
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


        holder.vacationReason.text = VacationList[position].reason
        holder.vacationStartDate.text = VacationList[position].startDate
        holder.vacationEndDate.text = VacationList[position].endDate
        holder.vacationDaysLeft.text = VacationList[position].totalDays + " " + AttendOnBApp.app?.baseContext?.resources?.getString(R.string.day)
        holder.vacationCreatedDate.text = VacationList[position].requestDate

    }


    class VacationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vacationControlView = view.findViewById<View>(R.id.vacation_control_container)
        val vacationIcon = view.findViewById<ImageView>(R.id.vacation_icon)
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