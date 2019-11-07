package com.spidersholidays.attendonb.ui.vacation.pending.model

import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PendingResponseData {

    @SerializedName("pending_vacations")
    @Expose
    var pendingVacations: List<Vacation>? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null


}
