package com.kanavdawra.pawmars.DashBoard.DashBoardFragments

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.kanavdawra.pawmars.PopUpFragmnent
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_events.*


class DashBoardEventsFragment : Fragment() {
    var eventsAdapter: EventsAdaptor? = null
    var reciever:DashBoardEventsReceiver?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dash_board_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onClickListners()
        setAdaptor()
        Interface()
    }

    fun onClickListners() {
        dashboard_events_fab.setOnClickListener {
            PopUpFragmnent(activity!!).create("AddEvent")
        }
    }

    fun setAdaptor() {
        eventsAdapter = EventsAdaptor(activity!!, Modal(activity!!).eventstoModal())
        dashboard_events_recycler_view.layoutManager = LinearLayoutManager(activity!!)
        dashboard_events_recycler_view.adapter = eventsAdapter
    }

    fun Interface() {
        val dashBoardEventInterFace = object : DashBoardEventsInterFace {
            override fun notifyDataSetChanged() {
                println("EventsList")
                dashboard_events_recycler_view.adapter =EventsAdaptor(activity!!, Modal(activity!!).eventstoModal())
            }
        }
        reciever=DashBoardEventsReceiver(dashBoardEventInterFace)
        activity!!.registerReceiver(reciever, IntentFilter("EventsList"))
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

