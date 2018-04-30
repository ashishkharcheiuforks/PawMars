package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoard
import com.kanavdawra.pawmars.InterFace.DashBoard_EventsInterFace

class DashBoard_EventsReceiver(val dashBoard_EventsInterFace: DashBoard_EventsInterFace):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.getStringExtra("task")=="EventViewPager"){
            dashBoard_EventsInterFace.fetchEventName(intent.getStringExtra("payload"))
        }
    }
}