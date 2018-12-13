package com.kanavdawra.pawmars.DashBoard

import android.content.Context
import android.content.Intent

class DashBoardUtility {
    fun create(context: Context, text: String) {
        val loader = Intent("DashBoard")
        loader.putExtra("task", "createPurple")
        loader.putExtra("text", text)
        context.sendBroadcast(loader)
    }

    fun dismiss(context: Context) {
        val loader = Intent("DashBoard")
        loader.putExtra("task", "dismissPurple")
        context.sendBroadcast(loader)
    }

    fun snackBar(context: Context, message: String, snackBarColor: Int, textColor: Int) {
        val snackbar = Intent("DashBoard")
        snackbar.putExtra("task", "snackBar")
        snackbar.putExtra("message", message)
        snackbar.putExtra("snackBarColor", snackBarColor)
        snackbar.putExtra("textColor", textColor)
        context.sendBroadcast(snackbar)
    }

    fun updateEmailVerificationFragment(context: Context, layout: Int) {
        val loader = Intent("DashBoard")
        loader.putExtra("task", "updateEmailVerificationFragment")
        loader.putExtra("layout", layout)
        context.sendBroadcast(loader)
    }

    fun toggleToolBar(context: Context) {
        val toggle = Intent("DashBoard")
        toggle.putExtra("task", "toggleToolBar")
        context.sendBroadcast(toggle)
    }

    fun toolBarColorchange(context: Context, color: Int?) {
        val toggle = Intent("DashBoard")
        toggle.putExtra("task", "toolBarColorChange")
        if (color != null) {
            toggle.putExtra("color", color)

        }
        context.sendBroadcast(toggle)
    }

    fun sendMessage(context: Context, message: String, resourceId: Int?) {
        val messageIntent = Intent("DashBoard")
        messageIntent.putExtra("task", "sendMessage")
        messageIntent.putExtra("message", message)
        if (resourceId == null) {
            messageIntent.putExtra("resourceId", -1)
        } else {
            messageIntent.putExtra("resourceId", resourceId)
        }
        context.sendBroadcast(messageIntent)
    }

    fun changeStatusBarColor(context: Context, color: Int?) {
        val colorIntent = Intent("DashBoard")
        colorIntent.putExtra("task", "statusBarColor")
        if (color != null) {
            colorIntent.putExtra("color", color)
        }
        context.sendBroadcast(colorIntent)
    }

}