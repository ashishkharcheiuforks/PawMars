package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.EventViewPagerInterFace

class EventViewPagerReceiver(val eventViewPagerInterFace: EventViewPagerInterFace):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.getStringExtra("task")=="positionChange"){
            eventViewPagerInterFace.pageChange(intent.getIntExtra("position",0))
        }
        if(intent.getStringExtra("task")=="notifyDataSetChange"){
            eventViewPagerInterFace.notifyDataSetChanged(intent.getIntExtra("position",0))
        }
    }
}