package com.attendance735.attendonb.ui.vacation.newvacation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.attendance735.attendonb.base.commonModel.ErrorMessageResponse
import com.attendance735.attendonb.network.BaseNetWorkApi
import com.attendance735.attendonb.ui.vacation.newvacation.model.NewVacationData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewVacationViewModel : ViewModel() {


    private val vacationFormData: MutableLiveData<NewVacationData> = MutableLiveData()
    private val newVacationProgressState: MutableLiveData<Boolean> = MutableLiveData()



    fun onVacationFormDataChange():LiveData<NewVacationData> =vacationFormData
    fun onVacationFormProgressChanged():LiveData<Boolean> =newVacationProgressState


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

                    if (it.status){
                        vacationFormData.postValue(it.data)
                    }else{
                        vacationFormData.postValue(null)
                    }

                    newVacationProgressState.postValue(false)

                },{t:Throwable ->

                    newVacationProgressState.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })
    }



}