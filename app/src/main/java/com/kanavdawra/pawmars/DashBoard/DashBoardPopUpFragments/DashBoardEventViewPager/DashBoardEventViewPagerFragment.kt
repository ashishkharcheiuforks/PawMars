package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Adapters.EventViewPagerAdaptor
import com.kanavdawra.pawmars.BroadCastReceiver.EventViewPagerReceiver
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.Constants.eventViewPagerReceiver
import com.kanavdawra.pawmars.DashBoardEventsUtility
import com.kanavdawra.pawmars.InterFace.DashBoard
import com.kanavdawra.pawmars.InterFace.EventViewPagerInterFace

import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_event_view_pager.*

class DashBoardEventViewPagerFragment : Fragment() {

    var eventViewPagerAdaptor: EventViewPagerAdaptor? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_event_view_pager, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdaptor()
        listener()
    }

    fun setAdaptor() {
        eventViewPagerAdaptor = EventViewPagerAdaptor(childFragmentManager)
        DashBoard_Event_ViewPager.adapter = eventViewPagerAdaptor
        if(activity!!.getSharedPreferences("Event_${eventName}_Details",0).getInt("FinalSave",1)==5){
            DashBoard_Event_ViewPager.setCurrentItem(5, false)
        }
        else{
            DashBoard_Event_ViewPager.setCurrentItem(1, false)
        }

    }

    fun listener() {

        DashBoard_Event_ViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {}

        })
        val eventViewPager = object : EventViewPagerInterFace {

            override fun notifyDataSetChanged(position: Int) {
                if (position == 0) {
                    eventViewPagerAdaptor?.dashBoardEventEditFragment = DashBoardEventEditFragment()
                }
                if (position == 1) {
                    DashBoard_Event_ViewPager.adapter = EventViewPagerAdaptor(childFragmentManager)
                    pageChange(1)
                }
                eventViewPagerAdaptor?.notifyDataSetChanged()
            }

            override fun pageChange(position: Int) {
                try {
                    DashBoard_Event_ViewPager.setCurrentItem(position, true)
                } catch (e: Exception) {
                    DashBoard_Event_ViewPager.setCurrentItem(1, true)
                }
            }

        }
        val receiver=EventViewPagerReceiver(eventViewPager)
        eventViewPagerReceiver=receiver
        activity!!.registerReceiver(receiver, IntentFilter("EventViewPager"))

    }

    override fun onPause() {
        activity!!.unregisterReceiver(eventViewPagerReceiver)
        super.onPause()
    }

    override fun onResume() {
        activity!!.registerReceiver(eventViewPagerReceiver, IntentFilter("EventViewPager"))
        super.onResume()
    }
}
