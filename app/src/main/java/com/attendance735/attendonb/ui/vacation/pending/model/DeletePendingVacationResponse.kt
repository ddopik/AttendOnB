package com.attendance735.attendonb.ui.vacation.pending.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class DeletePendingVacationResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("data")
    @Expose
    var data: DeletePendingVacationData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}