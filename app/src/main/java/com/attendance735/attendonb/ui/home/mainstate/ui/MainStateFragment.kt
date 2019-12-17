package com.attendance735.attendonb.ui.home.mainstate.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.BaseFragment
import com.attendance735.attendonb.ui.home.HomeActivity
import com.attendance735.attendonb.ui.home.mainstate.viewmodel.MainStateViewModel
import com.attendance735.attendonb.utilites.PrefUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main_stats.*


/**
 * Created by ddopik on 09,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class MainStateFragment : BaseFragment() {
    private val TAG = MainStateFragment::class.java.simpleName
    private var mainView: View? = null
    private var mainStateViewModel: MainStateViewModel? = null
    private val disposables = CompositeDisposable()


    companion object {
        val TAG = MainStateFragment::class.java.simpleName
        fun newInstance() = MainStateFragment()
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


    }

    override fun onResume() {
        super.onResume()
        mainStateViewModel?.checkAttendStatus(PrefUtil.getUserId(context!!)!!)

    }

    override fun intiView() {
        stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)
    }

    override fun initObservers() {



        mainStateViewModel?.onDataLoading()?.observe(this, Observer {
            if (it) {
                main_state_progress.visibility = View.VISIBLE
            } else {
                main_state_progress.visibility = View.GONE
            }
        })
        mainStateViewModel?.onAttendBtnChangeState()?.observe(this, Observer { stats ->

            stats_val.text = PrefUtil.getCurrentStatsMessage(context!!)

        })
    }






}