package com.spidersholidays.attendonb.ui.vacation.pending

import CustomErrorUtils
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.BaseErrorData
import com.spidersholidays.attendonb.base.SingleLiveEvent
import com.spidersholidays.attendonb.base.commonModel.ErrorMessageResponse
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PendingViewModel : ViewModel() {
    private val TAG = PendingFragment::javaClass.name

    companion object {
        private var INSTANCE: PendingViewModel? = null
        val VACATION_ID = "vacation_id"
        val VACATION_STATS = "vacation_delete_stats"
        fun getInstance(fragment: Fragment): PendingViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(fragment).get(PendingViewModel::class.java)
            }
            return INSTANCE
        }
    }


    private val progressBarState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val pendingVacationList: MutableLiveData<List<Vacation>> = SingleLiveEvent()
    private val deleteVacationState: MutableLiveData<Bundle> = SingleLiveEvent<Bundle>()

    fun onPendingVacationChange(): LiveData<List<Vacation>> = pendingVacationList
    fun onPendingProgressChange(): LiveData<Boolean> = progressBarState
    fun onPendingVacationDeleted(): LiveData<Bundle> = deleteVacationState

    @SuppressLint("CheckResult")
    fun getPendingVacations() {
        Log.e(TAG, "  ---->  getPendingVacations()")
        progressBarState.postValue(true)
        BaseNetWorkApi.getPendingVacation(PrefUtil.getUserId(AttendOnBApp.app!!.baseContext).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    if (it.status) {
                        Log.e(TAG, it.data?.msg!!)
                        it.data?.pendingVacations?.let {
                            Log.e(TAG, it.size.toString())
                            pendingVacationList.postValue(it.reversed())
                        }
                        if (it?.data?.pendingVacations == null) {
                            pendingVacationList.postValue(it?.data?.pendingVacations)
                        }
                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Pending vacation"), "0"))
                    }
                    progressBarState.postValue(false)

                }, { t: Throwable? ->
                    progressBarState.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })

    }


    @SuppressLint("CheckResult")
    fun deleteVacation(vacationId: String) {
        progressBarState.value = true

        BaseNetWorkApi.deletePendingVacation(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!).toString(), vacationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val bundle = Bundle()
                    bundle.putString(VACATION_ID, vacationId)
                    bundle.putBoolean(VACATION_STATS, it.status)
                    deleteVacationState.postValue(bundle)
                    progressBarState.value = false

                }, { t: Throwable ->
                    progressBarState.value = false
                    CustomErrorUtils.setError(TAG, t)
                }

                )
    }

}