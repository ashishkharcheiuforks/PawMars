package com.kanavdawra.pawmars.DashBoard.DashBoardFragments

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.R
import com.kanavdawra.pawmars.DataBase.PawMarsDataBase
import android.os.AsyncTask
import com.kanavdawra.pawmars.Adapters.ContactsAdapter
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import kotlinx.android.synthetic.main.fragment_dash_board_contacts.*
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import android.support.v7.widget.LinearLayoutManager
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardContactsReceiver
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardToolBarButtonReceiver
import com.kanavdawra.pawmars.Constants.selectedContacts
import com.kanavdawra.pawmars.DashBoardToolBar
import com.kanavdawra.pawmars.InterFace.DashBoard
import com.kanavdawra.pawmars.InterFace.DashBoardContactsInterface
import com.kanavdawra.pawmars.InterFace.ToolBarButtonsInterFace
import com.kanavdawra.pawmars.Modal
import com.kanavdawra.pawmars.PopUpFragmnent


class DashBoardContactsFragment : Fragment() {
    var contactsAdapter: ContactsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_contacts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchContacts()
        toolBarButtons()
        notifyDataSetChanged()
    }

    fun syncContacts() {
        DashBoardUtility().create(activity!!, "Loading...")
        val asyncTask = AsyncTask.execute {
            PawMarsDataBase().contacts(activity!!)
        }

    }

    fun fetchContacts() {

        contactsAdapter = ContactsAdapter(activity!!, Modal(activity!!).contactsToModal())
        contacts_recycler_view.adapter = contactsAdapter
        contacts_recycler_view.layoutManager = LinearLayoutManager(activity)
        contacts_recycler_view.addItemDecoration(StickyRecyclerHeadersDecoration(contactsAdapter))

    }

    fun toolBarButtons() {

        val toolBarButtonsInterFace = object : ToolBarButtonsInterFace {
            override fun flash() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun selectEvent() {}

            override fun inviteesList() {}

            override fun add() {
                selectedContacts = contactsAdapter!!.selectedContacts()
                contactsAdapter!!.selectionCount = 0
                contactsAdapter!!.selectionCheck = false
                PopUpFragmnent(activity!!).create("CreateContactList")
            }

            override fun cancel() {
                contactsAdapter!!.deSelectAll()
                DashBoardToolBar(activity!!).clean()
            }

        }

        activity!!.registerReceiver(DashBoardToolBarButtonReceiver(toolBarButtonsInterFace), IntentFilter("DashBoardToolBarButton"))

    }

    fun notifyDataSetChanged() {
        val dashBoardContactInterface = object : DashBoardContactsInterface {
            override fun notifyDataSetChanged() {
                contactsAdapter = ContactsAdapter(activity!!, Modal(activity!!).contactsToModal())
                contacts_recycler_view.adapter = contactsAdapter

                contactsAdapter!!.notifyDataSetChanged()
            }

        }
        activity!!.registerReceiver(DashBoardContactsReceiver(dashBoardContactInterface), IntentFilter("DashBoardContacts"))
    }

    fun pagerStrip(){

    }

}

