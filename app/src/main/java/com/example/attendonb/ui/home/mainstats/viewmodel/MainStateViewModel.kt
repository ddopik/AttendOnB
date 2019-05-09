package com.example.attendonb.ui.home.mainstats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.utilites.PrefUtil
import com.example.attendonb.utilites.Utilities

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


    private val isMockEnabled: MutableLiveData<Boolean> = MutableLiveData()

    fun isMockEnabled(): LiveData<Boolean> = isMockEnabled


    fun checkMockEnabled(){



    }

}