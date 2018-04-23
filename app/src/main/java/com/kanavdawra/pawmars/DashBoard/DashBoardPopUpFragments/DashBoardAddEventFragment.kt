package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developers.imagezipper.ImageZipper
import com.kanavdawra.pawmars.R
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_dash_board_add_event.*
import java.io.File

class DashBoardAddEventFragment : Fragment() {
    var eventImage: Bitmap? = null
    var imageName = "image1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_add_event, container, false)
    }

    fun addEventDefaultImage() {

        if (DashBoard_Add_Event_Image4.visibility == View.VISIBLE && DashBoard_Add_Event_Image3.visibility == View.VISIBLE && DashBoard_Add_Event_Image2.visibility == View.VISIBLE && DashBoard_Add_Event_Image1.visibility == View.VISIBLE) {
            return
        } else if (DashBoard_Add_Event_Image3.visibility == View.VISIBLE && DashBoard_Add_Event_Image2.visibility == View.VISIBLE && DashBoard_Add_Event_Image1.visibility == View.VISIBLE) {
            DashBoard_Add_Event_Image4.visibility = View.VISIBLE
            return
        } else if (DashBoard_Add_Event_Image2.visibility == View.VISIBLE && DashBoard_Add_Event_Image1.visibility == View.VISIBLE) {
            DashBoard_Add_Event_Image3.visibility = View.VISIBLE
            return
        } else if (DashBoard_Add_Event_Image1.visibility == View.VISIBLE) {
            DashBoard_Add_Event_Image2.visibility = View.VISIBLE
            return
        }

    }

    fun removeEventDefaultImage(image: String) {
        if(image=="image1"){
            DashBoard_Add_Event_Image1.setImageDrawable(activity!!.getDrawable(R.mipmap.event_default_image))
        }
        if(image=="image2"){
            DashBoard_Add_Event_Image2.setImageDrawable(activity!!.getDrawable(R.mipmap.event_default_image))
            if(DashBoard_Add_Event_Image3.visibility==View.GONE && DashBoard_Add_Event_Image4.visibility==View.GONE){
                DashBoard_Add_Event_Image2.visibility=View.GONE
            }

        }
        if(image=="image3"){
            DashBoard_Add_Event_Image3.setImageDrawable(activity!!.getDrawable(R.mipmap.event_default_image))
                    if(DashBoard_Add_Event_Image4.visibility==View.GONE){
                DashBoard_Add_Event_Image3.visibility=View.GONE
            }
        }
        if(image=="image4"){
            DashBoard_Add_Event_Image4.setImageDrawable(activity!!.getDrawable(R.mipmap.event_default_image))
            DashBoard_Add_Event_Image4.visibility=View.GONE
        }
    }

    fun setImage() {
        if (imageName == "image1") {
            DashBoard_Add_Event_Image1.setImageBitmap(eventImage)
        } else if (imageName == "image2") {
            DashBoard_Add_Event_Image2.setImageBitmap(eventImage)
        } else if (imageName == "image3") {
            DashBoard_Add_Event_Image3.setImageBitmap(eventImage)
        } else if (imageName == "image4") {
            DashBoard_Add_Event_Image4.setImageBitmap(eventImage)
        }
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
