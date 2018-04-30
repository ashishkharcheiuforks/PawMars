package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoardNavigationInterFace

class DashBoardNavigationReciever(val navigation: DashBoardNavigationInterFace) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getStringExtra("task") == "navigation") {
            if (intent.getStringExtra("navigation") == "home") {
                navigation.Home()
            }
            if (intent.getStringExtra("navigation") == "contacts") {
                navigation.Contacts()
            }
            if (intent.getStringExtra("navigation") == "events") {
                navigation.Events()
            }
            if (intent.getStringExtra("navigation") == "verify") {
                navigation.Verify()
            }
            if (intent.getStringExtra("navigation") == "history") {
                navigation.History()
            }
        }

    }
}