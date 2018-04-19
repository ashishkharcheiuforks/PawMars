package com.kanavdawra.pawmars

import android.content.Context
import android.support.v7.app.AlertDialog

class Alerts(val context: Context) {
    fun alertBox(title: String, message: String, icon: Int, positiveButton: String, negativeButton: String): Boolean {
        var returnBool: Boolean? = null
        AlertDialog.Builder(context).setMessage(message).setTitle(title).setIcon(icon).setPositiveButton(positiveButton) { dialogInterface, i ->
            returnBool = true
        }.setNegativeButton(negativeButton) { dialogInterface, i ->
            returnBool = false

        }.show()
        return returnBool!!

    }
}