package com.attendance735.attendonb.ui.vacationmangment.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PendingManagementVactionResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("data")
    @Expose
    var data: PendingManagementVacationsData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null

}