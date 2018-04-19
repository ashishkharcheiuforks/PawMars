package com.kanavdawra.pawmars.DataBase

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import com.google.firebase.database.FirebaseDatabase
import com.kanavdawra.pawmars.Constants.currUser
import android.content.ContentUris
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.developers.imagezipper.ImageZipper
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.io.File
import java.lang.Exception
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.kanavdawra.pawmars.Constants.fireBaseDataBase
import com.kanavdawra.pawmars.Constants.fireBaseDataBaseUsersData
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility


class PawMarsDataBase {
    fun contacts(context: Context) {
        Contacts(context).syncDataBase()

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


        fun exportContactList(name:String){

        }

        fun firstOfflineDataBase() {
            val dataBase = DataBase(context).writableDatabase
            val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null)


            while (phones.moveToNext()) {

                var name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val idPhone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

                val contact = ContentValues()
                val email = fetchEmail(idPhone)

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

}