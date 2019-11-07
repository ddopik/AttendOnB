package com.attendance735.attendonb.ui.vacation.newvacation.model

import com.attendance735.attendonb.base.commonModel.User
import com.attendance735.attendonb.base.commonModel.VacationsType
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewVacationData {
    @SerializedName("users")
    @Expose
    var users: MutableList<User>? = null
    @SerializedName("vacations_type")
    @Expose
    var vacationsType: List<VacationsType>? = null
    @SerializedName("vacation_days_limit")
    @Expose
    var vacationDaysLimit: List<VacationDaysLimit>? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null

}
