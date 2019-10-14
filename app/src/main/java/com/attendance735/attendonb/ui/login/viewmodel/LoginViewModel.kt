package com.attendance735.attendonb.ui.login.viewmodel

import CustomErrorUtils
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.attendance735.attendonb.network.BaseNetWorkApi
import com.attendance735.attendonb.ui.login.LoginActivity
import com.attendance735.attendonb.utilites.PrefUtil
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


     private var isUnknownError: MutableLiveData<Boolean> = MutableLiveData()
    private var loginState: MutableLiveData<Boolean> = MutableLiveData()
    private var isDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var source: MutableLiveData<String> = MutableLiveData()

    var fetchDataDisposable: Disposable? = null


    fun onDataLoading(): LiveData<Boolean> = isDataLoading
     fun onUnKnownError(): LiveData<Boolean> = isUnknownError
    fun onLoginStateChanged(): LiveData<Boolean> = loginState
    fun sourceListener(): LiveData<String> = source


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
                             isDataLoading.postValue(false)
                            loginState.postValue(false)
                            CustomErrorUtils.setError(TAG, t)
                        }

                    }
                    )



    }


}