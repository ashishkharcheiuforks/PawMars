package com.kanavdawra.pawmars.Adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kanavdawra.pawmars.DashBoard.DashBoardFragments.DashBoardEventDetailsImageLoaderFragment
import android.os.Bundle




class DashBoardEventDetailsImagePagerAdaptor(fragmentManager: FragmentManager,val context: Context, var imageArrayList: ArrayList<String>) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
       return imageArrayList.count()
    }

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        args.putString("URL", imageArrayList[position])
       val dashBoardEventDetailsImageLoaderFragment=   DashBoardEventDetailsImageLoaderFragment()
        dashBoardEventDetailsImageLoaderFragment.arguments=args
        return dashBoardEventDetailsImageLoaderFragment
    }


}