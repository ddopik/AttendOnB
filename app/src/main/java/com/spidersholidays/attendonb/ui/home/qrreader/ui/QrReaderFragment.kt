package com.spidersholidays.attendonb.ui.home.qrreader.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.home.qrreader.viewmodel.QrReaderViewModel
import com.spidersholidays.attendonb.ui.result.ResultActivity
import com.spidersholidays.attendonb.utilites.Constants
import com.spidersholidays.attendonb.utilites.Constants.Companion.REQUEST_CODE_CAMERA
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.spidersholidays.attendonb.ui.home.HomeActivity.Companion.VIEW_CONFIRM_DIALOG
import com.spidersholidays.attendonb.ui.home.mainstate.stateconfirmdialog.StateConfirmDialog
import com.spidersholidays.attendonb.utilites.PrefUtil
import kotlinx.android.synthetic.main.qr_reader_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class QrReaderFragment : BaseFragment() {

    private val TAG = QrReaderFragment::class.java.simpleName

    var currentLat: Double? = null
    var currentLng: Double? = null
    var barcodeDetector: BarcodeDetector? = null
    var cameraSource: CameraSource? = null
    var scanResult = ""
    private lateinit var qrReaderViewModel: QrReaderViewModel


    companion object {
        fun newInstance(currentLat: Double?, currentLng: Double?): QrReaderFragment? {

            currentLat?.let {
                val instance = QrReaderFragment()
                instance.currentLat = currentLat
                instance.currentLng = currentLng
                return instance
            }

            return null
        }
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.qr_reader_fragment, container, false)
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
            intent.putExtra(VIEW_CONFIRM_DIALOG,true)
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


    override fun intiView() {
        initObservers()

        barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true)
                .build()


        /* Adding Callback method to SurfaceView */
        surfaceQRScanner.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                cameraSource?.start(surfaceQRScanner.holder)

            }
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {

                cameraSource?.stop()
            }
        })

        /* Adding Processor to Barcode detector */
        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems /* Retrieving QR Code */
                if (barcodes.size() > 0) {

                    barcodeDetector?.release() /* Releasing barcodeDetector */

                    val toneNotification = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100) /* Setting beep sound */
                    toneNotification.startTone(ToneGenerator.TONE_PROP_BEEP, 150)

                    scanResult = barcodes.valueAt(0).displayValue.toString() /* Retrieving text from QR Code */

                    val intent = Intent(this@QrReaderFragment.context, ResultActivity::class.java)
                    intent.putExtra("ScanResult", scanResult) /* Sending text to next activity to display */

                    qrReaderViewModel.sendAttendRequest(currentLat!!, currentLng!!)
//                    cameraSource?.stop()


                }
            }
        })
    }


    @AfterPermissionGranted(REQUEST_CODE_CAMERA)
    private fun requestCameraPermutation() {
         if (EasyPermissions.hasPermissions(activity?.baseContext!!, Manifest.permission.CAMERA)) {
            intiView()
        } else {
            EasyPermissions.requestPermissions(this, getString(com.spidersholidays.attendonb.R.string.need_camera_permation), Constants.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)

        }
    }

    override fun onResume() {
        super.onResume()
        requestCameraPermutation()
    }

    ///////////



}
