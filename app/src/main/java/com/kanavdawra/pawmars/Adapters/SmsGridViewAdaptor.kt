package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kanavdawra.pawmars.Modals.EventContact
import com.kanavdawra.pawmars.R
import de.hdodenhof.circleimageview.CircleImageView


class SmsGridViewAdaptor(val context: Context, var contacts: ArrayList<EventContact>) : RecyclerView.Adapter<SmsGridViewAdaptor.SmsGridVeiwViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsGridVeiwViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.name_grid_cancelable_layout, parent, false)
        return SmsGridVeiwViewHolder(view)
    }


    override fun getItemCount(): Int {
        return contacts.count()
    }

    override fun onBindViewHolder(holder: SmsGridVeiwViewHolder, position: Int) {
        println(contacts[position].name.length % 10)
        if (contacts[position].name.length % 10 == 0) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_red)
        } else if (contacts[position].name.length % 10 == 1) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_pink)
        } else if (contacts[position].name.length % 10 == 2) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_purple)
        } else if (contacts[position].name.length % 10 == 3) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_indigo)
        } else if (contacts[position].name.length % 10 == 4) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_blue)
        } else if (contacts[position].name.length % 10 == 5) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_teal)
        } else if (contacts[position].name.length % 10 == 6) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_green)
        } else if (contacts[position].name.length % 10 == 7) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_amber)
        } else if (contacts[position].name.length % 10 == 8) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_orange)
        } else if (contacts[position].name.length % 10 == 9) {
            holder.image.setImageDrawable(ColorDrawable(getImageColor(contacts[position].name)))
            holder.imageText.text = getImageText(contacts[position].name)
            holder.name.text = "${contacts[position].name}    X"
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded_corners_brown)
        }


    }

    fun getImageText(name: String): String {
        val temp = name.toUpperCase()
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

    fun getImageColor(name: String): Int {
        val text = name.length
        return Color.parseColor(imageColor[text % 10])
    }

    inner class SmsGridVeiwViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.Grid_Cancelable_Name)
        var image = view.findViewById<CircleImageView>(R.id.Grid_Cancelable_Image)
        var imageText = view.findViewById<TextView>(R.id.Grid_Cancelable_Image_Text)
    }
}