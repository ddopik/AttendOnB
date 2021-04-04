package com.spidersholidays.attendonb.ui.vacation.rejected

import CustomErrorUtils
import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.BaseErrorData
import com.spidersholidays.attendonb.base.commonModel.ErrorMessageResponse
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RejectedViewModel : ViewModel() {

    private val TAG = RejectedViewModel::class.java.name

    companion object {
        private var INSTANCE: RejectedViewModel? = null

        fun getInstance(fragment: Fragment): RejectedViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(fragment).get(RejectedViewModel::class.java)
            }
            return INSTANCE
        }
    }


    private val progressBarState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val rejectedVacationList: MutableLiveData<List<Vacation>> = MutableLiveData()


    fun onRejectedVacationChange(): LiveData<List<Vacation>> = rejectedVacationList
    fun onRejectedProgressChange(): LiveData<Boolean> = progressBarState


    @SuppressLint("CheckResult")
    fun getRejectedVacations() {
        Log.e(TAG, "getRejectedVacations()")
        progressBarState.value = true
        BaseNetWorkApi.getRejectedVacation(PrefUtil.getUserId(AttendOnBApp.app!!.baseContext).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status) {
                        Log.e(TAG, it.data?.msg?:"error")
                        it.data?.rejectedVacations?.let {
                            rejectedVacationList.postValue(it.reversed())
                        }
                        if (it.data?.rejectedVacations == null) {
                            rejectedVacationList.postValue(it.data?.rejectedVacations)
                        }
                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Rejected vacation"), "0"))
                        progressBarState.value = false
                    }
                    progressBarState.value = false

                }, { t: Throwable? ->
                    progressBarState.value = false
                    CustomErrorUtils.setError(TAG, t)
                })

    }
}