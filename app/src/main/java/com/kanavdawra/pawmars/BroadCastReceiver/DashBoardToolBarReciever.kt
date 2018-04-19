package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoardToolBarInterface

class DashBoardToolBarReciever(val dashBoardToolBarInterface:DashBoardToolBarInterface):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.getStringExtra("toolbar")=="contacts")
        {
            dashBoardToolBarInterface.contacts()
        }
        if(intent.getStringExtra("toolbar")=="clean")
        {
            dashBoardToolBarInterface.clean()
        }
        if(intent.getStringExtra("toolbar")=="addCancel")
        {
            dashBoardToolBarInterface.addCancel()
        }
    }
}