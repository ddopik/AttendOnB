package com.attendance735.attendonb.utilites

class Constants {
    companion object {


        val REQUEST_PERMISSIONS_REQUEST_CODE = 1224
        const val REQUEST_CODE_CAMERA = 1225
        const val REQUEST_CODE_LOCATION_CAMERA = 1225
        const val REQUEST_CODE_PHONE_STATE = 1226
        const val REQUEST_CODE_LOGIN_PERMATION = 1227
        const val CHECK_STATS_TIME_OUT: Long = 2000
        val QUERY_SEARCH_TIME_OUT = 600
        val MangerListActivity_REQUESE_CODE = 12
        val VACATION_TYPE_LIST_ACTIVITY_REQUESE_CODE = 13
        val CREATE_VACATION_PRIOR_DAY_BREAK =3


                const val QR_SCANNER_CONSTANT = "https://hr-arabjet.com/en/"
//        const val QR_SCANNER_CONSTANT="https://nfc.spiderholidays.co/en/"

        const val ENTER = "1"
        const val OUT = "2"
        const val ENDED = "3"


        const val PENDING_VACATION_ALLOWED ="0"
        const val PENDING_VACATION_UNDER_REVISION ="1"


             }

    enum class ErrorType {
        ONLINE_DISCONNECTED, ONLINE_CONNECTED, GPS_PROVIDER, MOCK_LOCATION, OUT_OF_AREA
    }

    enum class ErrorTypeStateAction {
        OPEN_SETTING
    }

    /**
     * this class used to determine whiter controll view to hide
     * */
    public enum class AttendType{
        MAIN_CONTAINER,QR,NETWORK
    }

}