package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developers.imagezipper.ImageZipper
import com.kanavdawra.pawmars.*
import com.kanavdawra.pawmars.Adapters.CreateContactListAdaptor
import com.kanavdawra.pawmars.Constants.contactListName

import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.snatik.storage.Storage
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import kotlinx.android.synthetic.main.fragment_dash_board_contact_list_details.*
import java.io.*
import com.kanavdawra.pawmars.DataBase.DataBase
import needle.Needle


class DashBoardContactListDetailsFragment : Fragment() {
    var contactsAdapter: CreateContactListAdaptor? = null
    var contactListImage: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_contact_list_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onClickListners()
        setData()
    }

    fun setAdapter() {
        contactsAdapter = CreateContactListAdaptor(activity!!, Modal(activity!!).contactListContactstoModal())
        contact_list_details_recyclerView.adapter = contactsAdapter
        contact_list_details_recyclerView.layoutManager = LinearLayoutManager(activity)
        contact_list_details_recyclerView.addItemDecoration(StickyRecyclerHeadersDecoration(contactsAdapter))
    }

    fun setData() {
        setAdapter()
        setImage()
        setName()
    }

    fun setImage() {

        val cw = ContextWrapper(context!!.applicationContext)

        val mypath = cw.getDir("ContactList", Context.MODE_PRIVATE)

        try {
            contact_list_details_camera_image.visibility = View.GONE
            val f = File(mypath, "${contactListName}.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            contact_list_details_image.setImageBitmap(b)
            contact_list_details_dp_edit.visibility = View.GONE
            contact_list_details_dp_delete.visibility = View.VISIBLE
        } catch (e: FileNotFoundException) {
            contact_list_details_camera_image.visibility = View.VISIBLE
            contact_list_details_dp_edit.visibility = View.VISIBLE
            contact_list_details_dp_delete.visibility = View.GONE
        }
    }

    fun setName() {
        contact_list_details_name_edit.setText(contactListName)
    }

    fun saveData() {
        activity!!.getSharedPreferences(contactListName, 0)
        if (contactListName != contact_list_details_name_edit.text.toString()) {
            AsyncTask.execute {
                changeName()
            }

        }

        AsyncTask.execute {
            if (contact_list_details_dp_delete.visibility == View.GONE) {
                deleteDp()
            } else {
                saveImage()
            }
        }

        PopUpFragmnent(activity!!).dismiss("ContactListDetails")
        DashBoardContactsUtility(activity!!).notifyDataSetChanged("DashBoardContactList")
    }

    fun onClickListners() {
        contact_list_details_back.setOnClickListener {
            PopUpFragmnent(activity!!).dismiss("ContactListDetails")
        }
        contact_list_details_delete.setOnClickListener {

            AlertDialog.Builder(activity!!).setMessage("By clicking Delete the contact list will be deleted permanently.")
                    .setTitle("Are you sure?")
                    .setIcon(R.drawable.ic_trash_can_x_dark)
                    .setPositiveButton("Delete") { dialogInterface, i ->
               deleteList()
            }.setNegativeButton("Cancel") { dialogInterface, i ->
               dialogInterface.dismiss()

            }.show()

        }
        contact_list_details_save.setOnClickListener {
            saveData()
        }
        contact_list_details_dp_edit.setOnClickListener {
            onImagePicker()
        }
        contact_list_details_dp_delete.setOnClickListener {
            setimage()
        }
        contact_list_details_camera_image.setOnClickListener {
            onImagePicker()
        }
        contact_list_details_image.setOnClickListener {
            onImagePicker()
        }
    }

    fun deleteList() {
        DataBase(activity!!).writableDatabase.execSQL("DELETE FROM contactList where name='$contactListName'")
        activity!!.getSharedPreferences(contactListName,0).edit().clear().apply()
        deleteDp()
        PopUpFragmnent(activity!!).dismiss("ContactListDetails")
        DashBoardContactsUtility(activity!!).notifyDataSetChanged("DashBoardContactList")
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
                val userBitmapImage = ImageZipper(activity!!).compressToBitmap(userImageFile)
                contactListImage = userBitmapImage
                setimage()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    fun setimage() {
        if (contact_list_details_dp_edit.visibility == View.VISIBLE) {
            contact_list_details_image.setImageBitmap(contactListImage)
            contact_list_details_camera_image.visibility = View.GONE
            contact_list_details_dp_delete.visibility = View.VISIBLE
            contact_list_details_dp_edit.visibility = View.GONE
        } else if (contact_list_details_dp_delete.visibility == View.VISIBLE) {
            contactListImage = null
            contact_list_details_image.setImageBitmap(null)
            contact_list_details_camera_image.visibility = View.VISIBLE
            contact_list_details_dp_delete.visibility = View.GONE
            contact_list_details_dp_edit.visibility = View.VISIBLE

        }
    }

    fun saveImage() {

        try {
            deleteDp()
        } catch (e: Exception) {
        }

        val storage = Storage(activity!!.applicationContext)
        val cw = ContextWrapper(activity!!.applicationContext)

        // path to /data/data/yourapp/app_data/imageDir

        val directory = cw.getDir("ContactList", Context.MODE_PRIVATE)

        val mypath = File(directory, "${contact_list_details_name_edit.text}.jpg")
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(mypath)
            contactListImage!!.compress(Bitmap.CompressFormat.PNG, 50, fos)
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

    fun deleteDp() {
        val cw = ContextWrapper(activity!!.applicationContext)

        // path to /data/data/yourapp/app_data/imageDir

        val directory = cw.getDir("ContactList", Context.MODE_PRIVATE)

        val mypath = File(directory, "$contactListName.jpg")

        val file=File(mypath.toURI())
        file.canonicalFile.delete()

    }

    fun changeName() {
        val count = activity!!.getSharedPreferences(contactListName, 0).all.size
        val old = activity!!.getSharedPreferences(contactListName, 0)
        val new = activity!!.getSharedPreferences(contact_list_details_name_edit.text.toString(), 0).edit()
        for (i in 1..count - 2) {
            new.putString("data$i", old.getString("data$i", "null"))
        }
        new.putString("image", old.getString("image", "null"))
        new.putString("name", contact_list_details_name_edit.text.toString()).apply()
        old.edit().clear().apply()
        DataBase(activity!!).writableDatabase.execSQL("UPDATE contactList SET name='${contact_list_details_name_edit.text}' WHERE name='$contactListName';")
    }

}
