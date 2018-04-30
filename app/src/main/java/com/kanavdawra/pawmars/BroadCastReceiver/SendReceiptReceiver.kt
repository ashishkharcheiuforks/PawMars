package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.EventSendInterface

/**
 * Created by Kanav on 5/8/2018.
 */
class SendReceiptReceiver(val eventSendInterface: EventSendInterface):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.getStringExtra("task")=="SMS"){
            eventSendInterface.rowDataChanged("SMS",intent.getIntExtra("position",0),intent.getIntExtra("receipt",0))
        }
        if(intent.getStringExtra("task")=="Email"){
            eventSendInterface.rowDataChanged("Email",intent.getIntExtra("position",0),intent.getIntExtra("receipt",0))
        }
    }
}