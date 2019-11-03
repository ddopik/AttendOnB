package com.attendance735.attendonb.ui.vacation.pending.model;

import com.attendance735.attendonb.base.commonModel.Vacation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingResponseData {

    @SerializedName("pending_vacations")
    @Expose
    public List<Vacation> pendingVacations = null;
    @SerializedName("msg")
    @Expose
    public String msg;


}
