package com.kanavdawra.pawmars.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsManager
import com.kanavdawra.pawmars.Modals.ParcelableEventContact
import android.content.IntentFilter
import android.app.Activity
import android.content.BroadcastReceiver
import android.app.PendingIntent
import android.content.Context
import com.kanavdawra.pawmars.DashBoardEventsUtility
import com.kanavdawra.pawmars.EmailSender


class DashBoardEventSendService : Service() {
    val binder = EventSendServiceBinder()
    var count = 0
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.getBundleExtra("EventContacts")
        val eventContacts = bundle?.getParcelableArrayList<ParcelableEventContact>("EventContacts")
        val sms = bundle?.getString("SMS")
        val link = bundle?.getString("Link")
        val subID = bundle?.getInt("SubscriptionID")
        for (event in eventContacts!!) {

            sendSMS(event.phoneNo, "$sms [$link]", subID!!, eventContacts.indexOf(event))
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun sendSMS(phoneNumber: String, message: String, subID: Int, position: Int) {
        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"

        val sentPI = PendingIntent.getBroadcast(this, 0,
                Intent(SENT), 0)

        val deliveredPI = PendingIntent.getBroadcast(this, 0,
                Intent(DELIVERED), 0)

        //---when the SMS has been sent---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
//                    Activity.RESULT_OK -> Toast.makeText(baseContext, "SMS sent",
//                            Toast.LENGTH_SHORT).show()
//                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(baseContext, "Generic failure",
//                            Toast.LENGTH_SHORT).show()
//                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(baseContext, "No service",
//                            Toast.LENGTH_SHORT).show()
//                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(baseContext, "Null PDU",
//                            Toast.LENGTH_SHORT).show()
//                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(baseContext, "Radio off",
//                            Toast.LENGTH_SHORT).show()
                }
            }
        }, IntentFilter(SENT))

        //---when the SMS has been delivered---
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
//                    Activity.RESULT_OK -> Toast.makeText(baseContext, "SMS delivered",
//                            Toast.LENGTH_SHORT).show()
//                    Activity.RESULT_CANCELED -> Toast.makeText(baseContext, "SMS not delivered",
//                            Toast.LENGTH_SHORT).show()
                    Activity.RESULT_OK -> sendBroadcast(Intent("SendEventReceipt")
                            .putExtra("task", "SMS")
                            .putExtra("position", position)
                            .putExtra("receipt", 1)
                    )
                }
            }
        }, IntentFilter(DELIVERED))
        if (message.length <= 160) {
            val sms = SmsManager.getSmsManagerForSubscriptionId(subID)
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
        } else {
            val sendList = ArrayList<PendingIntent>()
            sendList.add(sentPI)

            val deliverList = ArrayList<PendingIntent>()
            deliverList.add(deliveredPI)
            val messageArray = SmsManager.getSmsManagerForSubscriptionId(subID).divideMessage(message)
            val sms = SmsManager.getSmsManagerForSubscriptionId(subID)
            sms.sendMultipartTextMessage(phoneNumber, null, messageArray, sendList, deliverList)
        }

    }

    override fun onDestroy() {
        DashBoardEventsUtility(this).pagerPositionChange(3)
        super.onDestroy()
    }
    fun eventSendServiceBinder(): EventSendServiceBinder {
        return EventSendServiceBinder()
    }

    inner class EventSendServiceBinder : android.os.Binder() {
        fun getService(): DashBoardEventSendService {
            return this@DashBoardEventSendService
        }
    }
}
