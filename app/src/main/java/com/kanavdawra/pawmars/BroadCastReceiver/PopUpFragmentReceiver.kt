package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.PopUpPage

/**
 * Created by Kanav on 4/4/2018.
 */
class PopUpFragmentReceiver(val popUpPage: PopUpPage):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.getStringExtra("task")=="create"){
            popUpPage.create(intent.getStringExtra("fragment"))
        }
        else if(intent.getStringExtra("task")=="dismiss")
        {
            popUpPage.dismiss(intent.getStringExtra("fragment"))
        }
    }
}