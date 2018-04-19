package com.kanavdawra.pawmars.DashBoard

import android.content.Context
import android.content.Intent

class DashBoardUtility {
    fun create(context: Context,text:String){
        val loader=Intent("DashBoard")
        loader.putExtra("task","createPurple")
        loader.putExtra("text",text)
        context.sendBroadcast(loader)
    }
    fun dismiss(context: Context){
        val loader=Intent("DashBoard")
        loader.putExtra("task","dismissPurple")
        context.sendBroadcast(loader)
    }
    fun snackBar(context: Context,message:String,snackBarColor:Int,textColor:Int){
        val snackbar=Intent("DashBoard")
        snackbar.putExtra("task","snackBar")
        snackbar.putExtra("message",message)
        snackbar.putExtra("snackBarColor",snackBarColor)
        snackbar.putExtra("textColor",textColor)
        context.sendBroadcast(snackbar)
    }
    fun updateEmailVerificationFragment(context: Context,layout:Int){
        val loader=Intent("DashBoard")
        loader.putExtra("task","updateEmailVerificationFragment")
        loader.putExtra("layout",layout)
        context.sendBroadcast(loader)
    }
}