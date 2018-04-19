package com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardAuthenticationSignUp

import android.app.Activity
import com.kanavdawra.pawmars.Constants.fireBaseDataBaseUsersID
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.inputmethod.InputMethodManager
import com.google.firebase.database.*
import com.kanavdawra.pawmars.Constants.currUser
import com.kanavdawra.pawmars.Constants.firebaseAuth
import com.kanavdawra.pawmars.InterFace.FireBaseSignUpInterFace
import com.kanavdawra.pawmars.Constants.emailVerificationSend
import com.kanavdawra.pawmars.Constants.fireBaseCurrentUser
import com.kanavdawra.pawmars.Constants.signupkeyboard
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.kanavdawra.pawmars.Constants.firebaseStorage
import android.net.Uri
import java.io.ByteArrayOutputStream
import com.google.firebase.auth.UserProfileChangeRequest
import android.provider.MediaStore.Images
import com.google.android.gms.tasks.OnCompleteListener
import com.kanavdawra.pawmars.Constants


class OnBoardAuthenticationSignUpFireBase {
    var sending = true
    var uploading = true
    var registering = true
    fun emailCheck(email: String, onBoardAuthenticationSignUpFireBaseInterface: FireBaseSignUpInterFace) {
        var check = true
        fireBaseDataBaseUsersID.child(email.replace('.', '_', false)).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot!!.value != null) {
                    check = false
                }
                onBoardAuthenticationSignUpFireBaseInterface.checkemail(check)
            }
        })
    }

    fun saveUser(firstName: String, lastName: String, email: String, password: String, userImage: Bitmap?, context: Context, activity: Activity) {


//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//
//                        val userName = email.replace('.', '_', false)
//                        val dataRef = fireBaseDataBaseUsers.child(userName).ref
//
//                        dataRef.child("Email").setValue(email)
//                        dataRef.child("FirstName").setValue(firstName)
//                        dataRef.child("LastName").setValue(lastName)
//                        dataRef.keepSynced(true)
//
//                        ImageStorage(userImage, userName,dataRef,context)
//
//                        fireBaseDataBaseUsersID.child(userName).setValue(email)
//                        Constants.email = email
//                        fireBaseCurrentUser = currUser()
//                        fireBaseCurrentUser!!.updateProfile()
//                        fireBaseCurrentUser?.sendEmailVerification()!!
//                                .addOnCompleteListener {
//
//                                    emailVerificationSend = when {
//                                        it.isSuccessful -> true
//                                        else -> false
//                                    }
//
//                                    val intent = Intent("AppStartActivity")
//                                    println("page 5 firebase")
//                                    intent.putExtra("task", "inflate")
//                                    intent.putExtra("position", 10)
//                                    if (signupkeyboard) {
//                                        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//                                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
//                                    }
//                                    context.sendBroadcast(intent)
//
//                                }
//                    } else {
//
//                        val authSnackbar = Intent("AppStartActivity")
//                        authSnackbar.putExtra("task", "snackbar")
//                        authSnackbar.putExtra("message", "Authentication failed. Please try again")
//                        context.sendBroadcast(authSnackbar)
//                    }
//
//                    val dismissLoader = Intent("AppStartActivity")
//                    dismissLoader.putExtra("task", "loading")
//                    context.sendBroadcast(dismissLoader)
//
//                }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        registering = false
                        loader(context)
                        val userName = email.replace('.', '_', false)
                        fireBaseDataBaseUsersID.child(userName).setValue(email)

                        fireBaseCurrentUser = currUser()
                        var path: String? = null
                        val profileUpdates: UserProfileChangeRequest
                        println(userImage)
                        if (userImage != null) {
                            val bytes = ByteArrayOutputStream()
                            userImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                            path = Images.Media.insertImage(context.contentResolver, userImage, "Title", null)
                            profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + lastName)
                                    .setPhotoUri(Uri.parse(path))
                                    .build()
                        } else {
                            profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + lastName)
                                    .build()
                        }

                        fireBaseCurrentUser?.updateProfile(profileUpdates)!!
                                .addOnCompleteListener(OnCompleteListener<Void> { task ->
                                    if (task.isSuccessful) {
                                        uploading = false
                                        loader(context)
                                    }
                                })

                        fireBaseCurrentUser?.sendEmailVerification()!!
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        emailVerificationSend = true
                                        sending = false
                                        loader(context)
                                    } else {
                                        emailVerificationSend = false
                                    }

                                    val intent = Intent("AppStartActivity")
                                    intent.putExtra("task", "inflate")
                                    intent.putExtra("position", 10)
                                    Constants.email = email
                                    if (signupkeyboard) {
                                        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
                                    }
                                    context.sendBroadcast(intent)

                                }
                    } else {

                        val authSnackbar = Intent("AppStartActivity")
                        authSnackbar.putExtra("task", "snackbar")
                        authSnackbar.putExtra("message", "Authentication failed. Please try again")
                        context.sendBroadcast(authSnackbar)
                        val dismissloader = Intent("AppStartActivity")
                        dismissloader.putExtra("task", "loading")
                        context.sendBroadcast(dismissloader)
                    }


                }


    }

    fun loader(context: Context) {
        if (!registering) {
            var createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 12)
            context.sendBroadcast(createLoader)
            if (!uploading) {
                createLoader = Intent("AppStartActivity")
                createLoader.putExtra("task", "loading")
                createLoader.putExtra("position", 15)
                context.sendBroadcast(createLoader)
                if (!sending) {
                    val dismissloader = Intent("AppStartActivity")
                    dismissloader.putExtra("task", "loading")
                    context.sendBroadcast(dismissloader)
                }
            }
        }
    }

    fun ImageStorage(userImage: Bitmap, userName: String, dataRef: DatabaseReference, context: Context) {
        val baos = ByteArrayOutputStream()
        userImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val ref = firebaseStorage.child("Users").child(userName + "/" + "user_id_image.jpg")
        val uploadTask = ref.putBytes(data)

        /////////////////////////////////////////////////////////////////////////Loader

        val createLoader = Intent("AppStartActivity")
        createLoader.putExtra("task", "loading")
        createLoader.putExtra("position", 1)
        context.sendBroadcast(createLoader)

        /////////////////////////////////////////////////////////////////////////Upload

        uploadTask.addOnFailureListener(OnFailureListener {
            val authSnackbar = Intent("AppStartActivity")
            authSnackbar.putExtra("task", "snackbar")
            authSnackbar.putExtra("message", "Uploading image failed. Please reUpload after LogIn")
            context.sendBroadcast(authSnackbar)
        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->

            /////////////////////////////////////////////////////////////////////Dismiss Loader
            val dismissLoader = Intent("AppStartActivity")
            dismissLoader.putExtra("task", "loading")
            context.sendBroadcast(dismissLoader)
            val downloadUrl = taskSnapshot.downloadUrl
        })
    }
}