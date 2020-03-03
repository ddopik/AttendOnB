package com.spidersholidays.attendonb.ui.vacationmangment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VacationMangerRequestResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("data")
    @Expose
    var vacationMangerApprovedData: VacationMangerApprovedData? = null

    @SerializedName("code")
    @Expose
    var code: String? = null
}