package com.kanavdawra.pawmars

import android.content.Context
import android.content.Intent
import com.kanavdawra.pawmars.DataBase.DataBase

/**
 * Created by Kanav on 4/11/2018.
 */
class DashBoardContactsUtility(val context: Context) {
    fun notifyDataSetChanged(action:String) {
        context.sendBroadcast(Intent(action).putExtra("task", "NotifyDataSetChanged"))
}
}