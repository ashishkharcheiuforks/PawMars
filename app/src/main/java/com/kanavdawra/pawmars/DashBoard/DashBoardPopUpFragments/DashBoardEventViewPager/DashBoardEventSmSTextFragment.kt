package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager


import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Adapters.SmsGridViewAdaptor
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.DashBoardEventsUtility
import com.kanavdawra.pawmars.Modal
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_event_sms_text.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.telephony.SubscriptionInfo
import android.text.Editable
import android.text.TextWatcher
import com.kanavdawra.pawmars.BroadCastReceiver.SendReceiptReceiver
import com.kanavdawra.pawmars.Constants.sendReceiptReceiver
import com.kanavdawra.pawmars.InterFace.EventSendInterface
import com.kanavdawra.pawmars.Modals.ParcelableEventContact
import com.kanavdawra.pawmars.Service.DashBoardEventSendService
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import github.nisrulz.easydeviceinfo.base.EasySimMod
import kotlinx.android.synthetic.main.select_sim.*


class DashBoardEventSmSTextFragment : Fragment(), ServiceConnection {

    val imageBorderColor = java.util.ArrayList<String>()
    var list: ArrayList<ParcelableEventContact>? = null
    var adaptor: SmsGridViewAdaptor? = null
    var eventSendService: DashBoardEventSendService? = null
    val names = ArrayList<String>()
    val recipts = ArrayList<Int>()
    var color0: Int = 0
    var color1: Int = 0
    var color2: Int = 0
    var color3: Int = 0
    var color4: Int = 0
    var color5: Int = 0
    var color6: Int = 0
    var color7: Int = 0
    var color8: Int = 0
    var color9: Int = 0

    var colorB0: Int = 0
    var colorB1: Int = 0
    var colorB2: Int = 0
    var colorB3: Int = 0
    var colorB4: Int = 0
    var colorB5: Int = 0
    var colorB6: Int = 0
    var colorB7: Int = 0
    var colorB8: Int = 0
    var colorB9: Int = 0

    init {
        color0 = Color.parseColor("#EF5350")
        color1 = Color.parseColor("#EC407A")
        color2 = Color.parseColor("#AB47BC")
        color3 = Color.parseColor("#7E57C2")
        color4 = Color.parseColor("#1E88E5")
        color5 = Color.parseColor("#009688")
        color6 = Color.parseColor("#43A047")
        color7 = Color.parseColor("#FFC107")
        color8 = Color.parseColor("#F4511E")
        color9 = Color.parseColor("#3E2723")

        colorB0 = Color.parseColor("#EF9A9A")
        colorB1 = Color.parseColor("#F48FB1")
        colorB2 = Color.parseColor("#CE93D8")
        colorB3 = Color.parseColor("#9FA8DA")
        colorB4 = Color.parseColor("#90CAF9")
        colorB5 = Color.parseColor("#80CBC4")
        colorB6 = Color.parseColor("#A5D6A7")
        colorB7 = Color.parseColor("#FFE082")
        colorB8 = Color.parseColor("#FFAB91")
        colorB9 = Color.parseColor("#BCAAA4")


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_sms_text, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        setAdaptor()
        eventSendInterface()
        clickListners()
    }

    fun setAdaptor() {

        list = Modal(activity!!).parcelableEventContactstoMoadal(eventName)
//        adaptor = SmsGridViewAdaptor(activity!!, list!!)
//        DashBoard_Event_SmS_Text_RecyclerView.layoutManager = StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL)
//        DashBoard_Event_SmS_Text_RecyclerView.adapter=adaptor
        names.clear()
        for (name in list!!) {
            names.add(name.name)
        }
        DashBoard_Event_SmS_Text_TagContainerLayout.removeAllTags()
        DashBoard_Event_SmS_Text_TagContainerLayout.setTags(names)
        DashBoard_Event_SmS_Text_TagContainerLayout.backgroundColor = Color.WHITE
        DashBoard_Event_SmS_Text_TagContainerLayout.tagBackgroundColor = color4
        DashBoard_Event_SmS_Text_TagContainerLayout.tagBorderColor = color4
        DashBoard_Event_SmS_Text_TagContainerLayout.tagTextColor = Color.BLACK
        DashBoard_Event_SmS_Text_TagContainerLayout.rippleDuration = 300

    }

    @SuppressLint("SetTextI18n")
    fun setData() {
        DashBoard_Event_SmS_Text_EditText.setText("[${activity!!.getSharedPreferences("Event_${eventName}_Details",0).getString("Link","Link")}]")
    }

    fun sendSMS(subscriptionID: Int) {

        val bundle = Bundle()
        bundle.putParcelableArrayList("EventContacts", list)
        bundle.putString("SMS", DashBoard_Event_SmS_Text_EditText.text.toString())
        bundle.putString("Link", activity!!.getSharedPreferences("Event_${eventName}_Details", 0).getString("Link", "Link"))
        bundle.putInt("SubscriptionID", subscriptionID)
        activity!!.startService(Intent(activity!!, DashBoardEventSendService::class.java).putExtra("EventContacts", bundle))
    }

    fun clickListners() {
        DashBoard_Event_SmS_Text_Back.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(1)
        }
        DashBoard_Event_SmS_Text_Next.setOnClickListener {
            if (DashBoard_Event_SmS_Text_EditText.text.length > 104) {
                DashBoard_Event_SmS_Text_EditText_Layout.error = "Max Char limit exceeded."
            } else {
                AlertDialog.Builder(activity!!).setMessage("Are you okay by sending ${list!!.count()} SMS's in Background.")
                        .setTitle("Background SMS sender")
                        .setIcon(R.mipmap.sms)
                        .setPositiveButton("YES") { dialogInterface, i ->
                            setSimData()

                        }.setNegativeButton("No") { dialogInterface, i ->
                            DashBoardEventsUtility(activity!!).pagerPositionChange(4)

                        }.show()
            }

        }
        DashBoard_Event_SmS_Text_EditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 104) {
                    DashBoard_Event_SmS_Text_EditText_Layout.error = "Max Char limit exceeded."
                }
                else{
                    DashBoard_Event_SmS_Text_EditText_Layout.error = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun selectSim(list: List<SubscriptionInfo>, count: Int) {
        var selectedSim = -1
        val alert = AlertDialog.Builder(activity!!)
                .setTitle("Select SIM Card")
                .setIcon(R.mipmap.sms)
                .setView(R.layout.select_sim)
                .setPositiveButton("Select") { dialogInterface, i ->
                    if (selectedSim != -1) {
                        sendSMS(selectedSim)
                    }


                }.setNegativeButton("Select") { dialogInterface, i ->
                    dialogInterface.dismiss()

                }.show()

        alert.Alert_Select_Sim1_Layout.setOnClickListener {
            selectedSim = list[0].subscriptionId
            alert.Alert_Select_Sim1_Layout.background = ContextCompat.getDrawable(activity!!, R.color.LightGrey)
        }
        alert.Alert_Select_Sim2_Layout.setOnClickListener {
            selectedSim = list[1].subscriptionId
            alert.Alert_Select_Sim2_Layout.background = ContextCompat.getDrawable(activity!!, R.color.LightGrey)
        }
        alert.Alert_Select_Sim3_Layout.setOnClickListener {
            selectedSim = list[2].subscriptionId
            alert.Alert_Select_Sim3_Layout.background = ContextCompat.getDrawable(activity!!, R.color.LightGrey)
        }
        alert.Alert_Select_Sim4_Layout.setOnClickListener {
            selectedSim = list[3].subscriptionId
            alert.Alert_Select_Sim4_Layout.background = ContextCompat.getDrawable(activity!!, R.color.LightGrey)
        }
        if (count == 4) {
            alert.Alert_Select_Sim4_Carrier.text = list[3].carrierName
            alert.Alert_Select_Sim3_Carrier.text = list[2].carrierName
            alert.Alert_Select_Sim2_Carrier.text = list[1].carrierName
            alert.Alert_Select_Sim1_Carrier.text = list[0].carrierName
        }
        if (count == 3) {
            alert.Alert_Select_Sim4_Layout.visibility = View.GONE
            alert.Alert_Select_Sim3_Carrier.text = list[2].carrierName
            alert.Alert_Select_Sim2_Carrier.text = list[1].carrierName
            alert.Alert_Select_Sim1_Carrier.text = list[0].carrierName
        }
        if (count == 2) {
            alert.Alert_Select_Sim4_Layout.visibility = View.GONE
            alert.Alert_Select_Sim3_Layout.visibility = View.GONE
            alert.Alert_Select_Sim2_Carrier.text = list[1].carrierName
            alert.Alert_Select_Sim1_Carrier.text = list[0].carrierName
        }
        if (count == 1) {
            alert.Alert_Select_Sim4_Layout.visibility = View.GONE
            alert.Alert_Select_Sim3_Layout.visibility = View.GONE
            alert.Alert_Select_Sim2_Layout.visibility = View.GONE
            alert.Alert_Select_Sim1_Carrier.text = list[0].carrierName
        }

    }

    fun eventSendInterface() {
        val eventSendInterface = object : EventSendInterface {
            override fun rowDataChanged(task: String, position: Int, receipt: Int) {
                if (task == "SMS") {
                    val temp = list!![position]
                    temp.phnoBool = receipt
                    list!!.set(position, temp)
                }
                DashBoard_Event_SmS_Text_TagContainerLayout.setTags(names)
                DashBoard_Event_SmS_Text_TagContainerLayout.rippleDuration = 300
                recipts.add(position)
                for (i in recipts) {
                    val tag = DashBoard_Event_SmS_Text_TagContainerLayout.getTagView(i)
                    tag.setTagBackgroundColor(colorB6)
                    tag.setTagBorderColor(color6)
                }

            }

        }
        sendReceiptReceiver = SendReceiptReceiver(eventSendInterface)
        println("sendReceiptReceiver" + sendReceiptReceiver)
        activity!!.registerReceiver(sendReceiptReceiver, IntentFilter("SendEventReceipt"))
    }

    fun setSimData() {
        val easySimMod = EasySimMod(context)
        try {
            if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                Dexter.withActivity(activity!!)
                        .withPermission(android.Manifest.permission.READ_PHONE_STATE)
                        .withListener(object : PermissionListener {
                            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {}

                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                    val count = easySimMod.numberOfActiveSim
                                    val list = easySimMod.activeMultiSimInfo
                                    selectSim(list, count)
                                }
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                AlertDialog.Builder(activity!!).setMessage("You have DENIED the permission to read you phone state. Please give permissions in application settings")
                                        .setTitle("Permission Denied")
                                        .setIcon(R.mipmap.permissions)
                                        .setPositiveButton("OK") { dialogInterface, i ->
                                            dialogInterface.dismiss()
                                        }.setNegativeButton("Cancel") { dialogInterface, i ->
                                            dialogInterface.dismiss()

                                        }.show()
                            }

                        }).check()
            } else {
                val count = easySimMod.numberOfActiveSim
                val list = easySimMod.activeMultiSimInfo
                selectSim(list, count)
            }


        } catch (e: Exception) {
        }

    }

    override fun onResume() {
        super.onResume()
        activity!!.registerReceiver(sendReceiptReceiver, IntentFilter("SendEventReceipt"))
        activity!!.bindService(Intent(activity!!, DashBoardEventSendService::class.java), this@DashBoardEventSmSTextFragment, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        println("sendReceiptReceiver" + sendReceiptReceiver)
        activity!!.unregisterReceiver(sendReceiptReceiver)
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
