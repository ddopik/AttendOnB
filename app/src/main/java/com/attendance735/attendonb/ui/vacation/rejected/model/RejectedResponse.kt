package com.attendance735.attendonb.ui.vacation.rejected.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RejectedResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("data")
    @Expose
    var data: RejectedResponseData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}