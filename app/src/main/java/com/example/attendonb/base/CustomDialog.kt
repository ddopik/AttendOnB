package com.example.attendonb.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.example.attendonb.R
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_dialog.custom_dialog_content
import kotlinx.android.synthetic.main.custom_dialog_2.*


/**
 * Created by ddopik on 19,May,2019
 * ddopik.01@gmail.com
 * brandeda.net company,
 * cairo, Egypt.
 */
class CustomDialog(val activity: Activity) : Dialog(activity) {


    var  dialogOption:DialogOption? = null
    var onCustomDialogPositiveClick: OnCustomDialogPositiveClick? = null
    var customDialogContent: String? = null

    companion object {

        fun getInstance(activity: Activity,dialogOption:DialogOption ): CustomDialog {
            val customDialog = CustomDialog(activity)
            customDialog.dialogOption=dialogOption;
            return customDialog;
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState?: Bundle())
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        when (dialogOption){
            DialogOption.OPTION_1 ->{
                setContentView(R.layout.custom_dialog)
            }
            DialogOption.OPTION_2 ->{
                setContentView(R.layout.custom_dialog_2)
            }
        }

        initView()
        initListener()

    }

    fun initView() {
        custom_dialog_content.setText(customDialogContent);
    }

    fun initListener() {
        custom_dialog_btn?.setOnClickListener {
            onCustomDialogPositiveClick?.onPositiveClicked()
        }
        custom_dialog_yes_btn?.setOnClickListener {
            onCustomDialogPositiveClick?.onPositiveClicked()
        }

        custom_dialog_no_btn?.setOnClickListener {
            onCustomDialogPositiveClick?.onNectiveClicked()
        }
    }

     interface OnCustomDialogPositiveClick {
        fun onPositiveClicked()
         fun onNectiveClicked();
    }

    enum class DialogOption{
        OPTION_1,
        OPTION_2
    }
}