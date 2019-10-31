package com.attendance735.attendonb.ui.vacation.approved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.BaseFragment

class ApprovedFragment :BaseFragment() {

    companion object{
        val TAG=ApprovedFragment::javaClass.name
        fun getInstance():ApprovedFragment{
         return  ApprovedFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_approved,container,false)
    }

    override fun intiView() {
     }

    override fun initObservers() {
     }


}