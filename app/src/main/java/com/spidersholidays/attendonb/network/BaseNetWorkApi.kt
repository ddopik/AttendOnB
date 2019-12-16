package com.spidersholidays.attendonb.network

import android.os.Build
import android.util.Log
import com.androidnetworking.common.Priority
import com.ddopik.linktask.ui.home.model.ArticlesResponse
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.ui.home.model.CheckUserManagementStatsResponse
import com.spidersholidays.attendonb.ui.home.qrreader.model.AttendResponse
import com.spidersholidays.attendonb.ui.login.viewmodel.model.LoginResponse
import com.spidersholidays.attendonb.ui.payroll.model.PayRollResponse
import com.spidersholidays.attendonb.ui.vacation.approved.model.ApprovedResponse
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.CreateNewVacationResponse
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.NewVacationDataResponse
import com.spidersholidays.attendonb.ui.vacation.pending.model.DeletePendingVacationResponse
import com.spidersholidays.attendonb.ui.vacation.pending.model.PendingResponse
import com.spidersholidays.attendonb.ui.vacation.rejected.model.RejectedResponse
import com.spidersholidays.attendonb.ui.vacationmangment.model.PendingManagementVactionResponse
import com.spidersholidays.attendonb.ui.vacationmangment.model.VacationMangerRequestResponse
import com.spidersholidays.attendonb.utilites.PrefUtil
import com.spidersholidays.attendonb.utilites.Tls12SocketFactory
import io.reactivex.Observable
import okhttp3.ConnectionSpec
import okhttp3.ConnectionSpec.*
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext


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
        val ERROR_STATE_2 = "attend by ip - 400"


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
        private val CHECK_USER_MANAGEMENT_STATS_URL = "$BASE_URL/check_if_user_is_manager"

        /////
        private val PAY_ROLL_DATA_URL = "$BASE_URL/payroll/{$USER_PATH_PARAMETER}"
        private val PENDING_Management_VACATION_IRL = "$BASE_URL/Get_manager_pending_vacations/{$USER_PATH_PARAMETER}"
        private val APPROVED_Management_VACATION_IRL = "$BASE_URL/Get_manager_approved_vacations/{$USER_PATH_PARAMETER}"
        private val REJECTED_Management_VACATION_URL = "$BASE_URL/Get_manager_rejected_vacations/{$USER_PATH_PARAMETER}"


        private val APPROVE_Management_VACATION_IRL = "$BASE_URL/approve_vacation"
        private val REJECTE_Management_VACATION_URL = "$BASE_URL/reject_vacation"

/////

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
                    .addBodyParameter("attend_method", "QR")
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


        fun sendAttendNetworkRequest(uid: String, platform: String, device: String, deviceDetails: String, deviceImei: String): Observable<AttendResponse> {
            return Rx2AndroidNetworking.post(ATTEND_NETWORK_URL)
                    .addBodyParameter("uid", uid)
                    .addBodyParameter("platform", platform)
                    .addBodyParameter("device", device)
                    .addBodyParameter("device_details", deviceDetails)
                    .addBodyParameter("imei", deviceImei)
                    .addBodyParameter("latitude", "0.0")
                    .addBodyParameter("longitude", "0.0")
                    .addBodyParameter("mobile_flag", "1")
                    .addBodyParameter("attend_method", "NW")
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

        fun checkUserManagementStats(userId:String):Observable<CheckUserManagementStatsResponse>{
            return Rx2AndroidNetworking.post(CHECK_USER_MANAGEMENT_STATS_URL)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .addBodyParameter(UID,userId)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(CheckUserManagementStatsResponse::class.java)
        }

        fun getPayRollData(userId: String): Observable<PayRollResponse> {
            return Rx2AndroidNetworking.post(PAY_ROLL_DATA_URL)
                    .addBodyParameter(UID, userId)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(PayRollResponse::class.java)

        }

        fun getPendingManagementVacation(uid: String): Observable<PendingManagementVactionResponse> {
            return Rx2AndroidNetworking.post(PENDING_Management_VACATION_IRL)
                    .addBodyParameter(UID, uid)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(PendingManagementVactionResponse::class.java)
        }

        fun getApprovedManagementVacation(uid: String): Observable<PendingManagementVactionResponse> {
            return Rx2AndroidNetworking.post(APPROVED_Management_VACATION_IRL)
                    .addBodyParameter(UID, uid)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(PendingManagementVactionResponse::class.java)
        }


        fun getRejectedManagementVacation(uid: String): Observable<PendingManagementVactionResponse> {
            return Rx2AndroidNetworking.post(REJECTED_Management_VACATION_URL)
                    .addBodyParameter(UID, uid)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER, PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .setPriority(Priority.HIGH)
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(PendingManagementVactionResponse::class.java)
        }


        fun sendApproveManagementVacation(userId: String, vacationID: String): Observable<VacationMangerRequestResponse> {
            return Rx2AndroidNetworking.post(APPROVE_Management_VACATION_IRL)
                    .addBodyParameter(UID, userId)
                    .addBodyParameter("id", vacationID)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER,PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(VacationMangerRequestResponse::class.java)

        }

        fun sendRejectManagementVacation(userId: String, vacationID: String,reason:String): Observable<VacationMangerRequestResponse> {
            return Rx2AndroidNetworking.post(REJECTE_Management_VACATION_URL)
                    .addBodyParameter(UID, userId)
                    .addBodyParameter("id", vacationID)
                    .addBodyParameter("reason",reason)
                    .addPathParameter(LANGUAGE_PATH_PARAMETER,PrefUtil.getAppLanguage(AttendOnBApp.app!!))
                    .responseOnlyFromNetwork
                    .build()
                    .getObjectObservable(VacationMangerRequestResponse::class.java)

        }



        val ARTICLE_1_URL =
                "https://newsapi.org/v1/articles?source=the-next-web&apiKey=533af958594143758318137469b41ba9"
        val ARTICLE_2_URL =
                "https://newsapi.org/v1/articles?source=associated-press&apiKey=533af958594143758318137469b41ba9"


        fun getArticleList_1(): Observable<ArticlesResponse> {
            return Rx2AndroidNetworking.get(ARTICLE_1_URL)
                    .build()
                    .getObjectObservable(ArticlesResponse::class.java)


        }    fun getArticleList_2(): Observable<ArticlesResponse> {
            return Rx2AndroidNetworking.get(ARTICLE_2_URL)
                    .build()
                    .getObjectObservable(ArticlesResponse::class.java)


        }

        /**
         *
         */
        open fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder? {
            if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
                try {
                    val sc = SSLContext.getInstance("TLSv1.2")
                    sc.init(null, null, null)
                    client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))
                    val cs = Builder(MODERN_TLS)
                            .tlsVersions(TlsVersion.TLS_1_2).build()
                    val specs: MutableList<ConnectionSpec> = ArrayList()
                    specs.add(cs)
                    specs.add(COMPATIBLE_TLS)
                    specs.add(CLEARTEXT)
                    client.connectionSpecs(specs)
                } catch (exc: Exception) {
                    Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
                }
            }
            return client
        }

        fun getNewHttpClient(): OkHttpClient? {
            val client = OkHttpClient.Builder().followRedirects(true).followSslRedirects(true)
                    .retryOnConnectionFailure(true).cache(null).connectTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
            return enableTls12OnPreLollipop(client)!!.build()
        }
    }




}