package com.example.attendonb.ui.home.mainstats.viewmodel

import ErrorUtils
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.R
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.network.BaseNetWorkApi
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.ui.home.model.AttendMessage
import com.example.attendonb.ui.home.qrreader.model.AttendResponse
import com.example.attendonb.utilites.Constants.Companion.CHECK_STATS_TIME_OUT
import com.example.attendonb.utilites.Constants.Companion.ENDED
import com.example.attendonb.utilites.Constants.Companion.ENTER
import com.example.attendonb.utilites.Constants.Companion.OUT
import com.example.attendonb.utilites.MapUtls
import com.example.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStateViewModel : ViewModel(), MapUtls.OnLocationUpdate {


    private val TAG = MainStateViewModel::class.java.simpleName


    private var attendBtnState: MutableLiveData<Boolean> = MutableLiveData()

    private var progressBarState: MutableLiveData<Boolean> = MutableLiveData()

    private var currentStats: MutableLiveData<String> = MutableLiveData()


    private var attendActionCurrentStats: MutableLiveData<AttendMessage> = MutableLiveData()

    private var mapUtls: MapUtls? = null

    fun onAttendBtnChangeState(): LiveData<Boolean> = attendBtnState
    fun onDataLoading(): LiveData<Boolean> = progressBarState
    fun getCurrentMessageStats(): LiveData<String> = currentStats
     fun onAttendAction(): LiveData<AttendMessage> = attendActionCurrentStats


    companion object {
        private var INSTANCE: MainStateViewModel? = null

        fun getInstance(activity: HomeActivity): MainStateViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(MainStateViewModel::class.java)
             }
            return INSTANCE
        }
    }

    @SuppressLint("CheckResult")
    fun checkAttendStatus(uid: String) {
        progressBarState.postValue(true)

        BaseNetWorkApi.sendAttendCheckRequest(uid)
                .subscribeOn(Schedulers.io())
                .debounce(CHECK_STATS_TIME_OUT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ attendResponse: AttendResponse? ->

                    attendResponse?.attendData?.attendStatus?.let {
                        PrefUtil.setCurrentUserStatsID(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.status!!)
                        PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.msg!!)
                         when (attendResponse.attendData?.attendStatus?.status) {
                            ENTER -> {

                                attendBtnState.postValue(true)
                            }
                            OUT -> {
                                attendBtnState.postValue(true)

                            }
                            ENDED -> {
                                attendBtnState.postValue(false)
                            }
                        }
                    }
                    mapUtls?.removeLocationRequest()
                    progressBarState.postValue(false)
                 }, { t: Throwable? ->
                    progressBarState.postValue(false)
                    ErrorUtils.setError(TAG, t)

                })
    }

    @SuppressLint("CheckResult")
    fun sendAttendAction(uid: String, currentLocation: Location) {
        progressBarState.postValue(true)

        BaseNetWorkApi.sendAttendCheckRequest(uid)
                .subscribeOn(Schedulers.io())
                .debounce(CHECK_STATS_TIME_OUT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ attendResponse: AttendResponse? ->

                    attendResponse?.attendData?.attendStatus?.let {
                        PrefUtil.setCurrentUserStatsID(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.status!!)
                        PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.msg!!)
                        val attendMessage=AttendMessage()
                        when (attendResponse.attendData?.attendStatus?.status) {
                            ENTER -> {
                                attendMessage.currentLocation=currentLocation;
                                attendMessage.attendFlag=AttendMessage.AttendFlags.ENTER
                                attendActionCurrentStats.postValue(attendMessage)
                            }
                            OUT -> {
                                attendMessage.currentLocation=currentLocation;
                                attendMessage.attendFlag= AttendMessage.AttendFlags.OUT
                                attendActionCurrentStats.postValue(attendMessage)
                            }
                            ENDED -> {
                                attendMessage.currentLocation=currentLocation;
                                attendMessage.attendFlag= AttendMessage.AttendFlags.ENDED
                            }
                        }
                    }
                    mapUtls?.removeLocationRequest()
                    progressBarState.postValue(false)

                }, { t: Throwable? ->
                    progressBarState.postValue(false)
                    ErrorUtils.setError(TAG, t)

                })
    }

    override fun onLocationUpdate(location: Location) {

         progressBarState.postValue(false)
        if (location.isFromMockProvider) {
            currentStats.postValue(AttendOnBApp.app?.getString(R.string.mock_location_warrning))
            attendBtnState.postValue(false)
            Log.e(TAG, "----->isFromMockProvider")

        } else if (!PrefUtil.isInsideRadius(AttendOnBApp.app!!)) {
            currentStats.postValue(AttendOnBApp.app?.getString(R.string.you_are_out_of_area))
            attendBtnState.postValue(false)
            Log.e(TAG, "----->Noy isInsideRadius")

        } else {
            sendAttendAction(PrefUtil.getUserId(AttendOnBApp.app!!), location)
        }


    }


    fun attendRequest(context: Context) {
        mapUtls=MapUtls(this);
        progressBarState.postValue(true)
        attendBtnState.postValue(false)
        mapUtls?.startLocationUpdates(context, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
    }




    fun isCloseLocation(currentLocation: Location) {

        val centralLocation = Location(LocationManager.GPS_PROVIDER)

        centralLocation.latitude = PrefUtil.getCurrentCentralLat(AttendOnBApp.app?.baseContext!!).toDouble()
        centralLocation.longitude = PrefUtil.getCurrentCentralLng(AttendOnBApp.app?.baseContext!!).toDouble()


        val distanceInKiloMeters = currentLocation.distanceTo(centralLocation) // as distance is in meter




        if (distanceInKiloMeters <= 1) {

//            Log.e(TAG,"isCloseLocation 1--->$distanceInKiloMeters")
        } else {

//            Log.e(TAG,"isCloseLocation 2--->$distanceInKiloMeters")
        }
    }
}