package com.spidersholidays.attendonb.ui.vacationmangment.model


data class VacationStatsChange(val id: String,val vacationStatsType:VacationStatsType,var requestStatus:String? ) {




    enum class VacationStatsType{
        APPROVED,REJECTED,PENDING
    }
}