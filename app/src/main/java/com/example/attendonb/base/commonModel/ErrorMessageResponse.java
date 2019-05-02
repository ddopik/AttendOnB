package com.example.attendonb.base.commonModel;

import com.example.attendonb.base.BaseErrorResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abdalla_maged On Dec,2018
 */

/**
 * Custom Obj for custom Requests
 * in case 500.401,....
 */

public class ErrorMessageResponse {
    @SerializedName("errors")
    @Expose
    public List<BaseErrorResponse> errors = null;

}
