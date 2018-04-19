package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developers.imagezipper.ImageZipper
import com.kanavdawra.pawmars.Constants.selectedcontact
import com.kanavdawra.pawmars.DashBoardContactsUtility
import com.kanavdawra.pawmars.DataBase.DataBase
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_dash_board_contact_list_details.*
import kotlinx.android.synthetic.main.fragment_dash_board_contacts_update.*
import java.io.*

class DashBoardContactsUpdateFragment : Fragment() {
    var model: Cursor? = null
    var dp:Bitmap?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_contacts_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolBarButtons()

        setContents()

        editContent()


    }

    fun setContents() {
        model = DataBase(activity!!).writableDatabase.rawQuery("SELECT * FROM contacts WHERE contact_id=$selectedcontact", null)
        model!!.moveToFirst()
        contact_display_collapseBar.title = model!!.getString(model!!.getColumnIndex("name"))
        contact_display_phone_number.text = model!!.getString(model!!.getColumnIndex("phno"))
        contact_display_email_address.text = model!!.getString(model!!.getColumnIndex("email"))
        model!!.close()
    }

    fun toolBarButtons() {
        contact_display_back.setOnClickListener {
            PopUpFragmnent(activity!!).dismiss("ContactsUpdate")
        }
        contact_display_delete.setOnClickListener {
            setimage(null)
        }
        contact_display_dp.setOnClickListener {
            editDp()
        }
        contact_display_dp_camera.setOnClickListener {
            editDp()
        }
    }

    fun editContent() {
        contact_display_edit.setOnClickListener {
            println("Edit")
            contact_display_save.visibility = View.VISIBLE
            contact_display_email_address_edit.visibility = View.VISIBLE
            contact_display_phone_number_edit.visibility = View.VISIBLE
            contact_display_name_edit.visibility = View.VISIBLE
            contact_display_phone_number.visibility = View.GONE
            contact_display_edit.visibility = View.GONE
            contact_display_email_address.visibility = View.GONE


            contact_display_phone_number_edit.setText(contact_display_phone_number.text.toString())
            contact_display_email_address_edit.setText(contact_display_email_address.text.toString())
            contact_display_name_edit.setText(contact_display_collapseBar.title)
            contact_display_collapseBar.title = ""
        }
        contact_display_save.setOnClickListener {
            DataBase(activity!!).writableDatabase.execSQL("UPDATE contacts SET phno='${contact_display_phone_number_edit.text}', " +
                    "email='${contact_display_email_address_edit.text}', name='${contact_display_name_edit.text}' WHERE contact_id='$selectedcontact'")
            contact_display_collapseBar.title = contact_display_name_edit.text
            contact_display_save.visibility = View.GONE
            contact_display_email_address_edit.visibility = View.GONE
            contact_display_phone_number_edit.visibility = View.GONE
            contact_display_name_edit.visibility = View.GONE
            contact_display_phone_number.visibility = View.VISIBLE
            contact_display_edit.visibility = View.VISIBLE
            contact_display_email_address.visibility = View.VISIBLE
            contact_display_email_address.text = contact_display_email_address_edit.text
            contact_display_phone_number.text = contact_display_phone_number_edit.text
            DashBoardContactsUtility(activity!!).notifyDataSetChanged("DashBoardContacts")
            saveimage()
        }


    }
    fun saveimage(){
        val cw = ContextWrapper(activity!!.applicationContext)

        // path to /data/data/yourapp/app_data/imageDir

        val directory = cw.getDir("ContactsImages", Context.MODE_PRIVATE)

        val mypath = File(directory, "$selectedcontact.jpg")
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(mypath)
            dp!!.compress(Bitmap.CompressFormat.PNG, 50, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    fun fetchImage(){
        val cw = ContextWrapper(context!!.applicationContext)

        val mypath = cw.getDir("ContactsImages", Context.MODE_PRIVATE)

        try {
            contact_list_details_camera_image.visibility = View.GONE
            val f = File(mypath, "$selectedcontact.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            contact_display_dp.setImageBitmap(b)
            contact_list_details_dp_edit.visibility = View.GONE
            contact_list_details_dp_delete.visibility = View.VISIBLE
        } catch (e: FileNotFoundException) {
            contact_list_details_camera_image.visibility = View.VISIBLE
            contact_list_details_dp_edit.visibility = View.VISIBLE
            contact_list_details_dp_delete.visibility = View.GONE
        }
    }
    fun editDp() {
        onImagePicker()
    }

    fun onImagePicker() {
        val image = ArrayList<Image>()

        ImagePicker.with(this)
                .setToolbarColor("#9C27B0")
                .setStatusBarColor("#7B1FA2")
                .setToolbarTextColor("#FFFFFF")
                .setToolbarIconColor("#FFFFFF")
                .setProgressBarColor("#4CAF50")
                .setBackgroundColor("#FFFFFF")
                .setCameraOnly(false)
                .setMultipleMode(false)
                .setFolderMode(true)
                .setShowCamera(true)
                .setFolderTitle("Albums")
                .setImageTitle("user_image")
                .setDoneTitle("Done")
                .setSavePath("PawMars")
                .setSelectedImages(image)
                .setKeepScreenOn(true)
                .start()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Parcelable>(Config.EXTRA_IMAGES)
            val image = images[0] as Image

            val userImageFile = File(image.path)

            if (userImageFile.exists()) {
//                val userBitmap = BitmapFactory.decodeFile(userImageFile.absolutePath)
//                userImage = userBitmap
                val userBitmapImage = ImageZipper(activity!!).compressToBitmap(userImageFile)

                setimage(userBitmapImage)
                dp=userBitmapImage
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    fun setimage(image: Bitmap?) {

        if (image == null) {
            contact_display_dp.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.Teal))
            contact_display_dp.setImageBitmap(null)
            contact_display_dp_camera.visibility = View.VISIBLE
        } else {
            contact_display_dp.setImageBitmap(image)
            contact_display_dp_camera.visibility = View.GONE
        }

    }
}
