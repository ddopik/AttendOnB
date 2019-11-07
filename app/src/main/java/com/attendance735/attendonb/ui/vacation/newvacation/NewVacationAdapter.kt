package com.attendance735.attendonb.ui.vacation.newvacation

import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.attendance735.attendonb.R
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.commonModel.User
import android.content.Context.LAYOUT_INFLATER_SERVICE
import androidx.core.content.ContextCompat.getSystemService




class NewVacationAdapter(val userList: MutableList<User>,layout:Int) : ArrayAdapter<User>(AttendOnBApp.app?.baseContext!!,0,userList) {


    var layoutInflater: LayoutInflater? = null
    val TAG = NewVacationAdapter::javaClass.name


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            Log.e(TAG,"getView() ---> new View inflated")

            layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
            return layoutInflater?.inflate(R.layout.view_holder_user_spinner, parent, false)!!
        }
        val userName = convertView.findViewById<TextView>(R.id.manger_id)
        userName.text = userList[position].name
        return convertView
    }

    //
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//         if (convertView == null) {
//            Log.e(TAG,"getView() ---> new View inflated")
//
//             layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
//            return layoutInflater?.inflate(R.layout.view_holder_user_spinner, parent, false)!!
//        }
//        val userName = convertView.findViewById<TextView>(R.id.manger_id)
//        userName.text = userList[position].name
//         return convertView
//    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
         if (convertView == null) {
            Log.e(TAG,"getDropDownView() ---> new View inflated")
            layoutInflater = LayoutInflater.from(AttendOnBApp.app?.applicationContext)
            return layoutInflater?.inflate(R.layout.view_holder_user_spinner, parent, false) as View
        }

        val userName = convertView.findViewById<TextView>(R.id.manger_id)
        userName.text = userList[position].name

        return convertView

    }













}