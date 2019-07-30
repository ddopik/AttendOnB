package com.kspidersholidays.attendonb.ui.home.mainstate.model

/**
 * Created by ddopik on 22,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStateMessage {

    var message: String? = null


    interface StatsMessageCallBack {
        fun onStatsMessageAction()
    }
}