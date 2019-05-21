package com.example.attendonb.ui.login.viewmodel

import ErrorUtils
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.network.BaseNetWorkApi
import com.example.attendonb.ui.login.LoginActivity
import com.example.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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


    private var isNetworkError: MutableLiveData<String> = MutableLiveData()
    private var isUnknownError: MutableLiveData<Boolean> = MutableLiveData()
    private var loginState: MutableLiveData<Boolean> = MutableLiveData()
    private var isDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var source: MutableLiveData<String> = MutableLiveData()

    var fetchDataDisposable: Disposable? = null


    fun onDataLoading(): LiveData<Boolean> = isDataLoading
    fun onNetWorkError(): LiveData<String> = isNetworkError
    fun onUnKnownError(): LiveData<Boolean> = isUnknownError
    fun onLoginStateChanged(): LiveData<Boolean> = loginState
    fun sourceListener(): LiveData<String> = source

    init {

        if (PrefUtil.isLoggedIn(AttendOnBApp.app?.baseContext!!)) {
            Log.e(TAG, "----> init isLoggedIn()")
            loginState.postValue(true)
            source.postValue("from init")
         }
    }

    @SuppressLint("CheckResult")
    fun loginUser(context: Context, userName: String, currentLat: Double, currentLng: Double, password: String, deviceImei: String) {
         isDataLoading.postValue(true)
//        if ( !Utilities.areThereMockPermissionApps(AttendOnBApp.app)) {
        fetchDataDisposable = BaseNetWorkApi.login(userName = userName, password = password, currentLat = currentLat.toString(), currentLng = currentLng.toString(), deviceImei = deviceImei)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ loginResponse ->
                        Log.e(TAG, "---->isLoggedIn()")
                        PrefUtil.setIsFirstTimeLogin(context, false)
                        PrefUtil.setIsLoggedIn(context, true)
                        PrefUtil.setUserToken(context, loginResponse.loginData?.userData?.token!!)
                        PrefUtil.setUserID(context, loginResponse.loginData?.userData?.uid!!)
                        PrefUtil.setUserName(context, loginResponse.loginData?.userData?.name!!)
                        PrefUtil.setUserMail(context, loginResponse.loginData?.userData?.email!!)
                        PrefUtil.setUserProfilePic(context, loginResponse.loginData?.userData?.img!!)
                        PrefUtil.setUserGender(context, loginResponse.loginData?.userData?.gender!!)
                        PrefUtil.setUserTrackId(context, loginResponse.loginData?.userData?.track!!)
                        PrefUtil.setCurrentStatsMessage(context, loginResponse.loginData?.attendStatus?.msg!!)
                        PrefUtil.setCurrentUserStatsID(context, loginResponse.loginData?.attendStatus?.status!!)
                        PrefUtil.setCurrentCentralLat(context, loginResponse.loginData?.userData?.lat!!)
                        PrefUtil.setCurrentCentralLng(context, loginResponse.loginData?.userData?.Lng!!)
                        PrefUtil.setCurrentCentralRadius(context, loginResponse.loginData?.userData?.radius!!)

                        isDataLoading.postValue(false)
                        loginState.postValue(true)
                         source.postValue("from loginUser")

                    }, { t: Throwable? ->

                        run {
                            isNetworkError.postValue(t?.message)
                            isDataLoading.postValue(false)
                            loginState.postValue(false)
                            ErrorUtils.setError(TAG, t)
                        }

                    }
                    )



    }


}