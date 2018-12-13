package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.R
import com.scottyab.aescrypt.AESCrypt
import java.security.GeneralSecurityException
import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.android.synthetic.main.fragment_dash_board_event_qr_code.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.BitMatrix
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File.separator
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.DashBoardEventsUtility
import com.kanavdawra.pawmars.Utility
import java.io.*


@RequiresApi(Build.VERSION_CODES.M)
class DashBoardEventQrCodeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_qr_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.getSharedPreferences("Event_${eventName}_Details",0).edit().putString("Tab","U").apply()
        setData()
        listeners()
    }
    fun encryptEvent(): String {
        val id = activity!!.getSharedPreferences("Event_${eventName}_Details", 0).getString("ID", "")
        var encryptionid = ""
        val password = eventName
        val message = id
        try {
            val encryptedMsg = AESCrypt.encrypt(password, message)
            encryptionid = encryptedMsg
        } catch (e: GeneralSecurityException) {
            Log.e("AES Error", e.toString())
        }
        return encryptionid
    }

    fun appendEventname(): String {
        val encryptedText = encryptEvent()
        val eventname = eventName
        val lenght = eventname.length
        val prefix = "N~$lenght~$eventname"
        val qrText = prefix + encryptedText
        return qrText
    }

    fun createQrCode(): BitMatrix {
        val qrCodeText = appendEventname()
        val qrBitMatrix = QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, 250, 250)

        return qrBitMatrix
    }

    fun getBitMap(): Bitmap {
        val bitMatrix = createQrCode()
        val height = bitMatrix.getHeight()
        val width = bitMatrix.getWidth()
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    fun listeners() {
        DashBoard_Event_EventCode_Share.setOnClickListener {
            shareImage()
        }
        DashBoard_Event_EventCode_Save.setOnClickListener {
            saveImage()
        }
        DashBoard_Event_EventCode_Back.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(1)
        }
    }

    fun shareImage() {
        val icon = getBitMap()
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "title")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = activity!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outstream: OutputStream?
        try {
            outstream = activity!!.contentResolver.openOutputStream(uri)
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream.close();
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, "Share Image"))


    }

    fun saveImage() {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/PawMars/Images")
        myDir.mkdirs()
        val fname = "$eventName.jpg"
        val file = File(myDir, fname)
        Log.i(TAG, "" + file)
        if (file.exists())
            file.delete()
        try {Utility(activity!!).toast("Saving Image...",2000)
            val out = FileOutputStream(file)
            getBitMap().compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Utility(activity!!).toast("Save Successful",2000)
        } catch (e: Exception) {
            Utility(activity!!).toast("Error Saving the Image",2000)
            e.printStackTrace()
        }

    }

    fun setData() {
        DashBoard_Event_EventCode_EventCode.setImageBitmap(getBitMap())
    }
}


