package com.kanavdawra.pawmars

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class PawMars:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}