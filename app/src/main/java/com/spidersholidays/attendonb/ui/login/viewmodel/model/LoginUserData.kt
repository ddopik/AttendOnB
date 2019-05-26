package com.spidersholidays.attendonb.ui.login.viewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class LoginUserData {
    @SerializedName("uid")
    @Expose
    var uid: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("lat")
    @Expose
    var lat: String? = null

    @SerializedName("Lng")
    @Expose
    var Lng: String? = null

    @SerializedName("radius")
    @Expose
    var radius: String? = null


    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("img")
    @Expose
    var img: String? = null
    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("track")
    @Expose
    var track: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
}