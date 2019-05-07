package com.example.attendonb.network

import com.androidnetworking.common.Priority
import com.example.attendonb.ui.viewmodel.model.LoginResposne
import com.rx2androidnetworking.Rx2AndroidNetworking

class BaseNetWorkApi {
    companion object {


        //Network Status
        var STATUS_OK = "200"
        var DEFAULT_USER_TYPE = "0"
        val STATUS_BAD_REQUEST = 400
        val STATUS_401 = 401
        val STATUS_404 = 404
        val STATUS_500 = 500
        var STATUS_ERROR = "405"
        val ERROR_STATE_1 = "1"


        private val BASE_URL = "baseUrl"
        private val LOGIN_URL = "$BASE_URL/login_url"


        fun login(userName: String, password: String, currentLat: String, currentLng: String): io.reactivex.Observable<LoginResposne> {
            return Rx2AndroidNetworking.post(LOGIN_URL)
                    .addBodyParameter("userName", userName)
                    .addBodyParameter("password", password)
                    .addBodyParameter("currentLat", currentLat)
                    .addBodyParameter("currentLng", currentLng)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getObjectObservable(LoginResposne::class.java)
        }

    }
}