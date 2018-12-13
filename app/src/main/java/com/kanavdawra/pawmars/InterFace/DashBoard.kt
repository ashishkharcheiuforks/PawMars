package com.kanavdawra.pawmars.InterFace


interface DashBoard {
    fun createPurple(text:String)
    fun dismissPurple()
    fun snackBar(message:String,snackBarColor:Int,textColor:Int)
    fun updateEmailVerificationFragment(layout:Int)
    fun toggleToolBar()
    fun toolBarColorChange(color:Int?)
    fun sendMessage(message: String,resource:Int?)
    fun statusBarColorChange(color:Int?)
}