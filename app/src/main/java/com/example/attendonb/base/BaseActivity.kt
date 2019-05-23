package com.example.attendonb.base

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.attendonb.R
import com.example.attendonb.utilites.Constants.ErrorType.*
import com.example.attendonb.utilites.rxeventbus.RxEventBus
import com.example.attendonb.utilites.rxeventbus.RxForceRefreshEvent
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.java.simpleName
    private var snackBar: Snackbar? = null
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    abstract fun initObservers()

    protected fun addFragment(containerId: Int, fragment: Fragment, title: String?, tag: String?, stackState: Boolean) {

        if (stackState) {
            supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).addToBackStack(tag).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).commit()
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLog(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun onResume() {
        super.onResume()
        //        snackBar?.addCallback(getSnackBarCallBack())
        val disposable = RxEventBus.getInstance().getConnectionStatsSubject().subscribe({ event ->
            when (event?.messageType) {
                ONLINE_DISCONNECTED -> {
                    showSnackBar(messag = event.message, action = View.OnClickListener {
                        RxEventBus.getInstance().post(RxForceRefreshEvent(true))
                    }, dismiss = false, actionText = "refresh")
                }
                //////////////
                GPS_PROVIDER -> {
                    showSnackBar(messag = event.message, action = View.OnClickListener {
                        val onGPS = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(onGPS)
                    }, actionText = resources.getString(R.string.enable), dismiss = false)

                }
                MOCK_LOCATION -> {
                    showSnackBar(event.message, false)
                }
                OUT_OF_AREA -> {
                    showSnackBar(event.message, false)
                }
                ONLINE_CONNECTED -> {

                }
//                    snackBar?.dismiss()
            }

        }, { throwable ->
            Log.e(TAG, throwable.message)
        })
        disposables.add(disposable)
    }

    fun showSnackBar(messag: String, dismiss: Boolean) {
//        if (snackBar?.isShown!!) {
        snackBar = Snackbar.make(findViewById<View>(android.R.id.content), "", Snackbar.LENGTH_LONG)
        snackBar?.setText(messag)
        snackBar?.show()
        Log.e(TAG, "show SnackBar Visible")
//        }
//        else if (dismiss) {
//            snackBar?.dismiss()
//            Log.e(TAG, "showSnackBar dismissed")
//        }

    }

    fun showSnackBar(messag: String, action: View.OnClickListener, dismiss: Boolean, actionText: String) {
//        if (!snackBar?.isShown!!) {
        snackBar = Snackbar.make(findViewById<View>(android.R.id.content), "", Snackbar.LENGTH_INDEFINITE)
        snackBar?.setText(messag)
        snackBar?.setAction(actionText, action)
        snackBar?.show()
        Log.e(TAG, "show SnackBar Visible")
//        }

//        else if (dismiss) {
//            snackBar?.dismiss()
//            Log.e(TAG, "showSnackBar dismissed")
//        }
    }




}