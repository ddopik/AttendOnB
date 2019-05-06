package com.example.attendonb.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.ui.LoginActivity

class LoginViewModel : ViewModel() {

    companion object {
        private var INSTANCE: LoginViewModel? = null

        fun getInstance(activity: LoginActivity): LoginViewModel? {
            if (INSTANCE == null) {
                INSTANCE = ViewModelProviders.of(activity).get(LoginViewModel::class.java)
            }
            return INSTANCE
        }
    }


    var isNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    var isUnknownError: MutableLiveData<Boolean> = MutableLiveData()
    var isDataLoading: MutableLiveData<Boolean> = MutableLiveData()


    fun isDataLoading(): LiveData<Boolean> = isDataLoading
    fun isNetWorkError(): LiveData<Boolean> = isNetworkError
    fun isUnKnownError(): LiveData<Boolean> = isUnknownError





    /**
     * VM Publisher method 2
     * */
//    fun doSearch(q: String, limit: Int = 1, offset: Int = 0): LiveData<Resource<SearchResponse>>{
//        val data = MutableLiveData<Resource<SearchResponse>>();
//
//        api.search(q, limit, offset).enqueue(object : Callback<SearchResponse> {
//            override fun onResponse(call: Call<SearchResponse>?, response: Response<SearchResponse>?) {
//                data.value = Resource.success(response?.body());
//            }
//
//            override fun onFailure(call: Call<SearchResponse>?, t: Throwable?) {
//                val exception = AppException(t)
//                data.value = Resource.error( exception)
//            }
//        });
//        return data;
//    }
}