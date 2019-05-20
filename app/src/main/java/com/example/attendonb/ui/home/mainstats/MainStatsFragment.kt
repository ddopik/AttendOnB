package com.example.attendonb.ui.home.mainstats

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.attendonb.R
import com.example.attendonb.base.BaseFragment
import com.example.attendonb.base.CustomDialog
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.ui.home.mainstats.viewmodel.MainStateViewModel
import com.example.attendonb.ui.home.qrreader.ui.QrReaderActivity
import com.example.attendonb.utilites.Constants.Companion.ENDED
import com.example.attendonb.utilites.Constants.Companion.ENTER
import com.example.attendonb.utilites.Constants.Companion.OUT
import com.example.attendonb.utilites.MapUtls
import com.example.attendonb.utilites.PrefUtil
import com.google.android.material.snackbar.Snackbar
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.fragment_main_stats.*
import pub.devrel.easypermissions.EasyPermissions


/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStatsFragment : BaseFragment(), MapUtls.OnLocationUpdate {
    private val TAG = MainStatsFragment::class.java.simpleName
    private var mainView: View? = null
    private var mainStateViewModel: MainStateViewModel? = null
    private var mapUtls: MapUtls? = null
    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var isFromMockProvider: Boolean? = null
    private var snackBar: Snackbar? = null
    private var isSnackBarCurrentlyShowen = false

    companion object {
        fun newInstance() = MainStatsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_main_stats, container, false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackBar = Snackbar.make(parent_view, "", Snackbar.LENGTH_INDEFINITE);
        mainStateViewModel = MainStateViewModel.getInstance(activity as HomeActivity)
        mapUtls = MapUtls(this)
        initObservers()
        intiView()
        intiListeners()


    }

    override fun intiView() {
        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
        apply_stats.text = resources.getString(R.string.apply)

    }

    private fun intiListeners() {
        apply_stats.setOnClickListener {
            ///
            isFromMockProvider?.let {
                if (it) {
                    showPhotoDialog(activity!!,"",resources.getString(R.string.please_disable_mock_location_apps)) }
            }
            if (!PrefUtil.isInsideRadius(context!!)) {
                showPhotoDialog(activity!!,"",resources.getString(R.string.you_are_out_of_area))
            }
            /////////
            when (PrefUtil.getCurrentUserStatsID(context!!)) {
                ENTER -> {
                    navigateToScanScreen()
                }
                OUT -> {
                    navigateToScanScreen()
                }
                ENDED -> {
                    apply_stats.visibility = View.GONE
                }

            }
        }

    }


    override fun onResume() {
        super.onResume()
        mapUtls?.startLocationUpdates(activity, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
    }



    override fun onLocationUpdate(location: Location) {
        currentLat = location.latitude
        currentLng = location.longitude
        mainStateViewModel?.isCloseLocation(location)

        isFromMockProvider = location.isFromMockProvider

        if (isFromMockProvider!!){
            setBtnAttendBtnState(false)
            showSnackBar(resources.getString(R.string.mock_location_warrning))
            Log.e(TAG, "----->isFromMockProvider")

        }else if(!PrefUtil.isInsideRadius(context!!)){
            setBtnAttendBtnState(false)
            showSnackBar(resources.getString(R.string.you_are_out_of_area))
            Log.e(TAG, "----->Noy isInsideRadius")

        }else{
            Log.e(TAG, "----->true isInsideRadius")
//             mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!))
            snackBar?.dismiss()

        }
//        mapUtls?.removeLocationRequest()


    }

    private fun setBtnAttendBtnState(state: Boolean) {

        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
        if (!state) {
            apply_stats.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray400))
            apply_stats.isEnabled = false
        } else {
            apply_stats.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
            apply_stats.isEnabled = true

        }
    }

    override fun initObservers() {
        mainStateViewModel?.onAttendBtnChangeState()?.observe(this, Observer {
            setBtnAttendBtnState(it)
        })

        mainStateViewModel?.onDataLoading()?.observe(this, Observer {
            if (it) {
                main_state_progress.visibility = View.VISIBLE
            } else {
                main_state_progress.visibility = View.GONE
            }

//            if(PrefUtil.getCurrentUserStatsID(context!!) ==ENDED){
//                        mapUtls?.removeLocationRequest()
//            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToScanScreen(){

        main_state_progress.visibility = View.GONE
            val intent = Intent(activity, QrReaderActivity::class.java)
            intent.putExtra(QrReaderActivity.CURRENT_LAT, currentLat)
            intent.putExtra(QrReaderActivity.CURRENT_LNG, currentLng)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)

    }


    private fun showPhotoDialog(activity: Activity, title: String?, message: CharSequence) {


        val customDialog = CustomDialog.getInstance(activity);
        customDialog.customDialogContent=title;
        customDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
            override fun onPositiveClicked() {
                customDialog.dismiss()
            }

        }
        customDialog.show()


    }


    fun showSnackBar(messag: String) {
        snackBar?.setText(messag)
        snackBar?.addCallback(getSnackBarCallBack())
        if (!isSnackBarCurrentlyShowen) {
            snackBar?.show()

        }
    }


    fun getSnackBarCallBack(): Snackbar.Callback {
        return object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                super.onShown(sb)
                isSnackBarCurrentlyShowen = true
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                isSnackBarCurrentlyShowen = false
                snackBar?.dismiss()
            }
        }
    }
}