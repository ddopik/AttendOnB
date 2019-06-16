package com.spidersholidays.attendonb.base.commonModel;

import com.spidersholidays.attendonb.base.BaseErrorData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abdalla_maged On Dec,2018
 */

/**
 * Custom Obj for custom Requests
 * in case 500.401,....
 */

public class ErrorMessageResponse {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public BaseErrorData data;
    @SerializedName("code")
    @Expose
    public String code;


}
