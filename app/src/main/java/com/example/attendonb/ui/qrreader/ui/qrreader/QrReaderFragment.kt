package com.example.attendonb.ui.qrreader.ui.qrreader

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.attendonb.R
import com.example.attendonb.ui.result.ResultActivity
import com.example.attendonb.utilites.Constants
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.qr_reader_fragment.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class QrReaderFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        fun newInstance() = QrReaderFragment()
    }

    var qrImg: Bitmap? = null

    var barcodeDetector: BarcodeDetector? = null
    var cameraSource: CameraSource? = null
    var scanResult = ""
    private lateinit var viewModel: QrReaderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.qr_reader_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QrReaderViewModel::class.java)
        EasyPermissions.requestPermissions(this, getString(R.string.need_location_permation), Constants.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)
    }


    /* Function used to initialize components of activity */
    fun initStuff() {

        /* Initializing objects */

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
                    getScreenShoot()
//                    var stream: ByteArrayOutputStream = ByteArrayOutputStream();
//                    qrImg?.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    val byteArray = stream.toByteArray()
//                    intent.putExtra("img", byteArray)
//                    startActivity(intent)
                }
            }
        })
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        EasyPermissions.requestPermissions(this, getString(R.string.need_location_permation), Constants.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)

    }

    private fun getScreenShoot()
    {
//        val bitmap: Bitmap = Bitmap.createBitmap (surfaceQRScanner.getWidth(), surfaceQRScanner.getHeight(), Bitmap.Config.ARGB_8888);
//        val canvas: Canvas  =  Canvas(bitmap)
//        var bgDrawable : Drawable = surfaceQRScanner?.background!!
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        img_qrr?.draw(canvas)
/////
//
//        var bitMap: Bitmap? = null
//        surfaceQRScanner.setDrawingCacheEnabled(true);
//        bitMap = Bitmap.createBitmap(surfaceQRScanner.getDrawingCache());
//        qrImg = bitMap
//        surfaceQRScanner.setDrawingCacheEnabled(false);
//        img_qrr.setImageBitmap(qrImg)
//        // Write File to internal Storage
//
//        val FILENAME: String = "captured.png";
//        var fos: FileOutputStream? = null;
//
//        try {
//
//            fos = activity?.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//
//        } catch (e1: FileNotFoundException) {
//
//            e1.printStackTrace();
//            Log.e("", "FileNotFoundException: " + e1.message);
//
//        }
//
//        try {
//            bitMap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//            fos?.flush()
//            fos?.close()
//
//        } catch (e2: FileNotFoundException) {
//
//            Log.e("", "FileNotFoundException 2: " + e2.message);
//        } catch (e3: IOException) {
//
//            Log.e("", "IOException 3: " + e3.message);
//        }
//// ////
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initStuff()
    }

    override fun onResume() {
        super.onResume()
        EasyPermissions.requestPermissions(this, getString(R.string.need_location_permation), Constants.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)

    }

}
