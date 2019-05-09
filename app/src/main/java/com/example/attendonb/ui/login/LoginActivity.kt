package com.example.attendonb.ui.login

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.attendonb.utilites.Constants.Companion.REQUEST_CODE_LOCATION
import com.example.attendonb.utilites.MapUtls
import com.example.attendonb.utilites.Utilities
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.activity_login.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class LoginActivity : BaseActivity(), MapUtls.OnLocationUpdate {

    private var loginViewModel: LoginViewModel? = null
    private var mapUtls: MapUtls? = null
    private var curentLat: Double? = null
    private var curentLng: Double? = null
    private var isFromMockProvider: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = LoginViewModel.getInstance(this)
        mapUtls = MapUtls(this)

        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        requestLoginPermeation()
    }

    override fun initObservers() {
        loginViewModel?.isDataLoading?.observe(this, Observer {

            if (it) {
                login_progress.visibility = View.VISIBLE
            } else {
                login_progress.visibility = View.GONE
            }
        })


        loginViewModel?.isNetworkError?.observe(this, Observer {
            if (it) {
                showToast(resources.getString(R.string.net_work_error))
            }
        })


        loginViewModel?.isUnknownError?.observe(this, Observer {
            if (it) {
                showToast(resources.getString(R.string.un_known_error))
            }

        })

        loginViewModel?.loginErrorMessage?.observe(this, Observer {

            showToast(it)
        })

        loginViewModel?.loginState?.observe(this, Observer { loginState ->
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
            if (validateLoginInput()) {
                loginViewModel?.loginUser(userName = login_user_name.text.toString(), password = login_password.text.toString(), currentLat = curentLat!!, currentLng = curentLng!!, deviceImei = Utilities.getDeviceIMEI(baseContext), context = baseContext)
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


        if (curentLat == null && curentLng == null) {

            showToast("error gitting current location")
            requestLoginPermeation()
            return false
        }

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
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun requestLoginPermeation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mapUtls?.startLocationUpdates(this, MapUtls.MapConst.UPDATE_INTERVAL_INSTANT)
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, getString(R.string.need_location_permation), REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)


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






    /**
     * VM Consumer method 2
     * */
//        productDetailViewModel?.searchResult?.observe(this, Observer<Resource<SearchResponse>> { resource ->
//            if (resource != null) {
//                when (resource.status) {
//                    Resource.Status.SUCCESS -> {
//                        val product = resource.data;
//                        val products = product?.products;
//                        if (products != null) {
//                            if (!products.isEmpty()) {
//                                view?.populateProduct(products.first())
//                            }
//                        }
//                    }
//                    Resource.Status.ERROR->{
//                        Toast.makeText(this, "Error: "+resource.exception?.message, Toast.LENGTH_LONG);
//                    }
//                    Resource.Status.LOADING->{
//
//                    }
//                }
//            }
//        })
}