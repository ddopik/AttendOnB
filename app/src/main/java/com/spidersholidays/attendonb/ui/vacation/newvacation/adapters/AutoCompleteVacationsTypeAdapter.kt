package com.spidersholidays.attendonb.ui.vacation.newvacation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spidersholidays.attendonb.R
import com.spidersholidays.attendonb.app.AttendOnBApp
 import com.spidersholidays.attendonb.base.commonModel.VacationsType
import java.util.ArrayList

class AutoCompleteVacationsTypeAdapter (val vacationTypeList: MutableList<VacationsType>) : RecyclerView.Adapter<AutoCompleteVacationsTypeAdapter.VacationTypeViewHolder>(), Filterable {

    lateinit var filteredVacationList: MutableList<VacationsType>
    var onVacationSelected: OnVacationSelected?= null

    init {
        filteredVacationList = vacationTypeList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationTypeViewHolder {
        return VacationTypeViewHolder(LayoutInflater.from(AttendOnBApp.app?.applicationContext).inflate(R.layout.view_holder_vacation_type, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredVacationList.size
    }

    override fun onBindViewHolder(holder: VacationTypeViewHolder, position: Int) {


        holder.vacation_type.text = filteredVacationList[position].name


        onVacationSelected?.let {
            holder.vacation_type.setOnClickListener {
                onVacationSelected?.onVacationClickListener(filteredVacationList[position])
            }
        }


    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredVacationList = vacationTypeList
                } else {
                    val filteredList = ArrayList<VacationsType>()
                    for (row in vacationTypeList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    filteredVacationList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredVacationList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filteredVacationList = filterResults.values as ArrayList<VacationsType>
                notifyDataSetChanged()
            }
        }
    }


    class VacationTypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var vacation_type: TextView = view.findViewById(R.id.vacation_type)
    }


    interface OnVacationSelected{
        fun onVacationClickListener(vacationsType: VacationsType?)
    }


}