package com.kspidersholidays.attendonb.ui.home.model

import android.location.Location

/**
 * Created by ddopik on 21,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class AttendMessage {

    var currentLocation:Location ?=null
    var attendFlag :AttendFlags ?=null

    enum class AttendFlags {
        ENTER, OUT, ENDED
    }
}