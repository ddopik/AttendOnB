package com.spidersholidays.attendonb.ui.login

import CustomErrorUtils
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.BaseActivity
import com.spidersholidays.attendonb.ui.home.HomeActivity
import com.spidersholidays.attendonb.ui.login.viewmodel.LoginViewModel
import com.spidersholidays.attendonb.utilites.Constants
import com.spidersholidays.attendonb.utilites.Constants.Companion.REQUEST_CODE_LOGIN_PERMATION
import com.spidersholidays.attendonb.utilites.MapUtls
import com.spidersholidays.attendonb.utilites.PrefUtil
import com.spidersholidays.attendonb.utilites.Utilities
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.activity_login.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class LoginActivity : BaseActivity(), MapUtls.OnLocationUpdate, EasyPermissions.PermissionCallbacks {

    private val TAG = LoginActivity::class.java.simpleName
    private var loginViewModel: LoginViewModel? = null
    private var mapUtls: MapUtls? = null
    private var curentLat: Double = 0.0
    private var curentLng: Double = 0.0
    private var isFromMockProvider: Boolean? = null
    private var isPermissionGranted: Boolean = false
//    private var snackBar: Snackbar? = null
//    private var isSnackBarCurrentlyShowen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        snackBar = Snackbar.make(login_main_paren_view, "", Snackbar.LENGTH_INDEFINITE);


        loginViewModel = LoginViewModel.getInstance(this)
        requestLoginPermeation()
        initListeners()


    }

    override fun initObservers() {
        loginViewModel?.onDataLoading()?.observe(this, Observer {

            if (it) {
                login_progress.visibility = View.VISIBLE
            } else {
                login_progress.visibility = View.GONE
            }
        })


        loginViewModel?.onUnKnownError()?.observe(this, Observer {
            if (it) {
                showToast(resources.getString(R.string.un_known_error))
            }

        })


//
        loginViewModel?.sourceListener()?.observe(this, Observer { source ->

            Log.e(TAG, "---->sourceListener() $source")

        })


        loginViewModel?.onLoginStateChanged()?.observe(this, Observer { loginState ->

            if (loginState && PrefUtil.isLoggedIn(this)) {
                Log.e(TAG, "---->onLoginStateChanged() $loginState")
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                loginViewModel?.onLoginStateChanged()?.removeObservers(this)

                startActivity(intent)
                finish()
            }
        })


    }

    private fun initListeners() {
        btn_login.setOnClickListener {

            if (validateLoginInput() && isPermissionGranted) {
                loginViewModel?.loginUser(userName = login_user_name.text.toString(), password = login_password.text.toString(), currentLat = curentLat, currentLng = curentLng, deviceImei = Utilities.getDeviceIMEI(baseContext), context = baseContext)
            } else {
                requestLoginPermeation()
            }

        }
    }


    private fun validateLoginInput(): Boolean {
//        val off = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
//        } else {
//            val stats :LocationManager= AttendOnBApp.app?.baseContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//              stats.isProviderEnabled(LocationManager.GPS_PROVIDER)
//         }
//
        val gpsStats: LocationManager = AttendOnBApp.app?.baseContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val off = gpsStats.isProviderEnabled(LocationManager.GPS_PROVIDER)


        if (off == false) {
            val onGPS = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(onGPS)
            return false
        }
        if (login_user_name.text.isEmpty()) {
            input_user_name.error = resources.getString(R.string.invalid_user_name)
            return false
        } else {
            input_user_name.isErrorEnabled = false
        }

        if (login_password.text.isEmpty()) {
            input_password.error = resources.getString(R.string.invalid_password)
            return false
        } else {
            input_password.isErrorEnabled = false
        }


//        if (curentLat == null && curentLng == null) {
//
//            showToast("error gitting current location")
//            requestLoginPermeation()
//            return false
//        }

        return true

    }


    //    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onLocationUpdate(location: Location) {
        // New location has now been determined
//        val latLng = LatLng(location.latitude, location.longitude)
        curentLat = location.latitude
        curentLng = location.longitude
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            isFromMockProvider = location.isFromMockProvider
        }
        if (isFromMockProvider!!) {
            btn_login.isEnabled = false
            CustomErrorUtils.viewSnackBarError(Constants.ErrorType.MOCK_LOCATION)

        } else {
//            btn_login.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.text_input_color))
            btn_login.isEnabled = true
        }
        mapUtls?.removeLocationRequest()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOGIN_PERMATION)
    private fun requestLoginPermeation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)) {
            mapUtls?.startLocationUpdates(this, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
            mapUtls = MapUtls(this)
            initObservers()
            isPermissionGranted = true
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.login_permutation_message), REQUEST_CODE_LOGIN_PERMATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)

        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CODE_LOGIN_PERMATION && perms.size > 0) {
            isPermissionGranted = false
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                AppSettingsDialog.Builder(this).build().show()
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CODE_LOGIN_PERMATION) {
            isPermissionGranted = true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            @NonNull permissions: Array<String>,
                                            @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)) {
                mapUtls?.startLocationUpdates(this, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
                mapUtls = MapUtls(this)
                initObservers()
                isPermissionGranted = true
            }
        }
    }

//    fun showSnackBar(messag: String) {
//        snackBar?.setText(messag)
//        snackBar?.addCallback(getSnackBarCallBack())
//        if (!isSnackBarCurrentlyShowen) {
//            snackBar?.show()
//
//        }
//    }
//
//    fun getSnackBarCallBack(): Snackbar.Callback {
//        return object : Snackbar.Callback() {
//            override fun onShown(sb: Snackbar?) {
//                super.onShown(sb)
//                isSnackBarCurrentlyShowen = true
//            }
//
//            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                super.onDismissed(transientBottomBar, event)
//                isSnackBarCurrentlyShowen = false
//                snackBar?.dismiss()
//            }
//        }
//    }

    override fun onStop() {
        super.onStop()
        mapUtls?.removeLocationRequest()
        loginViewModel?.fetchDataDisposable?.dispose()
        Log.e(TAG, "onStop")
    }
}