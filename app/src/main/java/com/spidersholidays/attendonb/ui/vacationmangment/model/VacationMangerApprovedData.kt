package com.spidersholidays.attendonb.ui.vacationmangment.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class VacationMangerApprovedData {
    @SerializedName("vacation_approved")
    @Expose
    var vacationApproved: Boolean? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null

}