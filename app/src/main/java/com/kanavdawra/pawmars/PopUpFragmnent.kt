package com.kanavdawra.pawmars

import android.content.Context
import android.content.Intent

/**
 * Created by Kanav on 4/4/2018.
 */
class PopUpFragmnent(val context: Context) {
    fun create(fragment: String){
        context.sendBroadcast(Intent("PopUp").putExtra("task","create").putExtra("fragment",fragment))
    }
    fun dismiss(fragment: String){
        context.sendBroadcast(Intent("PopUp").putExtra("task","dismiss").putExtra("fragment",fragment))
    }
}