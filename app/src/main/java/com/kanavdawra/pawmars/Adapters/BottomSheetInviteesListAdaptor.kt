package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Modals.VerifyContacts
import com.kanavdawra.pawmars.R
import me.grantland.widget.AutofitTextView


class BottomSheetInviteesListAdaptor(val context: Context, var inviteesList: ArrayList<VerifyContacts>) : RecyclerView.Adapter<BottomSheetInviteesListAdaptor.BottomSheetInviteesListAdaptorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetInviteesListAdaptorViewHolder {
        return BottomSheetInviteesListAdaptorViewHolder(LayoutInflater.from(context).inflate(R.layout.bottomsheet_inviteeslist_row_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return inviteesList.count()
    }

    override fun onBindViewHolder(holder: BottomSheetInviteesListAdaptorViewHolder, position: Int) {
        var In = ""
        var out = ""
        In = In + inviteesList[position].In!![0] + inviteesList[position].In!![1] + inviteesList[position].In!![2] + inviteesList[position].In!![3] + inviteesList[position].In!![4]
        if (inviteesList[position].out != null) {
            out = out + inviteesList[position].out!![0] + inviteesList[position].out!![1] + inviteesList[position].out!![2] + inviteesList[position].out!![3] + inviteesList[position].out!![4]
        }
        holder.name.text = inviteesList[position].name
        holder.In.text = In
        holder.out.text = out
    }

    inner class BottomSheetInviteesListAdaptorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<AutofitTextView>(R.id.BottomSheet_InviteesList_Name)
        val In = view.findViewById<AutofitTextView>(R.id.BottomSheet_InviteesList_In)
        val out = view.findViewById<AutofitTextView>(R.id.BottomSheet_InviteesList_Out)
    }
}