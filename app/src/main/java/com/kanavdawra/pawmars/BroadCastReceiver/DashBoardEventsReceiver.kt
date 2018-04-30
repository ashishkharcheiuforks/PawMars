package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoardContactsInterface
import com.kanavdawra.pawmars.InterFace.DashBoardEventsInterFace

/**
 * Created by Kanav on 4/27/2018.
 */
class DashBoardEventsReceiver(val dashBoardEventsInterFace: DashBoardEventsInterFace): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.getStringExtra("task")=="NotifyDataSetChanged"){
            dashBoardEventsInterFace.notifyDataSetChanged()
        }
    }

}