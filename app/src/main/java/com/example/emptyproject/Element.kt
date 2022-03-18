package com.example.emptyproject

import android.os.Parcel
import android.os.Parcelable
import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

data class Element(var name: String = "", var value: Int = 0) : Externalizable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeExternal(out: ObjectOutput?) {
        if (out == null) return
        out.writeUTF(name)
        out.writeInt(value)
    }

    override fun readExternal(input: ObjectInput?) {
        if (input == null) return
        name = input.readUTF()
        value = input.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }
}