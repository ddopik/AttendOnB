package com.spidersholidays.attendonb.network

import com.androidnetworking.common.Priority
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.ui.home.qrreader.model.AttendResponse
import com.spidersholidays.attendonb.ui.login.viewmodel.model.LoginResponse
import com.spidersholidays.attendonb.ui.vacation.approved.model.ApprovedResponse
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.CreateNewVacationResponse
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.NewVacationDataResponse
import com.spidersholidays.attendonb.ui.vacation.pending.model.DeletePendingVacationResponse
import com.spidersholidays.attendonb.ui.vacation.pending.model.PendingResponse
import com.spidersholidays.attendonb.ui.vacation.rejected.model.RejectedResponse
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.Observable

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
        val ERROR_STATE_1 = "login-400"
        val ERROR_STATE_2= "attend by ip - 400"


        ///Body Parameter
        val UID = "uid"
        ////

        private val LANGUAGE_PATH_PARAMETER = "lang"
        private val USER_PATH_PARAMETER = "user_id"
        private val VACATION_ID_PATH_PARAMETER = "vacation"
        val IMAGE_BASE_URL = "https://hr-arabjet.com/uploads/thump/"

//                private  var BASE_URL :String ?="https://hr-arabjet.com/{lang}/Api"
        private var BASE_URL: String? = "https://nfc.spiderholidays.co/{lang}/Api"
        private val LOGIN_URL = "$BASE_URL/login_check"
        private val ATTEND_ACTION_URL = "$BASE_URL/attend_action"
        private val ATTEND_NETWORK_URL = "$BASE_URL/network_check"
        private val ATTEND_CHECK_URL = "$BASE_URL/attend_check"
        private val PENDING_VACATION_IRL = "$BASE_URL/Vacations/pending/{$USER_PATH_PARAMETER}"
        private val APPROVED_VACATION_URL = "$BASE_URL/Vacations/approved/{$USER_PATH_PARAMETER}"
        private val REJECTED_VACATION_URL = "$BASE_URL/Vacations/rejected/{$USER_PATH_PARAMETER}"
        private val DELETE_PENDING_VACATION_URL = "$BASE_URL/Vacations/delete/{$VACATION_ID_PATH_PARAMETER}"
        private val NEW_VACATION_DATA_URL = "$BASE_URL/Vacations/create"
        private val CREATE_VACATION_URL = "$BASE_URL/Vacations/create_action"


        fun login(userName: String, password: String, currentLat: String, currentLng: String, deviceImei: String): Observable<LoginResponse> {
            return Rx2AndroidNetworking.post(LOGIN_URL)
                    .addBodyParameter("username", userName)
                    .addBodyParameter("pass", password)
                    .addBodyParameter("latitude", currentLat)
                    .addBodyParameter("longitude", currentLng)
                    .addBodyParameter("imei", deviceImei)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getObjectObservable(LoginResponse::class.java)
        }


        fun sendAttendRequest(uid: String, platform: String, device: String, deviceDetails: String, deviceImei: String, latitude: String, longitude: String): Observable<AttendResponse> {

            return Rx2AndroidNetworking.post(ATTEND_ACTION_URL)
                    .addBodyParameter("uid", uid)
                    .addBodyParameter("platform", platform)
                    .addBodyParameter("device", device)
                    .addBodyParameter("device_details", deviceDetails)
                    .addBodyParameter("imei", deviceImei)
                    .addBodyParameter("latitude", latitude)
                    .addBodyParameter("longitude", longitude)
                    .addBodyParameter("mobile_flag", "1")
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))

                    .setPriority(Priority.HIGH)

                    .responseOnlyFromNetwork

                    .build()

                    .getObjectObservable(AttendResponse::class.java)


        }

        fun sendAttendCheckRequest(uid: String): Observable<AttendResponse> {
            return Rx2AndroidNetworking.post(ATTEND_CHECK_URL)
                    .addBodyParameter("uid", uid)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(AttendResponse::class.java)

        }


        fun sendAttendNetworkRequest(uid: String, platform: String, device: String, deviceDetails: String, deviceImei: String, latitude: String, longitude: String): Observable<AttendResponse>{
            return Rx2AndroidNetworking.post(ATTEND_NETWORK_URL)
                    .addBodyParameter("uid", uid)
                    .addBodyParameter("platform", platform)
                    .addBodyParameter("device", device)
                    .addBodyParameter("device_details", deviceDetails)
                    .addBodyParameter("imei", deviceImei)
                    .addBodyParameter("latitude", latitude)
                    .addBodyParameter("longitude", longitude)
                    .addBodyParameter("mobile_flag", "1")
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))

                    .setPriority(Priority.HIGH)

                    .responseOnlyFromNetwork

                    .build()

                    .getObjectObservable(AttendResponse::class.java)

        }


        fun getPendingVacation(uid: String): Observable<PendingResponse> {
            return Rx2AndroidNetworking.get(PENDING_VACATION_IRL)
                    .addPathParameter(USER_PATH_PARAMETER, uid.toString())
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(PendingResponse::class.java)
        }

        fun getApprovedVacation(uid: String): Observable<ApprovedResponse> {
            return Rx2AndroidNetworking.get(APPROVED_VACATION_URL)
                    .addPathParameter(USER_PATH_PARAMETER, uid.toString())
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(ApprovedResponse::class.java)
        }


        fun getRejectedVacation(uid: String): Observable<RejectedResponse> {
            return Rx2AndroidNetworking.get(REJECTED_VACATION_URL)
                    .addPathParameter(USER_PATH_PARAMETER, uid.toString())
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(RejectedResponse::class.java)
        }


        fun deletePendingVacation(uid: String, vacationId: String): Observable<DeletePendingVacationResponse> {
            return Rx2AndroidNetworking.get(DELETE_PENDING_VACATION_URL)
                    .addPathParameter(VACATION_ID_PATH_PARAMETER, vacationId)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(DeletePendingVacationResponse::class.java)

        }


        fun getNewVacationData(): Observable<NewVacationDataResponse> {
            return Rx2AndroidNetworking.get(NEW_VACATION_DATA_URL)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(NewVacationDataResponse::class.java)
        }


        fun sendNewVacationRequest(uid: String, reason: String, startDate: String, endDate: String, requestTo: String, vacationTypeId: String): Observable<CreateNewVacationResponse> {
            return Rx2AndroidNetworking.post(CREATE_VACATION_URL)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .addBodyParameter(UID, uid)
                    .addBodyParameter("reason", reason)
                    .addBodyParameter("start_date", startDate)
                    .addBodyParameter("end_date", endDate)
                    .addBodyParameter("request_to", requestTo)
                    .addBodyParameter("vacations_type_id", vacationTypeId)
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(CreateNewVacationResponse::class.java)

        }

    }
}