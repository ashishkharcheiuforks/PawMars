package com.kanavdawra.pawmars.InterFace

/**
 * Created by Kanav on 3/16/2018.
 */
interface DashBoard {
    fun createPurple(text:String)
    fun dismissPurple()
    fun snackBar(message:String,snackBarColor:Int,textColor:Int)
    fun updateEmailVerificationFragment(layout:Int)
}