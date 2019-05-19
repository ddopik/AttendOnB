package com.example.attendonb.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.example.attendonb.R
import kotlinx.android.synthetic.main.custom_dialog.*


/**
 * Created by ddopik on 19,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class CustomDialog(val activity: Activity) : Dialog(activity) {


    var onCustomDialogPositiveClick: OnCustomDialogPositiveClick? = null
    var customDialogContent: String? = null

    companion object {

        fun getInstance(activity: Activity): CustomDialog {
            val customDialog = CustomDialog(activity)
            return customDialog;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState?: Bundle())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)
        initView()
        initListener()

    }

    fun initView() {
        custom_dialog_content.setText(customDialogContent);
    }

    fun initListener() {
        custom_dialog_btn.setOnClickListener {
            onCustomDialogPositiveClick?.onPositiveClicked()
        }
    }

    public interface OnCustomDialogPositiveClick {
        fun onPositiveClicked()
    }
}