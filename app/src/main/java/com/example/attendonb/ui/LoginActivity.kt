package com.example.attendonb.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.attendonb.R
 import androidx.fragment.app.FragmentActivity
import com.example.attendonb.base.BaseFragmentActivity
import com.example.attendonb.ui.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseFragmentActivity() {

    var loginViewModel:LoginViewModel ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel=LoginViewModel.getInstance(this)

     }

    override fun initObservers() {
        loginViewModel?.isDataLoading?.observe(this, Observer {

            if (it){
                login_progress.visibility= View.VISIBLE
            }else{
                login_progress.visibility= View.GONE
            }
        })


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