package com.spidersholidays.attendonb.ui.vacation.pending.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PendingResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("data")
    @Expose
    var data: PendingResponseData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}
