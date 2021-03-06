package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developers.imagezipper.ImageZipper
import com.kanavdawra.pawmars.*
import com.kanavdawra.pawmars.Adapters.AddEventContactListAdaptor
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.DataBase.DataBase
import com.kanavdawra.pawmars.InterFace.AddEventInterFace
import com.kanavdawra.pawmars.Modals.ContactList
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_dash_board_event_edit.*
import needle.Needle
import java.io.File
import java.util.*


class DashBoardEventEditFragment : Fragment() {
    var eventImage: Bitmap? = null
    var imageName = "image1"
    var image1: Bitmap? = null
    var image2: Bitmap? = null
    var image3: Bitmap? = null
    var image4: Bitmap? = null
    var Year = 0
    var Month = 1
    var Date = 1
    var Day = "mon"
    var Hour = 1
    var Minute = 1
    var Am_pm = "AM"
    var timeZone = TimeZone.getDefault()
    var contactListAdaptor: AddEventContactListAdaptor? = null
    var addEventInterface: AddEventInterFace? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        addEventInterface()
        setAdaptor()
        onClickListners()
    }

    fun setData() {
        val shPref = activity!!.getSharedPreferences("Event_$eventName", 0)
        DashBoard_Event_Edit_EventName.setText(eventName)
        DashBoard_Event_Edit_PlaceName.setText(shPref.getString("PlaceName", ""))
        DashBoard_Event_Edit_AddressLine1.setText(shPref.getString("AddressLine1", ""))
        DashBoard_Event_Edit_AddressLine2.setText(shPref.getString("AddressLine2", ""))
        DashBoard_Event_Edit_City.setText(shPref.getString("City", ""))
        DashBoard_Event_Edit_PinCode.setText(shPref.getString("PinCode", ""))
        DashBoard_Event_Edit_Description.setText(shPref.getString("Description", ""))
        var hour = shPref.getInt("hour", 12)
        if (shPref.getString("Am_PM", "AM") == "PM") {
            hour += 12
        }
        formatDateTime(shPref.getInt("Year", 2018), shPref.getInt("Month", 1), shPref.getInt("Date", 1),
                hour, shPref.getInt("Minute", 0), 0)
        if (shPref.getString("Image1", "null") != "null") {
            image1 = Utility(activity!!).fetchImage("Events", "Event_${eventName}_Image1")
            DashBoard_Event_Edit_Image1.setImageBitmap(image1)
            DashBoard_Event_Edit_Image1.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image2.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image1_Cross.visibility = View.VISIBLE

        }
        if (shPref.getString("Image2", "null") != "null") {
            image2 = Utility(activity!!).fetchImage("Events", "Event_${eventName}_Image2")
            DashBoard_Event_Edit_Image2.setImageBitmap(image2)
            DashBoard_Event_Edit_Image2.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image3.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image2_Cross.visibility = View.VISIBLE
        }
        if (shPref.getString("Image3", "null") != "null") {
            image3 = Utility(activity!!).fetchImage("Events", "Event_${eventName}_Image3")
            DashBoard_Event_Edit_Image3.setImageBitmap(image3)
            DashBoard_Event_Edit_Image3.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image4.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image3_Cross.visibility = View.VISIBLE
        }
        if (shPref.getString("Image4", "null") != "null") {
            image4 = Utility(activity!!).fetchImage("Events", "Event_${eventName}_Image4")
            DashBoard_Event_Edit_Image4.setImageBitmap(image4)
            DashBoard_Event_Edit_Image4.visibility = View.VISIBLE
            DashBoard_Event_Edit_Image4_Cross.visibility = View.VISIBLE
        }
    }

    fun setAdaptor() {
        contactListAdaptor = AddEventContactListAdaptor(activity!!, Modal(activity!!).contactListNameToModal(), addEventInterface!!)
        DashBoard_Event_Edit_ContactList.layoutManager = LinearLayoutManager(activity!!)
        DashBoard_Event_Edit_ContactList.adapter = contactListAdaptor
    }

    fun onClickListners() {
        DashBoard_Event_Edit_Back.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(1)
        }
        DashBoard_Event_Edit_Save.setOnClickListener {
            saveData()
        }
        DashBoard_Event_Edit_Calender_Clock.setOnClickListener {
            fetchDateTime()
        }
        DashBoard_Event_Edit_Image1.setOnClickListener {
            onImagePicker()
            imageName = "image1"
        }
        DashBoard_Event_Edit_Image2.setOnClickListener {
            onImagePicker()
            imageName = "image2"
        }
        DashBoard_Event_Edit_Image3.setOnClickListener {
            onImagePicker()
            imageName = "image3"
        }
        DashBoard_Event_Edit_Image4.setOnClickListener {
            onImagePicker()
            imageName = "image4"
        }
        DashBoard_Event_Edit_Image1_Cross.setOnClickListener { removeEventDefaultImage(1) }
        DashBoard_Event_Edit_Image2_Cross.setOnClickListener { removeEventDefaultImage(2) }
        DashBoard_Event_Edit_Image3_Cross.setOnClickListener { removeEventDefaultImage(3) }
        DashBoard_Event_Edit_Image4_Cross.setOnClickListener { removeEventDefaultImage(4) }

    }

    fun addEventDefaultImage() {

        if (DashBoard_Event_Edit_Image4.visibility == View.VISIBLE && DashBoard_Event_Edit_Image3.visibility == View.VISIBLE && DashBoard_Event_Edit_Image2.visibility == View.VISIBLE && DashBoard_Event_Edit_Image1.visibility == View.VISIBLE) {
            return
        } else if (DashBoard_Event_Edit_Image3.visibility == View.VISIBLE && DashBoard_Event_Edit_Image2.visibility == View.VISIBLE && DashBoard_Event_Edit_Image1.visibility == View.VISIBLE) {
            DashBoard_Event_Edit_Image4.visibility = View.VISIBLE
            return
        } else if (DashBoard_Event_Edit_Image2.visibility == View.VISIBLE && DashBoard_Event_Edit_Image1.visibility == View.VISIBLE) {
            DashBoard_Event_Edit_Image3.visibility = View.VISIBLE
            return
        } else if (DashBoard_Event_Edit_Image1.visibility == View.VISIBLE) {
            DashBoard_Event_Edit_Image2.visibility = View.VISIBLE
            return
        }

    }

    fun removeEventDefaultImage(image: Int) {
        if (image == 1) {
            image1 = image2
            image2 = image3
            image3 = image4
            image4 = null
        }
        if (image == 2) {
            image2 = image3
            image3 = image4
            image4 = null
        }
        if (image == 3) {
            image3 = image4
            image4 = null
        }
        if (image == 4) {
            image4 = null
        }
        DashBoard_Event_Edit_Image1.setImageBitmap(image1)
        DashBoard_Event_Edit_Image2.setImageBitmap(image2)
        DashBoard_Event_Edit_Image3.setImageBitmap(image3)
        DashBoard_Event_Edit_Image4.setImageBitmap(image4)
        if (image4 == null && image3 != null && image2 != null && image1 != null) {
            DashBoard_Event_Edit_Image4.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image4_Cross.visibility = View.GONE
        }
        if (image4 == null && image3 == null && image2 != null && image1 != null) {
            DashBoard_Event_Edit_Image3.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image4.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image4_Cross.visibility = View.GONE
            DashBoard_Event_Edit_Image4.visibility = View.GONE
            DashBoard_Event_Edit_Image3_Cross.visibility = View.GONE
        }
        if (image4 == null && image3 == null && image2 == null && image1 != null) {
            DashBoard_Event_Edit_Image4.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image3.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image2.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image4_Cross.visibility = View.GONE
            DashBoard_Event_Edit_Image4.visibility = View.GONE
            DashBoard_Event_Edit_Image3.visibility = View.GONE
            DashBoard_Event_Edit_Image3_Cross.visibility = View.GONE
            DashBoard_Event_Edit_Image2_Cross.visibility = View.GONE
        }
        if (image4 == null && image3 == null && image2 == null && image1 == null) {
            DashBoard_Event_Edit_Image4.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image3.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image2.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image1.setImageDrawable(ContextCompat.getDrawable(activity!!, R.mipmap.event_default_image))
            DashBoard_Event_Edit_Image4_Cross.visibility = View.GONE
            DashBoard_Event_Edit_Image4.visibility = View.GONE
            DashBoard_Event_Edit_Image3.visibility = View.GONE
            DashBoard_Event_Edit_Image3_Cross.visibility = View.GONE
            DashBoard_Event_Edit_Image2.visibility = View.GONE
            DashBoard_Event_Edit_Image2_Cross.visibility = View.GONE
            DashBoard_Event_Edit_Image1_Cross.visibility = View.GONE
        }
    }

    fun addEventInterface() {
        addEventInterface = object : AddEventInterFace {
            override fun guestCount(selectedContacts: ArrayList<ContactList>) {
                setTotalGuestCount(selectedContacts)
            }

        }
    }

    fun setTotalGuestCount(selectedContacts: ArrayList<ContactList>) {
        DashBoard_Event_Edit_ContactList_Contacts_Count.text = Modal(activity!!).addEventContactListContacts(selectedContacts).count().toString()
    }

    fun setImage() {
        if (imageName == "image1") {
            DashBoard_Event_Edit_Image1.setImageBitmap(eventImage)
            image1 = eventImage
            DashBoard_Event_Edit_Image1_Cross.visibility = View.VISIBLE
            addEventDefaultImage()
        } else if (imageName == "image2") {
            DashBoard_Event_Edit_Image2.setImageBitmap(eventImage)
            image2 = eventImage
            DashBoard_Event_Edit_Image2_Cross.visibility = View.VISIBLE
            addEventDefaultImage()
        } else if (imageName == "image3") {
            DashBoard_Event_Edit_Image3.setImageBitmap(eventImage)
            image3 = eventImage
            DashBoard_Event_Edit_Image3_Cross.visibility = View.VISIBLE
            addEventDefaultImage()
        } else if (imageName == "image4") {
            DashBoard_Event_Edit_Image4.setImageBitmap(eventImage)
            image4 = eventImage
            DashBoard_Event_Edit_Image4_Cross.visibility = View.VISIBLE
            addEventDefaultImage()
        }
    }

    fun fetchDateTime() {
        val now = Calendar.getInstance()

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener
        { view, year, monthOfYear, dayOfMonth ->
            now.set(Calendar.YEAR, year)
            now.set(Calendar.MONTH, monthOfYear)
            now.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val time = Calendar.getInstance()
            TimePickerDialog.newInstance(TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->
                time.set(Calendar.HOUR, hourOfDay)
                time.set(Calendar.MINUTE, minute)
                time.set(Calendar.SECOND, second)
                formatDateTime(year, monthOfYear + 1, dayOfMonth, hourOfDay, minute, second)

            }, time.get(Calendar.HOUR), time.get(Calendar.MINUTE), time.get(Calendar.SECOND), false).show(activity!!.fragmentManager, "TimePickerDialog")

        }, now).show(activity!!.fragmentManager, "DatePickerDialog")
    }

    fun setDateTime(time: String, day: String, date: String, timeZone: String) {
        DashBoard_Event_Edit_TimeText.text = time
        DashBoard_Event_Edit_DayText.text = day
        DashBoard_Event_Edit_DateText.text = date
        DashBoard_Event_Edit_TimeZoneText.text = timeZone
        changeViewSize()
    }

    fun formatDateTime(year: Int, month: Int, date: Int, hour: Int, minute: Int, second: Int) {
        var hour = hour
        var am_pm = "AM"
        if (hour > 12) {
            hour = hour - 12
            am_pm = "PM"
        }
        var dateZero = ""
        var hourZero = ""
        var minuteZero = ""
        if (date < 10) {
            dateZero = "0"
        }
        if (hour < 10) {
            hourZero = "0"
        }
        if (minute < 10) {
            minuteZero = "0"
        }
        val time = "$hourZero$hour:$minuteZero$minute $am_pm, "
        val day = findDay(year, month, date)
        val dateformat = ", $dateZero$date-${findMonth(month)}-$year, "
        val timeZone = findTimeZone()

        Year = year
        Month = month
        Date = date
        Day = day
        Hour = hour
        Minute = minute
        Am_pm = am_pm

        setDateTime(time, day, dateformat, timeZone)
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
            return "Sat"
        }
        if (h == 1) {
            return "Sun"
        }
        if (h == 2) {
            return "Mon"
        }
        if (h == 3) {
            return "Tues"
        }
        if (h == 4) {
            return "Wed"
        }
        if (h == 5) {
            return "Thurs"
        } else {
            return "Fri"
        }
    }

    fun findMonth(month: Int): String {
        if (month == 1) {
            return "Jan"
        }
        if (month == 2) {
            return "Feb"
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
            return "Aug"
        }
        if (month == 9) {
            return "Sept"
        }
        if (month == 10) {
            return "Oct"
        }
        if (month == 11) {
            return "Nov"
        } else {
            return "Dec"
        }

    }

    fun findTimeZone(): String {
        val tz = TimeZone.getDefault()
        val cal = GregorianCalendar.getInstance(tz)
        val offsetInMillis = tz.getOffset(cal.timeInMillis)

        var offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs(offsetInMillis / 60000 % 60))
        offset = "GMT " + (if (offsetInMillis >= 0) "+" else "-") + offset
        return offset
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
        DashBoardContactsUtility(activity!!).notifyDataSetChanged("EventsList")
    }

    fun saveData() {
        deleteEvent(eventName)
        val eventName = DashBoard_Event_Edit_EventName.text.toString()
        val placeName = DashBoard_Event_Edit_PlaceName.text.toString()
        val addressLine1 = DashBoard_Event_Edit_AddressLine1.text.toString()
        val addressLine2 = DashBoard_Event_Edit_AddressLine2.text.toString()
        val city = DashBoard_Event_Edit_City.text.toString()
        val country = DashBoard_Event_Edit_CountryPicker.selectedCountryName
        val pinCode = DashBoard_Event_Edit_PinCode.text.toString()
        val description = DashBoard_Event_Edit_Description.text.toString()
        val contactIds = fetchContactIDs(contactListAdaptor!!.selectedList)
        val checkValidation = validation(eventName, placeName, city, pinCode)
        if (checkValidation) {
            createSharedPrefrence(eventName, placeName, addressLine1, addressLine2, city, country, pinCode, description, contactIds)
            val databaseValue = ContentValues()
            databaseValue.put("name", eventName)
            DashBoard_Event_Edit_Save_Loader.visibility = View.VISIBLE
            DashBoard_Event_Edit_Save.visibility = View.GONE
            saveImages(eventName)

            DataBase(activity!!).writableDatabase.insert("eventList", null, databaseValue)



        }

    }

    var image1bool = false
    var image2bool = false
    var image3bool = false
    var image4bool = false
    fun saveCheck() {
        println("saveCheck")
        if (image1bool && image2bool && image3bool && image4bool) {
            Picasso.get().invalidate(File(Utility(activity!!).fetchUrl("Events","Event_${eventName}_Image1")))
            Picasso.get().invalidate(File(Utility(activity!!).fetchUrl("Events","Event_${eventName}_Image2")))
            Picasso.get().invalidate(File(Utility(activity!!).fetchUrl("Events","Event_${eventName}_Image3")))
            Picasso.get().invalidate(File(Utility(activity!!).fetchUrl("Events","Event_${eventName}_Image4")))
//            DashBoard_Event_Edit_Save_Loader.visibility = View.GONE
  //          DashBoard_Event_Edit_Save.visibility = View.VISIBLE
            DashBoardEventsUtility(activity!!).notifyDataSetChange(1)
            DashBoardEventsUtility(context!!).notifyDataSetChanged("EventsList")
        }
    }

    fun saveImages(eventName: String) {
        Needle.onBackgroundThread().withThreadPoolSize(10).execute {
            if (image1 != null) {
                Utility(activity!!).saveImage("Events", "Event_${eventName}_Image1", image1!!, 100)
                image1bool = true
                saveCheck()
            }
            else if(image1==null){
                image1bool = true
                saveCheck()
            }
        }

        Needle.onBackgroundThread().execute {
            if (image2 != null) {
                Utility(activity!!).saveImage("Events", "Event_${eventName}_Image2", image2!!, 100)
                image2bool = true
                saveCheck()
            }
            else if(image2==null){
                image2bool = true
                saveCheck()
            }
        }

        Needle.onBackgroundThread().execute {
            if (image3 != null) {
                Utility(activity!!).saveImage("Events", "Event_${eventName}_Image3", image3!!, 100)
                image3bool = true
                saveCheck()
            }
            else if(image3==null){
                image3bool = true
                saveCheck()
            }
        }

        Needle.onBackgroundThread().execute {
            if (image4 != null) {
                Utility(activity!!).saveImage("Events", "Event_${eventName}_Image4", image4!!, 100)
                image4bool = true
                saveCheck()
            }
            else if(image4==null){
                image4bool = true
                saveCheck()
            }
        }

    }

    fun fetchContactIDs(selectedContactLists: ArrayList<ContactList>): ArrayList<String> {
        return Modal(activity!!).addEventContactListContacts(selectedContactLists)
    }

    fun validation(eventName: String, placeName: String, city: String, pincode: String): Boolean {
        var bool = true
        if (eventName == "") {
            DashBoard_Event_Edit_EventName_Layout.error = "Event Name cannot be left empty."
            bool = false
        } else {
            DashBoard_Event_Edit_EventName_Layout.error = ""
        }
        if (placeName == "") {
            DashBoard_Event_Edit_PlaceName_Layout.error = "Place Name cannot be left empty."
            bool = false
        } else {
            DashBoard_Event_Edit_PlaceName_Layout.error = ""
        }
        if (city == "") {
            DashBoard_Event_Edit_City_Layout.error = "City field cannot be left empty."
            bool = false
        } else {
            DashBoard_Event_Edit_City_Layout.error = ""
        }
        if (pincode == "") {
            DashBoard_Event_Edit_PinCode_Layout.error = "Pincode field cannot be left empty."
            bool = false
        } else {
            DashBoard_Event_Edit_PinCode_Layout.error = ""
        }
        if (Year == 0) {
            DashBoard_Event_Edit_Time_Error.visibility = View.VISIBLE
            bool = false
        } else {
            DashBoard_Event_Edit_Time_Error.visibility = View.GONE
        }
        if (contactListAdaptor!!.selectedList.count() == 0) {
            DashBoardUtility().snackBar(activity!!, "You have select at least one Contact List.", R.color.Red, R.color.White)
            bool = false
        }
        val checkEvent = DataBase(activity!!).readableDatabase.rawQuery("SELECT * FROM eventList WHERE name='$eventName'", null)
        checkEvent.moveToFirst()
        try {
            if (checkEvent.getString(checkEvent.getColumnIndex("name")) == eventName) {
                DashBoardUtility().snackBar(activity!!, "Event name should be unique.", R.color.Red, R.color.White)
                bool = false
            }
        } catch (e: Exception) {
        }
        checkEvent.close()
        return bool
    }

    fun createSharedPrefrence(eventName: String, placeName: String, addressLine1: String, addressLine2: String, city: String, country: String,
                              pincode: String, description: String, contactsIds: ArrayList<String>) {

        val editor = activity!!.getSharedPreferences("Event_$eventName", 0).edit()
                .putString("EventName", eventName)
                .putString("PlaceName", placeName)
                .putString("AddressLine1", addressLine1)
                .putString("AddressLine2", addressLine2)
                .putString("City", city)
                .putString("PinCode", pincode)
                .putString("Country", country)
                .putString("Description", description)
                .putInt("Year", Year)
                .putInt("Month", Month)
                .putInt("Date", Date)
                .putString("Day", Day)
                .putInt("Hour", Hour)
                .putInt("Minute", Minute)
                .putString("AM_PM", Am_pm)
                .putString("TimeZone", findTimeZone())
        var i = 1
        for (contact in contactsIds) {
            editor.putString("data$i", contact)
            i++
        }
        if (image1 != null) {
            editor.putString("Image1", "Event_${eventName}_Image1")
        } else {
            editor.putString("Image1", "null")
        }
        if (image2 != null) {
            editor.putString("Image2", "Event_${eventName}_Image2")
        } else {
            editor.putString("Image2", "null")
        }
        if (image3 != null) {
            editor.putString("Image3", "Event_${eventName}_Image3")
        } else {
            editor.putString("Image3", "null")
        }
        if (image4 != null) {
            editor.putString("Image4", "Event_${eventName}_Image4")
        } else {
            editor.putString("Image4", "null")
        }
        editor.apply()
    }

    fun onImagePicker() {
        val image = ArrayList<Image>()

        ImagePicker.with(this)
                .setToolbarColor("#0be9be")
                .setStatusBarColor("#008975")
                .setToolbarTextColor("#FFFFFF")
                .setToolbarIconColor("#FFFFFF")
                .setProgressBarColor("#4CAF50")
                .setBackgroundColor("#FFFFFF")
                .setCameraOnly(false)
                .setMultipleMode(false)
                .setFolderMode(true)
                .setShowCamera(true)
                .setDoneTitle("Done")
                .setSelectedImages(image)
                .setKeepScreenOn(true)
                .start()


    }

    fun changeViewSize() {
        val layout = activity!!.findViewById<View>(R.id.DashBoard_Event_Edit_Calender_Clock_UnderLine)
        val params = layout.getLayoutParams()
        params.height = 6
        params.width = Int.MAX_VALUE
        layout.setLayoutParams(params)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Parcelable>(Config.EXTRA_IMAGES)
            val image = images[0] as Image

            val userImageFile = File(image.path)

            if (userImageFile.exists()) {
                val userBitmapImage = ImageZipper(activity!!).compressToBitmap(userImageFile)
                eventImage = userBitmapImage
                setImage()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }
}
