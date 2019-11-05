package com.attendance735.attendonb.ui.vacation.pending

import CustomErrorUtils
import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.BaseErrorData
import com.attendance735.attendonb.base.commonModel.ErrorMessageResponse
import com.attendance735.attendonb.base.commonModel.Vacation
import com.attendance735.attendonb.network.BaseNetWorkApi
import com.attendance735.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PendingViewModel : ViewModel() {
    private val TAG = PendingFragment::javaClass.name

    companion object {
        private var INSTANCE: PendingViewModel? = null

        fun getInstance(fragment: Fragment): PendingViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(fragment).get(PendingViewModel::class.java)
            }
            return INSTANCE
        }
    }


    private val progressBarState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val pendingVacationList: MutableLiveData<List<Vacation>> = MutableLiveData()


    fun onPendingVacationChange(): LiveData<List<Vacation>> = pendingVacationList
    fun onPendingProgressChange(): LiveData<Boolean> = progressBarState


    @SuppressLint("CheckResult")
    fun getPendingVacations() {
        progressBarState.value = true
        BaseNetWorkApi.getPendingVacation(PrefUtil.getUserId(AttendOnBApp.app!!.baseContext).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status) {
                        Log.e(TAG, it.data?.msg!!)

                        it.data?.pendingVacations?.let {
                            Log.e(TAG, it.size.toString())
                            pendingVacationList.value = it
                        }

                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Pending vacation"), "0"))
                    }
                    progressBarState.value = false

                }, { t: Throwable? ->
                    progressBarState.value = false
                    CustomErrorUtils.setError(TAG, t)
                })

    }

}