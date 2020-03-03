package com.spidersholidays.attendonb.ui.vacationmangment.viewmodel

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
import com.spidersholidays.attendonb.base.SingleLiveEvent
import com.spidersholidays.attendonb.base.commonModel.ErrorMessageResponse
import com.spidersholidays.attendonb.base.commonModel.Vacation
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.ui.vacationmangment.model.VacationMangerRequestResponse
import com.spidersholidays.attendonb.ui.vacationmangment.model.VacationStatsChange
import com.spidersholidays.attendonb.utilites.Constants.Companion.PENDING_VACATION_UNDER_REVISION
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ManageVacationViewModel : ViewModel() {

    companion object {
        val TAG = ManageVacationViewModel::class.java.name
        fun getInstance(fragment: Fragment): ManageVacationViewModel {
            return ViewModelProviders.of(fragment).get(ManageVacationViewModel::class.java)
        }

    }


    private val vacationProgressChange = MutableLiveData<Boolean>()
    private val pendingListChange = MutableLiveData<MutableList<Vacation>>()
    private val pendingItemChange = SingleLiveEvent<VacationStatsChange>()


    private val approvedListChange = MutableLiveData<MutableList<Vacation>>()
    private val rejectedListChange = MutableLiveData<MutableList<Vacation>>()


    fun onPendingVacationListChanged(): LiveData<MutableList<Vacation>> = pendingListChange
    fun onPendingItemChange(): LiveData<VacationStatsChange> = pendingItemChange

    fun onVacationProgressChanged(): LiveData<Boolean> = vacationProgressChange


    fun onApprovedVacationListChanged(): LiveData<MutableList<Vacation>> = approvedListChange
    fun onRejectedVacationListChanged(): LiveData<MutableList<Vacation>> = rejectedListChange


    @SuppressLint("CheckResult")
    fun getPendingManagementVacation() {
        Log.e(TAG, "  ---->  getPendingManagementVacation()")
        vacationProgressChange.postValue(true)
        BaseNetWorkApi.getPendingManagementVacation(PrefUtil.getUserId(AttendOnBApp.app!!.baseContext).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status) {
                        Log.e(TAG, it.data?.msg!!)
                        it.data?.vacationData?.let {
                            Log.e(TAG, it.size.toString())
                            pendingListChange.postValue(it.asReversed())
                        }
                        if (it?.data?.vacationData == null) {
                            pendingListChange.postValue(it?.data?.vacationData)
                        }
                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Pending vacation"), "0"))
                    }
                    vacationProgressChange.postValue(false)

                }, { t: Throwable? ->
                    vacationProgressChange.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })

    }


    @SuppressLint("CheckResult")
    fun rejectVacation(vacationId: String, reason:String) {
        BaseNetWorkApi.sendRejectManagementVacation(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!),vacationId,reason)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ requestResponse :VacationMangerRequestResponse ->
                    if (requestResponse.status){
                        pendingItemChange.postValue(VacationStatsChange(vacationId,VacationStatsChange.VacationStatsType.REJECTED,null))
                        vacationProgressChange.postValue(false)

                    }

                },{t: Throwable? ->
                    CustomErrorUtils.setError(TAG, t)
                    vacationProgressChange.postValue(false)
                })
    }

    @SuppressLint("CheckResult")
    fun approveVacation(vacationId: String) {
        vacationProgressChange.postValue(true)

        BaseNetWorkApi.sendApproveManagementVacation(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!),vacationId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ requestResponse :VacationMangerRequestResponse ->
               if (requestResponse.status){
                   pendingItemChange.postValue(VacationStatsChange(vacationId,VacationStatsChange.VacationStatsType.APPROVED,PENDING_VACATION_UNDER_REVISION))
                   vacationProgressChange.postValue(false)

               }

                },{t: Throwable? ->
                    CustomErrorUtils.setError(TAG, t)
                    vacationProgressChange.postValue(false)
                })

    }


    @SuppressLint("CheckResult")
    fun getApprovedManagementVacation( userID:String) {
        Log.e(TAG, "  ---->  getApprovedManagementVacation()")
        vacationProgressChange.postValue(true)
        BaseNetWorkApi.getApprovedManagementVacation(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status) {
                        Log.e(TAG, it.data?.msg!!)
                        it.data?.vacationData?.let {
                            Log.e(TAG, it.size.toString())
                            approvedListChange.postValue(it.asReversed())
                        }
                        if (it?.data?.vacationData == null) {
                            approvedListChange.postValue(it?.data?.vacationData)
                        }
                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Management Approved vacation"), "0"))
                    }
                    vacationProgressChange.postValue(false)

                }, { t: Throwable? ->
                    vacationProgressChange.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })
    }


    @SuppressLint("CheckResult")
    fun getRejectedManagementVacation( userID:String) {
        Log.e(TAG, "  ---->  getRejectedManagementVacation()")
        vacationProgressChange.postValue(true)
        BaseNetWorkApi.getRejectedManagementVacation(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status) {
                        Log.e(TAG, it.data?.msg!!)
                        it.data?.vacationData?.let {
                            Log.e(TAG, it.size.toString())
                            rejectedListChange.postValue(it.asReversed())
                        }
                        if (it?.data?.vacationData == null) {
                            rejectedListChange.postValue(it?.data?.vacationData)
                        }
                    } else {
                        CustomErrorUtils.viewError(TAG, ErrorMessageResponse(false, BaseErrorData("Failed to get Management Rejected vacation"), "0"))
                    }
                    vacationProgressChange.postValue(false)

                }, { t: Throwable? ->
                    vacationProgressChange.postValue(false)
                    CustomErrorUtils.setError(TAG, t)
                })
    }
}