package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.R
import com.scottyab.aescrypt.AESCrypt
import java.security.GeneralSecurityException


@RequiresApi(Build.VERSION_CODES.M)
class DashBoardEventSelectSIMAndEmailFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_select_sim_and_email, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun encryptEvent(): String {
        val id = activity!!.getSharedPreferences("Event_${eventName}_Details", 0).getString("ID", "")
        var encryptionid = ""
        val password = id
        val message = id
        try {
            val encryptedMsg = AESCrypt.encrypt(password, message)
            encryptionid = encryptedMsg
        } catch (e: GeneralSecurityException) {
            Log.e("AES Error", e.toString())
        }
        return encryptionid
    }
    fun appendEventname():String{
        val encryptedText=encryptEvent()
        val eventname= eventName
        val lenght=eventname.length
        val prefix="[$lenght]~$eventname"
        val qrText=prefix+encryptedText
        return qrText
    }

    fun createQrCode(){

    }
}


