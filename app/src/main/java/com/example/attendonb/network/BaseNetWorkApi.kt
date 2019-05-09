package com.example.attendonb.network

import com.androidnetworking.common.Priority
import com.example.attendonb.ui.login.viewmodel.model.LoginResponse
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
        val ERROR_STATE_1 = "login-300"


        private const val BASE_URL = "https://nfc.spiderholidays.co/en"
        private const val LOGIN_URL = "$BASE_URL/Api/login_check"


        fun login(userName: String, password: String, currentLat: String, currentLng: String,deviceImei:String): io.reactivex.Observable<LoginResponse> {
            return Rx2AndroidNetworking.post(LOGIN_URL)
                    .addBodyParameter("username", userName)
                    .addBodyParameter("pass", password)
                    .addBodyParameter("latitude", currentLat)
                    .addBodyParameter("longitude", currentLng)
                    .addBodyParameter("imei", deviceImei)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getObjectObservable(LoginResponse::class.java)
        }

    }
}