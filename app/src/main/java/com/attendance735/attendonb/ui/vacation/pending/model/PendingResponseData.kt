package com.attendance735.attendonb.ui.vacation.pending.model

import com.attendance735.attendonb.base.commonModel.Vacation
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PendingResponseData {

    @SerializedName("pending_vacations")
    @Expose
    var pendingVacations: MutableList<Vacation>? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null


}
