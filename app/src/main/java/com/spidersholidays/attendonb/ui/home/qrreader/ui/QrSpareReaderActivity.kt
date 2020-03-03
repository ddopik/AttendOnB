package com.spidersholidays.attendonb.ui.home.qrreader.ui


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.google.zxing.Result;
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.base.CustomDialog
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.home.qrreader.viewmodel.QrReaderViewModel
import com.spidersholidays.attendonb.utilites.Constants
import me.dm7.barcodescanner.zxing.ZXingScannerView;


class QrSpareReaderActivity : BaseActivity(), ZXingScannerView.ResultHandler {
    private val TAG = QrSpareReaderActivity::class.java.simpleName

    companion object {
        val CURRENT_LAT = "current_lat"
        val CURRENT_LNG = "CURRENT_LNG"
    }

    private var mScannerView: ZXingScannerView? = null
    var currentLat: Double? = null
    var currentLng: Double? = null
    var scanResult = ""
    private lateinit var qrReaderViewModel: QrReaderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         qrReaderViewModel = ViewModelProviders.of(this).get(QrReaderViewModel::class.java)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)

        intent.getDoubleExtra(CURRENT_LAT, 0.0).takeIf { it != 0.0 }.apply {
            currentLat = this
        }.also {
            intent.getDoubleExtra(CURRENT_LAT, 0.0).takeIf { it != 0.0 }.apply {
                currentLng = this
            }
        }
        initObservers()
    }

    override fun initObservers() {
        qrReaderViewModel.OnDataLoading().observe(this, Observer {

        })
        qrReaderViewModel.isNetWorkError().observe(this, Observer {
            showToast(it)

        })
        qrReaderViewModel.onAttendResponse().observe(this, Observer {


            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(HomeActivity.VIEW_CONFIRM_DIALOG, true)
            startActivity(intent)
            finish()
        })

        qrReaderViewModel.isUnKnownError().observe(this, Observer {
            Log.e(TAG, it)
        })
    }


    override fun handleResult(p0: Result?) {

         if(p0?.text == Constants.QR_SCANNER_CONSTANT){
            mScannerView?.stopCamera()
            qrReaderViewModel.sendAttendRequest(currentLat!!, currentLng!!)
        }else {
            val alertDialog = CustomDialog.getInstance(this, CustomDialog.DialogOption.OPTION_1)
            alertDialog.customDialogContent = resources.getString(R.string.wrong_qr)
            alertDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
                override fun onNectiveClicked() {
                }
                override fun onPositiveClicked() {
                    finish()
                }
            }
            alertDialog.show()
        }
    }
    public override fun onResume() {
        super.onResume()
        if (mScannerView == null) {
            mScannerView = ZXingScannerView(this)
            setContentView(mScannerView)
        }
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()



    }


    override fun onDestroy() {
        super.onDestroy()
        mScannerView?.stopCamera()
    }

}