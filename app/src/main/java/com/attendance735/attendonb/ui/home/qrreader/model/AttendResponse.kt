package com.attendance735.attendonb.ui.home.qrreader.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by ddopik on 15,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class AttendResponse  {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("data")
    @Expose
    var attendData: AttendData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}