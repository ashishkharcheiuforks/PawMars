package com.kanavdawra.pawmars.BroadCastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.InterFace.DashBoard
import com.kanavdawra.pawmars.R


class DashBoardReciever(val dashboard:DashBoard):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.getStringExtra("task")=="createPurple"){

            dashboard.createPurple(intent.getStringExtra("text"))
        }
        if(intent?.getStringExtra("task")=="dismissPurple"){
            dashboard.dismissPurple()
        }
        if(intent?.getStringExtra("task")=="snackBar"){
            dashboard.snackBar(intent.getStringExtra("message"),intent.getIntExtra("snackBarColor",R.color.Red),intent.getIntExtra("textColor",R.color.White) )
        }
        if(intent?.getStringExtra("task")=="updateEmailVerificationFragment"){
            dashboard.updateEmailVerificationFragment(intent.getIntExtra("layout",1))
        }

    }
}