package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.*
import com.kanavdawra.pawmars.Modals.ParcelableEventContact
import kotlinx.android.synthetic.main.fragment_dash_baord_event_email_text.*
import android.content.ActivityNotFoundException
import android.net.Uri


class DashBaordEventEmailTextFragment : Fragment() {

    var list: ArrayList<ParcelableEventContact>? = null
    val names = ArrayList<String>()
    var emails = "mailto:"
    var pager = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_baord_event_email_text, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        setAdaptor()
        clickListners()
    }

    fun setData() {
        val Shpref = activity!!.getSharedPreferences("Event_${Constants.eventName}_Details", 0)
        DashBoard_Event_Email_Text_EditText.setText(Shpref.getString("Link", "Link"))
    }

    fun setAdaptor() {

        list = Modal(activity!!).parcelableEventContactstoMoadal(Constants.eventName)
        names.clear()
        for (name in list!!) {
            if (name.emailId != "") {
                names.add(name.name)
                emails = emails + "," + name.emailId
            }

        }

        DashBoard_Event_Email_Text_TagContainerLayout.removeAllTags()
        DashBoard_Event_Email_Text_TagContainerLayout.setTags(names)
        DashBoard_Event_Email_Text_TagContainerLayout.backgroundColor = Color.WHITE
        DashBoard_Event_Email_Text_TagContainerLayout.tagBackgroundColor = Color.parseColor("#1E88E5")
        DashBoard_Event_Email_Text_TagContainerLayout.tagBorderColor = Color.parseColor("#90CAF9")
        DashBoard_Event_Email_Text_TagContainerLayout.tagTextColor = Color.BLACK
        DashBoard_Event_Email_Text_TagContainerLayout.rippleDuration = 300

    }

    fun clickListners() {
        DashBoard_Event_Email_Text_Back.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(2)
        }
        DashBoard_Event_Email_Text_Next.setOnClickListener {
            if (names.count() == 0) {
                AlertDialog.Builder(activity!!).setMessage("You have not set email addresses of the contact please set some to proceed")
                        .setTitle("No Emails Found!")
                        .setIcon(R.mipmap.email_icon)
                        .setPositiveButton("Set") { dialogInterface, i ->
                            PopUpFragmnent(activity!!).dismiss("EventViewPager")
                            activity!!.sendBroadcast(Intent("Navigation").putExtra("task", "navigation").putExtra("navigation", "contacts"))

                        }.setNegativeButton("Skip") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            DashBoardEventsUtility(activity!!).pagerPositionChange(5)

                        }.show()
            } else {
                sendEmailIntent()
            }

        }
    }


    fun sendEmailIntent() {
        val mailto = emails +
                "?cc=" + "" +
                "&subject=" + Uri.encode(DashBoard_Event_Email_Subject_EditText.text.toString()) +
                "&body=" + Uri.encode(DashBoard_Event_Email_Text_EditText.text.toString())

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)

        try {
            startActivity(emailIntent)
            pager = 1
        } catch (e: ActivityNotFoundException) {
        }


    }


    override fun onResume() {
        if (pager == 1) {
            DashBoardEventsUtility(activity!!).pagerPositionChange(5)
        }
        super.onResume()
    }
}
