package com.spidersholidays.attendonb.ui.attend.view

import CustomErrorUtils
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseFragment
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.home.mainstate.model.ApplyButtonState
import com.spidersholidays.attendonb.ui.home.mainstate.stateconfirmdialog.StateConfirmDialog
import com.spidersholidays.attendonb.ui.home.mainstate.viewmodel.MainStateViewModel
import com.spidersholidays.attendonb.ui.home.model.AttendMessage
import com.spidersholidays.attendonb.ui.home.qrreader.ui.QrSpareReaderActivity
import com.spidersholidays.attendonb.utilites.Constants
import com.spidersholidays.attendonb.utilites.PrefUtil
import com.spidersholidays.attendonb.utilites.rxeventbus.RxEventBus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_attend.*

class AttendFragment : BaseFragment() {

    private val TAG = AttendFragment::class.java.simpleName
    private var mainStateViewModel: MainStateViewModel? = null
    private val disposables = CompositeDisposable()
    private var attendType: Constants.AttendType? = null

    companion object {
        val TAG = AttendFragment::class.java.simpleName
        fun getInstance(): AttendFragment {
            val attendFragment = AttendFragment()
            return attendFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mainView = inflater.inflate(R.layout.fragment_attend, container, false)
        return mainView
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
        mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!))
    }

    override fun intiView() {


//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
//            qr_attend_btn.visibility = View.GONE
//        } else {
//            qr_attend_btn.visibility = View.VISIBLE
//
//        }

        /**
         * Initial request to update view stats
         * */
        val disposable = RxEventBus.getInstance().refreshStatsSubject.subscribe({ reFreshStats ->
            mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!))

        }, { throwable ->
            Log.e(TAG, "Error --->" + throwable.message)
        })
        disposables.add(disposable)

        attend_message_type.text = resources.getString(R.string.please_select_attend_type) + context?.let { PrefUtil.getCurrentStatsMessage(it) }
    }

    private fun intiListeners() {

        network_attend_btn.setOnClickListener {
            setBtnAttendBtnState(ApplyButtonState(btnEnabled = false, btnVisible = true, btnType = Constants.AttendType.NETWORK))
            mainStateViewModel?.sendAttendNetworkRequest()

            /**
             * Check location has been currently disabled
             * */
//            mainStateViewModel?.attendRequest(context!!)
//            if (!utilities.Utilities.isLocationEnabled(context)) {
//                CustomErrorUtils.viewSnackBarError(Constants.ErrorType.GPS_PROVIDER)
//            } else {
//
//                attendType = Constants.AttendType.NETWORK
//                setBtnAttendBtnState(ApplyButtonState(btnEnabled = false, btnVisible = true, btnType = Constants.AttendType.NETWORK))
//                mainStateViewModel?.attendRequest(context!!)
//            }

        }


        qr_attend_btn.setOnClickListener {
            if (!utilities.Utilities.isLocationEnabled(context)) {
                CustomErrorUtils.viewSnackBarError(Constants.ErrorType.GPS_PROVIDER)
            } else {

                attendType = Constants.AttendType.QR
                setBtnAttendBtnState(ApplyButtonState(btnEnabled = false, btnVisible = true, btnType = Constants.AttendType.QR))
                mainStateViewModel?.attendRequest(context!!)
            }
        }


    }

    @SuppressLint("SetTextI18n")
    override fun initObservers() {

        mainStateViewModel?.onAttendBtnChangeState()?.observe(this, Observer { stats ->
            setBtnAttendBtnState(stats)
            setAttendTitle()
        })

        mainStateViewModel?.onDataLoading()?.observe(this, Observer {
            if (it) {
                fragment_attend_progress.visibility = View.VISIBLE
            } else {
                fragment_attend_progress.visibility = View.GONE
            }
        })


        /**
         * this observer checks wither can attend or not
         * if  "yes"  ----> processes will continue to desired method
         *     "No"   ----> an rejection reason should be displayed from "ViewModel"
         */
        mainStateViewModel?.onAttendAction()?.observe(this, Observer {
            when (it.attendFlag) {
                AttendMessage.AttendFlags.ENTER, AttendMessage.AttendFlags.OUT -> {
                    when (attendType) {
                        Constants.AttendType.QR -> {

                            navigateToScanScreen(it.currentLocation)
                            setBtnAttendBtnState(ApplyButtonState(btnEnabled = true, btnVisible = true, btnType = Constants.AttendType.QR))

                        }
                        Constants.AttendType.NETWORK -> {
                            /**
                             * Location check has been disabled
                             * */
//                            Log.e(TAG, "Network attend reqested")
//                            mainStateViewModel?.sendAttendNetworkRequest()
                        }
                    }
                }
                AttendMessage.AttendFlags.ENDED -> {
                }
            }
            attendType = null
        })



        mainStateViewModel?.onAttendNetworkAction()?.observe(viewLifecycleOwner, Observer {

            if (it) {
                if (PrefUtil.getCurrentUserStatsID(context!!) == Constants.OUT) {
                    StateConfirmDialog.getInstance(PrefUtil.getUserName(context!!)!!, Constants.OUT).show(childFragmentManager.beginTransaction(), StateConfirmDialog::javaClass.name)
                } else if (PrefUtil.getCurrentUserStatsID(context!!) == Constants.ENDED) {
                    StateConfirmDialog.getInstance(PrefUtil.getUserName(context!!)!!, Constants.ENDED).show(childFragmentManager.beginTransaction(), StateConfirmDialog::javaClass.name)
                }
            }
        })

    }


    private fun setBtnAttendBtnState(applyButtonState: ApplyButtonState) {


        when (applyButtonState.btnType) {

            Constants.AttendType.QR, Constants.AttendType.NETWORK -> {
                if (applyButtonState.btnEnabled) {
                    qr_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
                    qr_attend_btn.isEnabled = true

                    network_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
                    network_attend_btn.isEnabled = true
                } else {
                    qr_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray400))
                    qr_attend_btn.isEnabled = false
                    network_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.gray400))
                    network_attend_btn.isEnabled = false
                }
            }


            Constants.AttendType.MAIN_CONTAINER -> {
                if (applyButtonState.btnVisible) {
                    attend_btn_container.visibility = View.VISIBLE
                    qr_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
                    network_attend_btn.setBackgroundColor(ContextCompat.getColor(context!!, R.color.text_input_color))
                    qr_attend_btn.isEnabled = true
                    network_attend_btn.isEnabled = true


                } else {
                    attend_btn_container.visibility = View.GONE
                }

            }


        }

    }

    private fun navigateToScanScreen(location: Location) {
        val intent = Intent(activity, QrSpareReaderActivity::class.java)
        intent.putExtra(QrSpareReaderActivity.CURRENT_LAT, location.latitude)
        intent.putExtra(QrSpareReaderActivity.CURRENT_LNG, location.longitude)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)

    }

    @SuppressLint("SetTextI18n")
    private fun setAttendTitle() {
        val currentAttendState = context?.let { PrefUtil.getCurrentUserStatsID(it) }
        attend_message_type_label.visibility = View.VISIBLE

        when (currentAttendState) {
            Constants.ENTER -> {
                attend_message_type.text = " " + resources.getString(R.string.attend)

            }
            Constants.OUT -> {
                attend_message_type.text = " " + resources.getString(R.string.out)

            }
            Constants.ENDED -> {
                attend_message_type_label.visibility = View.INVISIBLE
                attend_message_type.text = resources.getString(R.string.attend_taken)
            }
        }


    }

}