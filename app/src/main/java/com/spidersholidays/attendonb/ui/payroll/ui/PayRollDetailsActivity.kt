package com.spidersholidays.attendonb.ui.payroll.ui

import android.os.Bundle
import android.os.PersistableBundle
import bases.BaseActivity
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.commonModel.PayRoll

class PayRollDetailsActivity :BaseActivity() {

    companion object{
        val TAG =PayRollDetailsActivity::class.java.name
        val PAY_ROL_OBJ= "payroll_obj"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_roll_details)

       val intent= intent.getParcelableExtra<PayRoll>(PAY_ROL_OBJ)
        intent?.let {
            initView(it)
        }

    }

    private fun initView(payRoll:PayRoll){

    }

}