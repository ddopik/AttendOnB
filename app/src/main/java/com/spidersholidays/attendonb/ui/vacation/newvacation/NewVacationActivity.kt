package com.spidersholidays.attendonb.ui.vacation.newvacation

import android.os.Bundle
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.base.commonModel.User
import com.spidersholidays.attendonb.ui.vacation.newvacation.NewVacationAdapter.OnUserSpinnerItemClickListener
import kotlinx.android.synthetic.main.activity_new_vacation.*


class NewVacationActivity : BaseActivity() {


    val TAG = NewVacationActivity::class.java.simpleName
    var newVacationViewModel: NewVacationViewModel? = null
    val mangerList: MutableList<User> = mutableListOf<User>()
    var userSpinnerAdapter: NewVacationAdapter? = null

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
                if (it.size > 0) {
                    mangerList.clear()
                    val user = User()
                    user.name = resources.getString(R.string.please_choose_manger)
                    mangerList.add(user)
                    mangerList.addAll(it)
                    userSpinnerAdapter = NewVacationAdapter(mangerList, R.layout.view_holder_user_spinner)
                    spinner_user_manger.adapter = userSpinnerAdapter
                }
            }

        })


        userSpinnerAdapter?.onUserSpinnerItemClickListener = object : OnUserSpinnerItemClickListener {
            override fun onUserItemClicked(user: User) {
                if (mangerList[0].id == null)
                    mangerList.removeAt(0)
                userSpinnerAdapter?.notifyDataSetChanged()
            }
        }


    }

}