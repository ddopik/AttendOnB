package com.spidersholidays.attendonb.ui.result

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        if(intent.getByteArrayExtra("img") !=null){
            val byteArray = intent.getByteArrayExtra("img")
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            qr_test_img.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue))
            qr_test_img.setImageBitmap(bmp)
        }
    }

    override fun initObservers() {
     }
}