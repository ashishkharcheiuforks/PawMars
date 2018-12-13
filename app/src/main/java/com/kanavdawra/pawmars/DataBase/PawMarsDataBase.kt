package com.kanavdawra.pawmars.DataBase

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.developers.imagezipper.ImageZipper
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.kanavdawra.pawmars.Constants.currUser
import com.kanavdawra.pawmars.Constants.smsTaskInterFace
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.Modals.EventContact
import com.kanavdawra.pawmars.Utility
import needle.Needle
import java.io.File
import java.lang.Exception


class PawMarsDataBase {
    fun contacts(context: Context) {
        Contacts(context).syncDataBase()
    }

    fun Events(context: Context, eventName: String): Boolean {
        return Event(context, eventName).uploadEventtoFireBase()
    }

    class Contacts(val context: Context) {

        fun syncDataBase() {

            if (context.getSharedPreferences("Main", 0).getBoolean("firstSyncCheck", false)) {
                //contactsSnapShot()
            }
            if (!context.getSharedPreferences("Main", 0).getBoolean("firstSyncCheck", false)) {
                firstOfflineDataBase()
                context.getSharedPreferences("Main", 0).edit().putBoolean("firstSyncCheck", true).apply()
            }

        }


        fun exportContactList(name: String) {

        }

        fun firstOfflineDataBase() {
            val dataBase = DataBase(context).writableDatabase
            val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null)


            while (phones.moveToNext()) {

                var name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val idPhone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

                val contact = ContentValues()
                val email = fetchEmail(idPhone)
                phoneNumber = phoneNumber.replace(" ", "")
                phoneNumber = phoneNumber.replace("-", "")

                name = name.capitalize()
                contact.put("name", name)
                contact.put("phno", phoneNumber)
                contact.put("email", email)
                contact.put("photo", checkPhoto(idPhone))
                contact.put("sync", 0)
                contact.put("contact_id", idPhone)

                val id = DataBase(context).writableDatabase.rawQuery("SELECT _id FROM contacts WHERE contact_id=$idPhone", null)
                if (id.count == 0) {
                    dataBase.insert("contacts", null, contact)
                }
                id.close()

            }
            phones.close()
            DashBoardUtility().dismiss(context)


        }

        fun firebaseQuery(name: String, phoneNumber: String, email: String, id: String) {

            val query = FirebaseDatabase.getInstance().reference.child("UsersData").child(currUser().uid).child("Contacts").child(id).ref
            query.child("Name").setValue(name)
            query.child("PhoneNumber").setValue(phoneNumber)
            query.child("Email").setValue(email)
            DataBase(context).writableDatabase.rawQuery("UPDATE contacts SET sync=1 WHERE contact_id=$id", null)

        }

        fun fetchEmail(idPhone: String): String {
            val emails = context.contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null, null, null, null)

            while (emails.moveToNext()) {
                val idEmail = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID))
                val email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                if (idPhone == idEmail) {
                    return email
                }
            }
            emails.close()
            return ""
        }

        fun checkPhoto(idPhone: String): String {
            val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idPhone.toLong())
            val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
            val image = File(photoUri.path)
            try {
                val BitmapImage = ImageZipper(context).compressToBitmap(image)
            } catch (e: Exception) {
                return "false"
            }
            return "true"
        }

        fun fetchPhoto(idPhone: String) {
            val storageRef = FirebaseStorage.getInstance().reference
            val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idPhone.toLong())
            val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
            val riversRef = storageRef.child("${currUser().uid}/Contacts").child(idPhone)
            try {
                riversRef.putFile(photoUri)
            } catch (e: Exception) {
                println(idPhone + e)
            }
        }
    }

    class Event(val context: Context, val eventName: String) {
        var image1 = ""
        var image2 = ""
        var image3 = ""
        var image4 = ""
        var push = ""
        fun uploadEventtoFireBase(): Boolean {

            val eventContacts = checkCountryCode()
            createEventID()
            val contacts = FirebaseDatabase.getInstance().reference.child("Events").child(push).child("Contacts").ref
            uploadToFireBase(eventContacts, contacts)
            uploadDetails()
            uploadImagesToFireBase(eventName)
            return eventContacts.size != 0
        }

        fun createEventID() {
            val link = FirebaseDatabase.getInstance().reference.child("Events").push().ref
            push = link.key
            context.getSharedPreferences("Event_${eventName}_Details", 0).edit().putString("ID", push).apply()
            context.getSharedPreferences("Event_${eventName}_Details", 0).edit().putString("Link", "http://www.kanavdawra.com/?id=$push").apply()
            val message = ArrayList<String>()
            message.add("http://www.kanavdawra.com/?id=$push&n=1")
            //6smsTaskInterFace!!.string(message)
        }

        fun uploadToFireBase(eventContacts: ArrayList<EventContact>, contacts: DatabaseReference) {
            var i = 0
            for (eventContact in eventContacts) {
                fireBaseQuery(eventContact, contacts, i)
                i++
            }
        }

        fun uploadDetails() {
            val shPref = context.getSharedPreferences("Event_$eventName", 0)
            val EventName = shPref.getString("EventName", "")
            val Name = shPref.getString("Name", "")
            val PlaceName = shPref.getString("PlaceName", "")
            val AddressLine1 = shPref.getString("AddressLine1", "")
            val AddressLine2 = shPref.getString("AddressLine2", "")
            val City = shPref.getString("City", "")
            val PinCode = shPref.getString("PinCode", "")
            val Country = shPref.getString("Country", "")
            val Description = shPref.getString("Description", "")
            val Year = shPref.getInt("Year", 2018)
            val Month = shPref.getInt("Month", 1)
            val Date = shPref.getInt("Date", 1)
            val Day = shPref.getString("Day", "Mon")
            val Hour = shPref.getInt("Hour", 12)
            val Minute = shPref.getInt("Minute", 10)
            val AM_PM = shPref.getString("AM_PM", "AM")
            val TimeZone = shPref.getString("TimeZone", "GMT 05:30")
            val Details = FirebaseDatabase.getInstance().reference.child("Events").child(push).child("EventDetails").ref
            Details.child("EventName").setValue(EventName)
            Details.child("Name").setValue(Name)
            Details.child("PlaceName").setValue(PlaceName)
            Details.child("AddressLine1").setValue(AddressLine1)
            Details.child("AddressLine2").setValue(AddressLine2)
            Details.child("City").setValue(City)
            Details.child("Country").setValue(Country)
            Details.child("PinCode").setValue(PinCode)
            Details.child("Description").setValue(Description)
            Details.child("Year").setValue(Year)
            Details.child("Month").setValue(Month)
            Details.child("Date").setValue(Date)
            Details.child("Day").setValue(Day)
            Details.child("Hour").setValue(Hour)
            Details.child("Minute").setValue(Minute)
            Details.child("AM_PM").setValue(AM_PM)
            Details.child("TimeZone").setValue(TimeZone)
            FirebaseDatabase.getInstance().reference.child("UsersData").child(currUser().uid).child("Events").child(eventName).setValue(push)
        }

        fun uploadImagesToFireBase(eventName: String) {
            val shPref = context.getSharedPreferences("Event_$eventName", 0)
            Needle.onBackgroundThread().withThreadPoolSize(10).execute {
                if (shPref.getString("Image1", "") != "null") {
                    uploadImageQuery("Image1", Utility(context).fetchUrl("Events", "Event_${eventName}_Image1"), 1)
                } else {
                    image1 = "null"
                    uploadImageUrl()
                }
            }
            Needle.onBackgroundThread().withThreadPoolSize(10).execute {
                if (shPref.getString("Image2", "") != "null") {
                    uploadImageQuery("Image2", Utility(context).fetchUrl("Events", "Event_${eventName}_Image2"), 2)
                } else {
                    image2 = "null"
                    uploadImageUrl()
                }
            }

            Needle.onBackgroundThread().withThreadPoolSize(10).execute {
                if (shPref.getString("Image3", "") != "null") {
                    uploadImageQuery("Image3", Utility(context).fetchUrl("Events", "Event_${eventName}_Image3"), 3)
                } else {
                    image3 = "null"
                    uploadImageUrl()
                }
            }
            Needle.onBackgroundThread().withThreadPoolSize(10).execute {
                if (shPref.getString("Image4", "") != "null") {
                    uploadImageQuery("Image4", Utility(context).fetchUrl("Events", "Event_${eventName}_Image4"), 4)
                } else {
                    image4 = "null"
                    uploadImageUrl()
                }
            }

        }

        fun uploadImageUrl() {
            if (image1 != "" && image2 != "" && image3 != "" && image4 != "") {
                val images = FirebaseDatabase.getInstance().reference.child("Events").child(push).child("EventDetails").child("Images").ref
                images.child("Image1").setValue(image1)
                images.child("Image2").setValue(image2)
                images.child("Image3").setValue(image3)
                images.child("Image4").setValue(image4)
            }
        }

        fun uploadImageQuery(name: String, path: String, imageID: Int) {
            val storageRef = FirebaseStorage.getInstance().reference

            val ImageRef = storageRef.child("$name.jpg")

            val EventImagesRef = storageRef.child("${currUser().uid}/Events/$eventName/$name")

//            ImageRef.getName() == EventImagesRef.getName()    // true
//            ImageRef.getPath() == EventImagesRef.getPath()


            val file = Uri.fromFile(File(path))
            val uploadTask = EventImagesRef.putFile(file)

            uploadTask.addOnFailureListener(OnFailureListener {
            }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                val downloadUrl = taskSnapshot.downloadUrl
                if (imageID == 1) {
                    image1 = downloadUrl.toString()
                }
                if (imageID == 2) {
                    image2 = downloadUrl.toString()
                }
                if (imageID == 3) {
                    image3 = downloadUrl.toString()
                }
                if (imageID == 4) {
                    image4 = downloadUrl.toString()
                }
                uploadImageUrl()
            })
        }

        fun fireBaseQuery(eventContact: EventContact, contacts: DatabaseReference, i: Int) {
            val contact = contacts.child(i.toString()).push().ref
            contact.child("Name").setValue(eventContact.name)
            contact.child("PhoneNumber").setValue(eventContact.phoneNo)
            contact.child("EmailAddress").setValue(eventContact.emailId)
        }

        fun checkCountryCode(): ArrayList<EventContact> {

            val eventContacts = ArrayList<EventContact>()
            val shPref = context.getSharedPreferences("Event_$eventName", 0)
            for (i in 1..shPref.all.size - 20) {
                val eventContact = EventContact()
                val cursor = DataBase(context).readableDatabase.rawQuery("SELECT * FROM contacts WHERE contact_id='${shPref.getString("data$i", "")}'", null)
                cursor.moveToFirst()
                println(cursor.count)
                eventContact.name = cursor.getString(cursor.getColumnIndex("name"))
                eventContact.phoneNo = cursor.getString(cursor.getColumnIndex("phno"))
                eventContact.emailId = cursor.getString(cursor.getColumnIndex("email"))
                if (eventContact.phoneNo[0].toString() != "+") {
                    eventContacts.clear()
                    return eventContacts
                }
                cursor.close()
                eventContacts.add(eventContact)

            }
            return eventContacts
        }
    }
}