package com.spidersholidays.attendonb.ui.payroll.ui

import android.os.Bundle
import bases.BaseActivity
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.commonModel.PayRoll
import kotlinx.android.synthetic.main.activity_pay_roll_details.*

class PayRollDetailsActivity : BaseActivity() {

    companion object {
        val TAG = PayRollDetailsActivity::class.java.name
        val PAY_ROL_OBJ = "payroll_obj"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_roll_details)

        val intent = intent.getParcelableExtra<PayRoll>(PAY_ROL_OBJ)
        intent?.let {
            initView(it)
        }

    }

    private fun initView(payRoll: PayRoll) {
        days_in_month.text = payRoll.daysInMonth
        workdays.text = payRoll.workdays
        emp_total_attend_days.text = payRoll.empTotalAttendDays
        emp_total_missing_days.text = payRoll.empTotalMissingDays
        emp_total_leave_days.text = payRoll.empTotalLeaveDays
        total_national_holidays.text = payRoll.totalNationalHolidays
        emp_noshow_days.text = payRoll.empNoshowDays
        emp_remaining_leave_days.text = payRoll.empRemainingLeaveDays
        emp_total_late_days.text = payRoll.empTotalLateDays
        emp_total_late_hours.text = payRoll.empTotalLateHours
        emp_total_uncomplate_days.text = payRoll.empTotalUncomplateDays
        emp_total_benefits_amount.text = payRoll.empTotalBenefitsAmount
        emp_total_deductions_amount.text = payRoll.empTotalDeductionsAmount
        emp_total_missing_leave_days.text = payRoll.empTotalMissingLeaveDays
        total_salary.text = payRoll.totalSalary
        payroll_year.text = payRoll.payrollYear
        payroll_month.text = payRoll.payrollMonth
    }

}