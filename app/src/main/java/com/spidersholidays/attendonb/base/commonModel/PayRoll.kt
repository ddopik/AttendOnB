package com.spidersholidays.attendonb.base.commonModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PayRoll : Parcelable {

    @SerializedName("id")
    @Expose
    var id: String ?=null
    @SerializedName("uid")
    @Expose
    var uid: String ?=null
    @SerializedName("days_in_month")
    @Expose
    var daysInMonth: String ?=null
    @SerializedName("workdays")
    @Expose
    var workdays: String ?=null
    @SerializedName("emp_total_attend_days")
    @Expose
    var empTotalAttendDays: String ?=null
    @SerializedName("emp_total_missing_days")
    @Expose
    var empTotalMissingDays: String ?=null
    @SerializedName("emp_total_leave_days")
    @Expose
    var empTotalLeaveDays: String ?=null
    @SerializedName("total_national_holidays")
    @Expose
    var totalNationalHolidays: String ?=null
    @SerializedName("emp_noshow_days")
    @Expose
    var empNoshowDays: String ?=null
    @SerializedName("emp_remaining_leave_days")
    @Expose
    var empRemainingLeaveDays: String ?=null
    @SerializedName("emp_total_late_days")
    @Expose
    var empTotalLateDays: String ?=null
    @SerializedName("emp_total_late_hours")
    @Expose
    var empTotalLateHours: String ?=null
    @SerializedName("emp_total_uncomplate_days")
    @Expose
    var empTotalUncomplateDays: String ?=null
    @SerializedName("emp_total_uncomplate_times")
    @Expose
    var empTotalUncomplateTimes: String ?=null
    @SerializedName("emp_total_benefits_amount")
    @Expose
    var empTotalBenefitsAmount: String ?=null
    @SerializedName("emp_total_deductions_amount")
    @Expose
    var empTotalDeductionsAmount: String ?=null
    @SerializedName("emp_total_missing_leave_days")
    @Expose
    var empTotalMissingLeaveDays: String ?=null
    @SerializedName("total_salary")
    @Expose
    var totalSalary: String ?=null
    @SerializedName("payroll_year")
    @Expose
    var payrollYear: String ?=null
    @SerializedName("payroll_month")
    @Expose
    var payrollMonth: String ?=null
    @SerializedName("viewed")
    @Expose
    var viewed: String ?=null
    @SerializedName("approved_status")
    @Expose
    var approvedStatus: String ?=null
    @SerializedName("approved_by")
    @Expose
    var approvedBy: String ?=null
    @SerializedName("approved_date")
    @Expose
    var approvedDate: String ?=null
    @SerializedName("added_by")
    @Expose
    var addedBy: String ?=null
    @SerializedName("added_date")
    @Expose
    var addedDate: String ?=null

    protected constructor(`in`: Parcel) {
        this.id = `in`.readValue(String::class.java.classLoader) as String
        this.uid = `in`.readValue(String::class.java.classLoader) as String
        this.daysInMonth = `in`.readValue(String::class.java.classLoader) as String
        this.workdays = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalAttendDays = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalMissingDays = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalLeaveDays = `in`.readValue(String::class.java.classLoader) as String
        this.totalNationalHolidays = `in`.readValue(String::class.java.classLoader) as String
        this.empNoshowDays = `in`.readValue(String::class.java.classLoader) as String
        this.empRemainingLeaveDays = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalLateDays = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalLateHours = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalUncomplateDays = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalUncomplateTimes = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalBenefitsAmount = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalDeductionsAmount = `in`.readValue(String::class.java.classLoader) as String
        this.empTotalMissingLeaveDays = `in`.readValue(String::class.java.classLoader) as String
        this.totalSalary = `in`.readValue(String::class.java.classLoader) as String
        this.payrollYear = `in`.readValue(String::class.java.classLoader) as String
        this.payrollMonth = `in`.readValue(String::class.java.classLoader) as String
        this.viewed = `in`.readValue(String::class.java.classLoader) as String
        this.approvedStatus = `in`.readValue(String::class.java.classLoader) as String
        this.approvedBy = `in`.readValue(String::class.java.classLoader) as String
        this.approvedDate = `in`.readValue(String::class.java.classLoader) as String
        this.addedBy = `in`.readValue(String::class.java.classLoader) as String
        this.addedDate = `in`.readValue(String::class.java.classLoader) as String
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(uid)
        dest.writeValue(daysInMonth)
        dest.writeValue(workdays)
        dest.writeValue(empTotalAttendDays)
        dest.writeValue(empTotalMissingDays)
        dest.writeValue(empTotalLeaveDays)
        dest.writeValue(totalNationalHolidays)
        dest.writeValue(empNoshowDays)
        dest.writeValue(empRemainingLeaveDays)
        dest.writeValue(empTotalLateDays)
        dest.writeValue(empTotalLateHours)
        dest.writeValue(empTotalUncomplateDays)
        dest.writeValue(empTotalUncomplateTimes)
        dest.writeValue(empTotalBenefitsAmount)
        dest.writeValue(empTotalDeductionsAmount)
        dest.writeValue(empTotalMissingLeaveDays)
        dest.writeValue(totalSalary)
        dest.writeValue(payrollYear)
        dest.writeValue(payrollMonth)
        dest.writeValue(viewed)
        dest.writeValue(approvedStatus)
        dest.writeValue(approvedBy)
        dest.writeValue(approvedDate)
        dest.writeValue(addedBy)
        dest.writeValue(addedDate)
    }

    override fun describeContents(): Int {
        return 0
    }



    companion object CREATOR : Parcelable.Creator<PayRoll> {
        override fun createFromParcel(parcel: Parcel): PayRoll {
            return PayRoll(parcel)
        }

        override fun newArray(size: Int): Array<PayRoll?> {
            return arrayOfNulls(size)
        }
    }

}