package com.example.attendonb.ui.home.mainstats.viewmodel

import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.utilites.PrefUtil

/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStateViewModel : ViewModel() {


    private val TAG = MainStateViewModel::class.java.simpleName

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
    public fun isCloseLocation(currentLocation: Location) {

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


}