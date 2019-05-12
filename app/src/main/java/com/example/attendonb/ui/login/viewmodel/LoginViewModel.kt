package com.example.attendonb.ui.login.viewmodel

import ErrorUtils
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.R
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.network.BaseNetWorkApi
import com.example.attendonb.ui.login.LoginActivity
import com.example.attendonb.utilites.PrefUtil
import com.example.attendonb.utilites.Utilities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {


    private val TAG = LoginViewModel::class.java.simpleName

    companion object {
        private var INSTANCE: LoginViewModel? = null

        fun getInstance(activity: LoginActivity): LoginViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(LoginViewModel::class.java)
            }
            return INSTANCE
        }
    }


    var isNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    var isUnknownError: MutableLiveData<Boolean> = MutableLiveData()
    var loginState: MutableLiveData<Boolean> = MutableLiveData()
    var isDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    var loginErrorMessage: MutableLiveData<String> = MutableLiveData()


    fun isDataLoading(): LiveData<Boolean> = isDataLoading
    fun isNetWorkError(): LiveData<Boolean> = isNetworkError
    fun isUnKnownError(): LiveData<Boolean> = isUnknownError


    init {
        if (PrefUtil.isLoggedIn(AttendOnBApp.app?.baseContext!!)) {
            loginState.postValue(true)
        }
    }

    @SuppressLint("CheckResult")
    fun loginUser(context: Context, userName: String, currentLat: Double, currentLng: Double, password: String, deviceImei: String) {
         isDataLoading.postValue(true)
//        if ( !Utilities.areThereMockPermissionApps(AttendOnBApp.app)) {
            BaseNetWorkApi.login(userName = userName, password = password, currentLat = currentLat.toString(), currentLng = currentLng.toString(), deviceImei = deviceImei)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ loginResponse ->

                        PrefUtil.setIsFirstTimeLogin(context, false)
                        PrefUtil.setIsLoggedIn(context, true)
                        PrefUtil.setUserToken(context, loginResponse.loginData?.userData?.token!!)
                        PrefUtil.setUserID(context, loginResponse.loginData?.userData?.uid!!)
                        PrefUtil.setUserMail(context, loginResponse.loginData?.userData?.email!!)
                        PrefUtil.setUserProfilePic(context, loginResponse.loginData?.userData?.img!!)
                        PrefUtil.setUserGender(context, loginResponse.loginData?.userData?.gender!!)
                        PrefUtil.setUserTrackId(context, loginResponse.loginData?.userData?.track!!)
                        PrefUtil.setCurrentStatsMessage(context, loginResponse.loginData?.attendStatus?.msg!!)
                        PrefUtil.setCurrentUserStatsID(context, loginResponse.loginData?.attendStatus?.status!!)
                        PrefUtil.setCurrentCentralLat(context, loginResponse.loginData?.userData?.lat!!)
                        PrefUtil.setCurrentCentralLng(context, loginResponse.loginData?.userData?.Lng!!)
                        PrefUtil.setCurrentCentralRadious(context, loginResponse.loginData?.userData?.radius!!)

                        isDataLoading.postValue(false)
                        loginState.postValue(true)

                    }, { t: Throwable? ->

                        run {
                            isNetworkError.postValue(false)
                            isDataLoading.postValue(false)

                            loginState.postValue(false)
                            ErrorUtils.setError(TAG, t)
                        }

                    }

                    )
//        }
//        else {
//
//            isDataLoading.postValue(false)
//            loginErrorMessage.postValue(AttendOnBApp.app?.resources?.getString(R.string.please_disable_mock_location_apps))
//            loginState.postValue(false)
//
//        }


    }
//    , deviceOS = "Android", deviceModel = Utilities.getDeviceName(), deviceIMEI = deviceIMEI

    /**
     * VM Publisher method 2
     * */
//    fun doSearch(q: String, limit: Int = 1, offset: Int = 0): LiveData<Resource<SearchResponse>>{
//        val data = MutableLiveData<Resource<SearchResponse>>();
//
//        api.search(q, limit, offset).enqueue(object : Callback<SearchResponse> {
//            override fun onResponse(call: Call<SearchResponse>?, response: Response<SearchResponse>?) {
//                data.value = Resource.success(response?.body());
//            }
//
//            override fun onFailure(call: Call<SearchResponse>?, t: Throwable?) {
//                val exception = AppException(t)
//                data.value = Resource.error( exception)
//            }
//        });
//        return data;
//    }
}