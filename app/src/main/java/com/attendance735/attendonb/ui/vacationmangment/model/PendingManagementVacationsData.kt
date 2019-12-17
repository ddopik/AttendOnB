package com.attendance735.attendonb.ui.vacationmangment.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.attendance735.attendonb.base.commonModel.Vacation


class PendingManagementVacationsData {
    @SerializedName("vacation_data")
    @Expose
    var vacationData: MutableList<Vacation>? = null
    @SerializedName("is_manager")
    @Expose
    var isManager: Boolean? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null

}