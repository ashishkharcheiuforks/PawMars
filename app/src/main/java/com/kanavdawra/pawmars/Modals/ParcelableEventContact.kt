package com.kanavdawra.pawmars.Modals

import android.os.Parcel
import android.os.Parcelable

class ParcelableEventContact() : Parcelable {
    var name = ""
    var phoneNo = ""
    var emailId = ""
    var phnoBool = 0
    var emailBool = 0
    var entryPass=""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        phoneNo = parcel.readString()
        emailId = parcel.readString()
        phnoBool = parcel.readInt()
        emailBool = parcel.readInt()
        entryPass=parcel.readString()
    }

    fun ParcelableEventContact(name: String, phoneNo: String, emailId: String, phnoBool: Int, emailBool: Int) {
        this.name = name
        this.phoneNo=phoneNo
        this.emailId=emailId
        this.phnoBool=phnoBool
        this.emailBool=emailBool
        this.entryPass=entryPass
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phoneNo)
        parcel.writeString(emailId)
        parcel.writeInt(phnoBool)
        parcel.writeInt(emailBool)
        parcel.writeString(entryPass)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableEventContact> {
        override fun createFromParcel(parcel: Parcel): ParcelableEventContact {
            return ParcelableEventContact(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableEventContact?> {
            return ParcelableEventContact.newArray(size)
        }
    }
}