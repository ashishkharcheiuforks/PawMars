package com.kanavdawra.pawmars

import android.content.Context
import android.content.Intent

class DashBoardToolBar(val context: Context) {
    fun clean(){
        context.sendBroadcast(Intent("DashBoardToolBar").putExtra("toolbar","clean"))
    }
    fun contacts(){
        context.sendBroadcast(Intent("DashBoardToolBar").putExtra("toolbar","contacts"))

    }
    fun addCancel(){
        context.sendBroadcast(Intent("DashBoardToolBar").putExtra("toolbar","addCancel"))

    }
    class DashBoardToolBarButtons(val context: Context){
        fun add(){
            context.sendBroadcast(Intent("DashBoardToolBarButton").putExtra("button","add"))
        }
        fun cancel(){
            context.sendBroadcast(Intent("DashBoardToolBarButton").putExtra("button","cancel"))
        }
    }
}