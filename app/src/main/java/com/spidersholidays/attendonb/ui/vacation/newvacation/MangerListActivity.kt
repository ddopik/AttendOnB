package com.spidersholidays.attendonb.ui.vacation.newvacation

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import bases.BaseActivity
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.commonModel.User
import com.spidersholidays.attendonb.ui.vacation.newvacation.model.AutoCompleteMangersAdapter
import com.spidersholidays.attendonb.utilites.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_manger_list.*
import java.util.concurrent.TimeUnit

class MangerListActivity : BaseActivity() {


    val TAG = MangerListActivity::javaClass.name
    private var autoCompleteMangersAdapter: AutoCompleteMangersAdapter? = null
    private val disposable = CompositeDisposable()
    private var selectedManger: User? = null

    companion object {
        val userList = "user_list"
        val SELECTED_MANGER = "selected_user"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manger_list)


        val userTempList = intent.getStringArrayListExtra(userList) as MutableList<User>
        autoCompleteMangersAdapter = AutoCompleteMangersAdapter(userTempList)

        autoCompleteMangersAdapter?.onMangerSelected = object : AutoCompleteMangersAdapter.OnMangerSelected {
            override fun onMangerClickListener(user: User?) {
                selectedManger = user
                manger_search_view.setText(user?.name, TextView.BufferType.EDITABLE)
                onBackPressed()
            }
        }

        rv_manger_list.adapter = autoCompleteMangersAdapter


        disposable.add(

                manger_search_view.textChangeEvents()
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

                autoCompleteMangersAdapter?.filter?.filter(manger_search_view.text)

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(SELECTED_MANGER, selectedManger)
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }
}