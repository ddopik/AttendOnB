package com.spidersholidays.attendonb.ui.vacation.newvacation

import CustomErrorUtils
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.CreateNewVacationResponse
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.NewVacationData
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewVacationViewModel : ViewModel() {


    private val vacationFormData: MutableLiveData<NewVacationData> = MutableLiveData()
    private val newVacationProgressState: MutableLiveData<Boolean> = MutableLiveData()
    private val vacationCreateState: MutableLiveData<Boolean> = MutableLiveData()


    fun onVacationFormDataChange(): LiveData<NewVacationData> = vacationFormData
    fun onVacationFormProgressChanged(): LiveData<Boolean> = newVacationProgressState
    fun onNewVacationCreated(): LiveData<Boolean> = vacationCreateState


    companion object {
        val TAG = NewVacationViewModel::class.java.simpleName
        private var INSTANCE: NewVacationViewModel? = null

        fun getInstance(activity: NewVacationActivity): NewVacationViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(NewVacationViewModel::class.java)
            }
            return INSTANCE
        }
    }


    @SuppressLint("CheckResult")
    fun getVacationFormData() {
        newVacationProgressState.postValue(true)
        BaseNetWorkApi.getNewVacationData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    if (it.status) {
                        vacationFormData.postValue(it.data)
                    } else {
                        vacationFormData.postValue(null)
                    }

                    newVacationProgressState.postValue(false)

                }, { t: Throwable ->

                    newVacationProgressState.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })
    }


    @SuppressLint("CheckResult")
    fun sendVacationRequest(reason: String, startDate: String, endDate: String, requestTo: String, vacationType: String) {
        newVacationProgressState.postValue(true)

        startDate.replace("/", "-")
        val start = startDate.split("/").toTypedArray()
        start.reverse()
        val startString= start.joinToString(separator = "-")

        endDate.replace("/", "-")
        val end = endDate.split("/").toTypedArray()
        end.reverse()
        val endString= end.joinToString(separator = "-")



         BaseNetWorkApi.sendNewVacationRequest(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!), reason, startString, endString, requestTo, vacationType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ createNewVacationResponse: CreateNewVacationResponse ->

                    newVacationProgressState.postValue(false)
                    createNewVacationResponse.status?.let {
                        if (it) {
                            vacationCreateState.postValue(true)
                        } else {
                            vacationCreateState.postValue(false)

                        }
                    }

                }, { t: Throwable? ->
                    newVacationProgressState.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })
    }


}