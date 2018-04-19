package com.kanavdawra.pawmars.InterFace

import android.view.View
import android.widget.RelativeLayout

/**
 * Created by Kanav on 3/30/2018.
 */
interface ContactsSelection {
    fun select(view: RelativeLayout,contactId:String)
    fun selectCheck(contactId:String):Int
}