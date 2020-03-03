package com.spidersholidays.attendonb.ui.login.viewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class AttendStatus {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null
}