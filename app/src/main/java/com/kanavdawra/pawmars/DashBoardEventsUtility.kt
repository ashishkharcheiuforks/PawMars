package com.kanavdawra.pawmars

import android.content.Context
import android.content.Intent

class DashBoardEventsUtility(val context: Context) {
    fun notifyDataSetChanged(action:String) {
        context.sendBroadcast(Intent(action).putExtra("task", "NotifyDataSetChanged"))
    }
    fun pagerPositionChange(position: Int){
        context.sendBroadcast(Intent("EventViewPager").putExtra("task","positionChange").putExtra("position",position))
    }
    fun notifyDataSetChange(position: Int){
        context.sendBroadcast(Intent("EventViewPager").putExtra("task","notifyDataSetChange").putExtra("position",position))
    }
}