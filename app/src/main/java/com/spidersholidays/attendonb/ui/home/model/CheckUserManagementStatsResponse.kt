package com.spidersholidays.attendonb.ui.home.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CheckUserManagementStatsResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("data")
    @Expose
    var data: CheckUserManagementStatsData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}