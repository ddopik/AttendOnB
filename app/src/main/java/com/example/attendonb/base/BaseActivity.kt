package com.example.attendonb.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class BaseActivity : AppCompatActivity() {


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
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show();
    }

    fun showLog(tag: String, msg: String) {
        Log.e(tag, msg)
    }


}