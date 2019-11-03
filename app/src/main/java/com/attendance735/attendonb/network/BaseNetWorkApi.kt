package com.attendance735.attendonb.network

import com.androidnetworking.common.Priority
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.ui.home.qrreader.model.AttendResponse
import com.attendance735.attendonb.ui.login.viewmodel.model.LoginResponse
import com.attendance735.attendonb.ui.vacation.pending.model.PendingResponse
import com.attendance735.attendonb.utilites.PrefUtil
import com.rx2androidnetworking.Rx2AndroidNetworking

class BaseNetWorkApi {
    companion object {


//        init {
//            BASE_URL = if (PrefUtil.getAppLanguage(AttendOnBApp.app!!) == PrefUtil.ARABIC_LANG ){
//                Log.e(BaseNetWorkApi::class.java.simpleName,"BASE_URL ---> AR")
//                "https://nfc.spiderholidays.co/ar/Api"
//            } else{
//                Log.e(BaseNetWorkApi::class.java.simpleName,"BASE_URL ---> EN")
//
//                "https://nfc.spiderholidays.co/en/Api"
//            }
//
//
//
//}
        //Network Status
        var STATUS_OK = "200"
        var DEFAULT_USER_TYPE = "0"
        val STATUS_BAD_REQUEST = 400
        val STATUS_401 = 401
        val STATUS_404 = 404
        val STATUS_500 = 500
        var STATUS_ERROR = "405"
        val ERROR_STATE_1 = "login-400"
        private val LANGUAGE_PATH_PARAMETER = "lang"
        private val USER_PATH_PARAMETER = "user_id"
        val IMAGE_BASE_URL = "https://hr-arabjet.com/uploads/thump/"

        //        private  var BASE_URL :String ?="https://hr-arabjet.com/{lang}/Api"
        private var BASE_URL: String? = "https://nfc.spiderholidays.co/{lang}/Api"
        private   val LOGIN_URL = "$BASE_URL/login_check"
        private   val ATTEND_ACTION_URL = "$BASE_URL/attend_action"
        private   val ATTEND_CHECK_URL = "$BASE_URL/attend_check"
        private val PENDING_VACATION_IRL = "$BASE_URL/Vacations/pending/{$USER_PATH_PARAMETER}"
        private val APPROVED_VACATION_IRL = "$BASE_URL/Vacations/approved/{$USER_PATH_PARAMETER}"
        private val REJECTED_VACATION_IRL = "$BASE_URL/Vacations/rejected/{$USER_PATH_PARAMETER}"


        fun login(userName: String, password: String, currentLat: String, currentLng: String,deviceImei:String): io.reactivex.Observable<LoginResponse> {
            return Rx2AndroidNetworking.post(LOGIN_URL)
                    .addBodyParameter("username", userName)
                    .addBodyParameter("pass", password)
                    .addBodyParameter("latitude", currentLat)
                    .addBodyParameter("longitude", currentLng)
                    .addBodyParameter("imei", deviceImei)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER,PrefUtil.getAppLanguage(AttendOnBApp.app!!) )
                    .setPriority(Priority.HIGH)
                    .build()
                    .getObjectObservable(LoginResponse::class.java)
        }


        fun sendAttendRequest(uid :String,platform:String,device:String,deviceDetails:String,deviceImei:String,latitude:String,longitude:String) :io.reactivex.Observable<AttendResponse>{

            return Rx2AndroidNetworking.post(ATTEND_ACTION_URL)
                    .addBodyParameter("uid",uid)
                    .addBodyParameter("platform",platform)
                    .addBodyParameter("device",device)
                    .addBodyParameter("device_details",deviceDetails)
                    .addBodyParameter("imei",deviceImei)
                    .addBodyParameter("latitude",latitude)
                    .addBodyParameter("longitude",longitude)
                    .addBodyParameter("mobile_flag","1")
                    .addPathParameter(LANGUAGE_PATH_PARAMETER,PrefUtil.getAppLanguage(AttendOnBApp.app!!) )

                    .setPriority(Priority.HIGH)

                    .responseOnlyFromNetwork

                    .build()

                    .getObjectObservable(AttendResponse::class.java)


        }

        fun sendAttendCheckRequest(uid :String) :io.reactivex.Observable<AttendResponse>{
            return Rx2AndroidNetworking.post(ATTEND_CHECK_URL)
                    .addBodyParameter("uid",uid)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER,PrefUtil.getAppLanguage(AttendOnBApp.app!!) )
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(AttendResponse::class.java)

        }


        fun getPendingVacation(uid: String): io.reactivex.Observable<PendingResponse> {
            return Rx2AndroidNetworking.get(PENDING_VACATION_IRL)
                    .addPathParameter(USER_PATH_PARAMETER, uid.toString())
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(PendingResponse::class.java)
        }



    }
}