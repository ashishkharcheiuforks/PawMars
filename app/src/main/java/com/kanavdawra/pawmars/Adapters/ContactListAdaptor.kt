package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kanavdawra.pawmars.Constants.contactListName
import com.kanavdawra.pawmars.Modals.ContactList
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R


class ContactListAdaptor(val context: Context, var contactList: ArrayList<ContactList>) : RecyclerView.Adapter<ContactListAdaptor.ContactListViewHolder>() {

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.name.text = contactList[position].name
        holder.badge.text = contactList[position].badge.toString()
        if (contactList[position].dp != null) {
            holder.dp.setImageBitmap(contactList[position].dp)
        }

        holder.layout.setOnClickListener {
            PopUpFragmnent(context).create("ContactListDetails")
            contactListName=contactList[position].name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_list_row_layout, parent, false)
        return ContactListViewHolder(view!!)
    }


    override fun getItemCount(): Int {
        return contactList.count()
    }

    class ContactListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dp = view.findViewById<ImageView>(R.id.contact_list_row_image)
        val name = view.findViewById<TextView>(R.id.contact_list_row_name)
        val badge = view.findViewById<TextView>(R.id.contact_list_row_badge)
        val layout = view.findViewById<RelativeLayout>(R.id.contact_list_row_layout)
    }

}