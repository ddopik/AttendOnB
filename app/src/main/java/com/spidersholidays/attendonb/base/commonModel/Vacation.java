package com.spidersholidays.attendonb.base.commonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vacation {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("reason")
    @Expose
    public String reason;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("total_days")
    @Expose
    public String totalDays;
    @SerializedName("vacations_type_id")
    @Expose
    public String vacationsTypeId;
    @SerializedName("request_to")
    @Expose
    public String requestTo;
    @SerializedName("request_status")
    @Expose
    public String requestStatus;
    @SerializedName("admin_approved_by")
    @Expose
    public String adminApprovedBy;
    @SerializedName("admin_rejected_by")
    @Expose
    public String adminRejectedBy;
    @SerializedName("approved_by")
    @Expose
    public String approvedBy;
    @SerializedName("rejected_by")
    @Expose
    public String rejectedBy;
    @SerializedName("reject_reason")
    @Expose
    public String rejectReason;
    @SerializedName("approve_reject_date")
    @Expose
    public String approveRejectDate;
    @SerializedName("request_date")
    @Expose
    public String requestDate;
}
