package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.*
import com.kanavdawra.pawmars.Adapters.DashBoardEventDetailsImagePagerAdaptor
import com.kanavdawra.pawmars.Adapters.EventContactDetailsContactsAdaptor
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.Constants.eventViewPagerReceiver
import com.kanavdawra.pawmars.DataBase.DataBase
import kotlinx.android.synthetic.main.fragment_dash_board_event_details.*


class DashBoardEventDetailsFragment : Fragment() {

    var image1: String? = null
    var image2: String? = null
    var image3: String? = null
    var image4: String? = null
    var ShPref: SharedPreferences? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DashBoard_Event_Details_Event_Name.text = eventName
        fetchData()
        clickListners()
    }

    fun clickListners() {
        DashBoard_Event_Details_Back.setOnClickListener {
         // activity!!.unregisterReceiver(eventViewPagerReceiver)
            PopUpFragmnent(activity!!).dismiss("EventViewPager")
        }

        DashBoard_Event_Details_Contact_Expand.setOnClickListener {
            DashBoard_Event_Details_Contact_Display_Layout.visibility = View.GONE
            DashBoard_Event_Details_Expandable_Layout.expand(true)
        }

        DashBoard_Event_Details_Contact_Collapse.setOnClickListener {
            DashBoard_Event_Details_Expandable_Layout.collapse(true)
            DashBoard_Event_Details_Contact_Display_Layout.visibility = View.VISIBLE
        }

        DashBoard_Event_Details_Delete.setOnClickListener {
            AlertDialog.Builder(activity!!).setMessage("By clicking Delete the event will be deleted permanently.")
                    .setTitle("Are you sure?")
                    .setIcon(R.drawable.ic_trash_can_x_dark)
                    .setPositiveButton("Delete") { dialogInterface, i ->
                        deleteEvent(eventName)
                    }.setNegativeButton("Cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()

                    }.show()

        }

        DashBoard_Event_Details_Edit.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(0)
        }

        DashBoard_Event_Details_Next.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(2)
        }

    }

    fun fetchImages() {
        val imageArrayList = ArrayList<String>()
        try {
            image1 = Utility(activity!!).fetchUrl("Events", "Event_${eventName}_Image1")
        } catch (e: Exception) {
        }
        try {
            image2 = Utility(activity!!).fetchUrl("Events", "Event_${eventName}_Image2")
        } catch (e: Exception) {
        }
        try {
            image3 = Utility(activity!!).fetchUrl("Events", "Event_${eventName}_Image3")
        } catch (e: Exception) {
        }
        try {
            image4 = Utility(activity!!).fetchUrl("Events", "Event_${eventName}_Image4")
        } catch (e: Exception) {
        }
        val shPref = activity!!.getSharedPreferences("Event_$eventName", 0)
        ShPref = shPref
        if (shPref.getString("Image1", "") != "null") {
            println("1 " + activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image1", "null"))
            imageArrayList.add(image1!!)
        }
        if (shPref.getString("Image2", "") != "null") {
            println("2 " + activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image2", "null"))
            imageArrayList.add(image2!!)
        }
        if (shPref.getString("Image3", "") != "null") {
            println("3 " + activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image3", "null"))
            imageArrayList.add(image3!!)
        }
        if (shPref.getString("Image4", "") != "null") {
            println("4 " + activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image4", "null"))
            imageArrayList.add(image4!!)
        }

        imageViewPager(imageArrayList)
    }

    fun findDay(year: Int, month: Int, date: Int): String {
        var month = month
        var year = year
        if (month == 1) {
            month = 13
            year--
        }
        if (month == 2) {
            month = 14
            year--
        }
        val q = date
        val k = year % 100
        val j = year / 100
        var h = q + 13 * (month + 1) / 5 + k + k / 4 + j / 4 + 5 * j
        h = h % 7
        if (h == 0) {
            return "Saturday"
        }
        if (h == 1) {
            return "Sunday"
        }
        if (h == 2) {
            return "Monday"
        }
        if (h == 3) {
            return "Tuesday"
        }
        if (h == 4) {
            return "Wednesday"
        }
        if (h == 5) {
            return "Thursday"
        } else {
            return "Friday"
        }
    }

    fun findMonth(month: Int): String {
        if (month == 1) {
            return "January"
        }
        if (month == 2) {
            return "February"
        }
        if (month == 3) {
            return "March"
        }
        if (month == 4) {
            return "April"
        }
        if (month == 5) {
            return "May"
        }
        if (month == 6) {
            return "June"
        }
        if (month == 7) {
            return "July"
        }
        if (month == 8) {
            return "August"
        }
        if (month == 9) {
            return "September"
        }
        if (month == 10) {
            return "October"
        }
        if (month == 11) {
            return "November"
        } else {
            return "December"
        }

    }

    fun fetchData() {
        fetchImages()
        val shPref = activity!!.getSharedPreferences("Event_$eventName", 0)
        val year = shPref.getInt("Year", 2000)
        val month = shPref.getInt("Month", 1)
        val date = shPref.getInt("Date", 1)
        val hour = shPref.getInt("Hour", 1)
        val minute = shPref.getInt("Minute", 1)
        val am_pm = shPref.getString("AM_PM", "AM")
        val timeZone = shPref.getString("TimeZone", "GMT +5:30")
        val day = findDay(year, month, date)
        val monthEnglish = findMonth(month)
        var dateSuffix = "th"
        if (date == 1) {
            dateSuffix = "st"
        }
        if (date == 2) {
            dateSuffix = "nd"
        }
        if (date == 3) {
            dateSuffix = "rd"
        }
        var hourSuffix = ""
        if (hour <= 9) {
            hourSuffix = "0"
        }
        var minuteSuffix = ""
        if (minute <= 9) {
            minuteSuffix = "0"
        }

        val finalDate = "$day, $date$dateSuffix $monthEnglish, $year"
        val time = "$hourSuffix$hour:$minuteSuffix$minute $am_pm, $timeZone"
        val placeName = shPref.getString("PlaceName", "Martian Palace")
        val addressLine1 = shPref.getString("AddressLine1", "Mars, sōlāris, MilkyWay")
        val addressLine2 = shPref.getString("AddressLine2", "25.369, 30.654")
        val city = shPref.getString("City", "Drextron")
        val pinCode = shPref.getString("PinCode", "DER458")
        val country = shPref.getString("Country", "Deratorly")
        val description = shPref.getString("Description", "Description")
        val city_pinCode = "$city, $pinCode"
        if (description == "") {
            DashBoard_Event_Details_Description.visibility = View.GONE
        }
        if (addressLine1 == "") {
            DashBoard_Event_Details_AddressLine1.visibility = View.GONE
        }
        if (addressLine2 == "") {
            DashBoard_Event_Details_AddressLine2.visibility = View.GONE
        }
        setData(time, finalDate, placeName, addressLine1, addressLine2, city_pinCode, country, description)
    }

    fun fetchContacts() {
        val dataBase = DataBase(activity!!).readableDatabase
        val length = ShPref!!.all.size - 20
        val plusSize = "+${length - 4}"
        if (ShPref!!.all.size - 24 == 0) {
            DashBoard_Event_Details_Contact_RemainingCount.visibility = View.GONE
        }
        var name1 = ""
        var name2 = ""
        var name3 = ""
        var name4 = ""
        DashBoard_Event_Details_Contact_RemainingCount.text = plusSize



        try {
            val temp = dataBase.rawQuery("Select name from contacts where contact_id='${ShPref!!.getString("data1", "2015")}'", null)
            temp.moveToFirst()
            val n1 = temp.getString(temp.getColumnIndex("name"))
            name1 = n1
            temp.close()
        } catch (e: Exception) {
            println(e)
        }


        try {
            val temp = dataBase.rawQuery("Select name from contacts where contact_id='${ShPref!!.getString("data2", "2015")}'", null)
            temp.moveToFirst()
            val n2 = temp.getString(temp.getColumnIndex("name"))
            name2 = n2
            temp.close()
        } catch (e: Exception) {
            println(e)
        }

        try {
            val temp = dataBase.rawQuery("Select name from contacts where contact_id='${ShPref!!.getString("data3", "2015")}'", null)
            temp.moveToFirst()
            val n3 = temp.getString(temp.getColumnIndex("name"))
            name3 = n3
            temp.close()
        } catch (e: Exception) {
            println(e)
        }


        try {
            val temp = dataBase.rawQuery("SELECT * FROM contacts WHERE contact_id='${ShPref!!.getString("data4", "2015")}'", null)
            temp.moveToFirst()
            val n4 = temp.getString(temp.getColumnIndex("name"))
            name4 = n4
            temp.close()
        } catch (e: Exception) {
        }

        setContacts(name1, name2, name3, name4)
    }

    fun setContacts(name1: String, name2: String, name3: String, name4: String) {
        if (name1 != "") {
            DashBoard_Event_Details_Contact_Image1.setImageDrawable(ColorDrawable(getImageColor(name1)))
            DashBoard_Event_Details_Contact_TextView1.text = getImageText(name1)
        } else {
            DashBoard_Event_Details_Contact_Image1.visibility = View.GONE
            DashBoard_Event_Details_Contact_TextView1.visibility = View.GONE
        }
        if (name2 != "") {
            DashBoard_Event_Details_Contact_Image2.setImageDrawable(ColorDrawable(getImageColor(name2)))
            DashBoard_Event_Details_Contact_TextView2.text = getImageText(name2)
        } else {
            DashBoard_Event_Details_Contact_Image2.visibility = View.GONE
            DashBoard_Event_Details_Contact_TextView2.visibility = View.GONE
        }
        if (name3 != "") {
            DashBoard_Event_Details_Contact_Image3.setImageDrawable(ColorDrawable(getImageColor(name3)))
            DashBoard_Event_Details_Contact_TextView3.text = getImageText(name3)
        } else {
            DashBoard_Event_Details_Contact_Image3.visibility = View.GONE
            DashBoard_Event_Details_Contact_TextView3.visibility = View.GONE
        }
        if (name4 != "") {
            DashBoard_Event_Details_Contact_Image4.setImageDrawable(ColorDrawable(getImageColor(name4)))
            DashBoard_Event_Details_Contact_TextView4.text = getImageText(name4)
        } else {
            DashBoard_Event_Details_Contact_Image4.visibility = View.GONE
            DashBoard_Event_Details_Contact_TextView4.visibility = View.GONE
        }
        setContactsAdaptor()
    }

    fun setContactsAdaptor() {
        println("setContactsAdaptor " + Modal(activity!!).eventNamestoModal(eventName).count())
        DashBoard_Event_Details_Contact_Expand_RecyclerView.adapter = EventContactDetailsContactsAdaptor(activity!!, Modal(activity!!).eventNamestoModal(eventName))
        DashBoard_Event_Details_Contact_Expand_RecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    fun setData(time: String, date: String, placeName: String, addressLine1: String, addressLine2: String, city_pinCode: String, country: String, description: String) {
        DashBoard_Event_Details_Time.text = time
        DashBoard_Event_Details_Date.text = date
        DashBoard_Event_Details_PlaceName.text = placeName
        DashBoard_Event_Details_AddressLine1.text = addressLine1
        DashBoard_Event_Details_AddressLine2.text = addressLine2
        DashBoard_Event_Details_City_PinCode.text = city_pinCode
        DashBoard_Event_Details_Country.text = country
        DashBoard_Event_Details_Description.text = description
        fetchContacts()
    }

    fun imageViewPager(imageArrayList: ArrayList<String>) {
        DashBoard_Event_Details_ImagesViewPager_Indicators.count = imageArrayList.size
        DashBoard_Event_Details_ImagesViewPager_Indicators.selection = 0
        DashBoard_Event_Details_ImagesViewPager.adapter = DashBoardEventDetailsImagePagerAdaptor(childFragmentManager, activity!!, imageArrayList)
    }

    fun deleteEvent(eventName: String) {
        DataBase(activity!!).writableDatabase.execSQL("DELETE FROM eventList WHERE name='$eventName'")

        try {
            if (activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image1", "null") != "null") {
                Utility(activity!!).deleteImage("Events", "Event_${eventName}_Image1")
            }
            if (activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image2", "null") != "null") {
                Utility(activity!!).deleteImage("Events", "Event_${eventName}_Image2")
            }
            if (activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image3", "null") != "null") {
                Utility(activity!!).deleteImage("Events", "Event_${eventName}_Image3")
            }
            if (activity!!.getSharedPreferences("Event_$eventName", 0).getString("Image4", "null") != "null") {
                Utility(activity!!).deleteImage("Events", "Event_${eventName}_Image4")
            }
        } catch (e: Exception) {
        }
        activity!!.getSharedPreferences("Event_$eventName", 0).edit().clear().apply()
        PopUpFragmnent(activity!!).dismiss("EventViewPager")
        DashBoardContactsUtility(activity!!).notifyDataSetChanged("EventsList")
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
        println(text)
        return Color.parseColor(imageColor[text % 10])
    }
}