package com.attendance735.attendonb.ui.vacation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attendance735.attendonb.R
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.commonModel.Vacation


class VacationAdapter(val pendingVacationList: MutableList<Vacation>, val vacationType: VacationType) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {

        val layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
        return VacationViewHolder(layoutInflater.inflate(R.layout.view_holder_vacation, parent, false))


    }

    override fun getItemCount(): Int {
        return pendingVacationList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {

//        when (vacationType)
//        {
//            VacationType.PENDING ->
//        }
//
        holder.vacationReason.text = pendingVacationList[position].reason
        holder.vacationStartDate.text = pendingVacationList[position].startDate
        holder.vacationEndDate.text = pendingVacationList[position].endDate
        holder.vacationDaysLeft.text = pendingVacationList[position].totalDays + " " + AttendOnBApp.app?.baseContext?.resources?.getString(R.string.day)

    }


    class VacationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vacationReason: TextView = view.findViewById(R.id.vacation_reason)
        var vacationStartDate: TextView = view.findViewById(R.id.vacation_start_date)
        var vacationEndDate: TextView = view.findViewById(R.id.vacation_end_date)
        var vacationDaysLeft: TextView = view.findViewById(R.id.vacation_day_count)

    }

    enum class VacationType {
        PENDING, REJECTED, APPROVED
    }
}