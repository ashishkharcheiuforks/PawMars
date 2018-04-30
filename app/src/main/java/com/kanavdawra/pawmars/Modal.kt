package com.kanavdawra.pawmars

import android.content.Context
import com.kanavdawra.pawmars.DataBase.DataBase
import android.graphics.BitmapFactory
import com.snatik.storage.Storage
import android.content.ContextWrapper
import com.kanavdawra.pawmars.Constants.contactListName
import com.kanavdawra.pawmars.Modals.*
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

    fun contactListNameToModal(): java.util.ArrayList<ContactList> {
        val contactListCursor = DataBase(context).writableDatabase.rawQuery("SELECT * FROM contactList", null)
        val contactListArray = ArrayList<ContactList>()
        while (contactListCursor.moveToNext()) {
            val contactList = ContactList()
            contactList.name = contactListCursor.getString(contactListCursor.getColumnIndex("name"))
            contactList.badge = context.getSharedPreferences(contactList.name, 0).all.size - 2
            contactListArray.add(contactList)

        }
        contactListCursor.close()
        return contactListArray
    }

    fun contactListContactstoModal(): java.util.ArrayList<Contact> {
        val contacts = contactsToModal()
        val contact_id = ArrayList<String>()
        val contactListDetails = ArrayList<Contact>()
        val lenght = context.getSharedPreferences(contactListName, 0).all.size - 2
        val shared = context.getSharedPreferences(contactListName, 0)
        for (i in 1..lenght) {
            contact_id.add(shared.getString("data$i", "null"))
        }
        for (i in contacts) {
            if (contact_id.contains(i.contact_id)) {
                contactListDetails.add(i)
            }
        }
        return contactListDetails
    }

    fun addEventContactListContacts(selectedContacts: ArrayList<ContactList>): ArrayList<String> {
        val contactsID = ArrayList<String>()
        for (list in selectedContacts) {
            val lenght = context.getSharedPreferences(list.name, 0).all.size - 2
            val shared = context.getSharedPreferences(list.name, 0)
            for (i in 1..lenght) {
                if (shared.getString("data$i", "null") != "null") {
                    if (!contactsID.contains(shared.getString("data$i", "null"))) {
                        contactsID.add(shared.getString("data$i", "null"))
                    }
                }
            }
        }
        return contactsID

    }

    fun eventstoModal(): ArrayList<Event> {
        val events = ArrayList<Event>()
        val eventsCursor = DataBase(context).readableDatabase.rawQuery("SELECT * FROM eventList", null)
        while (eventsCursor.moveToNext()) {


            val eventName = eventsCursor.getString(eventsCursor.getColumnIndex("name"))
            val eventPref = context.getSharedPreferences("Event_$eventName", 0)
            val event = Event()



            event.eventName = eventPref.getString("EventName", "EventName")
            event.placeName = eventPref.getString("PlaceName", "PlaceName")
            event.addressLine1 = eventPref.getString("AddressLine1", "AddressLine1")
            event.addressLine2 = eventPref.getString("AddressLine2", "AddressLine2")
            event.city = eventPref.getString("City", "City")
            event.pinCode = eventPref.getString("PinCode", "123456")
            event.country = eventPref.getString("Country", "Country")
            event.year = eventPref.getInt("Year", 2018)
            event.month = eventPref.getInt("Month", 1)
            event.date = eventPref.getInt("Date", 1)
            event.day = eventPref.getString("Day", "Monday")
            event.hour = eventPref.getInt("Hour", 12)
            event.minute = eventPref.getInt("Minute", 55)
            event.am_pm = eventPref.getString("AM_PM", "AM")
            event.timeZone = eventPref.getString("TimeZone", "GMT +05:30")


            val lenght = eventPref.all.size - 20

            event.inviteesCount = lenght

            event.image = Utility(context).fetchUrl("Events", "Event_${event.eventName}_Image1")
//            try {
//                if (eventPref.getString("Image1", "null") != "null") {
//                    event.image = Utility(context).fetchImage("Events", "Event_${event.eventName}_Image1")
//                }
//            } catch (e: Exception) {
//            }


            events.add(event)


        }
        eventsCursor.close()
        return events
    }

    fun eventNamestoModal(eventName: String): ArrayList<ContactName> {
        val contactsNames = ArrayList<ContactName>()
        val shPref = context.getSharedPreferences("Event_$eventName", 0)



        for (i in 1..shPref.all.size - 20) {
            if (shPref.getString("data$i", "") != "") {
                val contactsName = ContactName()
                val contactsCursor = DataBase(context).writableDatabase.rawQuery("SELECT name FROM contacts WHERE contact_id='${shPref.getString("data$i", "")}'", null)
                contactsCursor.moveToFirst()
                contactsName.name = contactsCursor.getString(contactsCursor.getColumnIndex("name"))
                contactsNames.add(contactsName)
                contactsCursor.close()
            }
        }
        return contactsNames
    }

    fun eventContactstoMoadal(eventName: String): ArrayList<EventContact> {
        val shPref = context.getSharedPreferences("Event_$eventName", 0)
        val eventContacts = ArrayList<EventContact>()
        for (i in 1..shPref.all.size - 20) {
            if (shPref.getString("data$i", "") != "") {
                val eventContact = EventContact()
                val contactsCursor = DataBase(context).writableDatabase.rawQuery("SELECT * FROM contacts WHERE contact_id='${shPref.getString("data$i", "")}'", null)
                contactsCursor.moveToFirst()
                eventContact.name = contactsCursor.getString(contactsCursor.getColumnIndex("name"))
                eventContact.phoneNo = contactsCursor.getString(contactsCursor.getColumnIndex("phno"))
                eventContact.emailId = contactsCursor.getString(contactsCursor.getColumnIndex("email"))
                eventContacts.add(eventContact)
                contactsCursor.close()
            }
        }
        return eventContacts
    }
    fun parcelableEventContactstoMoadal(eventName: String): ArrayList<ParcelableEventContact> {
        val shPref = context.getSharedPreferences("Event_$eventName", 0)
        val eventContacts = ArrayList<ParcelableEventContact>()
        for (i in 1..shPref.all.size - 20) {
            if (shPref.getString("data$i", "") != "") {
                val eventContact = ParcelableEventContact()
                val contactsCursor = DataBase(context).writableDatabase.rawQuery("SELECT * FROM contacts WHERE contact_id='${shPref.getString("data$i", "")}'", null)
                contactsCursor.moveToFirst()
                eventContact.ParcelableEventContact(contactsCursor.getString(contactsCursor.getColumnIndex("name"))
                        ,contactsCursor.getString(contactsCursor.getColumnIndex("phno"))
                        ,contactsCursor.getString(contactsCursor.getColumnIndex("email")),0,0)
                eventContacts.add(eventContact)
                contactsCursor.close()
            }
        }
        return eventContacts
    }

}