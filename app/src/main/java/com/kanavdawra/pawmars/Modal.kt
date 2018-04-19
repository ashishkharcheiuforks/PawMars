package com.kanavdawra.pawmars

import android.content.Context
import com.kanavdawra.pawmars.DataBase.DataBase
import com.kanavdawra.pawmars.Modals.Contact
import com.kanavdawra.pawmars.Modals.ContactList
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Environment.getExternalStorageDirectory
import com.snatik.storage.Storage
import android.R.attr.path
import android.R.attr.path
import android.content.ContextWrapper
import android.widget.ImageView
import com.kanavdawra.pawmars.Constants.contactListName
import kotlinx.android.synthetic.main.fragment_dash_board_create_contact_list.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class Modal(val context: Context) {
    fun contactsToModal(): java.util.ArrayList<Contact> {

        val contacts = DataBase(context).writableDatabase.rawQuery("SELECT * FROM contacts ORDER BY name", null)
        val contactsList = ArrayList<Contact>()
        while (contacts.moveToNext()) {
            val contact = Contact()
            contact.name = contacts.getString(contacts.getColumnIndex("name"))
            contact.phoneNumber = contacts.getString(contacts.getColumnIndex("phno"))
            contact.email = contacts.getString(contacts.getColumnIndex("email"))
            contact.contact_id = contacts.getString(contacts.getColumnIndex("contact_id"))
            contact.photo = contacts.getInt(contacts.getColumnIndex("contact_id")) > 0
            println(contacts.getString(contacts.getColumnIndex("name")))
            contactsList.add(contact)
        }
        contacts.close()
        return contactsList
    }

    fun contactListToModal(): java.util.ArrayList<ContactList> {
        val contactListCursor = DataBase(context).writableDatabase.rawQuery("SELECT * FROM contactList", null)
        val storage = Storage(context.applicationContext)
        val contactListArray = ArrayList<ContactList>()
        while (contactListCursor.moveToNext()) {
            val contactList = ContactList()
            contactList.name = contactListCursor.getString(contactListCursor.getColumnIndex("name"))
            contactList.badge = context.getSharedPreferences(contactList.name, 0).all.size - 2
            val cw = ContextWrapper(context.applicationContext)

            val mypath = cw.getDir("ContactList", Context.MODE_PRIVATE)

            try {
                val f = File(mypath, "${contactList.name}.jpg")
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                contactList.dp = b
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                contactList.dp = null
            }




            contactListArray.add(contactList)

        }
        contactListCursor.close()
        return contactListArray
    }

    fun contactListContactstoModal(): java.util.ArrayList<Contact> {
        val contacts = contactsToModal()
        val contact_id=ArrayList<String>()
        val contactListDetails=ArrayList<Contact>()
        val lenght=context.getSharedPreferences(contactListName,0).all.size-2
        val shared=context.getSharedPreferences(contactListName,0)
        for(i in 1..lenght){
            contact_id.add(shared.getString("data$i","null"))
        }
        for(i in contacts){
            if(contact_id.contains(i.contact_id)){
                contactListDetails.add(i)
            }
        }
        return contactListDetails
    }
}