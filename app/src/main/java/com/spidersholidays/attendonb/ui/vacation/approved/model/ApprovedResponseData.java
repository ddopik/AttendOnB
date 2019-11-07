package com.spidersholidays.attendonb.ui.vacation.approved.model;

import com.spidersholidays.attendonb.base.commonModel.Vacation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApprovedResponseData {

    @SerializedName("approved_vacations")
    @Expose
    public List<Vacation> approvedVacations = null;
    @SerializedName("msg")
    @Expose
    public String msg;

}
