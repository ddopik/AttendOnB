package com.attendance735.attendonb.ui.vacation.pending.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingResponse {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public PendingResponseData data;
    @SerializedName("code")
    @Expose
    public String code;
}
