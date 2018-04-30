package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kanavdawra.pawmars.Constants.selectedcontact
import com.kanavdawra.pawmars.DashBoardToolBar
import com.kanavdawra.pawmars.Modals.Contact
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import de.hdodenhof.circleimageview.CircleImageView


class ContactsAdapter(val context: Context, var contacts: ArrayList<Contact>) : StickyRecyclerHeadersAdapter<ContactsAdapter.ContactsHeaderViewHolder>, RecyclerView.Adapter<ContactsAdapter.ContactsRowViewHolder>() {


    var view: View? = null
    var selectionCheck = false
    var selectionCount = 0
    val imageColor = java.util.ArrayList<String>()

    init {
        imageColor.add(0, "#EF5350")
        imageColor.add(1, "#EC407A")
        imageColor.add(2, "#AB47BC")
        imageColor.add(3, "#7E57C2")
        imageColor.add(4, "#1E88E5")
        imageColor.add(5, "#009688")
        imageColor.add(6, "#43A047")
        imageColor.add(7, "#FFC107")
        imageColor.add(8, "#F4511E")
        imageColor.add(9, "#3E2723")


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsRowViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.row_contacts, parent, false)
        return ContactsRowViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ContactsRowViewHolder, position: Int) {
        holder.name.setTextColor(ContextCompat.getColor(context,R.color.Black))
        holder.name.text = contacts[position].name
        val ccodeCheck = contacts[position].phoneNumber[0]
        if (ccodeCheck.toString() != "+" && contacts[position].phoneNumber.count()>=10) {
            holder.name.text = "*Add Country Code"
            holder.name.setTextColor(ContextCompat.getColor(context,R.color.Red))
        }
        holder.imageText.text = getImageText(position)
        holder.image.setImageDrawable(ColorDrawable(getImageColor(position)))
        if (contacts[position].isSelected) {
            holder.row.setBackgroundColor(ContextCompat.getColor(context, R.color.TranslucentBlue))
        } else {
            holder.row.setBackgroundColor(ContextCompat.getColor(context, R.color.Transparent))
        }
        if (contacts[position].email == "") {
            holder.email.visibility = View.GONE
        } else {
            holder.email.visibility = View.VISIBLE
        }

    }

    fun getImageText(position: Int): String {
        val temp = contacts[position].name.toUpperCase()
        if (temp[0] in 'A'..'z') {

        } else {
            return "#"
        }
        val text = java.util.ArrayList<Char>()
        text.add(temp[0])
        for (char in 0..temp.length - 1) {
            if (temp[char].toString() == " ") {

                text.add(temp[char + 1])
                return text.joinToString().removeRange(1, 3)

            }
        }
        text.add(temp[1])
        return text.joinToString().removeRange(1, 3)
    }

    fun getImageColor(position: Int): Int {
        val text = contacts[position].name.length
        println(text)
        return Color.parseColor(imageColor[text % 10])
    }

    override fun getItemCount(): Int {
        return contacts.count()
    }

    override fun getHeaderId(position: Int): Long {
        return if (contacts[position].name[0] in 'A'..'z') {
            contacts[position].name[0].toLong()
        } else {
            '#'.toLong()
        }
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): ContactsHeaderViewHolder {
        return ContactsHeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.header_contacts_layout, parent, false))
    }

    override fun onBindHeaderViewHolder(holder: ContactsHeaderViewHolder?, position: Int) {

        if (contacts[position].name[0] in 'A'..'z') {
            holder!!.header.text = contacts[position].name[0].toString()
        } else {
            holder!!.header.text = "#"
        }
    }

    fun selectedContacts(): ArrayList<Contact> {
        val selectedContacts = ArrayList<Contact>()
        for (data in contacts) {
            if (data.isSelected) {
                data.isSelected = false
                selectedContacts.add(data)
            }
        }
        return selectedContacts
    }

    fun deSelectAll() {
        for (data in 0..contacts.count() - 1) {
            if (contacts[data].isSelected) {
                contacts[data].isSelected = false
                selectionCheck = false
                selectionCount = 0
            }
        }

        notifyDataSetChanged()
    }

    inner class ContactsRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.row_contact_name)
        var row = view.findViewById<RelativeLayout>(R.id.row_contact_row)
        var email = view.findViewById<ImageView>(R.id.row_contact_email)
        var imageText = view.findViewById<TextView>(R.id.row_contact_image_textView)
        var image = view.findViewById<CircleImageView>(R.id.row_contact_image)

        init {

            view.setOnLongClickListener({
                if (!selectionCheck) {
                    row.setBackgroundColor(ContextCompat.getColor(context, R.color.TranslucentBlue))
                    contacts[adapterPosition].isSelected = true
                    selectionCheck = true
                    selectionCount++
                    DashBoardToolBar(context).addCancel()
                }
                true
            })



            view.setOnClickListener {
                if (selectionCheck) {
                    if (contacts[adapterPosition].isSelected) {
                        row.setBackgroundColor(ContextCompat.getColor(context, R.color.Transparent))
                        contacts[adapterPosition].isSelected = false
                        selectionCount--
                    } else {
                        row.setBackgroundColor(ContextCompat.getColor(context, R.color.TranslucentBlue))
                        contacts[adapterPosition].isSelected = true
                        selectionCount++
                    }

                } else {
                    selectedcontact = contacts[adapterPosition].contact_id
                    PopUpFragmnent(context).create("ContactsUpdate")
                }

                if (selectionCount <= 0) {
                    selectionCheck = false
                    DashBoardToolBar(context).clean()
                }
            }
        }

    }


    class ContactsHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var header = view.findViewById<TextView>(R.id.header_contact_textview)
    }
}