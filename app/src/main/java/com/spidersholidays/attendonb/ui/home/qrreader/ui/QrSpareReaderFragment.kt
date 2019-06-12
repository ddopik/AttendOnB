package com.spidersholidays.attendonb.ui.home.qrreader.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.zxing.Result
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.home.qrreader.viewmodel.QrReaderViewModel
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrSpareReaderFragment :BaseFragment() , ZXingScannerView.ResultHandler
{
    private var mScannerView: ZXingScannerView? = null

    private val TAG = QrSpareReaderFragment::class.java.simpleName

    var currentLat: Double? = null
    var currentLng: Double? = null
      var scanResult = ""
    private lateinit var qrReaderViewModel: QrReaderViewModel


    companion object {
        fun newInstance(currentLat: Double?, currentLng: Double?): QrSpareReaderFragment? {

            currentLat?.let {
                val instance = QrSpareReaderFragment()
                instance.currentLat = currentLat
                instance.currentLng = currentLng
                return instance
            }

            return null
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mScannerView =  ZXingScannerView(context)

        return mScannerView!!
    }



    override fun intiView() {
     }

    override fun initObservers() {
        qrReaderViewModel.OnDataLoading().observe(activity as QrReaderActivity, Observer {

        })
        qrReaderViewModel.isNetWorkError().observe(activity as QrReaderActivity, Observer {
            showToast(it)

        })
        qrReaderViewModel.onAttendResponse().observe(activity as QrReaderActivity, Observer {


            val intent = Intent(activity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(HomeActivity.VIEW_CONFIRM_DIALOG,true)
            startActivity(intent)





        })
        qrReaderViewModel.isUnKnownError().observe(activity as QrReaderActivity, Observer {
            Log.e(TAG, it)
        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        qrReaderViewModel = ViewModelProviders.of(this).get(QrReaderViewModel::class.java)

    }

    override fun handleResult(p0: Result?) {
        qrReaderViewModel.sendAttendRequest(currentLat!!, currentLng!!)    }
    override fun onResume() {
        super.onResume()


        if (mScannerView == null) {
            mScannerView = ZXingScannerView(context)

            mScannerView?.setResultHandler(this)
            mScannerView?.startCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView?.stopCamera()

    }

}