package com.example.attendonb.ui.home.mainstats.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.network.BaseNetWorkApi
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.ui.home.qrreader.model.AttendResponse
import com.example.attendonb.utilites.Constants.Companion.ENTER
import com.example.attendonb.utilites.Constants.Companion.OUT
import com.example.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStateViewModel : ViewModel() {


    private val TAG = MainStateViewModel::class.java.simpleName


    var attendBtnState: MutableLiveData<Boolean> = MutableLiveData()

    var progressBarState: MutableLiveData<Boolean> = MutableLiveData();

    fun onAttendBtnChangeState(): LiveData<Boolean> = attendBtnState
    fun onDataLoading(): LiveData<Boolean> = progressBarState

    companion object {
        private var INSTANCE: MainStateViewModel? = null

        fun getInstance(activity: HomeActivity): MainStateViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(MainStateViewModel::class.java)
            }
            return INSTANCE
        }
    }


    //    private fun isCloseLocation(currentLocationLat: Double, currentLocationLng: Double) {
    fun isCloseLocation(currentLocation: Location) {

        val centralLocation  = Location(LocationManager.GPS_PROVIDER)

        centralLocation.latitude= PrefUtil.getCurrentCentralLat(AttendOnBApp.app?.baseContext!!)?.toDouble()!!
        centralLocation.longitude= PrefUtil.getCurrentCentralLng(AttendOnBApp.app?.baseContext!!)?.toDouble()!!



        val distanceInKiloMeters = currentLocation.distanceTo(centralLocation) // as distance is in meter




        if (distanceInKiloMeters <= 1) {

//            Log.e(TAG,"isCloseLocation 1--->$distanceInKiloMeters")
        } else {

//            Log.e(TAG,"isCloseLocation 2--->$distanceInKiloMeters")
        }
    }


    @SuppressLint("CheckResult")
    fun checkAttendStatus(uid: String) {

        progressBarState.postValue(true)
        BaseNetWorkApi.sendAttendCheckRequest(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ attendResponse: AttendResponse? ->

                    attendResponse?.attendData?.attendStatus?.let {
                        PrefUtil.setCurrentUserStatsID(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.status!!)
                        PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.msg!!)

                        if (attendResponse.attendData?.attendStatus?.status!! == ENTER || attendResponse.attendData?.attendStatus?.status!! == OUT) {
                            attendBtnState.postValue(true)
                        } else {
                            attendBtnState.postValue(false)
                        }
                        progressBarState.postValue(false)
                    }


                }, { t: Throwable? ->
                    progressBarState.postValue(false)
                    ErrorUtils.setError(TAG, t)

                })
    }


}