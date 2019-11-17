package com.spidersholidays.attendonb.ui.home.model

import android.location.Location

/**
 * Created by ddopik on 21,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
  class AttendMessage(var currentLocation:Location,var attendFlag :AttendFlags) {



    enum class AttendFlags {
        ENTER, OUT, ENDED
    }

}