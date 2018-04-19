package com.kanavdawra.pawmars.DashBoard.DashBoardFragments


import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Adapters.ContactListAdaptor
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardContactsReceiver
import com.kanavdawra.pawmars.InterFace.DashBoardContactsInterface
import com.kanavdawra.pawmars.Modal
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_contact_list.*


class DashBoardContactListFragment : Fragment() {

var contactListAdaptor:ContactListAdaptor?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_contact_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdaptor()
        notifyDataSetChanged()

    }

    fun setAdaptor() {
        contactListAdaptor=ContactListAdaptor(activity!!, Modal(activity!!).contactListToModal())
        contact_list_recyclerView.adapter=contactListAdaptor
        contact_list_recyclerView.layoutManager=LinearLayoutManager(activity!!)

    }
    fun notifyDataSetChanged(){
        val dashBoardContactInterface=object :DashBoardContactsInterface{
            override fun notifyDataSetChanged() {
                contactListAdaptor!!.contactList=Modal(activity!!).contactListToModal()
                contactListAdaptor!!.notifyDataSetChanged()
            }

        }
        activity!!.registerReceiver(DashBoardContactsReceiver(dashBoardContactInterface), IntentFilter("DashBoardContactList"))
    }

}