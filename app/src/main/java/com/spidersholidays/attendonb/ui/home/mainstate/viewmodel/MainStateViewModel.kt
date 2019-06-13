package com.spidersholidays.attendonb.ui.home.mainstate.viewmodel

import CustomErrorUtils
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.SingleLiveEvent
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.home.mainstate.model.ApplyButtonState
import com.spidersholidays.attendonb.ui.home.model.AttendMessage
import com.spidersholidays.attendonb.ui.home.qrreader.model.AttendResponse
import com.spidersholidays.attendonb.utilites.Constants
import com.spidersholidays.attendonb.utilites.Constants.Companion.CHECK_STATS_TIME_OUT
import com.spidersholidays.attendonb.utilites.Constants.Companion.ENDED
import com.spidersholidays.attendonb.utilites.Constants.Companion.ENTER
import com.spidersholidays.attendonb.utilites.Constants.Companion.OUT
import com.spidersholidays.attendonb.utilites.MapUtls
import com.spidersholidays.attendonb.utilites.PrefUtil
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


    private var attendBtnState: SingleLiveEvent<ApplyButtonState> = SingleLiveEvent()

    private var progressBarState: SingleLiveEvent<Boolean> = SingleLiveEvent()


    private var attendActionCurrentStats: SingleLiveEvent<AttendMessage> = SingleLiveEvent()

    private var mapUtls: MapUtls? = null

    fun onAttendBtnChangeState(): SingleLiveEvent<ApplyButtonState> = attendBtnState
    fun onDataLoading(): LiveData<Boolean> = progressBarState
    fun onAttendAction(): SingleLiveEvent<AttendMessage> = attendActionCurrentStats


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
                        val applyButtonStats=ApplyButtonState();
                         when (attendResponse.attendData?.attendStatus?.status) {
                            ENTER -> {
                                applyButtonStats.isEnable=true
                                attendBtnState.postValue(applyButtonStats)
                            }
                            OUT -> {
                                applyButtonStats.isEnable=true
                                attendBtnState.postValue(applyButtonStats)

                            }
                            ENDED -> {
                                applyButtonStats.isEnable=false
                                applyButtonStats.isViable=false
                                attendBtnState.postValue(applyButtonStats)
                            }
                        }
                    }
                    mapUtls?.removeLocationRequest()
                    progressBarState.postValue(false)
                 }, { t: Throwable? ->
                    mapUtls?.removeLocationRequest()
                    progressBarState.postValue(false)
                    CustomErrorUtils.setError(TAG, t)

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
                                attendMessage.attendFlag= AttendMessage.AttendFlags.ENDED
                            }
                            else -> Log.e(TAG,"sendAttendCheckRequest ----> ${attendResponse.attendData?.attendStatus.toString()}")
                        }
                    }
                    mapUtls?.removeLocationRequest()
                    progressBarState.postValue(false)

                }, { t: Throwable? ->
                    mapUtls?.removeLocationRequest()
                    progressBarState.postValue(false)

                    if(PrefUtil.getCurrentUserStatsID(AttendOnBApp.app!!) != ENDED){
                        val applyButtonStats=ApplyButtonState()
                        applyButtonStats.isEnable=true
                        attendBtnState.postValue(applyButtonStats)
                    }

                    CustomErrorUtils.setError(TAG, t)

                })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onLocationUpdate(location: Location) {

         progressBarState.postValue(false)
        val applyButtonStats=ApplyButtonState()
        when {
            location.isFromMockProvider -> {
                CustomErrorUtils.viewSnackBarError(Constants.ErrorType.MOCK_LOCATION)
                applyButtonStats.isEnable = true
                attendBtnState.postValue(applyButtonStats)
                mapUtls?.removeLocationRequest()
                Log.e(TAG, "----->isFromMockProvider")

            }
            !PrefUtil.isInsideRadius(AttendOnBApp.app!!) -> {
                CustomErrorUtils.viewSnackBarError(Constants.ErrorType.OUT_OF_AREA)
                applyButtonStats.isEnable = true
                attendBtnState.postValue(applyButtonStats)
                mapUtls?.removeLocationRequest()
                Log.e(TAG, "----->Not InsideRadius")

            }
            CustomErrorUtils.haveNetworkConnection(AttendOnBApp.app?.baseContext!!) -> {
                sendAttendAction(PrefUtil.getUserId(AttendOnBApp.app!!), location)
            }
            else -> {
                CustomErrorUtils.viewSnackBarError(Constants.ErrorType.ONLINE_DISCONNECTED)
                mapUtls?.removeLocationRequest()
            }
        }


    }


    fun attendRequest(context: Context) {
        mapUtls=MapUtls(this)
        val applyButtonStats=ApplyButtonState()
        applyButtonStats.isEnable=false
        attendBtnState.postValue(applyButtonStats)
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