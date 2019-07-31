package com.spidersholidays.attendonb.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.base.CustomDialog
import com.spidersholidays.attendonb.network.BaseNetWorkApi.Companion.IMAGE_BASE_URL
import com.spidersholidays.attendonb.services.geofencing.GeoFencingService
import com.spidersholidays.attendonb.ui.home.mainstate.ui.MainStateFragment
 import com.spidersholidays.attendonb.utilites.PrefUtil
import com.google.android.material.navigation.NavigationView
import com.spidersholidays.attendonb.ui.attend.view.AttendFragment
import com.spidersholidays.attendonb.ui.home.mainstate.stateconfirmdialog.StateConfirmDialog
import com.spidersholidays.attendonb.ui.login.LoginActivity
import com.spidersholidays.attendonb.utilites.Constants
import com.spidersholidays.attendonb.utilites.GlideApp
import com.spidersholidays.attendonb.utilites.PrefUtil.Companion.ARABIC_LANG
import com.spidersholidays.attendonb.utilites.PrefUtil.Companion.ENGLISH_LANG
import com.spidersholidays.attendonb.utilites.Utilities
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    companion object {
        val VIEW_CONFIRM_DIALOG = "view_confirm_dialog"
    }

    private var geoFencingService: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction()
                .replace(R.id.home_swap_container, MainStateFragment.newInstance(), MainStateFragment::class.java.simpleName)
                .commitNow()

        initView()
        initListeners()
    }

    override fun initObservers() {
    }

    override fun onResume() {
        super.onResume()
        geoFencingService = Intent(this, GeoFencingService::class.java)
        startService(geoFencingService)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
//            val currentScreenIndex = getCurrentViewIndex(MainStateFragment::class.java.simpleName)
//            if (currentScreenIndex < supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.home_swap_container, MainStateFragment.newInstance(), MainStateFragment::class.java.simpleName)
                     .commit()
//            }

        }

        else if (id == R.id.nav_attend) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.home_swap_container, AttendFragment.getInstance(), AttendFragment::class.java.simpleName)
                    .addToBackStack(MainStateFragment::class.java.simpleName)
                    .commit()

//
//
        }
        else if (id == R.id.language) {
            val customDialog = CustomDialog.getInstance(this, CustomDialog.DialogOption.LANGUAGE)
            customDialog.customDialogContent = resources.getString(R.string.language);
            customDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
                override fun onPositiveClicked() {
                    Utilities.changeAppLanguage(baseContext, ARABIC_LANG)
                    PrefUtil.setAppLang(baseContext, ARABIC_LANG)
                    customDialog.dismiss()
                    Utilities.restartContext(baseContext)
                }

                override fun onNectiveClicked() {
                    Utilities.changeAppLanguage(baseContext, ENGLISH_LANG)
                    PrefUtil.setAppLang(baseContext, ENGLISH_LANG)
                    customDialog.dismiss()
                    Utilities.restartContext(baseContext)
                }
            }
            customDialog.show()
            nav_view.getMenu().getItem(4).setCheckable(false)
        }
        else if (id == R.id.log_out) {
            val customDialog = CustomDialog.getInstance(this, CustomDialog.DialogOption.OPTION_2);
            customDialog.customDialogContent = resources.getString(R.string.log_out);
            customDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
                override fun onPositiveClicked() {
                    stopService(geoFencingService)
                    customDialog.dismiss()
                    PrefUtil.clearPrefUtil(baseContext)
                    finish()
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }

                override fun onNectiveClicked() {
                    customDialog.dismiss()

                }
            }
            customDialog.show()
            nav_view.getMenu().getItem(5).setCheckable(false)
        }


            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            return true
        }



        private fun initView() {

            val hView = nav_view.getHeaderView(0)
            val profileNavImg = hView.findViewById<ImageView>(R.id.profile_nav_img)
            val profileNavName = hView.findViewById<TextView>(R.id.nav_user_name)
            val profileNavMail = hView.findViewById<TextView>(R.id.nav_user_mail)
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
            GlideApp.with(this)
                    .load(IMAGE_BASE_URL + PrefUtil.getUserProfilePic(this))
                    .placeholder(R.drawable.app_icon)
                    .error(R.drawable.app_icon)
                    .apply(requestOptions)
                    .into(profileNavImg)

            profileNavName.text = PrefUtil.getUserName(this)
            profileNavMail.text = PrefUtil.getUserMail(this)

        }

        fun initListeners() {
            val state = intent.getBooleanExtra(VIEW_CONFIRM_DIALOG, false)

            if (state) {

                if (PrefUtil.getCurrentUserStatsID(baseContext) == Constants.OUT) {
                    StateConfirmDialog.getInstance(PrefUtil.getUserName(baseContext)!!, Constants.OUT).show(supportFragmentManager.beginTransaction(), StateConfirmDialog::javaClass.name)
                } else if (PrefUtil.getCurrentUserStatsID(baseContext) == Constants.ENDED) {
                    StateConfirmDialog.getInstance(PrefUtil.getUserName(baseContext)!!, Constants.ENDED).show(supportFragmentManager.beginTransaction(), StateConfirmDialog::javaClass.name)
                }



                intent.putExtra(VIEW_CONFIRM_DIALOG, false) //clear intent history to avoid dialog occupancy through onResume
            }


        }

        override fun onStop() {
            super.onStop()
            stopService(geoFencingService)
        }

}