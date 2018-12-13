package com.kanavdawra.pawmars.DashBoard.DashBoardFragments

import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Adapters.EventsAdaptor
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardEventsReceiver
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.InterFace.DashBoard
import com.kanavdawra.pawmars.InterFace.DashBoardEventsInterFace
import com.kanavdawra.pawmars.Modal
import com.kanavdawra.pawmars.Modals.Event
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_event_view_pager.*
import kotlinx.android.synthetic.main.fragment_dash_board_events.*


class DashBoardEventsFragment : Fragment() {
    var eventsAdapter: EventsAdaptor? = null
    var reciever: DashBoardEventsReceiver? = null
    var masterEventList = ArrayList<Event>()
    var showList = ArrayList<Event>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onClickListners()
        setTabs()
        setAdaptor()
        Interface()

    }

    fun onClickListners() {
        dashboard_events_fab.setOnClickListener {
            PopUpFragmnent(activity!!).create("AddEvent")
        }
    }

    fun setAdaptor() {
        masterEventList = Modal(activity!!).eventstoModal()
        eventsAdapter = EventsAdaptor(activity!!, masterEventList)
        dashboard_events_recycler_view.layoutManager = LinearLayoutManager(activity!!)
        dashboard_events_recycler_view.adapter = eventsAdapter
    }

    fun Interface() {
        val dashBoardEventInterFace = object : DashBoardEventsInterFace {
            override fun notifyDataSetChanged() {
                println("EventsList")
                dashboard_events_recycler_view.adapter = EventsAdaptor(activity!!, Modal(activity!!).eventstoModal())
            }
        }
        reciever = DashBoardEventsReceiver(dashBoardEventInterFace)
        activity!!.registerReceiver(reciever, IntentFilter("EventsList"))
    }

    fun setTabs() {
        dashboard_events_All.setOnClickListener {
            dashboard_events_All.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.Blue))
            dashboard_events_All.setTextColor(Color.WHITE)
            dashboard_events_Upcoming.background = ContextCompat.getDrawable(activity!!, R.drawable.half_rounded_view_layout_right_white)
            dashboard_events_Upcoming.setTextColor(ContextCompat.getColor(activity!!,R.color.Blue))
            dashboard_events_History.background = ContextCompat.getDrawable(activity!!, R.drawable.half_rounded_view_layout_left_white)
            dashboard_events_History.setTextColor(ContextCompat.getColor(activity!!,R.color.Blue))


            eventsAdapter!!.eventsList = masterEventList
            eventsAdapter!!.notifyDataSetChanged()

        }
        dashboard_events_History.setOnClickListener {
            dashboard_events_History.background = ContextCompat.getDrawable(activity!!, R.drawable.half_rounded_view_layout_left)
            dashboard_events_History.setTextColor(Color.WHITE)
            dashboard_events_Upcoming.background = ContextCompat.getDrawable(activity!!, R.drawable.half_rounded_view_layout_right_white)
            dashboard_events_Upcoming.setTextColor(ContextCompat.getColor(activity!!,R.color.Blue))
            dashboard_events_All.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.White))
            dashboard_events_All.setTextColor(ContextCompat.getColor(activity!!,R.color.Blue))
            dashboard_events_All.background=ContextCompat.getDrawable(activity!!, R.drawable.border_view_blue)
            showList.clear()
            for (i in masterEventList) {
                if (i.tab == "H") {
                    showList.add(i)
                }
            }
            eventsAdapter!!.eventsList = showList
            eventsAdapter!!.notifyDataSetChanged()
        }
        dashboard_events_Upcoming.setOnClickListener {
            dashboard_events_Upcoming.background = ContextCompat.getDrawable(activity!!, R.drawable.half_rounded_view_layout_right)
            dashboard_events_Upcoming.setTextColor(Color.WHITE)
            dashboard_events_All.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.White))
            dashboard_events_All.setTextColor(ContextCompat.getColor(activity!!,R.color.Blue))
            dashboard_events_All.background=ContextCompat.getDrawable(activity!!, R.drawable.border_view_blue)
            dashboard_events_History.background = ContextCompat.getDrawable(activity!!, R.drawable.half_rounded_view_layout_left_white)
            dashboard_events_History.setTextColor(ContextCompat.getColor(activity!!,R.color.Blue))
            showList.clear()
            for (i in masterEventList) {
                if (i.tab == "U") {
                    showList.add(i)
                }
            }
            eventsAdapter!!.eventsList = showList
            eventsAdapter!!.notifyDataSetChanged()
        }

    }

    override fun onStart() {
        super.onStart()
        DashBoardUtility().dismiss(activity!!)
    }

    override fun onPause() {
        activity!!.unregisterReceiver(reciever)
        super.onPause()
    }

    override fun onResume() {
        activity!!.registerReceiver(reciever, IntentFilter("EventsList"))
        super.onResume()
    }
}

