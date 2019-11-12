package com.spidersholidays.attendonb.ui.vacation.newvacation

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.base.commonModel.User
import com.spidersholidays.attendonb.ui.vacation.newvacation.NewVacationSpinnerAdapter.OnUserSpinnerItemClickListener
import kotlinx.android.synthetic.main.activity_new_vacation.*
import java.text.SimpleDateFormat
import java.util.*


class NewVacationActivity : BaseActivity() {


    val TAG = NewVacationActivity::class.java.simpleName
    var newVacationViewModel: NewVacationViewModel? = null
    val mangerList: MutableList<User> = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_vacation)
        newVacationViewModel = NewVacationViewModel.getInstance(this)

        initObservers()

        initListeners()
    }

    override fun initObservers() {
        newVacationViewModel?.getVacationFormData()


        newVacationViewModel?.onVacationFormDataChange()?.observe(this, Observer { it ->
            it.users?.let {
                if (it.size > 0) {
                    mangerList.clear()
//                    val user = User()
//                    user.name = resources.getString(R.string.choose_manger)
//                    mangerList.add(user)
                    mangerList.addAll(it)
//                    userSpinnerSpinnerAdapter = NewVacationSpinnerAdapter(mangerList, com.spidersholidays.attendonb.R.layout.view_holder_user_spinner)
//                    spinner_user_manger.adapter = userSpinnerSpinnerAdapter

                }
            }

        })



        newVacationViewModel?.onVacationFormProgressChanged()?.observe(this, Observer {
            if (it){
                new_vacation_form_progress.visibility = View.VISIBLE
            }else{
                new_vacation_form_progress.visibility = View.GONE

            }
        })


///////////////////////////////

    }


    private fun initListeners() {
        //////////////////
        val myStartDateCalendar = Calendar.getInstance()
        val myEndDateCalendar = Calendar.getInstance()
        myStartDateCalendar.add(Calendar.DAY_OF_MONTH, 3)
        myEndDateCalendar.add(Calendar.DAY_OF_MONTH, 3)

        val startDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myStartDateCalendar.set(Calendar.YEAR, year)
            myStartDateCalendar.set(Calendar.MONTH, monthOfYear)
            myStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MM/dd/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            start_date_val.setText(sdf.format(myStartDateCalendar.time))

        }


        val EndDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myStartDateCalendar.set(Calendar.YEAR, year)
            myStartDateCalendar.set(Calendar.MONTH, monthOfYear)
            myStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "MM/dd/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            end_date_val.setText(sdf.format(myStartDateCalendar.time))

        }


        start_date_val.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@NewVacationActivity, startDateListener, myStartDateCalendar.get(Calendar.YEAR), myStartDateCalendar.get(Calendar.MONTH), myStartDateCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = myStartDateCalendar.timeInMillis
            datePickerDialog.show()
        }
        end_date_val.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@NewVacationActivity, EndDateListener, myEndDateCalendar.get(Calendar.YEAR), myEndDateCalendar.get(Calendar.MONTH), myEndDateCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = myEndDateCalendar.timeInMillis
            datePickerDialog.show()
        }


        choose_manger_val.setOnClickListener {

            val intent = Intent(NewVacationActivity@ this, MangerListActivity::class.java)

            intent.putExtra(MangerListActivity.userList, mangerList as ArrayList<User>)
            startActivityForResult(intent,MangerListActivity.MangerListActivity_REQUESE_CODE)
        }


//        send_vacation_request.setOnClickListener {
//
//            val err = spinner_user_manger.selectedView.findViewById<TextView>(R.id.manger_id)
//            err.error = "asd"
//        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            MangerListActivity.MangerListActivity_REQUESE_CODE ->{
               val user = data?.getParcelableExtra<User>(MangerListActivity.SELECTED_MANGER)
                if(user !=null){
                    choose_manger_val.setText(user.name, TextView.BufferType.EDITABLE)
                    choose_manger_val.tag =user
                }

            }
        }

    }
}