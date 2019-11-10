package com.spidersholidays.attendonb.ui.vacation.newvacation

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.NonNull
 import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.base.commonModel.User


class CustomAdapter(@param:NonNull internal var mContext: Context, internal var spinnerTitles: MutableList<User>) : ArrayAdapter<String>(mContext, R.layout.view_holder_user_spinner) {

//    override fun getDropDownView(position: Int, @Nullable convertView: View, @NonNull parent: ViewGroup): View {
//        return getView(position, convertView, parent)
//    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return spinnerTitles.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
         var mViewHolder:ViewHolder ? = null
        if (convertView == null) {
            val mInflater = mContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val newConvertView = mInflater.inflate(R.layout.view_holder_user_spinner, parent, false)
             mViewHolder = ViewHolder()
            mViewHolder.mName = newConvertView.findViewById(R.id.manger_id)
            newConvertView.tag = mViewHolder
            mViewHolder.mName?.text = spinnerTitles[position].name
            return  newConvertView
        } else {
            mViewHolder = convertView.tag as ViewHolder
        }

        mViewHolder.mName!!.text = spinnerTitles[position].name
        return convertView
    }

    private class ViewHolder {
        internal var mName: TextView? = null
    }
}