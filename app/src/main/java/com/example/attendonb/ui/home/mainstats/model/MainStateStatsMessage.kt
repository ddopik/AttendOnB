package com.example.attendonb.ui.home.mainstats.model

/**
 * Created by ddopik on 22,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStateStatsMessage {

    var message: String? = null


    interface StatsMessageCallBack {
        fun onStatsMessageAction()
    }
}