package com.example.attendonb.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.attendonb.R
import com.example.attendonb.base.BaseActivity
import com.example.attendonb.ui.home.HomeActivity
import com.example.attendonb.ui.login.viewmodel.LoginViewModel
import com.example.attendonb.utilites.Constants.Companion.REQUEST_CODE_LOGIN_PERMATION
import com.example.attendonb.utilites.Constants.Companion.REQUEST_CODE_PHONE_STATE
import com.example.attendonb.utilites.MapUtls
import com.example.attendonb.utilites.Utilities
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.activity_login.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class LoginActivity : BaseActivity(), MapUtls.OnLocationUpdate {

    private var loginViewModel: LoginViewModel? = null
    private var mapUtls: MapUtls? = null
    private var curentLat: Double = 0.0
    private var curentLng: Double = 0.0
    private var isFromMockProvider: Boolean? = null
    private var isPermissionGranted: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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


        loginViewModel?.onNetWorkError()?.observe(this, Observer {
            if (it) {
                showToast(resources.getString(R.string.net_work_error))
            }
        })


        loginViewModel?.onUnKnownError()?.observe(this, Observer {
            if (it) {
                showToast(resources.getString(R.string.un_known_error))
            }

        })


         loginViewModel?.onLoginStateChanged()?.observe(this, Observer { loginState ->
            if (loginState) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        })

    }

    private fun initListeners() {
        btn_login.setOnClickListener {

            if (validateLoginInput() && isPermissionGranted ) {
                loginViewModel?.loginUser(userName = login_user_name.text.toString(), password = login_password.text.toString(), currentLat = curentLat, currentLng = curentLng, deviceImei = Utilities.getDeviceIMEI(baseContext), context = baseContext)
            }else{
                requestLoginPermeation()
            }

        }
    }


    private fun validateLoginInput(): Boolean {
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

//        if (!isPhoneStatePermeationGranted) {

            val builder1 = AlertDialog.Builder(this)
            builder1.setMessage(R.string.login_permutation_message)
            builder1.setCancelable(true)

            builder1.setPositiveButton(resources.getString(R.string.ok)) { dialog, id ->
                requestLoginPermeation()
                dialog.cancel()
            }

//        }


//        if (curentLat == null && curentLng == null) {
//
//            showToast("error gitting current location")
//            requestLoginPermeation()
//            return false
//        }

        return true

    }


    override fun onLocationUpdate(location: Location) {
        // New location has now been determined
//        val latLng = LatLng(location.latitude, location.longitude)
        curentLat = location.latitude
        curentLng = location.longitude
        isFromMockProvider = location.isFromMockProvider
        if (isFromMockProvider!!) {
            btn_login.isEnabled = false
            Toast.makeText(this, resources.getString(R.string.mock_location_warrning), Toast.LENGTH_LONG).show()
        } else {
            btn_login.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.text_input_color))
            btn_login.isEnabled = true
        }
        mapUtls?.removeLocationRequest()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOGIN_PERMATION)
    private fun requestLoginPermeation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA)) {
            mapUtls?.startLocationUpdates(this, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
            mapUtls = MapUtls(this)
            initObservers()
            isPermissionGranted=true
        } else {

            EasyPermissions.requestPermissions(this, getString(R.string.login_permutation_message), REQUEST_CODE_LOGIN_PERMATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA)

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
    }




}