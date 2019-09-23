package com.spidersattend.attendonb.ui.login.viewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class LoginResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("data")
    @Expose
    var loginData: LoginData? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
}