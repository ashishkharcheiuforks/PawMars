package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoardNavigationInterFace

class DashBoardNavigationReciever(val navigation: DashBoardNavigationInterFace) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getStringExtra("task") == "navigation") {
            if (intent.getStringExtra("navigation") == "home") {
                navigation.home()
            }
            if (intent.getStringExtra("navigation") == "contacts") {
                navigation.contacts()
            }
            if (intent.getStringExtra("navigation") == "events") {
                navigation.events()
            }
            if (intent.getStringExtra("navigation") == "verify") {
                navigation.verify()
            }
            if (intent.getStringExtra("navigation") == "history") {
                navigation.history()
            }
        }

    }
}