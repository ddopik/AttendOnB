package com.example.attendonb.ui.home.mainstats

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.attendonb.R
import com.example.attendonb.base.BaseFragment
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.ui.home.mainstats.viewmodel.MainStateViewModel
import com.example.attendonb.ui.home.qrreader.ui.QrReaderActivity
import com.example.attendonb.utilites.Constants.Companion.ENDED
import com.example.attendonb.utilites.Constants.Companion.ENTER
import com.example.attendonb.utilites.Constants.Companion.OUT
import com.example.attendonb.utilites.MapUtls
import com.example.attendonb.utilites.PrefUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main_stats.*


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
    }

    override fun intiView() {
        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
        apply_stats.text = resources.getString(R.string.apply)
        snakBar=Snackbar.make(parent_view, resources.getString(R.string.mock_location_warrning), Snackbar.LENGTH_INDEFINITE);

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

        currentLat = location.latitude
        currentLng = location.longitude
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

}