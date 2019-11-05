package com.attendance735.attendonb.ui.home.qrreader.model

import com.attendance735.attendonb.ui.login.viewmodel.model.AttendStatus
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by ddopik on 15,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class AttendData {
    @SerializedName("attend_status")
    @Expose
    var attendStatus: AttendStatus? = null

}