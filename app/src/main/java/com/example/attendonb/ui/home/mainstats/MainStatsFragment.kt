package com.example.attendonb.ui.home.mainstats

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
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
import com.example.attendonb.ui.home.mainstats.model.ApplyButtonStats
import com.example.attendonb.ui.home.mainstats.viewmodel.MainStateViewModel
import com.example.attendonb.ui.home.model.AttendMessage
import com.example.attendonb.ui.home.qrreader.ui.QrReaderActivity
import com.example.attendonb.utilites.Constants
import com.example.attendonb.utilites.PrefUtil
import com.example.attendonb.utilites.rxeventbus.RxEventBus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main_stats.*


/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStatsFragment : BaseFragment() {
    private val TAG = MainStatsFragment::class.java.simpleName
    private var mainView: View? = null
    private var mainStateViewModel: MainStateViewModel? = null
    private val disposables = CompositeDisposable()


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
        initObservers()
        intiView()
        intiListeners()


    }

    override fun onResume() {
        super.onResume()
        mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!))
    }

    override fun intiView() {
        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
        apply_stats.text = resources.getString(R.string.apply)

    }

    override fun initObservers() {

        mainStateViewModel?.onAttendBtnChangeState()?.observe(this, Observer { stats ->

            setBtnAttendBtnState(stats)
        })

        mainStateViewModel?.onDataLoading()?.observe(this, Observer {
            if (it) {
                main_state_progress.visibility = View.VISIBLE
            } else {
                main_state_progress.visibility = View.GONE
            }
        })


        mainStateViewModel?.onAttendAction()?.observe(this, Observer {
            when (it.attendFlag) {
                AttendMessage.AttendFlags.ENTER -> {

                    navigateToScanScreen(it.currentLocation!!)

                }
                AttendMessage.AttendFlags.OUT -> {
                    navigateToScanScreen(it.currentLocation!!)

                }
                AttendMessage.AttendFlags.ENDED -> {
                 }


            }
        })
    }

    private fun intiListeners() {
        apply_stats.setOnClickListener {
            val applyButtonStats = ApplyButtonStats()
            val off = Settings.Secure.getInt(context?.contentResolver, Settings.Secure.LOCATION_MODE)
            if (off == 0) {
//                applyButtonStats.isEnable = false
//                setBtnAttendBtnState(applyButtonStats)
                CustomErrorUtils.viewSnackBarError(Constants.ErrorType.GPS_PROVIDER)
            } else {
                mainStateViewModel?.attendRequest(context!!)
            }


        }

        val disposable = RxEventBus.getInstance().refreshStatsSubject.subscribe({ reFreshStats ->
            mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!))

        }, { throwable ->
            Log.e(TAG, throwable.message)
        })
        disposables.add(disposable)

    }


    private fun setBtnAttendBtnState(applyButtonStats: ApplyButtonStats) {

        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
        apply_stats.visibility = View.VISIBLE
        if (applyButtonStats.isEnable!!) {
            apply_stats.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
            apply_stats.isEnabled = true
        } else if (applyButtonStats.isViable !=null && !applyButtonStats.isViable!!) {
            apply_stats.visibility = View.INVISIBLE
        } else {
            apply_stats.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray400))
            apply_stats.isEnabled = false
        }
    }


    private fun navigateToScanScreen(location:Location){

        main_state_progress.visibility = View.GONE
            val intent = Intent(activity, QrReaderActivity::class.java)
            intent.putExtra(QrReaderActivity.CURRENT_LAT, location.latitude)
            intent.putExtra(QrReaderActivity.CURRENT_LNG, location.longitude)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)

    }


    private fun showPhotoDialog(activity: Activity, title: String?, message: CharSequence) {
        val customDialog = CustomDialog.getInstance(activity, CustomDialog.DialogOption.OPTION_1);
        customDialog.customDialogContent=title;
        customDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
            override fun onPositiveClicked() {
                customDialog.dismiss()
            }

            override fun onNectiveClicked() {
                customDialog.dismiss()

            }
        }
        customDialog.show()


    }

    override fun onStop() {
        super.onStop()
//        disposables.clear()
    }
}