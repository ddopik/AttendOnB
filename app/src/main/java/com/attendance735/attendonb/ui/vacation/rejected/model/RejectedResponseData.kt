package com.attendance735.attendonb.ui.vacation.rejected.model

import com.attendance735.attendonb.base.commonModel.Vacation
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RejectedResponseData {
    @SerializedName("rejected_vacations")
    @Expose
    var rejectedVacations: List<Vacation>? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null
}