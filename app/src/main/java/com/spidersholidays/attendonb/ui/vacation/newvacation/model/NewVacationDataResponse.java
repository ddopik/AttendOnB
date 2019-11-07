package com.spidersholidays.attendonb.ui.vacation.newvacation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewVacationDataResponse {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public NewVacationData data;
    @SerializedName("code")
    @Expose
    public String code;

}
