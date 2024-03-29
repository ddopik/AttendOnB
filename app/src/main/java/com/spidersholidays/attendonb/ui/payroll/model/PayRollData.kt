package com.spidersholidays.attendonb.ui.payroll.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.spidersholidays.attendonb.base.commonModel.PayRoll


class PayRollData{

    @SerializedName("payroll")
    @Expose
    var payroll: MutableList<PayRoll>? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null
}