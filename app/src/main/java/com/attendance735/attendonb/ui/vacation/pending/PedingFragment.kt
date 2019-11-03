package com.attendance735.attendonb.ui.vacation.pending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.BaseFragment

class PedingFragment:BaseFragment() {

    companion object{
        val TAG=PedingFragment::javaClass.name
        fun getInstance():PedingFragment{
            return  PedingFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        return layoutInflater.inflate(R.layout.fragment_pending,container,false)
    }

    override fun intiView() {
    }

    override fun initObservers() {
    }

}