package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.ToolBarButtonsInterFace

/**
 * Created by Kanav on 4/2/2018.
 */
class DashBoardToolBarButtonReceiver(val toolBarButtonsInterFace: ToolBarButtonsInterFace) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getStringExtra("button") == "add") {
            toolBarButtonsInterFace.add()
        }
        if (intent.getStringExtra("button") == "cancel") {
            toolBarButtonsInterFace.cancel()
        }
        if (intent.getStringExtra("button") == "inviteesList") {
            toolBarButtonsInterFace.inviteesList()
        }
        if (intent.getStringExtra("button") == "selectEvent") {
            toolBarButtonsInterFace.selectEvent()
        }
        if (intent.getStringExtra("button") == "flash") {
            toolBarButtonsInterFace.flash()
        }
    }
}