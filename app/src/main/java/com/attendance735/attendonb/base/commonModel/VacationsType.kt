package com.attendance735.attendonb.base.commonModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class VacationsType() :Parcelable {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(name)
    }


    override fun describeContents(): Int {
        return 0
     }

    companion object CREATOR : Parcelable.Creator<VacationsType> {
        override fun createFromParcel(parcel: Parcel): VacationsType {
            return VacationsType(parcel)
        }

        override fun newArray(size: Int): Array<VacationsType?> {
            return arrayOfNulls(size)
        }
    }
}