package com.spidersattend.attendonb.ui.home.mainstate.stateconfirmdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.spidersattend.attendonb.R
import com.spidersattend.attendonb.utilites.Constants.Companion.ENDED
import com.spidersattend.attendonb.utilites.Constants.Companion.OUT

class StateConfirmDialog :DialogFragment() {

    var mainView:View ?=null
    private var dialogMessage=""
    private var dialogState :String ?=null

    companion object {

        fun getInstance(message:String,dialogState:String): StateConfirmDialog {
            val stateConfirmDialog = StateConfirmDialog()
            stateConfirmDialog.dialogMessage=message
            stateConfirmDialog.dialogState=dialogState
            return  stateConfirmDialog
        }


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainView= layoutInflater.inflate(R.layout.dialog_confrim_state,container,false)

       dialogState?.let {
           when (it){

               OUT -> mainView?.findViewById<TextView>(R.id.confirm_message)?.text=resources.getString(R.string.welcome)+" : "+ dialogMessage
               ENDED ->{
                   mainView?.findViewById<TextView>(R.id.confirm_message)?.text=resources.getString(R.string.by_by)
               }
           }
       }
        return mainView
    }
}