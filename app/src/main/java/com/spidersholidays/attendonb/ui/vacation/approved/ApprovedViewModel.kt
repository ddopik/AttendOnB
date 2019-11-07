package com.spidersholidays.attendonb.ui.vacation.approved

import CustomErrorUtils
import android.annotation.SuppressLint
import android.util.Log
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

class ApprovedViewModel : ViewModel() {

    private val TAG = ApprovedViewModel::class.java.name

    companion object {
        private var INSTANCE: ApprovedViewModel? = null

        fun getInstance(fragment: ApprovedFragment): ApprovedViewModel {

            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(fragment).get(ApprovedViewModel::class.java)
            }

            return INSTANCE!!
        }

    }

    private val progressBarState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val ApprovedList: MutableLiveData<List<Vacation>> = MutableLiveData()


    fun onApprovrdVacationChange(): LiveData<List<Vacation>> = ApprovedList
    fun onApprovedProgressChange(): LiveData<Boolean> = progressBarState


    @SuppressLint("CheckResult")
    fun getApprovedVacations() {
        progressBarState.value = true
        BaseNetWorkApi.getApprovedVacation(PrefUtil.getUserId(AttendOnBApp.app!!.baseContext).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status) {
                        Log.e(TAG, it.data.msg)
                        it.data.approvedVacations?.let {
                            ApprovedList.postValue( it)
                        }
                        if ( it.data.approvedVacations ==null ){
//                            if(it.data.approvedVacations.size ==0 )
                            ApprovedList.postValue(it.data.approvedVacations)
                        }

                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Approved vacation"), "0"))
                    }
                    progressBarState.value = false


                }, { t: Throwable? ->
                    progressBarState.value = false
                    CustomErrorUtils.setError(TAG, t)
                })

    }
}