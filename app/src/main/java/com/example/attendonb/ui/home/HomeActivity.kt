package com.example.attendonb.ui.home

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
import com.example.attendonb.R
import com.example.attendonb.base.BaseActivity
import com.example.attendonb.base.CustomDialog
import com.example.attendonb.network.BaseNetWorkApi.Companion.IMAGE_BASE_URL
import com.example.attendonb.services.geofencing.GeoFencingService
import com.example.attendonb.ui.home.mainstats.MainStatsFragment
import com.example.attendonb.utilites.GlideApp
import com.example.attendonb.utilites.PrefUtil
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


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
                .replace(R.id.home_swap_container, MainStatsFragment.newInstance(), MainStatsFragment::class.java.simpleName)
                .commitNow()

        initView()
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//
//        return if (id == R.id.action_settings) {
//            true
//        } else super.onOptionsItemSelected(item)
//
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_home) {
            val currentScreenIndex = getCurrentViewIndex(MainStatsFragment::class.java.simpleName);
            if (currentScreenIndex < supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.home_swap_container, MainStatsFragment.newInstance(),MainStatsFragment::class.java.simpleName)
                        .commitNow()
            }

        } else if (id == R.id.log_out) {
            val customDialog = CustomDialog.getInstance(this, CustomDialog.DialogOption.OPTION_2);
            customDialog.customDialogContent = resources.getString(R.string.log_out);
            customDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
                override fun onPositiveClicked() {
                    stopService(geoFencingService)
                    customDialog.dismiss()
                    PrefUtil.clearPrefUtil(baseContext)
                    finish()
                }

                override fun onNectiveClicked() {
                    customDialog.dismiss()

                }
            }
            customDialog.show()

        }

//        else if (id == R.id.nav_gallery) {

//        }
//
//        else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getCurrentViewIndex(tagname: String): Int {
        val manager = supportFragmentManager
        for (i in 0 until manager.backStackEntryCount) {
            if (manager.getBackStackEntryAt(i).name.equals(tagname)) {
                return i
            }
        }
        return 0
    }


    private fun initView() {

         val hView = nav_view.getHeaderView(0)
        val profileNavImg=hView.findViewById<ImageView>(R.id.profile_nav_img)
        val profileNavName=hView.findViewById<TextView>(R.id.nav_user_name)
        val profileNavMail=hView.findViewById<TextView>(R.id.nav_user_mail)
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        GlideApp.with(this)
                .load(IMAGE_BASE_URL+PrefUtil.getUserProfilePic(this))
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .apply(requestOptions)
                .into(profileNavImg)

        profileNavName.text = PrefUtil.getUserName(this)
        profileNavMail.text=PrefUtil.getUserMail(this)

    }

    override fun onStop() {
        super.onStop()
        stopService(geoFencingService)
    }
}
