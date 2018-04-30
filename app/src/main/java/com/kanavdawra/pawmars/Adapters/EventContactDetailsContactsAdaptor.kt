package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import com.kanavdawra.pawmars.Modals.ContactName
import com.kanavdawra.pawmars.R
import java.util.ArrayList

class EventContactDetailsContactsAdaptor(val context: Context, val contactList: ArrayList<ContactName>) : RecyclerView.Adapter<EventContactDetailsContactsAdaptor.EventDetailsContactListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsContactListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_event_contact_list_row_layout, parent, false)
        return EventDetailsContactListViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return contactList.count()
    }

    override fun onBindViewHolder(holder: EventDetailsContactListViewHolder, position: Int) {
        holder.name.text = contactList[position].name

    }

    inner class EventDetailsContactListViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.DashBoard_Add_Event_ContactList_Name)
        val guestCount = view.findViewById<TextView>(R.id.DashBoard_Add_Event_ContactList_ContactCount)
        val guestText = view.findViewById<TextView>(R.id.DashBoard_Add_Event_ContactList_ContactText)
        val checkBox = view.findViewById<CheckBox>(R.id.DashBoard_Add_Event_ContactList_CheckBox)
        val row = view.findViewById<RelativeLayout>(R.id.DashBoard_Add_Event_ContactList_View)

        init {
            guestText.visibility = View.GONE
            checkBox.visibility = View.GONE
            guestCount.visibility=View.GONE
            name.textSize=15.toFloat()
        }
    }

}
