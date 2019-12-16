package com.spidersholidays.attendonb.ui.home.mainstate.viewmodel

import CustomErrorUtils
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
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
import com.spidersholidays.attendonb.utilites.Utilities
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

    private var attendActionNetWork: SingleLiveEvent<Boolean> = SingleLiveEvent()

    private var mapUtls: MapUtls? = null

    fun onAttendBtnChangeState(): LiveData<ApplyButtonState> = attendBtnState
    fun onDataLoading(): LiveData<Boolean> = progressBarState
    fun onAttendAction(): LiveData<AttendMessage> = attendActionCurrentStats
    fun onAttendNetworkAction(): LiveData<Boolean> = attendActionNetWork

    companion object {
        private var INSTANCE: MainStateViewModel? = null

        fun getInstance(activity: HomeActivity): MainStateViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(MainStateViewModel::class.java)
            }
            return INSTANCE
        }
    }

    /**
     * First check Validation
     */

    fun attendRequest(context: Context) {
        progressBarState.postValue(true)
        mapUtls = MapUtls(this)
        mapUtls?.startLocationUpdates(context, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
    }

    override fun onLocationUpdate(location: Location) {

        progressBarState.postValue(false)



        val centralLocation = Location("centrla")
        centralLocation.latitude = PrefUtil.getCurrentCentralLat(AttendOnBApp.app!!)!!
        centralLocation.longitude = PrefUtil.getCurrentCentralLng(AttendOnBApp.app!!)!!
        Log.e(TAG, "onLocationUpdate() ---> CentralLocation is " + PrefUtil.getCurrentCentralRadius(AttendOnBApp.app!!))
        Log.e(TAG, "onLocationUpdate() ---> CurrentLocation to Central Location is " + location.distanceTo(centralLocation))

        mapUtls?.removeLocationRequest()

        /**
         * as isFromMockProvider is not supported for lower Api
         * we enforce user to uninstall mocking app in order to attend
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            when {
                location.isFromMockProvider -> {
                    CustomErrorUtils.viewSnackBarError(Constants.ErrorType.MOCK_LOCATION)
                    Log.e(TAG, "----->isFromMockProvider")
                    return
                }
            }
        } else if (Utilities.areThereMockPermissionApps(AttendOnBApp.app?.baseContext!!)) {
            CustomErrorUtils.viewSnackBarError(Constants.ErrorType.MOCK_LOCATION)
            Log.e(TAG, "-----> Found Mock Location Apps must remove them in order to continue")
            return
        }


        validatesUserRadius(currentLocation = location, centralLocation = centralLocation)
    }


    /**
     * this method used to update current view state and visibility
     * */
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
                            ENTER, OUT -> {
                                /**
                                 * Bad practice vm should no nothing about view --->ApplyButtonState()
                                 * */
                                attendBtnState.postValue(ApplyButtonState(btnEnabled = true, btnVisible = true, btnType = Constants.AttendType.MAIN_CONTAINER))
                            }
                            ENDED -> {
                                attendBtnState.postValue(ApplyButtonState(btnEnabled = false, btnVisible = false, btnType = Constants.AttendType.MAIN_CONTAINER))

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


    private fun validatesUserRadius(currentLocation: Location, centralLocation: Location) {


        if (currentLocation.distanceTo(centralLocation) > PrefUtil.getCurrentCentralRadius(AttendOnBApp.app!!)!!) {
            CustomErrorUtils.viewSnackBarError(Constants.ErrorType.OUT_OF_AREA)
            Log.e(TAG, "----->Not InsideRadius")
            attendBtnState.postValue(ApplyButtonState(btnEnabled = true, btnVisible = true, btnType = Constants.AttendType.MAIN_CONTAINER))
            return
        }

        /**
         *  Final check to verify network connection before send attend request
         */
        if (CustomErrorUtils.haveNetworkConnection(AttendOnBApp.app?.baseContext!!)) {
            sendAttendAction(PrefUtil.getUserId(AttendOnBApp.app!!), currentLocation)
        } else {
            CustomErrorUtils.viewSnackBarError(Constants.ErrorType.ONLINE_DISCONNECTED)
        }
    }


    @SuppressLint("CheckResult")
    private fun sendAttendAction(uid: String, currentLocation: Location) {
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

                                attendActionCurrentStats.postValue(AttendMessage(currentLocation, AttendMessage.AttendFlags.ENTER))
                            }
                            OUT -> {

                                attendActionCurrentStats.postValue(AttendMessage(currentLocation, AttendMessage.AttendFlags.OUT))
                            }
                            ENDED -> {
//                                attendMessage.attendFlag= AttendMessage.AttendFlags.ENDED
                            }
                            else -> Log.e(TAG, "sendAttendCheckRequest ----> ${attendResponse.attendData?.attendStatus.toString()}")
                        }
                    }
                    progressBarState.postValue(false)

                }, { t: Throwable? ->
                    progressBarState.postValue(false)
                    CustomErrorUtils.setError(TAG, t)

                })
    }


    @SuppressLint("CheckResult")
    fun sendAttendNetworkRequest() {

        progressBarState.postValue(true)
        Log.e(TAG, "DEVICE--" + android.os.Build.DEVICE)
        Log.e(TAG, "MODEL--" + android.os.Build.MODEL)
        Log.e(TAG, "PRODUCT--" + android.os.Build.PRODUCT)
        BaseNetWorkApi.sendAttendNetworkRequest(
                uid = PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!)!!
                , platform = "Android"
                , deviceImei = Utilities.getDeviceIMEI(AttendOnBApp.app?.baseContext!!)
                , device = Build.MODEL.toString(),
                deviceDetails = Build.PRODUCT.toString())
                .subscribeOn(Schedulers.io())
                .distinct()
                .debounce(5000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ attendResponse: AttendResponse ->
                    if (attendResponse.status!!) {

                        Log.e(TAG, "--->${attendResponse.attendData?.attendStatus?.msg}")
                        PrefUtil.setCurrentUserStatsID(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.status!!)
                        PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, attendResponse.attendData?.attendStatus?.msg!!)
                        attendResponse.attendData?.attendStatus?.msg?.let {
                            PrefUtil.setCurrentStatsMessage(AttendOnBApp.app?.baseContext!!, it)
                        }


                        when (attendResponse.attendData?.attendStatus?.status) {
                            ENTER, OUT -> {
                                attendActionNetWork.postValue(true)
                                attendBtnState.postValue(ApplyButtonState(btnEnabled = true, btnVisible = true, btnType = Constants.AttendType.NETWORK))
                            }
                            ENDED -> {
                                attendActionNetWork.postValue(true)
                                attendBtnState.postValue(ApplyButtonState(btnEnabled = false, btnVisible = false, btnType = Constants.AttendType.MAIN_CONTAINER))

                            }
                            else -> Log.e(TAG, "sendAttendCheckRequest ----> ${attendResponse.attendData?.attendStatus.toString()}")
                        }

                    }

                    progressBarState.postValue(false)

                }, { t: Throwable? ->
                    progressBarState.postValue(false)
                    attendActionNetWork.postValue(false)
                    attendBtnState.postValue(ApplyButtonState(btnEnabled = true, btnVisible = true, btnType = Constants.AttendType.NETWORK))
                    CustomErrorUtils.setError(TAG, t)

                }
                )
    }


}