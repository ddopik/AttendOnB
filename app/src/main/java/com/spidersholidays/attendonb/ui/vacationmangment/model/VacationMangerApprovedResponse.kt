package com.spidersholidays.attendonb.ui.vacationmangment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VacationMangerApprovedResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("data")
    @Expose
    var vacationMangerApprovedData: VacationMangerApprovedData? = null

    @SerializedName("code")
    @Expose
    var code: String? = null
}