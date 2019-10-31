package com.attendance735.attendonb.ui.home.qrreader.viewmodel

import CustomErrorUtils
import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.network.BaseNetWorkApi
import com.attendance735.attendonb.ui.home.qrreader.model.AttendResponse
import com.attendance735.attendonb.utilites.PrefUtil
import com.attendance735.attendonb.utilites.Utilities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class QrReaderViewModel : ViewModel() {

    final val TAG = QrReaderViewModel::class.java.simpleName


    companion object {
        private var INSTANCE: QrReaderViewModel? = null

        fun getInstance(activity: AppCompatActivity): QrReaderViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(QrReaderViewModel::class.java)
            }
            return INSTANCE
        }
    }
    private var isNetworkError: MutableLiveData<String> = MutableLiveData()
    private var isUnknownError: MutableLiveData<String> = MutableLiveData()
    private var isDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    private var OnattendErrorMessage: MutableLiveData<String> = MutableLiveData()
    private var OnattendResponseCallBack: MutableLiveData<Boolean> = MutableLiveData()


    fun OnDataLoading(): LiveData<Boolean> = isDataLoading
    fun isNetWorkError(): LiveData<String> = isNetworkError
    fun isUnKnownError(): LiveData<String> = isUnknownError
    fun onAttendResponse(): LiveData<Boolean> = OnattendResponseCallBack


    @SuppressLint("CheckResult")
    fun sendAttendRequest(lat: Double, lng: Double) {

        isDataLoading.postValue(true)
        Log.e(TAG, "DEVICE--" + android.os.Build.DEVICE)
        Log.e(TAG, "MODEL--" + android.os.Build.MODEL)
        Log.e(TAG, "PRODUCT--" + android.os.Build.PRODUCT)
        BaseNetWorkApi.sendAttendRequest(
                uid = PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!)!!
                , platform = "Android"
                , deviceImei = Utilities.getDeviceIMEI(AttendOnBApp.app?.baseContext!!)
                , device = android.os.Build.MODEL.toString(),
                deviceDetails = android.os.Build.PRODUCT.toString(),
                latitude = lat.toString(),
                longitude = lng.toString())
                .subscribeOn(Schedulers.io())
                .distinct()
                .debounce(5000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ attendResponse: AttendResponse ->
                    takeIf { attendResponse.status!! }.apply {
                        Log.e(TAG,"--->${attendResponse.attendData?.attendStatus?.msg}")
                        PrefUtil.setCurrentUserStatsID(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.status!!)
                        PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.msg!!)
                    }

                    attendResponse.attendData?.attendStatus?.msg?.let {
                        PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, it)
                    }

                    isDataLoading.postValue(false)
                    OnattendResponseCallBack.postValue(true)

                }, { t: Throwable? ->
                    isNetworkError.postValue(t?.message)
                    isUnknownError.postValue(t?.message)
                    isDataLoading.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                    OnattendResponseCallBack.postValue(false)
                }
                )
    }

}
