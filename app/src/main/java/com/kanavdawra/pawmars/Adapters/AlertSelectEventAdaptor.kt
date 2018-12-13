package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.InterFace.TaskInterFace
import com.kanavdawra.pawmars.R
import me.grantland.widget.AutofitTextView

class AlertSelectEventAdaptor(val context: Context, var eventList: ArrayList<String>, val dialogInterface: DialogInterface,val taskInterface:TaskInterFace) : RecyclerView.Adapter<AlertSelectEventAdaptor.AlertSelectEventAdaptorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertSelectEventAdaptorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.alert_select_event_recycler_view_row_layout, parent, false)
        return AlertSelectEventAdaptorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.count()
    }

    override fun onBindViewHolder(holder: AlertSelectEventAdaptorViewHolder, position: Int) {
        holder.eventName.text = eventList[position]
    }

    inner class AlertSelectEventAdaptorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName = view.findViewById<AutofitTextView>(R.id.Alert_Select_Event_Row_TextView)
var message=ArrayList<String>()
        init {
            eventName.setOnClickListener {
                message.add(eventList[adapterPosition])
                taskInterface.string(message)
                dialogInterface.dismiss()
                DashBoardUtility().sendMessage(context, eventList[adapterPosition], R.id.DashBoard_ToolBar_EventName)
            }
        }
    }
}