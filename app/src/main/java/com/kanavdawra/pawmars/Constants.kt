package com.kanavdawra.pawmars

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.kanavdawra.pawmars.BroadCastReceiver.EventViewPagerReceiver
import com.kanavdawra.pawmars.BroadCastReceiver.SendReceiptReceiver
import com.kanavdawra.pawmars.Modals.Contact


object Constants {
    var splashTimeOut: Long = 0
    val fireBaseDataBase = FirebaseDatabase.getInstance().reference
    val fireBaseDataBaseUsersData = FirebaseDatabase.getInstance().reference.child("UsersData").ref
    val fireBaseDataBaseUsersID = FirebaseDatabase.getInstance().reference.child("UsersID").ref
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance().reference
    var fireBaseCurrentUser = firebaseAuth.currentUser
    var emailVerificationSend = true
    var email = ""
    var signupkeyboard = true
    var signedIn = false
    var logoutButton = false
    var selectedcontact = "1234"
    var selectedContacts = ArrayList<Contact>()
    var popUpName = "popup"
    var doubleBackPress = 2
    var contactListName = ""
    var eventName = ""
    var eventViewPagerReceiver: EventViewPagerReceiver? = null
    var sendReceiptReceiver: SendReceiptReceiver?=null
    fun currUser(): FirebaseUser {
        return firebaseAuth.currentUser!!
    }

}