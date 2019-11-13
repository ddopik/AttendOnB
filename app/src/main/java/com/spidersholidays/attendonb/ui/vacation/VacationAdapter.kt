package com.spidersholidays.attendonb.ui.vacation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.commonModel.Vacation


class VacationAdapter(val pendingVacationList: MutableList<Vacation>, val vacationType: VacationType) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {

    var onPendingVacationClick: OnPendingVacationClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
        return VacationViewHolder(layoutInflater.inflate(R.layout.view_holder_vacation, parent, false))
    }

    constructor(pendingVacationLis2t: MutableList<Vacation>, vacationType: VacationType, action: OnPendingVacationClick) : this(pendingVacationLis2t, vacationType) {

        this.onPendingVacationClick = action
    }

    override fun getItemCount(): Int {
        return pendingVacationList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {

        when (vacationType) {
            VacationType.PENDING -> {
                holder.vacationDaysLeft.visibility = View.VISIBLE
                holder.delteVacationBtn.setOnClickListener {
                    onPendingVacationClick?.onVacationDeleteClicked(pendingVacationList[position].id)
                }

            }
            else -> {
                holder.vacationDaysLeft.visibility = View.GONE

            }
        }


        holder.vacationReason.text = pendingVacationList[position].reason
        holder.vacationStartDate.text = pendingVacationList[position].startDate
        holder.vacationEndDate.text = pendingVacationList[position].endDate
        holder.vacationDaysLeft.text = pendingVacationList[position].totalDays + " " + AttendOnBApp.app?.baseContext?.resources?.getString(R.string.day)

    }


    class VacationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vacationReason: TextView = view.findViewById(R.id.vacation_reason_val)
        var vacationStartDate: TextView = view.findViewById(R.id.vacation_start_date)
        var vacationEndDate: TextView = view.findViewById(R.id.vacation_end_date)
        var vacationDaysLeft: TextView = view.findViewById(R.id.vacation_day_count)
        var delteVacationBtn: Button = view.findViewById(R.id.btn_vacation_delete)
    }


    interface OnPendingVacationClick {
        fun onVacationDeleteClicked(vacationId: String)
    }

    enum class VacationType {
        PENDING, REJECTED, APPROVED
    }
}