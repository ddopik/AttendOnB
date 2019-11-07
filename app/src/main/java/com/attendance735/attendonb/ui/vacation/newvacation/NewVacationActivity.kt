package com.attendance735.attendonb.ui.vacation.newvacation

import android.os.Bundle
import androidx.lifecycle.Observer
 import com.attendance735.attendonb.base.BaseActivity
import com.attendance735.attendonb.base.commonModel.User
import kotlinx.android.synthetic.main.activity_new_vacation.*
 import java.lang.reflect.AccessibleObject.setAccessible
import android.widget.Spinner
import defaultIntializarion.AppConfig.app
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.attendance735.attendonb.R


class NewVacationActivity : BaseActivity() {


    val TAG = NewVacationActivity::class.java.simpleName
    var newVacationViewModel: NewVacationViewModel? = null
    val mangerList: MutableList<User> = mutableListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.attendance735.attendonb.R.layout.activity_new_vacation)
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
                    val userSpinnerAdapter = NewVacationAdapter(it, R.layout.view_holder_user_spinner)
                     spinner_user_manger.adapter = userSpinnerAdapter
                 }
            }

        })

    }

 }