package com.kanavdawra.pawmars.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager.*

class EventViewPagerAdaptor(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    var dashBoardEventDetailsFragment: DashBoardEventDetailsFragment? = null
    var dashBoardEventEditFragment: DashBoardEventEditFragment? = null
    var dashBoardEventSmSTextFragment: DashBoardEventSmSTextFragment? = null
    var dashBoardEventEmailTextFragment: DashBaordEventEmailTextFragment? = null
    var dashBoardEventTermsAndConditionsFragment: DashBoardEventTermsAndConditionsFragment? = null
    var dashBoardEventQrCodeFragment: DashBoardEventQrCodeFragment? = null
    override fun getItem(position: Int): Fragment {
        eventDetailsFragment()
        eventEditFragment()
        eventSMSFragment()
        eventEmailFragment()
        eventTaCFragment()
        eventMasterCodeFragment()
        if (position == 0) {
            return dashBoardEventEditFragment!!
        } else if (position == 1) {
            return dashBoardEventDetailsFragment!!
        }  else if (position == 2) {
            return dashBoardEventTermsAndConditionsFragment!!
        } else if (position == 3) {
            return dashBoardEventSmSTextFragment!!
        } else if (position == 4) {
            return dashBoardEventEmailTextFragment!!
        }else{
            return dashBoardEventQrCodeFragment!!
        }

    }


    override fun getCount(): Int {
        return 6
    }

    fun eventDetailsFragment() {
        dashBoardEventDetailsFragment = DashBoardEventDetailsFragment()
    }

    fun eventEditFragment() {
        dashBoardEventEditFragment = DashBoardEventEditFragment()
    }

    fun eventSMSFragment() {
        dashBoardEventSmSTextFragment = DashBoardEventSmSTextFragment()
    }

    fun eventEmailFragment() {
        dashBoardEventEmailTextFragment = DashBaordEventEmailTextFragment()
    }

    fun eventTaCFragment() {
        dashBoardEventTermsAndConditionsFragment = DashBoardEventTermsAndConditionsFragment()
    }

    fun eventMasterCodeFragment() {
        dashBoardEventQrCodeFragment = DashBoardEventQrCodeFragment()
    }

}