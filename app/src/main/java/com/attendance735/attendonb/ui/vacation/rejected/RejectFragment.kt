package com.attendance735.attendonb.ui.vacation.rejected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.attendance735.attendonb.R
import com.attendance735.attendonb.base.BaseFragment

class RejectFragment :BaseFragment() {
    companion object{
        val TAG=RejectFragment::javaClass.name
        fun getInstance():RejectFragment{
            return  RejectFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_reject,container,false)
    }

    override fun intiView() {
    }

    override fun initObservers() {
    }
}