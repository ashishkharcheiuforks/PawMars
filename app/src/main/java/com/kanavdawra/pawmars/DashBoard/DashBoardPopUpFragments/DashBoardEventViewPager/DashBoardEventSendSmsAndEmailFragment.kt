package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Adapters.EventSmsEmailSend
import com.kanavdawra.pawmars.BroadCastReceiver.SendReceiptReceiver
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.InterFace.EventSendInterface
import com.kanavdawra.pawmars.Modal
import com.kanavdawra.pawmars.Modals.ParcelableEventContact
import com.kanavdawra.pawmars.R
import com.kanavdawra.pawmars.Service.DashBoardEventSendService
import kotlinx.android.synthetic.main.fragment_dash_board_event_send_sms_and_email.*

class DashBoardEventSendSmsAndEmailFragment : Fragment(), ServiceConnection {
    var eventSendService: DashBoardEventSendService? = null
    var eventContact: ArrayList<ParcelableEventContact>? = null
    var eventSendInterface: EventSendInterface? = null
    var eventSmsEmailSend: EventSmsEmailSend? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dash_board_event_send_sms_and_email, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setAdaptor()
        //eventSendInterface()
        clickListners()
    }

    fun clickListners() {
        DashBoard_Event_Send_Sms_Email_Publish.setOnClickListener {
            DashBoard_Event_Send_Sms_Email_RecyclerView.visibility = View.VISIBLE
            setService()
        }
    }

    fun setAdaptor() {
        eventContact = Modal(activity!!).parcelableEventContactstoMoadal(eventName)
        DashBoard_Event_Send_Sms_Email_RecyclerView.layoutManager = LinearLayoutManager(activity!!)
        eventSmsEmailSend = EventSmsEmailSend(activity!!, eventContact!!)
        DashBoard_Event_Send_Sms_Email_RecyclerView.adapter = eventSmsEmailSend

    }

    fun eventSendInterface() {
        eventSendInterface = object : EventSendInterface {
            override fun rowDataChanged(task:String,position: Int,receipt:Int) {
                if(task=="SMS"){
                    val temp=eventContact!![position]
                    temp.phnoBool=receipt
                    println("$task $position $receipt")
                    eventContact!!.set(position,temp)
                }
                eventSmsEmailSend!!.notifyItemChanged(position)

            }

        }
        activity!!.registerReceiver(SendReceiptReceiver(eventSendInterface!!), IntentFilter("SendEventReceipt"))
    }

    fun setService() {
        val bundle = Bundle()
        bundle.putParcelableArrayList("EventContacts", eventContact)
        bundle.putString("SMS", activity!!.getSharedPreferences("Event_${eventName}_Details",0).getString("SMS",""))
        bundle.putString("Email", activity!!.getSharedPreferences("Event_${eventName}_Details",0).getString("Email",""))
        bundle.putString("Link",activity!!.getSharedPreferences("Event_${eventName}_Details",0).getString("Link","Link"))
        bundle.putInt("SubscriptionID",activity!!.getSharedPreferences("Event_${eventName}_Details",0).getInt("SIMSubscriptionID",1))
        bundle.putString("UserName",activity!!.getSharedPreferences("Event_${eventName}_Details",0).getString("EmailAddress","EmailAddress"))
        bundle.putString("Password",activity!!.getSharedPreferences("Event_${eventName}_Details",0).getString("Password","Password"))
        activity!!.startService(Intent(activity!!, DashBoardEventSendService::class.java).putExtra("EventContacts", bundle))
    }

    override fun onResume() {
        super.onResume()
        activity!!.bindService(Intent(activity!!, DashBoardEventSendService::class.java), this@DashBoardEventSendSmsAndEmailFragment, Context.BIND_AUTO_CREATE)


    }

    override fun onPause() {
        super.onPause()
    }
    override fun onServiceDisconnected(name: ComponentName?) {
        eventSendService = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = DashBoardEventSendService().eventSendServiceBinder()
        eventSendService = binder.getService()
    }


}
