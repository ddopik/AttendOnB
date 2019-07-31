package com.kspidersholidays.attendonb.ui.attend.view

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kspidersholidays.attendonb.R
import com.kspidersholidays.attendonb.base.BaseFragment
import com.kspidersholidays.attendonb.ui.home.mainstate.model.ApplyButtonState
import com.kspidersholidays.attendonb.ui.home.mainstate.viewmodel.MainStateViewModel
import com.kspidersholidays.attendonb.ui.home.model.AttendMessage
import com.kspidersholidays.attendonb.ui.home.qrreader.ui.QrSpareReaderActivity
import com.kspidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_attend.*
import com.kspidersholidays.attendonb.utilites.rxeventbus.RxEventBus
import androidx.lifecycle.Observer
import com.kspidersholidays.attendonb.ui.home.HomeActivity
import com.kspidersholidays.attendonb.utilites.Constants

class AttendFragment :BaseFragment() {

    private val TAG=AttendFragment::class.java.simpleName
    private var mainStateViewModel: MainStateViewModel? = null
    private val disposables = CompositeDisposable()

    companion object {
        public fun getInstance(): AttendFragment {
            val attendFragment = AttendFragment()
            return attendFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mainView= inflater.inflate(R.layout.fragment_attend,container,false)
        return mainView;
     }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainStateViewModel = MainStateViewModel.getInstance(activity as HomeActivity)
        initObservers()
        intiView()
        intiListeners()
        setAttendTitle()

    }


    override fun onResume() {
        super.onResume()
        mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!)!!)
    }

    override fun intiView() {
        attend_message_type.text=resources.getString(R.string.please_select_attend_type)+ context?.let { PrefUtil.getCurrentStatsMessage(it) }


    }

    @SuppressLint("SetTextI18n")
    override fun initObservers() {

        mainStateViewModel?.onAttendBtnChangeState()?.observe(this, Observer { stats ->


            if ( context?.let { PrefUtil.getCurrentUserStatsID(it) } == Constants.ENDED ){
            attend_btn_container.visibility=View.GONE
        }else
            {
                attend_btn_container.visibility=View.VISIBLE

            }
            setAttendTitle()
            setBtnAttendBtnState(stats)
        })

        mainStateViewModel?.onDataLoading()?.observe(this, Observer {
            if (it) {
                fragment_attend_progress.visibility = View.VISIBLE
            } else {
                fragment_attend_progress.visibility = View.GONE
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
        qr_attend_btn.setOnClickListener {
            val applyButtonStats = ApplyButtonState()
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
            mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!)!!)

        }, { throwable ->
            Log.e(TAG, throwable.message)
        })
        disposables.add(disposable)

    }


    private fun setBtnAttendBtnState(applyButtonState: ApplyButtonState) {

         qr_attend_btn.visibility = View.VISIBLE
        if (applyButtonState.isEnable!!) {
            qr_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
            qr_attend_btn.isEnabled = true
        } else if (applyButtonState.isViable !=null && !applyButtonState.isViable!!) {
//            qr_attend_btn.visibility = View.INVISIBLE
        } else {
            qr_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray400))
            qr_attend_btn.isEnabled = false
        }
    }
    private fun navigateToScanScreen(location: Location){

        qr_attend_btn.visibility = View.GONE
        setBtnAttendBtnState(ApplyButtonState(btnEnabled = true, btnVisable = true))
        val intent = Intent(activity, QrSpareReaderActivity::class.java)
        intent.putExtra(QrSpareReaderActivity.CURRENT_LAT, location.latitude)
        intent.putExtra(QrSpareReaderActivity.CURRENT_LNG, location.longitude)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)

    }

    @SuppressLint("SetTextI18n")
    private fun setAttendTitle(){
        val currentAttendState= context?.let { PrefUtil.getCurrentUserStatsID(it) }

        when (currentAttendState){
            Constants.ENTER ->{
                attend_message_type.text= " "+ resources.getString(R.string.attend)

            }
            Constants.OUT ->{
            attend_message_type.text =" "+resources.getString(R.string.out)

            }
            Constants.ENDED ->{
                attend_message_type.text=resources.getString(R.string.attend_taken)

            }
        }



    }

}