package com.kanavdawra.pawmars.DashBoard.DashBoardFragments

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.R
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.flipboard.bottomsheet.BottomSheetLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kanavdawra.pawmars.Adapters.AlertSelectEventAdaptor
import com.kanavdawra.pawmars.Adapters.BottomSheetInviteesListAdaptor
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardToolBarButtonReceiver
import com.kanavdawra.pawmars.Constants.currUser
import com.kanavdawra.pawmars.DashBoardToolBar
import com.kanavdawra.pawmars.InterFace.TaskInterFace
import com.kanavdawra.pawmars.InterFace.ToolBarButtonsInterFace
import com.kanavdawra.pawmars.Modals.VerifyContacts
import com.scottyab.aescrypt.AESCrypt
import kotlinx.android.synthetic.main.fragment_dash_board_verify.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashBoardVerifyFragment : Fragment(), ZXingScannerView.ResultHandler {

    var bottomSheet: BottomSheetLayout? = null
    var scannerView: ZXingScannerView? = null
    var dashBoardToolBarButtonReceiver: DashBoardToolBarButtonReceiver? = null
    var needle = 0
    var contactsSnapShot: DataSnapshot? = null
    var eventssSnapShot: DataSnapshot? = null
    var contactSnapshotText = ""
    var verifyContactList = ArrayList<VerifyContacts>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DashBoardToolBar(activity!!).clean()
        DashBoardToolBar(activity!!).Verify()
        DashBoardUtility().changeStatusBarColor(activity!!, R.color.Black)
        bottomSheet = activity!!.findViewById(R.id.DashBoard_Verify_BottomSheet)
        firebaseEventFetch()
        setScanner()
        listners()
    }

    fun firebaseEventFetch() {
        FirebaseDatabase.getInstance().reference.child("UsersData").child(currUser().uid).child("Events").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                eventssSnapShot = p0
                DashBoardUtility().sendMessage(activity!!, "Select Event", R.id.DashBoard_ToolBar_EventName)
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun setScanner() {
        scannerView = ZXingScannerView(activity!!)
        DashBoard_Verify_Scanner.addView(scannerView)
        scannerView!!.setResultHandler(this)
        scannerView!!.startCamera()
        needle = 1
    }

    override
    fun onResume() {
        super.onResume()
        activity!!.registerReceiver(dashBoardToolBarButtonReceiver!!, IntentFilter("DashBoardToolBarButton"))
        if (needle == 1) {
            scannerView!!.setResultHandler(this)
            scannerView!!.startCamera()
        }

    }

    override fun onDetach() {
        DashBoardToolBar(activity!!).clean()
        DashBoardUtility().toolBarColorchange(activity!!, null)
        super.onDetach()
    }

    override
    fun onPause() {
        activity!!.unregisterReceiver(dashBoardToolBarButtonReceiver!!)
        scannerView!!.stopCamera()
        super.onPause()
    }

    override fun handleResult(p0: Result?) {
        readQrCode(p0!!.text)
        println(p0)
        //scannerView!!.resumeCameraPreview(this)
    }

    fun readQrCode(qrText: String) {
        val qrKind = qrText[0]
        var qrNameLength = ""
        var nameStart = 0
        var eventName = ""
        var count = 0
        var encodedText = ""
        var encodedTextStart = 0
        if (qrKind == 'N') {

            for (i in 2 until qrText.length) {
                if (qrText[i] == '~') {
                    nameStart = i + 1
                    break
                } else {
                    qrNameLength += qrText[i]
                }
            }
            val eventNameLength = qrNameLength.toInt()
            for (i in nameStart until eventNameLength + nameStart) {
                eventName += qrText[i]
                count++
            }
            encodedTextStart = nameStart + eventNameLength
            for (i in encodedTextStart until qrText.length) {
                encodedText += qrText[i]
            }
            decodeQrCode(qrKind.toString(), encodedText, eventName)
        } else {
            for (i in 1 until qrText.length) {
                encodedText += qrText[i]
            }
            decodeQrCode(qrKind.toString(), encodedText, null)
        }

    }

    fun decodeQrCode(qrKind: String, qrText: String, eventName: String?) {
        var decodedQrText = qrText
        if (qrKind == "N") {
            decodedQrText = AESCrypt.decrypt(eventName, qrText)

            checkSnapShot(decodedQrText, eventName!!)
        } else {
            inviteesInOut(decodedQrText)
        }


    }

    fun checkSnapShot(decodedText: String, eventName: String) {
        if (contactsSnapShot != null && contactSnapshotText != decodedText) {
            android.support.v7.app.AlertDialog.Builder(activity!!)
                    .setTitle("Update Event")
                    .setMessage("You will not be able to verify the current event's invitees list.")
                    .setIcon(R.mipmap.reload)
                    .setPositiveButton("Update") { dialogInterface, i ->
                        DashBoardUtility().create(activity!!, "Setting Up...")
                        verifyContactList.clear()
                        fireBaseSnapShot(decodedText, eventName)

                    }.setNegativeButton("Cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()
                        scannerView!!.resumeCameraPreview(this@DashBoardVerifyFragment)
                    }.show()
        } else if (contactsSnapShot == null) {
            DashBoardUtility().create(activity!!, "Setting Up...")
            fireBaseSnapShot(decodedText, eventName)
        } else {
            scannerView!!.resumeCameraPreview(this@DashBoardVerifyFragment)
        }
    }

    fun fireBaseSnapShot(decodedText: String, eventName: String) {
        if (contactsSnapShot == null) {
            FirebaseDatabase.getInstance().reference.child("Events").child(decodedText).child("Contacts").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    contactsSnapShot = p0
                    contactSnapshotText = decodedText
                    DashBoardUtility().dismiss(activity!!)
                    DashBoardUtility().sendMessage(activity!!, eventName, R.id.DashBoard_ToolBar_EventName)
                    populateInviteesList(decodedText)
                    scannerView!!.resumeCameraPreview(this@DashBoardVerifyFragment)
                }
            })
        }
    }

    fun populateInviteesList(id: String) {
        for (i in contactsSnapShot!!.children) {
            val verifyContacts = VerifyContacts()
            var out: String? = null
            var In: String? = null
            verifyContacts.id = i.key
            verifyContacts.name = i.child("Name").value.toString()
            verifyContacts.phone = i.child("PhoneNumber").value.toString()
            verifyContacts.email = i.child("EmailAddress").value.toString()
            try {
                verifyContacts.In = i.child("In").value.toString()
                verifyContacts.out = i.child("Out").value.toString()
            } catch (e: Exception) {
            }
            verifyContactList.add(verifyContacts)
        }
    }

    fun inviteesInOut(decodedQrText: String) {
        var name: Any? = null
        var phone: Any? = null
        var email: Any? = null
        var decodedText=""
        for(i in 0 until decodedQrText.count()-1){
            decodedText += decodedQrText[i]
        }
        val child=decodedQrText[decodedQrText.count()-1]
        try {
            name = contactsSnapShot?.child(child.toString())!!.child(decodedText)!!.child("Name").value
            phone = contactsSnapShot?.child(child.toString())!!.child(decodedText)!!.child("PhoneNumber").value
            email = contactsSnapShot?.child(child.toString())!!.child(decodedText)!!.child("EmailAddress").value
        } catch (e: Exception) {
        }
        showBottomSheet(name.toString(), phone.toString(), email.toString(), decodedText)
    }

    fun showBottomSheet(nameText: String?, phoneText: String?, emailText: String?, id: String) {

        val view = LayoutInflater.from(context).inflate(R.layout.verify_bottom_sheet, bottomSheet, false)
        bottomSheet!!.showWithSheetView(view)
        val inOutText = view.findViewById<TextView>(R.id.Verify_BottomSheet_inout)
        val image = view.findViewById<ImageView>(R.id.Verify_BottomSheet_Image)
        val name = view.findViewById<TextView>(R.id.Verify_BottomSheet_Name)
        val phone = view.findViewById<TextView>(R.id.Verify_BottomSheet_Phone)
        val email = view.findViewById<TextView>(R.id.Verify_BottomSheet_Email)
        val In = view.findViewById<TextView>(R.id.Verify_BottomSheet_In)
        val out = view.findViewById<TextView>(R.id.Verify_BottomSheet_Out)
        val personDetails = view.findViewById<RelativeLayout>(R.id.Verify_BottomSheet_Details)
        val inOutDetails = view.findViewById<RelativeLayout>(R.id.Verify_BottomSheet_InOutDetails)
        val layout = view.findViewById<RelativeLayout>(R.id.Verify_BottomSheet_Layout)

        if (nameText == "null") {
            layout.background = ContextCompat.getDrawable(activity!!, R.color.Red)
            inOutText.text = "N/I"
            image.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.error))
            personDetails.visibility = View.GONE
            inOutDetails.visibility = View.GONE

        } else {
            layout.background = ContextCompat.getDrawable(activity!!, R.color.Blue)
            image.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.checked))
            name.text = nameText
            phone.text = phoneText
            email.text = emailText

            val InText = getInInfo(id)
            val outText = getOutInfo(id)
            if (InText != null) {
                In.text = InText
            }
            if (outText != null) {
                out.text = outText
            }

            if (InText == null && outText == null) {
                In.text = getTimeStamp()
                inOutText.text = "iN"
                setBottomSheetData(id, nameText!!, phoneText, emailText, In.text.toString(), null)
            } else if (outText == null) {
                out.text = getTimeStamp()
                inOutText.text = "OUt"
                setBottomSheetDataAfterIn(id, nameText!!, phoneText, emailText, In.text.toString(), out.text.toString())
            } else {
                layout.background = ContextCompat.getDrawable(activity!!, R.color.PurpleDark)
                inOutText.text = ""

            }


        }

    }

    override fun onDestroyView() {
        DashBoardUtility().changeStatusBarColor(activity!!, null)
        super.onDestroyView()
    }

    fun setBottomSheetData(id: String, name: String, phone: String?, email: String?, In: String?, out: String?) {
        val verifyContacts = VerifyContacts()
        verifyContacts.id = id
        verifyContacts.name = name
        verifyContacts.phone = phone
        verifyContacts.email = email
        verifyContacts.In = In
        verifyContactList.add(verifyContacts)
        FirebaseDatabase.getInstance().reference.child("Events").child(contactSnapshotText).child("Contacts").child(id).child("In").setValue(In)
        FirebaseDatabase.getInstance().reference.child("Events").child(contactSnapshotText).child("Contacts").child(id).child("Out").setValue(out)
    }

    fun setBottomSheetDataAfterIn(id: String, name: String, phone: String?, email: String?, In: String?, out: String?) {
        for (i in verifyContactList) {
            if (i.id == id) {
                i.out = out
                FirebaseDatabase.getInstance().reference.child("Events").child(contactSnapshotText).child("Contacts").child(id).child("Out").setValue(out)
                return
            }
        }

    }

    fun getInInfo(id: String): String? {

        for (i in verifyContactList) {
            if (i.id == id) {
                return i.In
            }
        }
        return null
    }

    fun getOutInfo(id: String): String? {

        for (i in verifyContactList) {
            if (i.id == id) {
                return i.out
            }
        }
        return null
    }

    fun getTimeStamp(): String {
        val s = SimpleDateFormat("ddMMyyyyhhmmss")
        val format = s.format(Date())
        var date = ""
        date = date + format[0] + format[1] + "/" + format[2] + format[3] + "/" + format[4] + format[5] + format[6] + format[7]
        var time = ""
        time = time + format[8] + format[9] + ":" + format[10] + format[11]
        val dateTime = "$time $date"
        return dateTime
    }

    fun getEventArray(): ArrayList<String> {
        val array = ArrayList<String>()
        for (singleSnapshot in eventssSnapShot!!.children) {
            array.add(singleSnapshot.key)
        }
        return array
    }

    fun listners() {
        val taskInterFace = object : TaskInterFace {
            override fun string(message: ArrayList<String>) {
                val eventName = message[0]
                val id = eventssSnapShot!!.child(eventName).value
                checkSnapShot(id.toString(), eventName)
            }

        }

        val toolBarButtonsInterFace = object : ToolBarButtonsInterFace {

            override fun add() {}

            override fun cancel() {}

            override fun selectEvent() {
                val alert = android.support.v7.app.AlertDialog.Builder(activity!!)
                        .setTitle("Select Event")
                        .setView(R.layout.alert_select_event_layout)
                        .show()
                val recyclerView = alert.findViewById<RecyclerView>(R.id.Alert_Select_Event_RecyclerView)
                recyclerView?.layoutManager = LinearLayoutManager(activity!!)
                recyclerView?.adapter = AlertSelectEventAdaptor(activity!!, getEventArray(), alert, taskInterFace)
            }

            override fun inviteesList() {
                val view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_invietes_list_layout, bottomSheet, false)
                val recyclerView = view.findViewById<RecyclerView>(R.id.BottomSheet_InviteesList_RecyclerView)
                recyclerView.layoutManager = LinearLayoutManager(activity!!)
                recyclerView.adapter = BottomSheetInviteesListAdaptor(activity!!, verifyContactList)
                scannerView!!.stopCameraPreview()
                bottomSheet!!.showWithSheetView(view)
            }

            override fun flash() {
                scannerView!!.flash = !scannerView!!.flash
            }
        }
        bottomSheet!!.addOnSheetDismissedListener { scannerView!!.resumeCameraPreview(this@DashBoardVerifyFragment) }

        dashBoardToolBarButtonReceiver = DashBoardToolBarButtonReceiver(toolBarButtonsInterFace)
        activity!!.registerReceiver(dashBoardToolBarButtonReceiver!!, IntentFilter("DashBoardToolBarButton"))
    }

    override fun onStart() {
        super.onStart()
        DashBoardUtility().dismiss(activity!!)
    }
}

