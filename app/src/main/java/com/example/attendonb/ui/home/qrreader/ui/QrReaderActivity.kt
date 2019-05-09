package com.example.attendonb.ui.home.qrreader.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.attendonb.R
import com.example.attendonb.ui.home.qrreader.ui.QrReaderFragment
import kotlinx.android.synthetic.main.qr_reader_activity.*

class QrReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_reader_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, QrReaderFragment.newInstance())
                    .commitNow()


        }
    }

}
