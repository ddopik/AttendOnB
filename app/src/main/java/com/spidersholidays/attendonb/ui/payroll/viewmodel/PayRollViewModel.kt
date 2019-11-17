package com.spidersholidays.attendonb.ui.payroll.viewmodel

import CustomErrorUtils
import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.commonModel.PayRoll
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.ui.payroll.model.PayRollResponse
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PayRollViewModel : ViewModel() {


    private val payRollProgressChange: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val PayRollDataChanged: MutableLiveData<MutableList<PayRoll>> = MutableLiveData()


    fun onPayRollProgressChanged(): LiveData<Boolean> = payRollProgressChange
    fun onPayRollDataChanged(): MutableLiveData<MutableList<PayRoll>> = PayRollDataChanged

    companion object {
        val TAG = PayRollViewModel::class.java.name
        fun getInstance(context: Fragment): PayRollViewModel {
            return ViewModelProviders.of(context).get(PayRollViewModel::class.java)
        }
    }


    @SuppressLint("CheckResult")
    fun getPayRollData() {
        payRollProgressChange.postValue(true)
        BaseNetWorkApi.getPayRollData(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ payRollResponse: PayRollResponse ->

                    payRollProgressChange.postValue(false)
                    payRollResponse.status?.let {
                        PayRollDataChanged.postValue(payRollResponse.data?.payroll)
//                        Log.e(TAG,payRollResponse.data?.payroll?.size.toString())
                    }

                },
                        { t: Throwable? ->
                            CustomErrorUtils.setError(TAG, t)
                            payRollProgressChange.postValue(false)
                        })


    }


}


