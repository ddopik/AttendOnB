package com.example.attendonb.ui.qrreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.attendonb.R
import com.example.attendonb.ui.qrreader.ui.qrreader.QrReaderFragment
import kotlinx.android.synthetic.main.qr_reader_activity.*

class QrReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_reader_activity)
        if (savedInstanceState == null) {

            btnScanQRCode.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, QrReaderFragment.newInstance())
                    .commitNow() }

        }
    }

}
