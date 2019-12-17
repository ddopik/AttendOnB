package com.attendance735.attendonb.ui.vacation.newvacation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class VacationDaysLimit {

    @SerializedName("vacation_days_limit")
    @Expose
    var vacationDaysLimit: String? = null
}