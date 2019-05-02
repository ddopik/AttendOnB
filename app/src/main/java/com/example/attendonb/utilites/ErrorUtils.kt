package com.example.softmills.phlog.Utiltes

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.example.softmills.phlog.base.commonmodel.ErrorMessageResponse
import com.example.softmills.phlog.network.BaseNetworkApi.*
import com.google.gson.Gson

/**
 * Created by abdalla_maged on 11/6/2018.
 */
class ErrorUtils {
    companion object {

        private val TAG = ErrorUtils::class.java.simpleName

        //Bad RequestHandling
        fun setError(context: Context, contextTAG: String, error: String) {
            Log.e(TAG, "$contextTAG------>$error")
            Toast.makeText(context, "Request error", Toast.LENGTH_SHORT).show()
        }

        //Universal Error State From Server
        fun setError(context: Context, contextTAG: String, throwable: Throwable?) {
            try {
                throwable.takeIf { it is ANError }.apply {
                    (throwable as ANError).errorBody?.let {
                        val errorData = throwable.errorBody
                        val statusCode = throwable.errorCode
                        val gson = Gson()
                        when (statusCode) {
                            STATUS_BAD_REQUEST -> {
                                var errorMessageResponse: ErrorMessageResponse = gson.fromJson(errorData, ErrorMessageResponse::class.java)
                                viewError(context, contextTAG, errorMessageResponse)
                            }
                            STATUS_404 -> {
                                Log.e(TAG, contextTAG + "------>" + STATUS_404 + "---" + throwable.response)
                            }
                            STATUS_401 -> {
                                Log.e(TAG, contextTAG + "------>" + STATUS_401 + "---" + throwable.response)
                            }
                            STATUS_500 -> {
                                Log.e(TAG, contextTAG + "------>" + STATUS_500 + "---" + throwable.response)
                            }
                            else -> {
                                Log.e(TAG, contextTAG + "--------------->" + throwable.response)
                            }
                        }
                        return
                    }
                    Log.e(TAG, contextTAG + "--------------->" + throwable.message)
                }

            } catch (e: Exception) {
                Log.e(TAG, contextTAG + "--------------->" + throwable?.message)
            }
        }

        ///PreDefined Error Code From Server
        private fun viewError(context: Context, contextTAG: String, errorMessageResponse: ErrorMessageResponse) {
            for (i in errorMessageResponse.errors.indices) {
                if (errorMessageResponse.errors[i].code != null)
                    when (errorMessageResponse.errors[i].code) {
                        ERROR_STATE_1 -> {
                            Toast.makeText(context, errorMessageResponse.errors[i].message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Log.e(TAG, contextTAG + "------>" + errorMessageResponse.errors[i].message)
                        }
                    }
            }

        }
    }
}
// ErrorUtils.setError(context, TAG, throwable);