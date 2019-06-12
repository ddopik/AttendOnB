package com.spidersholidays.attendonb.ui.home.qrreader.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spidersholidays.attendonb.R

class QrReaderActivity : AppCompatActivity() {

    var currentLat :Double ?=null
    var currentLng :Double ?=null
    companion object{
        val CURRENT_LAT="current_lat"
        val CURRENT_LNG="current_lng"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_reader_activity)

        intent.getDoubleExtra(CURRENT_LAT,0.0).let {
            if(it >0.0 && intent.getDoubleExtra(CURRENT_LAT,0.0) >0.0 ){
                QrSpareReaderFragment.newInstance(it,intent.getDoubleExtra(CURRENT_LAT,0.0))?.let {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_container,it )
                            .commitNow()
                }
            }
        }


    }

}
