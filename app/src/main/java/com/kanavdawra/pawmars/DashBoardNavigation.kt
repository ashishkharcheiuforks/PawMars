package com.kanavdawra.pawmars

import android.content.Context
import android.content.Intent

class DashBoardNavigation(val context: Context) {
    fun home(){
        val home= Intent("Navigation")
        home.putExtra("task","navigation")
        home.putExtra("navigation","home")
        context.sendBroadcast(home)
    }
    fun events(){
        val events= Intent("Navigation")
        events.putExtra("task","navigation")
        events.putExtra("navigation","events")
        context.sendBroadcast(events)
    }
    fun contacts(){
        val contacts= Intent("Navigation")
        contacts.putExtra("task","navigation")
        contacts.putExtra("navigation","contacts")
        context.sendBroadcast(contacts)
    }
    fun verify(){
        val verify= Intent("Navigation")
        verify.putExtra("task","navigation")
        verify.putExtra("navigation","verify")
        context.sendBroadcast(verify)
    }
    fun history(){
        val history= Intent("Navigation")
        history.putExtra("task","navigation")
        history.putExtra("navigation","history")
        context.sendBroadcast(history)
    }
//    fun logout(){
//        val logout= Intent("Navigation")
//        logout.putExtra("task","navigation")
//        logout.putExtra("navigation","logout")
//        context.sendBroadcast(logout)
//    }

}