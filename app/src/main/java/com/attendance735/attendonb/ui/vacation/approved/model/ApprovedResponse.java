package com.attendance735.attendonb.ui.vacation.approved.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApprovedResponse {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public ApprovedResponseData data;
    @SerializedName("code")
    @Expose
    public String code;
}
