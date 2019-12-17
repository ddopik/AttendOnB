package com.attendance735.attendonb.ui.vacation.newvacation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.attendance735.attendonb.R
import com.attendance735.attendonb.app.AttendOnBApp
import com.attendance735.attendonb.base.commonModel.User


class NewVacationSpinnerAdapter(val userList: MutableList<User>, layout: Int) : ArrayAdapter<User>(AttendOnBApp.app?.baseContext!!, 0, userList) {


    val TAG = NewVacationSpinnerAdapter::javaClass.name
    var onUserSpinnerItemClickListener: OnUserSpinnerItemClickListener? = null


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var itemViewHolder: UserSpinnerViewHolder? = null
        var itemView: View? = convertView
        if (itemView == null) {

            val mInflater = AttendOnBApp.app?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = mInflater.inflate(R.layout.view_holder_user_spinner, parent, false)!!
        }

        if (itemView.tag == null) {
            itemViewHolder = UserSpinnerViewHolder()
            itemViewHolder.userName = itemView.findViewById<TextView>(R.id.manger_id)
            itemView.tag = itemViewHolder

        }

        itemViewHolder?.userName?.text = userList[position].name
        onUserSpinnerItemClickListener?.let {
            itemViewHolder?.userName?.setOnClickListener {
                onUserSpinnerItemClickListener?.onUserItemClicked(userList[position])
            }
        }
        return itemView
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return userList.size
    }


    class UserSpinnerViewHolder {

        var userName: TextView? = null

    }

    interface OnUserSpinnerItemClickListener {
        fun onUserItemClicked(user: User)
    }


}