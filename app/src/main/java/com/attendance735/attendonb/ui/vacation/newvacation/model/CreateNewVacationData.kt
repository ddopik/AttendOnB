package com.attendance735.attendonb.ui.vacation.newvacation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class CreateNewVacationData {
    @SerializedName("uid")
    @Expose
    var uid: String? = null
    @SerializedName("reason")
    @Expose
    var reason: String? = null
    @SerializedName("start_date")
    @Expose
    var startDate: String? = null
    @SerializedName("end_date")
    @Expose
    var endDate: String? = null
    @SerializedName("total_days")
    @Expose
    var totalDays: Int? = null
    @SerializedName("vacations_type_id")
    @Expose
    var vacationsTypeId: String? = null
    @SerializedName("request_to")
    @Expose
    var requestTo: String? = null
    @SerializedName("request_status")
    @Expose
    var requestStatus: String? = null
    @SerializedName("request_date")
    @Expose
    var requestDate: String? = null
    @SerializedName("msg")
    @Expose
    var msg: String? = null
}