package com.attendance735.attendonb.ui.payroll.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attendance735.attendonb.R
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.commonModel.PayRoll

class PayRollAdapter(var payRollList: MutableList<PayRoll>, var payRollAdapterClickListener: PayRollAdapterClickListener?) : RecyclerView.Adapter<PayRollAdapter.PayRollViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayRollViewHolder {

        val layoutInflater = LayoutInflater.from(AttendOnBApp.app?.baseContext!!)
        return PayRollViewHolder(layoutInflater.inflate(R.layout.view_holder_pay_roll, parent, false))
    }

    override fun getItemCount(): Int {
        return payRollList.size
    }

    override fun onBindViewHolder(holder: PayRollViewHolder, position: Int) {
        holder.payRollTotalSalary.text = payRollList[position].totalSalary
        holder.payRollMonth.text = payRollList[position].payrollMonth
        holder.payRollYear.text = payRollList[position].payrollYear


        payRollAdapterClickListener?.let {
            holder.payRollContainerView.setOnClickListener {
                payRollAdapterClickListener!!.onPayRollItemClickListener(payRollList[position])
            }
        }


    }

    class PayRollViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val payRollContainerView: View = view.findViewById(R.id.payroll_container_view)
        val payRollTotalSalary: TextView = view.findViewById(R.id.pay_roll_total_salary)
        val payRollMonth: TextView = view.findViewById(R.id.pay_roll_month)
        val payRollYear: TextView = view.findViewById(R.id.pay_roll_year)

    }


    interface PayRollAdapterClickListener {
        fun onPayRollItemClickListener(payRoll: PayRoll)
    }
}