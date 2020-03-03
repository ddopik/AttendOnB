package com.spidersholidays.attendonb.ui.home.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CheckUserManagementStatsData {
    @SerializedName("is_manager")
    @Expose
    var isManager: Boolean? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null
}