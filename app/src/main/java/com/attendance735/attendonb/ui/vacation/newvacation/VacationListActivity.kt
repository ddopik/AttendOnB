package com.attendance735.attendonb.ui.vacation.newvacation

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import bases.BaseActivity
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.attendance735.attendonb.R
 import com.attendance735.attendonb.base.commonModel.VacationsType
import com.attendance735.attendonb.ui.vacation.newvacation.adapters.AutoCompleteVacationsTypeAdapter
import com.attendance735.attendonb.utilites.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
 import kotlinx.android.synthetic.main.activity_vacation_type_list.*
import java.util.concurrent.TimeUnit

class VacationListActivity : BaseActivity()  {

    val TAG = VacationListActivity::javaClass.name
    private var autoCompleteVacationsTypeAdapter: AutoCompleteVacationsTypeAdapter? = null
    private val disposable = CompositeDisposable()
    private var selectedVacation: VacationsType? = null
    private  var vacationList:MutableList<VacationsType> = mutableListOf()
    companion object {
        val VACATION_LIST = "vacation_list"
        val SELECTED_VACATION = "selected_vacation"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_type_list)


        vacationList.addAll( intent.getStringArrayListExtra(VACATION_LIST) as MutableList<VacationsType>)
        autoCompleteVacationsTypeAdapter = AutoCompleteVacationsTypeAdapter(vacationList)

        autoCompleteVacationsTypeAdapter?.onVacationSelected = object : AutoCompleteVacationsTypeAdapter.OnVacationSelected {
            override fun onVacationClickListener(vacationsType :VacationsType?) {
                selectedVacation = vacationsType
                vacation_type_search_view.setText(vacationsType?.name, TextView.BufferType.EDITABLE)
                onBackPressed()
            }
        }

        rv_vacation_type_list.adapter = autoCompleteVacationsTypeAdapter


        disposable.add(

                vacation_type_search_view.textChangeEvents()
                        .skipInitialValue()
                        .debounce(Constants.QUERY_SEARCH_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery()))
    }


    private fun searchQuery(): DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(textViewTextChangeEvent: TextViewTextChangeEvent) {

                autoCompleteVacationsTypeAdapter?.filter?.filter(vacation_type_search_view.text)

             lifecycle.currentState

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(SELECTED_VACATION, selectedVacation)
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}