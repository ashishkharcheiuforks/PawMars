package com.kanavdawra.pawmars.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kanavdawra.pawmars.DashBoard.DashBoardFragments.DashBoardContactListFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardFragments.DashBoardContactsFragment
import java.util.ArrayList


class ContactsPagerAdaptor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    var pages = 1
    val tilesName = ArrayList<String>()
    val dashBoardContactsFragment = DashBoardContactsFragment()
    val dashBoardContactsListFragment = DashBoardContactListFragment()

    init {
        tilesName.add(0, "Contacts")
        tilesName.add(1, "Invitation List")
    }

    override fun getItem(position: Int): Fragment {
        if (pages == 1) {
            return dashBoardContactsFragment
        } else {
            if (position == 0) {
                return dashBoardContactsFragment
            } else {
                return dashBoardContactsListFragment
            }
        }

    }


    override fun getCount(): Int {
        return pages
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tilesName[position]
    }


}