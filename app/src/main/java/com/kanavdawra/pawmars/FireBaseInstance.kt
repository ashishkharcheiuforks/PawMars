package com.kanavdawra.pawmars

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class FireBaseInstance: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        var firebasetoken= FirebaseInstanceId.getInstance().token
    }
}