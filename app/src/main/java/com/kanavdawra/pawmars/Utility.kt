package com.kanavdawra.pawmars

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*

class Utility(val context: Context) {
    fun saveImage(directory:String,name:String,image: Bitmap,quality:Int):String{
        val cw = ContextWrapper(context.applicationContext)

        // path to /data/data/yourapp/app_data/imageDir

        val directory = cw.getDir(directory, Context.MODE_PRIVATE)

        val mypath = File(directory, "$name.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            image.compress(Bitmap.CompressFormat.PNG, quality, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return mypath.toString()
    }

    fun fetchUrl(directory:String,name:String):String{
        val cw = ContextWrapper(context.applicationContext)
        val Directory = cw.getDir(directory, Context.MODE_PRIVATE)
        val mypath = File(Directory, "$name.jpg")
        return mypath.toString()
    }

    fun fetchImage(directory:String,name:String):Bitmap{
        var image:Bitmap?=null
        val cw = ContextWrapper(context.applicationContext)
        val mypath = cw.getDir(directory, Context.MODE_PRIVATE)
        try {
            val f = File(mypath, "$name.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            image=b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            image=null
        }finally {

        }
        return image!!
    }

    fun deleteImage(directory:String,name:String){
        val cw = ContextWrapper(context.applicationContext)

        // path to /data/data/yourapp/app_data/imageDir

        val directory = cw.getDir(directory, Context.MODE_PRIVATE)

        val mypath = File(directory, "$name.jpg")

        val file=File(mypath.toURI())
        file.canonicalFile.delete()
    }

}