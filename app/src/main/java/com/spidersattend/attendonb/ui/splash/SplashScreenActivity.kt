package com.spidersattend.attendonb.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.spidersattend.attendonb.R
import com.spidersattend.attendonb.base.BaseActivity
import com.spidersattend.attendonb.ui.home.HomeActivity
import com.spidersattend.attendonb.ui.login.LoginActivity
import com.spidersattend.attendonb.utilites.PrefUtil
import android.view.View


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
        }, 3000)
    }

    override fun initObservers() {

    }
}
