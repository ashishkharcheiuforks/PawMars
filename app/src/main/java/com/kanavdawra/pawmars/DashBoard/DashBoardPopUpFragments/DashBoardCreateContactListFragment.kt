package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments


import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developers.imagezipper.ImageZipper
import com.kanavdawra.pawmars.Adapters.CreateContactListAdaptor
import com.kanavdawra.pawmars.Constants.selectedContacts
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.DashBoardContactsUtility
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import kotlinx.android.synthetic.main.fragment_dash_board_create_contact_list.*
import java.io.File
import java.io.FileOutputStream
import com.kanavdawra.pawmars.DataBase.DataBase
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R
import com.snatik.storage.Storage
import java.io.IOException


class DashBoardCreateContactListFragment : Fragment() {
    var contactListImage: Bitmap? = null
    var contactsAdapter: CreateContactListAdaptor? = null
    var mypath: File? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dash_board_create_contact_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        create_contact_list_collapseBar.title = ""
        SetAdaptor()
        toolBarButtons()
    }

    fun SetAdaptor() {
        contactsAdapter = CreateContactListAdaptor(activity!!, selectedContacts)
        create_contact_list_recyclerView.adapter = contactsAdapter
        create_contact_list_recyclerView.layoutManager = LinearLayoutManager(activity)
        create_contact_list_recyclerView.addItemDecoration(StickyRecyclerHeadersDecoration(contactsAdapter))
    }

    fun toolBarButtons() {
        create_contact_list_save.setOnClickListener {
            var i = 1

            if (create_contact_list_name_edit.text.toString() != "") {
                val coursor = DataBase(activity!!).writableDatabase.rawQuery("SELECT * FROM contactList WHERE name='${create_contact_list_name_edit.text}'", null)
                if (coursor.count <= 0) {
                    ////////////////////////////////////////SAVE IN SHARED PREFERENCES/////////////////////////

                    val editor = activity!!.getSharedPreferences(create_contact_list_name_edit.text.toString(), 0).edit()
                    for (data in selectedContacts) {
                        editor.putString("data$i", data.contact_id)
                        i++
                    }

                    editor.putString("name", create_contact_list_name_edit.text.toString())

                    if (contactListImage == null) {
                        editor.putString("image", "null")
                    } else {
                        editor.putString("image", "${mypath.toString()}")
                        saveImage()
                    }

                    val values = ContentValues()
                    values.put("name", create_contact_list_name_edit.text.toString())
                    DataBase(activity!!).writableDatabase.insert("contactList", null, values)

                    editor.apply()

                    ///////////////////////////////////////////////////END//////////////////////////////////

                    PopUpFragmnent(activity!!).dismiss("CreateContactList")
                    DashBoardContactsUtility(activity!!).notifyDataSetChanged("DashBoardContactsPager")
                    DashBoardContactsUtility(activity!!).notifyDataSetChanged("DashBoardContacts")
                    DashBoardContactsUtility(activity!!).notifyDataSetChanged("DashBoardContactList")
                }else{
                    DashBoardUtility().snackBar(activity!!,"Name should be unique",R.color.Blue,R.color.White)
                }
                coursor.close()

            } else {
                DashBoardUtility().snackBar(activity!!, "Name field cannot be empty!", R.color.Red, R.color.White)
            }
        }
        create_contact_list_dp_delete.setOnClickListener {
            setimage()
        }
        create_contact_list_dp_edit.setOnClickListener {
            onImagePicker()
        }
        create_contact_list_image.setOnClickListener {
            onImagePicker()
        }
        create_contact_list_back.setOnClickListener {
            PopUpFragmnent(activity!!).dismiss("CreateContactList")
        }
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
                contactListImage = userBitmapImage
                setimage()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    fun setimage() {
        if (create_contact_list_dp_edit.visibility == View.VISIBLE) {
            create_contact_list_image.setImageBitmap(contactListImage)
            create_contact_list_camera_image.visibility = View.GONE
            create_contact_list_dp_delete.visibility = View.VISIBLE
            create_contact_list_dp_edit.visibility = View.GONE
        } else if (create_contact_list_dp_delete.visibility == View.VISIBLE) {
            contactListImage = null
            create_contact_list_image.setImageBitmap(null)
            create_contact_list_camera_image.visibility = View.VISIBLE
            create_contact_list_dp_delete.visibility = View.GONE
            create_contact_list_dp_edit.visibility = View.VISIBLE

        }
    }

    fun saveImage() {
        val storage = Storage(activity!!.applicationContext)
        val cw = ContextWrapper(activity!!.applicationContext)

        // path to /data/data/yourapp/app_data/imageDir

        val directory = cw.getDir("ContactList", Context.MODE_PRIVATE)

        mypath = File(directory, "${create_contact_list_name_edit.text}.jpg")

//         val path = storage.externalStorageDirectory
//        println(path)
//        storage.createFile(path.toString(),contactListImage)

        println(mypath)
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(mypath)

            contactListImage!!.compress(Bitmap.CompressFormat.PNG, 100, fos)

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

}