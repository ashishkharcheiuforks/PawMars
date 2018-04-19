package com.kanavdawra.pawmars.DashBoard.DashBoardFragments

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Adapters.ContactsPagerAdaptor
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardContactsReceiver
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.DataBase.DataBase
import com.kanavdawra.pawmars.InterFace.DashBoard
import com.kanavdawra.pawmars.InterFace.DashBoardContactsInterface
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_contacts_view_pager.*

class DashBoardContactsViewPagerFragment : Fragment() {

    var contactsPagerAdaptor: ContactsPagerAdaptor? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_contacts_view_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdaptor()
        setInterface()
        setPagerStrip()
    }

    fun setAdaptor() {
        contactsPagerAdaptor = ContactsPagerAdaptor(childFragmentManager)
        if (DataBase(activity!!).writableDatabase.rawQuery("SELECT * FROM contactList", null).count == 0) {
            contactsPagerAdaptor!!.pages = 1
        } else {
            contactsPagerAdaptor!!.pages = 2
        }
        contacts_view_pager_ViewPager.adapter = contactsPagerAdaptor
    }

    fun setInterface() {
        val dashBoardContactsInterface = object : DashBoardContactsInterface {
            override fun notifyDataSetChanged() {
                val coursor = DataBase(activity!!).writableDatabase.rawQuery("SELECT * FROM contactList", null)
                val count = coursor.count
                if (count <= 0) {
                    contactsPagerAdaptor!!.pages = 1
                } else {
                    contactsPagerAdaptor!!.pages = 2
                }
                contactsPagerAdaptor!!.notifyDataSetChanged()
                coursor.close()
            }

        }
        activity!!.registerReceiver(DashBoardContactsReceiver(dashBoardContactsInterface), IntentFilter("DashBoardContactsPager"))

    }

    fun setPagerStrip() {
        contacts_view_pager_PagerStrip.tabIndicatorColor = ContextCompat.getColor(activity!!, R.color.White)
        contacts_view_pager_PagerStrip.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        contacts_view_pager_PagerStrip.setTextColor(ContextCompat.getColor(activity!!, R.color.White))
    }

    override fun onStart() {
        super.onStart()
        DashBoardUtility().dismiss(activity!!)
    }
}