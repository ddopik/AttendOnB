package com.spidersholidays.attendonb.ui.vacation.newvacation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.base.commonModel.User
import com.spidersholidays.attendonb.base.commonModel.VacationsType
import com.spidersholidays.attendonb.utilites.Constants.Companion.MangerListActivity_REQUESE_CODE
import com.spidersholidays.attendonb.utilites.Constants.Companion.VACATION_TYPE_LIST_ACTIVITY_REQUESE_CODE
import kotlinx.android.synthetic.main.activity_new_vacation.*
import java.text.SimpleDateFormat
import java.util.*


class NewVacationActivity : BaseActivity() {


    val TAG = NewVacationActivity::class.java.simpleName
    var newVacationViewModel: NewVacationViewModel? = null
    val mangerList: MutableList<User> = mutableListOf<User>()
    val vacationTypeList: MutableList<VacationsType> = mutableListOf<VacationsType>()

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
                    mangerList.addAll(it)

                }
            }
            it.vacationsType?.let {
                if (it.isNotEmpty()) {
                    vacationTypeList.clear()
                    vacationTypeList.addAll(it)

                }
            }

        })

        newVacationViewModel?.onVacationFormProgressChanged()?.observe(this, Observer {
            if (it) {
                new_vacation_form_progress.visibility = View.VISIBLE
            } else {
                new_vacation_form_progress.visibility = View.GONE

            }
        })


///////////////////////////////

    }


    private fun initListeners() {
        //////////////////
        var myStartDateCalendar = Calendar.getInstance()
        var myEndDateCalendar = Calendar.getInstance()
        /**
         * user can't select a vacation only before 2 days for desired date
         * */
        myStartDateCalendar.add(Calendar.DAY_OF_MONTH, 3)
        myEndDateCalendar.add(Calendar.DAY_OF_MONTH, 3)

        val myFormat = "MM/dd/yyyy" //In which you need put here


        val startDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myStartDateCalendar.set(Calendar.YEAR, year)
            myStartDateCalendar.set(Calendar.MONTH, monthOfYear)
            myStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val startDateFormat = SimpleDateFormat(myFormat, Locale.US)
            start_date_val.setText(startDateFormat.format(myStartDateCalendar.time))
            /**
             * Resitting time if user wan't to reselect
             * */
            myStartDateCalendar = Calendar.getInstance()
            myStartDateCalendar.add(Calendar.DAY_OF_MONTH, 3)
        }


        val endDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->


            myEndDateCalendar.set(Calendar.YEAR, year)
            myEndDateCalendar.set(Calendar.MONTH, monthOfYear)
            myEndDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val endDateFormat = SimpleDateFormat(myFormat, Locale.US)
            end_date_val.setText(endDateFormat.format(myEndDateCalendar.time))
            /**
             * Resitting time if user wan't to reselect
             * */
            myEndDateCalendar = Calendar.getInstance()
            myEndDateCalendar.add(Calendar.DAY_OF_MONTH, 3)

        }


        start_date_val.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@NewVacationActivity, startDateListener, myStartDateCalendar.get(Calendar.YEAR), myStartDateCalendar.get(Calendar.MONTH), myStartDateCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = myStartDateCalendar.timeInMillis
            datePickerDialog.show()
        }
        end_date_val.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this@NewVacationActivity, endDateListener, myEndDateCalendar.get(Calendar.YEAR), myEndDateCalendar.get(Calendar.MONTH), myEndDateCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = myEndDateCalendar.timeInMillis
            datePickerDialog.show()
        }


        choose_manger_val.setOnClickListener {

            if (mangerList.size >0) {
                val intent = Intent(NewVacationActivity@ this, MangerListActivity::class.java)
                intent.putExtra(MangerListActivity.userList, mangerList as ArrayList<User>)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivityForResult(intent, MangerListActivity_REQUESE_CODE)
            }
        }


        choose_vacation_type_val.setOnClickListener {

            if(vacationTypeList.size >0) {
                val intent = Intent(NewVacationActivity@ this, VacationListActivity::class.java)
                intent.putExtra(VacationListActivity.VACATION_LIST, vacationTypeList as ArrayList<VacationsType>)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivityForResult(intent, VACATION_TYPE_LIST_ACTIVITY_REQUESE_CODE)
            }
        }



        send_vacation_request.setOnClickListener {
            validateForm()
        }

    }


    @SuppressLint("SimpleDateFormat")
    private fun validateForm() {

        if (vacation_reason_val.text.isNullOrBlank()) {
            reason_input.error = resources.getString(R.string.field_required)
        } else {
            reason_input.isErrorEnabled = false
        }


        ///////////
        if (!start_date_val.text.isNullOrBlank() && !end_date_val.text.isNullOrBlank()) {

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val strDate = sdf.parse(start_date_val.text.toString())
            val endDate = sdf.parse(end_date_val.text.toString())

            if (strDate.time > endDate.time) {
                input_end_date.error = resources.getString(R.string.end_data_note)
            }
            input_start_date.isErrorEnabled = false
            input_end_date.isErrorEnabled = false
        } else {

            if (start_date_val.text.isNullOrBlank()) {
                input_start_date.error = resources.getString(R.string.field_required)
            } else {
                input_start_date.isErrorEnabled = false
            }

            if (end_date_val.text.isNullOrBlank()) {
                input_end_date.error = resources.getString(R.string.field_required)
            } else {
                input_end_date.isErrorEnabled = false
            }
        }
        ///////////////

        if (choose_manger_val.tag == null) {
            input_choose_manger.error = resources.getString(R.string.please_choose_manger)
        } else {
            input_choose_manger.isErrorEnabled = false
        }

        if (choose_vacation_type_val.tag == null) {
            input_vacation_type.error = resources.getString(R.string.please_choose_vacation_type)
        } else {
            input_vacation_type.isErrorEnabled = false
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            MangerListActivity_REQUESE_CODE -> {
                val user = data?.getParcelableExtra<User>(MangerListActivity.SELECTED_MANGER)
                if (user != null) {
                    choose_manger_val.setText(user.name, TextView.BufferType.EDITABLE)
                    choose_manger_val.tag = user
                }

            }


            VACATION_TYPE_LIST_ACTIVITY_REQUESE_CODE -> {
                val vacationType = data?.getParcelableExtra<VacationsType>(VacationListActivity.SELECTED_VACATION)
                if (vacationType != null) {
                    choose_vacation_type_val.setText(vacationType.name, TextView.BufferType.EDITABLE)
                    choose_vacation_type_val.tag = vacationType
                }

            }


        }

    }
}