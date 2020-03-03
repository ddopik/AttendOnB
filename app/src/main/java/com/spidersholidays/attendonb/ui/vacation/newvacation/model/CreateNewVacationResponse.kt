package com.spidersholidays.attendonb.ui.vacation.newvacation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CreateNewVacationResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("data")
    @Expose
    var data: CreateNewVacationData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}