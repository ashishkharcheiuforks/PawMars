package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import com.kanavdawra.pawmars.InterFace.AddEventInterFace
import com.kanavdawra.pawmars.Modals.ContactList
import com.kanavdawra.pawmars.R
import java.util.ArrayList

class AddEventContactListAdaptor(val context: Context, val contactList: ArrayList<ContactList>,val addEventInterFace: AddEventInterFace) : RecyclerView.Adapter<AddEventContactListAdaptor.AddEventContactListViewHolder>() {
    val selectedList=ArrayList<ContactList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddEventContactListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_event_contact_list_row_layout, parent, false)
        return AddEventContactListViewHolder(view,context)
    }

    override fun getItemCount(): Int {
        return contactList.count()
    }

    override fun onBindViewHolder(holder: AddEventContactListViewHolder, position: Int) {
        holder.name.text = contactList[position].name
        holder.guestCount.text=contactList[position].badge.toString()
    }
   inner class AddEventContactListViewHolder(view: View,context: Context) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.DashBoard_Add_Event_ContactList_Name)
        val guestCount = view.findViewById<TextView>(R.id.DashBoard_Add_Event_ContactList_ContactCount)
        val guestText = view.findViewById<TextView>(R.id.DashBoard_Add_Event_ContactList_ContactText)
        val checkBox = view.findViewById<CheckBox>(R.id.DashBoard_Add_Event_ContactList_CheckBox)
        val row = view.findViewById<RelativeLayout>(R.id.DashBoard_Add_Event_ContactList_View)
        init {
            row.setOnClickListener {
                if(!checkBox.isChecked){
                    checkBox.isChecked=true
                    name.setTextColor(ContextCompat.getColor(context,R.color.Blue))
                    guestText.setTextColor(ContextCompat.getColor(context,R.color.Black))
                    guestCount.setTextColor(ContextCompat.getColor(context,R.color.Black))
                    val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    guestCount.typeface=boldTypeface
                    selectedList.add(contactList[adapterPosition])
                }
                else if(checkBox.isChecked){
                    checkBox.isChecked=false
                    name.setTextColor(ContextCompat.getColor(context,R.color.Black))
                    guestText.setTextColor(ContextCompat.getColor(context,R.color.LightGrey))
                    guestCount.setTextColor(ContextCompat.getColor(context,R.color.LightGrey))
                    val normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    guestCount.typeface=normalTypeface
                    selectedList.remove(contactList[adapterPosition])
                }
                addEventInterFace.guestCount(selectedList)
            }
            checkBox.setOnClickListener {
                if(checkBox.isChecked){
                    name.setTextColor(ContextCompat.getColor(context,R.color.Blue))
                    guestText.setTextColor(ContextCompat.getColor(context,R.color.Black))
                    guestCount.setTextColor(ContextCompat.getColor(context,R.color.Black))
                    val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    guestCount.typeface=boldTypeface
                    selectedList.add(contactList[adapterPosition])
                }
                else if(!checkBox.isChecked){
                    name.setTextColor(ContextCompat.getColor(context,R.color.Black))
                    guestText.setTextColor(ContextCompat.getColor(context,R.color.LightGrey))
                    guestCount.setTextColor(ContextCompat.getColor(context,R.color.LightGrey))
                    val normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    guestCount.typeface=normalTypeface
                    selectedList.remove(contactList[adapterPosition])
                }
                addEventInterFace.guestCount(selectedList)
            }
        }

    }
}