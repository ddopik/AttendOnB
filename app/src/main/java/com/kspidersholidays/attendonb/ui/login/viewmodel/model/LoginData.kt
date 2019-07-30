package com.kspidersholidays.attendonb.ui.login.viewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class LoginData {

    @SerializedName("user_data")
    @Expose
    var userData: LoginUserData? = null
    @SerializedName("attend_status")
    @Expose
    var attendStatus: AttendStatus? = null
}