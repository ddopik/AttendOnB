package com.attendance735.attendonb.base.commonModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class VacationsType {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
}