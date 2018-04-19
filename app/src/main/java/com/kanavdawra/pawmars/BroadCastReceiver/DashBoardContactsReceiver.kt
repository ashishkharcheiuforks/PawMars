package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoardContactsInterface

class DashBoardContactsReceiver(val dashBoardContactsInterface: DashBoardContactsInterface):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.getStringExtra("task")=="NotifyDataSetChanged"){
            dashBoardContactsInterface.notifyDataSetChanged()
        }
    }

}