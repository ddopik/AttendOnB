package com.spidersholidays.attendonb.ui.vacation.newvacation

import android.os.Bundle
import androidx.lifecycle.Observer
 import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.base.commonModel.User
import kotlinx.android.synthetic.main.activity_new_vacation.*
import com.spidersholidays.attendonb.R


class NewVacationActivity : BaseActivity() {


    val TAG = NewVacationActivity::class.java.simpleName
    var newVacationViewModel: NewVacationViewModel? = null
    val mangerList: MutableList<User> = mutableListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.spidersholidays.attendonb.R.layout.activity_new_vacation)
        newVacationViewModel = NewVacationViewModel.getInstance(this)

        initObservers()


    }

    override fun initObservers() {
        newVacationViewModel?.getVacationFormData()


        newVacationViewModel?.onVacationFormDataChange()?.observe(this, Observer { it ->
            it.users?.let {
                if(it.size>0)
                {
                    mangerList.clear()
                     mangerList.addAll(it)
//                    val userSpinnerAdapter = CustomAdapter(baseContext,arr)
                    val userSpinnerAdapter = NewVacationAdapter(it, R.layout.view_holder_user_spinner)

                    spinner_user_manger.adapter = userSpinnerAdapter
                 }
            }

        })

    }

 }