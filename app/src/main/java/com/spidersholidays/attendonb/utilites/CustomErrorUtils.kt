import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.commonModel.ErrorMessageResponse
import com.spidersholidays.attendonb.network.BaseNetWorkApi.Companion.ERROR_STATE_1
import com.spidersholidays.attendonb.network.BaseNetWorkApi.Companion.STATUS_401
import com.spidersholidays.attendonb.network.BaseNetWorkApi.Companion.STATUS_404
import com.spidersholidays.attendonb.network.BaseNetWorkApi.Companion.STATUS_500
import com.spidersholidays.attendonb.network.BaseNetWorkApi.Companion.STATUS_BAD_REQUEST
import com.spidersholidays.attendonb.utilites.Constants
import com.spidersholidays.attendonb.utilites.rxeventbus.RxAppStatsEvent
import com.spidersholidays.attendonb.utilites.rxeventbus.RxEventBus
import com.google.gson.Gson


/**
 * Created by abdalla_maged on 11/6/2018.
 */
class CustomErrorUtils {
    companion object {

        private val TAG = CustomErrorUtils::class.java.simpleName

        //Bad RequestHandling
        fun setError(context: Context, contextTAG: String, error: String) {
            Log.e(TAG, "$contextTAG------>$error")
            Toast.makeText(context, "Request error", Toast.LENGTH_SHORT).show()
        }

        //Universal Error State From Server
        fun setError( contextTAG: String, throwable: Throwable?) {
            try {
                throwable.takeIf { it is ANError }.apply {
                    (throwable as ANError).errorBody?.let {
                        val errorData = throwable.errorBody
                        val statusCode = throwable.errorCode
                        val gson = Gson()
                        when (statusCode) {
                            STATUS_BAD_REQUEST -> {
                                var errorMessageResponse: ErrorMessageResponse = gson.fromJson(errorData, ErrorMessageResponse::class.java)
                                viewError(AttendOnBApp.app?.getApp()?.baseContext!!, contextTAG, errorMessageResponse)
                            }
                            STATUS_404 -> {
                                Log.e(TAG, contextTAG + "---STATUS_404--->" + STATUS_404 + "---" + throwable.response)
                            }
                            STATUS_401 -> {
                                Log.e(TAG, contextTAG + "---STATUS_401--->" + STATUS_401 + "---" + throwable.response)
                            }
                            STATUS_500 -> {
                                Log.e(TAG, contextTAG + "---STATUS_500--->" + STATUS_500 + "---" + throwable.response)
                            }
                            else -> {
                                Log.e(TAG, contextTAG + "-------unKnown_stats-------->" + throwable.response)
                            }
                        }
                        return
                    }
                    Log.e(TAG, contextTAG + "-------un_known _network_error-------->" + throwable.message)

                    if (!haveNetworkConnection(AttendOnBApp.app?.baseContext!!)) {

                        viewSnackBarError(Constants.ErrorType.ONLINE_DISCONNECTED)
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, contextTAG + "-------G_error-------->" + throwable?.message)
            }
        }

        ///PreDefined Error Code From Server
        private fun viewError(context: Context, contextTAG: String, errorMessageResponse: ErrorMessageResponse) {

                    when (errorMessageResponse.code) {
                        ERROR_STATE_1 -> {
                            Toast.makeText(context, errorMessageResponse.data.msg, Toast.LENGTH_LONG).show()
                        }
                        else -> {
//                            Toast.makeText(context, errorMessageResponse.data.msg, Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "$contextTAG------> ${errorMessageResponse.data.msg}")
                        }
                    }


        }

        fun viewSnackBarError(errorType: Constants.ErrorType) {
            when (errorType){
                Constants.ErrorType.GPS_PROVIDER->{
                    RxEventBus.getInstance().post(RxAppStatsEvent(AttendOnBApp.app?.getString(R.string.please_enaple_location_provider)!!, Constants.ErrorType.GPS_PROVIDER))
                }
                Constants.ErrorType.MOCK_LOCATION ->{
                    RxEventBus.getInstance().post(RxAppStatsEvent(AttendOnBApp.app?.getString(R.string.please_disable_mock_location_apps)!!,Constants.ErrorType.MOCK_LOCATION))

                }
                Constants.ErrorType.OUT_OF_AREA ->{
                    RxEventBus.getInstance().post(RxAppStatsEvent(AttendOnBApp.app?.getString(R.string.you_are_out_of_area)!!,Constants.ErrorType.OUT_OF_AREA))

                }
                Constants.ErrorType.ONLINE_DISCONNECTED->{
                    RxEventBus.getInstance().post(RxAppStatsEvent(AttendOnBApp.app?.getString(R.string.please_check_your_internet_conniction)!!,Constants.ErrorType.ONLINE_DISCONNECTED))

                }
                Constants.ErrorType.ONLINE_CONNECTED -> {
//                    RxEventBus.getInstance().post(RxAppStatsEvent(AttendOnBApp.app?.getString(R.string.please_check_your_internet_conniction)!!,Constants.ErrorType.ONLINE_DISCONNECTED))
                }
            }


        }

         fun haveNetworkConnection(context: Context): Boolean {
            var haveConnectedWifi = false
            var haveConnectedMobile = false

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val netInfo = cm!!.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName.equals("WIFI", ignoreCase = true))
                    if (ni.isConnected)
                        haveConnectedWifi = true
                if (ni.typeName.equals("MOBILE", ignoreCase = true))
                    if (ni.isConnected)
                        haveConnectedMobile = true
            }
            return haveConnectedWifi || haveConnectedMobile
        }
    }


}
// CustomErrorUtils.setError(context, TAG, throwable);