package com.spidersholidays.attendonb.utilites

class Constants {
    companion object {


    val REQUEST_PERMISSIONS_REQUEST_CODE = 1224
    const val REQUEST_CODE_CAMERA = 1225
    const val REQUEST_CODE_LOCATION_CAMERA = 1225
    const val REQUEST_CODE_PHONE_STATE = 1226
    const val REQUEST_CODE_LOGIN_PERMATION = 1227
    const val CHECK_STATS_TIME_OUT :Long = 2000


        const val ENTER="1"
        const val OUT="2"
        const val ENDED="3"


    }
    enum class ErrorType {
        ONLINE_DISCONNECTED,ONLINE_CONNECTED ,GPS_PROVIDER, MOCK_LOCATION,OUT_OF_AREA
    }

    enum class ErrorTypeStateAction {
        OPEN_SETTING
    }


}