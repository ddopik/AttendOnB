package com.attendance735.attendonb.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.attendance735.attendonb.R
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.BaseActivity
import com.attendance735.attendonb.base.CustomDialog
import com.attendance735.attendonb.network.BaseNetWorkApi.Companion.IMAGE_BASE_URL
import com.attendance735.attendonb.ui.attend.view.AttendFragment
import com.attendance735.attendonb.ui.home.mainstate.stateconfirmdialog.StateConfirmDialog
import com.attendance735.attendonb.ui.home.mainstate.ui.MainStateFragment
import com.attendance735.attendonb.ui.login.LoginActivity
import com.attendance735.attendonb.ui.payroll.ui.PayRollFragment
import com.attendance735.attendonb.ui.vacation.VacationFragment
import com.attendance735.attendonb.ui.vacationmangment.ui.ManagementVacationFragment
import com.attendance735.attendonb.utilites.Constants
import com.attendance735.attendonb.utilites.GlideApp
import com.attendance735.attendonb.utilites.PrefUtil
import com.attendance735.attendonb.utilites.PrefUtil.Companion.ARABIC_LANG
import com.attendance735.attendonb.utilites.PrefUtil.Companion.ENGLISH_LANG
import com.attendance735.attendonb.utilites.Utilities
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    val TAG = HomeActivity::javaClass.name

    companion object {
        val VIEW_CONFIRM_DIALOG = "view_confirm_dialog"
    }

    private var homeViewModel: HomeViewModel? = null
    private var geoFencingService: Intent? = null
    private var navigationView: NavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView?.setNavigationItemSelectedListener(this)

        initView()
        initListeners()
        initObservers()


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragmentTag: String? = ""

        // Handle navigation view item clicks here.
        val id = item.itemId

        ////////////////////
        if (id == R.id.nav_home) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.home_swap_container, MainStateFragment.newInstance(), MainStateFragment::class.java.simpleName)
                    .commit()


        }
        ////////////////////
        else if (id == R.id.nav_attend) {
            if (supportFragmentManager.findFragmentByTag(AttendFragment::class.java.simpleName) == null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.home_swap_container, AttendFragment.getInstance(), AttendFragment::class.java.simpleName)
//                        .addToBackStack(AttendFragment::class.java.simpleName)
                        .commit()
            }
        }
        //////////////////

        else if (id == R.id.nav_vacation) {
            if (supportFragmentManager.findFragmentByTag(VacationFragment::class.java.simpleName) == null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.home_swap_container, VacationFragment.getInstance(), VacationFragment::class.java.simpleName)
                        .commit()
            }
            ////////////////////

        } else if (id == R.id.nav_pay_roll) {
            if (supportFragmentManager.findFragmentByTag(PayRollFragment::class.java.simpleName) == null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.home_swap_container, PayRollFragment.getInstance(), PayRollFragment::class.java.simpleName)
//                        .addToBackStack(PayRollFragment::class.java.simpleName)
                        .commit()
            }
        }
        //////////////////
        else if (id == R.id.nav_vacation_manger_request) {
            if (supportFragmentManager.findFragmentByTag(ManagementVacationFragment::class.java.simpleName) == null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.home_swap_container, ManagementVacationFragment.getInstance(), ManagementVacationFragment::class.java.simpleName)
                         .commit()
            }
        }
        ////////////////////
        else if (id == R.id.language) {
            nav_view.menu.getItem(4).isChecked = false
            val customDialog = CustomDialog.getInstance(this, CustomDialog.DialogOption.LANGUAGE)
            customDialog.customDialogContent = resources.getString(R.string.choose_language)
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
        }
        ////////////////////
        else if (id == R.id.log_out) {
            val customDialog = CustomDialog.getInstance(this, CustomDialog.DialogOption.OPTION_2)
            customDialog.customDialogContent = resources.getString(R.string.log_out)
            customDialog.onCustomDialogPositiveClick = object : CustomDialog.OnCustomDialogPositiveClick {
                override fun onPositiveClicked() {
//                    stopService(geoFencingService)
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
            nav_view.menu.getItem(5).isCheckable = false
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
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(21))
        GlideApp.with(this)
                .load(IMAGE_BASE_URL + PrefUtil.getUserProfilePic(this))
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .apply(requestOptions)
                .into(profileNavImg)

        profileNavName.text = PrefUtil.getUserName(this)
        profileNavMail.text = PrefUtil.getUserMail(this)

        supportFragmentManager.beginTransaction()
                .replace(R.id.home_swap_container, MainStateFragment.newInstance(), MainStateFragment::class.java.simpleName)
                .commit()

        homeViewModel = HomeViewModel.getInstance(this)
        homeViewModel?.checkUserManagementStats(PrefUtil.getUserId(AttendOnBApp.app?.baseContext!!))


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

    override fun initObservers() {
        homeViewModel?.onUserManagementStatsChange()?.observe(this, Observer {
            navigationView!!.menu.findItem(R.id.nav_vacation_manger_request)?.isVisible = it
        })
    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {

            /**
             * Handling Back navigation
             * */
            val default = supportFragmentManager.fragments.last() is MainStateFragment
            if (default) {
                super.onBackPressed()
            } else {

                /**
                 * user have navigated to Main screen and first Activity in the stack
                 * */

                supportFragmentManager.findFragmentByTag(MainStateFragment::class.java.simpleName)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.home_swap_container, MainStateFragment.newInstance(), MainStateFragment::class.java.simpleName)
                        .commit()
                nav_view.menu.getItem(0).isChecked = true

            }


        }

    }

}
