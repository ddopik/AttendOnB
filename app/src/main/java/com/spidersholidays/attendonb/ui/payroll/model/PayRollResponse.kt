package com.spidersholidays.attendonb.ui.payroll.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class PayRollResponse{
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("data")
    @Expose
    var data: PayRollData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}