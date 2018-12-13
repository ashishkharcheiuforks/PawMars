package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.Modals.Event
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R
import com.kanavdawra.pawmars.Utility
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*


class EventsAdaptor(val context: Context, var eventsList: ArrayList<Event>) : RecyclerView.Adapter<EventsAdaptor.EventsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.events_card_layout, parent, false)
        return EventsViewHolder(view)
    }

    override fun getItemCount(): Int {
        println(eventsList.count())
        return eventsList.count()
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.name.text = eventsList[position].eventName
        holder.invitees.text = eventsList[position].inviteesCount.toString()
        holder.date_time.text = dateTime(position)
        val string = """${eventsList[position].placeName}, ${eventsList[position].city},
            |${eventsList[position].country}""".trimMargin()
        holder.place_city.text = string
        try {
            if (eventsList[position].image != "") {
                Picasso.get().load(File(eventsList[position].image)).placeholder(R.mipmap.ic_camera_fore).error(R.mipmap.ic_camera_fore).into(holder.image)
            }
        } catch (e: Exception) {
        }
        if(eventsList[position].tab=="H"){
            holder.date_time_modal.text="Was on"
        }else{
            holder.date_time_modal.text="Is on"
        }
    }

    fun dateTime(position: Int): String {
        val now = Calendar.getInstance()
        var returnString = "null"
        if (eventsList[position].year != now.get(Calendar.YEAR)) {
            returnString = "${eventsList[position].year}, ${getMonth(eventsList[position].month, "long")}"
        }
        if (eventsList[position].year == now.get(Calendar.YEAR)) {
            returnString = "${eventsList[position].date}, ${getMonth(eventsList[position].month, "long")}"
        }
        if (eventsList[position].month == now.get(Calendar.MONTH)) {
            returnString = "${eventsList[position].date}, ${getFullDay(eventsList[position].day)}"
        }
        if (eventsList[position].date - now.get(Calendar.DATE) <= 7) {
            returnString = "${eventsList[position].hour}:${eventsList[position].minute} ${eventsList[position].am_pm}, ${getFullDay(eventsList[position].day)}"
        }
        if (eventsList[position].date == now.get(Calendar.DATE)) {
            returnString = "${eventsList[position].hour}:${eventsList[position].minute} ${eventsList[position].am_pm}, Today"
        }
        return returnString
    }

    fun getMonth(month: Int, length: String): String {
        if (month == 1) {
            if (length == "long") {
                return "January"
            } else if (length == "short") {
                return "Jan"
            }
        }
        if (month == 2) {
            if (length == "long") {
                return "February"
            } else if (length == "short") {
                return "Feb"
            }
        }
        if (month == 3) {
            if (length == "long") {
                return "March"
            } else if (length == "short") {
                return "March"
            }
        }
        if (month == 4) {
            if (length == "long") {
                return "April"
            } else if (length == "short") {
                return "April"
            }
        }
        if (month == 5) {
            if (length == "long") {
                return "May"
            } else if (length == "short") {
                return "May"
            }
        }
        if (month == 6) {
            if (length == "long") {
                return "June"
            } else if (length == "short") {
                return "June"
            }
        }
        if (month == 7) {
            if (length == "long") {
                return "July"
            } else if (length == "short") {
                return "July"
            }
        }
        if (month == 8) {
            if (length == "long") {
                return "August"
            } else if (length == "short") {
                return "Aug"
            }
        }
        if (month == 9) {
            if (length == "long") {
                return "September"
            } else if (length == "short") {
                return "Sept"
            }
        }
        if (month == 10) {
            if (length == "long") {
                return "October"
            } else if (length == "short") {
                return "Oct"
            }
        }
        if (month == 11) {
            if (length == "long") {
                return "November"
            } else if (length == "short") {
                return "Nov"
            }
        }
        if (length == "long") {
            return "December"
        } else {
            return "Dec"
        }


    }

    fun getFullDay(day: String): String {
        if (day == "Mon") {
            return "Monday"
        } else if (day == "Tues") {
            return "Monday"
        } else if (day == "Wed") {
            return "Wednesday"
        } else if (day == "Thurs") {
            return "Thursday"
        } else if (day == "Fri") {
            return "Friday"
        } else if (day == "Sat") {
            return "Saturday"
        } else {
            return "Sunday"
        }

    }

    inner class EventsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.event_layout_name)
        var invitees = view.findViewById<TextView>(R.id.event_layout_invitees)
        var date_time = view.findViewById<TextView>(R.id.event_layout_date_time)
        var place_city = view.findViewById<TextView>(R.id.event_layout_place_city)
        var image = view.findViewById<ImageView>(R.id.event_layout_image)
        var layout = view.findViewById<CardView>(R.id.event_layout_view)
        var date_time_modal =view.findViewById<TextView>(R.id.event_layout_date_time_Modal)
                init {
                    layout.setOnClickListener {
                        eventName = eventsList[adapterPosition].eventName
                        PopUpFragmnent(context).create("EventViewPager")
                    }
                }
    }
}