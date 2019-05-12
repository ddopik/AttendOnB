package com.example.attendonb.ui.home.mainstats

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.attendonb.R
import com.example.attendonb.app.AttendOnBApp
import com.example.attendonb.base.BaseFragment
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.ui.home.mainstats.viewmodel.MainStateViewModel
import com.example.attendonb.ui.home.qrreader.ui.QrReaderActivity
import com.example.attendonb.utilites.Constants
import com.example.attendonb.utilites.Constants.Companion.ENDED
import com.example.attendonb.utilites.Constants.Companion.ENTER
import com.example.attendonb.utilites.Constants.Companion.OUT
import com.example.attendonb.utilites.Constants.Companion.REQUEST_CODE_LOCATION
import com.example.attendonb.utilites.Constants.Companion.REQUEST_CODE_PHONE_STATE
import com.example.attendonb.utilites.MapUtls
import com.example.attendonb.utilites.PrefUtil
import com.google.android.material.snackbar.Snackbar
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.fragment_main_stats.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStatsFragment : BaseFragment(), MapUtls.OnLocationUpdate {

    private var mainView: View? = null
    private var mainStateViewModel: MainStateViewModel? = null
    private var mapUtls: MapUtls? = null
    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var isFromMockProvider: Boolean? = null
    private  var snakBar :Snackbar ?=null
    companion object {
        fun newInstance() = MainStatsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_main_stats, container, false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainStateViewModel = MainStateViewModel.getInstance(activity as HomeActivity)
        mapUtls = MapUtls(this)

        initObservers()
        intiView()
        intiListeners()
        requestLoginPermeation();
    }

    override fun intiView() {
        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
        apply_stats.text = resources.getString(R.string.apply)
        snakBar=Snackbar.make(parent_view, resources.getString(R.string.mock_location_warrning), Snackbar.LENGTH_INDEFINITE)

    }

    private fun intiListeners() {

        apply_stats.setOnClickListener {
            when (PrefUtil.getCurrentUserStatsID(context!!)) {
                ENTER -> {
                    val intent = Intent(activity, QrReaderActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                OUT -> {

                    val intent = Intent(activity, QrReaderActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                ENDED -> {

                }

            }
        }

    }

    override fun initObservers() {


    }

    override fun onResume() {
        super.onResume()
        mapUtls?.startLocationUpdates(activity, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
    }



    override fun onLocationUpdate(location: Location) {
        // New location has now been determined
//        val latLng = LatLng(location.latitude, location.longitude)

        val fakeLocation  = Location(LocationManager.GPS_PROVIDER)
        fakeLocation.latitude=30.101218
        fakeLocation.longitude=31.369461

        mainStateViewModel?.isCloseLocation(fakeLocation)



        isFromMockProvider = location.isFromMockProvider


        if (isFromMockProvider!!) {
            apply_stats.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray400))
            apply_stats.isEnabled=false
            snakBar?.show()
        } else {
            snakBar?.dismiss()
            apply_stats.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
            apply_stats.isEnabled = true
        }
        mapUtls?.removeLocationRequest()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(Constants.REQUEST_CODE_LOCATION)
    private fun requestLoginPermeation() {
        if (EasyPermissions.hasPermissions(activity?.baseContext!!, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)) {
             mapUtls?.startLocationUpdates(activity, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
        } else {
            // Request one permission
             EasyPermissions.requestPermissions(this, getString(R.string.need_location_permation), REQUEST_CODE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)

        }
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


}