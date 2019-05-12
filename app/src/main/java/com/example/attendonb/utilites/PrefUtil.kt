package com.example.attendonb.utilites

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class PrefUtil {


    companion object {
        private val PREF_FILE_NAME = "AttendOnB"
        private val USER_ID = "id"
        private val USER_TOKEN = "user_token"
        private val USER_NAME = "user_name"
        private val USER_TRACK = "user_track"
        private val FIRST_TIME_LOGIN = "is_first_time_login"
        private val IS_LOGGED_IN = "is_logged_in"
        private val USER_MAIL = "user_mail"
        private val PROFILE_PIC = "profile_pic"
        private val USER_GENDER = "user_gender"
        private val CURRENT_USER_STATS_ID = "current_user_stats"
        private val CURRENT_STATS_MESSAGE = "current_stats_message"
        private val CURRENT_CENTRAL_LAT = "current_central_lat"
        private val CURRENT_CENTRAL_LNG = "current_central_lng"
        private val CURRENT_CENTRAL_RADIOUS = "CURRENT_CENTRAL_RADIOUS"


        private fun getSharedPref(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        }


        fun setUserToken(context: Context, userToken: String) {
            getSharedPref(context)
                    .edit()
                    .putString(USER_TOKEN, userToken)
                    .apply()
        }


        fun setUserID(context: Context, userId: String) {
            getSharedPref(context)
                    .edit()
                    .putString(USER_TOKEN, userId)
                    .apply()
        }
        fun setUserName(context: Context, userName: String) {
            getSharedPref(context)
                    .edit()
                    .putString(USER_NAME, userName)
                    .apply()
        }

        fun setUserTrackId(context: Context, trackID: String) {
            getSharedPref(context)
                    .edit()
                    .putString(USER_TRACK, trackID)
                    .apply()
        }

        fun setIsFirstTimeLogin(context: Context, isFirstTime: Boolean) {
            getSharedPref(context)
                    .edit()
                    .putBoolean(FIRST_TIME_LOGIN, isFirstTime)
                    .apply()
        }
        fun setIsLoggedIn(context: Context, isLoggedIn: Boolean) {
            getSharedPref(context)
                    .edit()
                    .putBoolean(IS_LOGGED_IN, isLoggedIn)
                    .apply()
        }

        fun setUserMail(context: Context, userMail: String) {
            getSharedPref(context)
                    .edit()
                    .putString(USER_MAIL, userMail)
                    .apply()
        }

        fun setUserProfilePic(context: Context, profileImg: String) {
            getSharedPref(context)
                    .edit()
                    .putString(PROFILE_PIC, profileImg)
                    .apply()
        }

        fun setUserGender(context: Context, userGender: String) {
            getSharedPref(context)
                    .edit()
                    .putString(USER_GENDER, userGender)
                    .apply()
        }
          fun setCurrentUserStatsID(context: Context, userStats: String) {
            getSharedPref(context)
                    .edit()
                    .putString(CURRENT_USER_STATS_ID, userStats)
                    .apply()
        }

        fun setCurrentStatsMessage(context: Context, currentStatsMessage: String) {
            getSharedPref(context)
                    .edit()
                    .putString(CURRENT_STATS_MESSAGE, currentStatsMessage)
                    .apply()
        }

        fun setCurrentCentralLat(context: Context, currentCentralLat: String) {
            getSharedPref(context)
                    .edit()
                    .putString(CURRENT_CENTRAL_LAT, currentCentralLat)
                    .apply()
        }

        fun setCurrentCentralLng(context: Context, currentCentralLng: String) {
            getSharedPref(context)
                    .edit()
                    .putString(CURRENT_CENTRAL_LNG, currentCentralLng)
                    .apply()
        }



        fun setCurrentCentralRadious(context: Context, currentCentralRadious: String) {
            getSharedPref(context)
                    .edit()
                    .putString(CURRENT_CENTRAL_RADIOUS, currentCentralRadious)
                    .apply()
        }


        fun getUseToken(mContext: Context): String? {
            return getSharedPref(mContext).getString(USER_TOKEN, "-1")

        }

        fun getUserId(mContext: Context): Int? {
            return getSharedPref(mContext).getInt(USER_ID, -1)

        }

        fun getUserName(mContext: Context): String? {
            return getSharedPref(mContext).getString(USER_NAME, "")

        }

        fun getUserTrack(mContext: Context): String? {
            return getSharedPref(mContext).getString(USER_TRACK, "-1")

        }

        fun getUserMail(mContext: Context): String? {
            return getSharedPref(mContext).getString(USER_MAIL, "")

        }

        fun getUserProfilePic(mContext: Context): String? {
            return getSharedPref(mContext).getString(PROFILE_PIC, "")

        }

        fun getCurrentUserStatsID(mContext: Context): String? {
            return getSharedPref(mContext).getString(CURRENT_USER_STATS_ID, "-1")

        }
        fun getUserGender(mContext: Context): String? {
            return getSharedPref(mContext).getString(FIRST_TIME_LOGIN, "")

        }
        fun getCurrentStatsMessage(mContext: Context): String? {
            return getSharedPref(mContext).getString(CURRENT_STATS_MESSAGE, "")

        }

        fun getCurrentCentralLat(mContext: Context): String? {
            return getSharedPref(mContext).getString(CURRENT_CENTRAL_LAT, "")

        }

        fun getCurrentCentralLng(mContext: Context): String? {
            return getSharedPref(mContext).getString(CURRENT_CENTRAL_LNG, "")

        }
        fun getCurrentCentralRadious(mContext: Context): String? {
            return getSharedPref(mContext).getString(CURRENT_CENTRAL_RADIOUS, "")

        }

        fun isFirstTimeLogin(mContext: Context): Boolean {
            return getSharedPref(mContext).getBoolean(FIRST_TIME_LOGIN, true)

        }
        fun isLoggedIn(mContext: Context): Boolean {
            return getSharedPref(mContext).getBoolean(IS_LOGGED_IN, false)

        }


    }


}