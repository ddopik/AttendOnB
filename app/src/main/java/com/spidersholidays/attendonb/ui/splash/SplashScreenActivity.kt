package com.spidersholidays.attendonb.ui.splash

import CustomErrorUtils
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.ddopik.linktask.ui.home.model.Article
import com.ddopik.linktask.ui.home.model.ArticlesResponse
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.network.BaseNetWorkApi
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.login.LoginActivity
import com.spidersholidays.attendonb.utilites.PrefUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashScreenActivity : BaseActivity() {

    val TAG = SplashScreenActivity::class.java.simpleName;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions

        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({
            if (PrefUtil.isLoggedIn(baseContext)) {
                Log.e(TAG, "----> init isLoggedIn()")
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))

            } else {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }
            finish()
        }, 750)
    }  @SuppressLint("CheckResult")


    override fun initObservers() {

    }
}
