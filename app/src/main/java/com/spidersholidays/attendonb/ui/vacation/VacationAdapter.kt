package com.spidersholidays.attendonb.ui.vacation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.utilites.Constants.Companion.PENDING_VACATION_ALLOWED


class VacationAdapter(val VacationList: MutableList<Vacation>, val vacationType: VacationType) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {

    var onPendingVacationClick: OnPendingVacationClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
        return VacationViewHolder(layoutInflater.inflate(R.layout.view_holder_vacation, parent, false))
    }

    constructor(pendingVacationList: MutableList<Vacation>, vacationType: VacationType, action: OnPendingVacationClick) : this(pendingVacationList, vacationType) {

        this.onPendingVacationClick = action
    }

    override fun getItemCount(): Int {
        return VacationList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {

        when (vacationType) {
            VacationType.PENDING -> {
                if (VacationList[position].requestStatus == PENDING_VACATION_ALLOWED) {
                    holder.deleteVacationBtn.visibility = View.VISIBLE
                    holder.vacationUnderRevisionIcon.visibility = View.GONE

                } else {
                    holder.vacationUnderRevisionIcon.visibility = View.VISIBLE
                    holder.deleteVacationBtn.visibility = View.GONE

                }
                holder.deleteVacationBtn.setOnClickListener {
                    onPendingVacationClick?.onVacationDeleteClicked(VacationList[position].id)
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
        holder.vacationCreatedDate.text = VacationList[position].requestDate


        if (VacationList[position].totalDays == "1") {
            holder.vacationDaysLeft.text = VacationList[position].totalDays + " " + AttendOnBApp.app?.baseContext?.resources?.getString(R.string.day)

        } else {
            holder.vacationDaysLeft.text = VacationList[position].totalDays + " " + AttendOnBApp.app?.baseContext?.resources?.getString(R.string.days)

        }

    }


    class VacationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vacationUnderRevisionIcon = view.findViewById<View>(R.id.ic_vacation_under_revision)
        val vacationIcon = view.findViewById<ImageView>(R.id.vacation_icon)
        val vacationReason: TextView = view.findViewById(R.id.vacation_reason_val)
        var vacationStartDate: TextView = view.findViewById(R.id.vacation_start_date)
        var vacationEndDate: TextView = view.findViewById(R.id.vacation_end_date)
        var vacationDaysLeft: TextView = view.findViewById(R.id.vacation_day_count)
        var vacationCreatedDate: TextView = view.findViewById(R.id.created_date_val)
        var deleteVacationBtn: Button = view.findViewById(R.id.btn_vacation_delete)
    }


    interface OnPendingVacationClick {
        fun onVacationDeleteClicked(vacationId: String)
    }

    enum class VacationType {
        PENDING, REJECTED, APPROVED
    }
}