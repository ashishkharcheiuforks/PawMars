package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Modals.EventContact
import com.kanavdawra.pawmars.Modals.ParcelableEventContact
import com.kanavdawra.pawmars.R
import com.tuyenmonkey.mkloader.MKLoader
import me.grantland.widget.AutofitTextView
import net.igenius.customcheckbox.CustomCheckBox
import java.util.zip.Inflater


class EventSmsEmailSend(val context: Context, var contact: ArrayList<ParcelableEventContact>) : RecyclerView.Adapter<EventSmsEmailSend.EventSmsEmailSendViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSmsEmailSendViewHolder {
        return EventSmsEmailSendViewHolder(LayoutInflater.from(context).inflate(R.layout.sms_send_row_template, parent, false))
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun onBindViewHolder(holder: EventSmsEmailSendViewHolder, position: Int) {
        holder.smsCheckBox.visibility = View.GONE
        holder.smsLoader.visibility = View.VISIBLE

        holder.emailCheckBox.visibility = View.GONE
        holder.emailLoader.visibility = View.VISIBLE

        holder.name.text = contact[position].name

        println(contact[position].emailId)
        if (contact[position].phoneNo != "") {
            holder.smsLoader.visibility = View.VISIBLE
            if (contact[position].phnoBool==1) {
                holder.smsLoader.visibility = View.GONE
                holder.smsCheckBox.visibility = View.VISIBLE
                holder.smsCheckBox.setChecked(true, true)
            } else if(contact[position].phnoBool==0){
                holder.smsLoader.visibility = View.VISIBLE
                holder.smsCheckBox.visibility = View.GONE
            }else{

            }

        }else{
            holder.smsLoader.visibility = View.GONE
        }

        if (contact[position].emailId != "") {
            holder.emailLoader.visibility = View.VISIBLE
            if (contact[position].emailBool==1) {
                holder.emailLoader.visibility = View.GONE
                holder.emailCheckBox.visibility = View.VISIBLE
                holder.emailCheckBox.setChecked(true, true)
            } else if(contact[position].emailBool==0){

                holder.emailLoader.visibility = View.VISIBLE
                holder.emailCheckBox.visibility = View.GONE
            }
            else{}
        }
        else{
            holder.emailLoader.visibility = View.GONE
        }

    }

    class EventSmsEmailSendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<AutofitTextView>(R.id.SMS_send_Name)
        var smsLoader = view.findViewById<MKLoader>(R.id.SMS_send_SMS_Loader)
        var emailLoader = view.findViewById<MKLoader>(R.id.SMS_send_Email_Loader)
        var emailCheckBox = view.findViewById<CustomCheckBox>(R.id.SMS_send_Email_CheckBox)
        var smsCheckBox = view.findViewById<CustomCheckBox>(R.id.SMS_send_SMS_CheckBox)
    }
}