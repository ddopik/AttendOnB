package com.spidersholidays.attendonb.ui.home

import CustomErrorUtils
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {


    private val isMangerStats: MutableLiveData<Boolean> = MutableLiveData()


    companion object {
        val TAG = HomeViewModel::javaClass.name
        fun getInstance(activity: HomeActivity): HomeViewModel {

            return ViewModelProviders.of(activity).get(HomeViewModel::class.java)
        }


    }

    fun onUserManagementStatsChange():LiveData<Boolean> = isMangerStats

    @SuppressLint("CheckResult")
    fun checkUserManagementStats(userId: String) {
        BaseNetWorkApi.checkUserManagementStats(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ check ->
                    if (check.status) {
                        check.data?.isManager?.let {
                            if (it) {
                                isMangerStats.postValue(true)
                            } else {
                                isMangerStats.postValue(false)

                            }
                        }
                    }
                }, { t: Throwable? ->
                    CustomErrorUtils.setError(TAG, t)
                })
    }


}