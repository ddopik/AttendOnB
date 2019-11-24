package com.spidersholidays.attendonb.ui.vacation.newvacation.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
import com.spidersholidays.attendonb.base.commonModel.User
import java.util.*

class AutoCompleteMangersAdapter(val userList: MutableList<User>) : RecyclerView.Adapter<AutoCompleteMangersAdapter.MangerViewHolder>(), Filterable {

    lateinit var filteredUserList: MutableList<User>
    var onMangerSelected: OnMangerSelected?=null

    init {
        filteredUserList = userList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangerViewHolder {
        return MangerViewHolder(LayoutInflater.from(AttendOnBApp.app?.applicationContext).inflate(R.layout.view_holder_manger, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredUserList.size
    }

    override fun onBindViewHolder(holder: MangerViewHolder, position: Int) {


        holder.mangerName.text = filteredUserList[position].name


        onMangerSelected?.let {
            holder.mangerName.setOnClickListener {
                onMangerSelected?.onMangerClickListener(filteredUserList[position])
            }
        }


    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredUserList = userList
                } else {
                    val filteredList = ArrayList<User>()
                    for (row in userList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    filteredUserList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredUserList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filteredUserList = filterResults.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }


    class MangerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mangerName: TextView = view.findViewById(R.id.manger_name)
    }


    interface OnMangerSelected{
        fun onMangerClickListener(user:User?)
    }
}