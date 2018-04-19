package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.AppStartInterFace

class appStartReciever(private val appStartInterFace: AppStartInterFace):BroadcastReceiver() {
    override fun onReceive(context: Context,intent: Intent) {
        if(intent.getStringExtra("task").toString()=="inflate")
        { println("page 5 reciver")
            appStartInterFace.inflate(intent.getIntExtra("position",0))
        }
        if(intent.getStringExtra("task").toString()=="loading"){
            appStartInterFace.inflate(intent.getIntExtra("position",0))
        }
        if(intent.getStringExtra("task").toString()=="snackbar"){
            appStartInterFace.snackBar(intent.getStringExtra("message"))
        }
    }
}