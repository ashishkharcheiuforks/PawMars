package com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanavdawra.pawmars.Constants.eventName
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.DashBoardEventsUtility
import com.kanavdawra.pawmars.DataBase.PawMarsDataBase
import com.kanavdawra.pawmars.PawMars

import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_event_terms_and_conditions.*
import android.text.method.ScrollingMovementMethod




class DashBoardEventTermsAndConditionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dash_board_event_terms_and_conditions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        clickListners()
        DashBoard_Event_TaC_Terms_And_Conditions.movementMethod = ScrollingMovementMethod()
    }

    fun clickListners() {
        DashBoard_Event_TaC_Text_Back.setOnClickListener {
            DashBoardEventsUtility(activity!!).pagerPositionChange(1)
        }
        DashBoard_Event_TaC_Text_Next.setOnClickListener {
            if (DashBoard_Event_TaC_CheckBox.isChecked) {
                AlertDialog.Builder(activity!!).setMessage("You won't be able to edit this event later.")
                        .setTitle("Final Save.")
                        .setIcon(R.mipmap.save)
                        .setPositiveButton("Save") { dialogInterface, i ->
                            if (PawMarsDataBase().Events(activity!!, eventName)){
                                activity!!.getSharedPreferences("Event_${eventName}_Details", 0).edit().putInt("Next", 3).apply()
                                DashBoardEventsUtility(activity!!).pagerPositionChange(activity!!.getSharedPreferences("Event_${eventName}_Details", 0).getInt("Next", 3))
                            }
                            else{
                                DashBoardUtility().snackBar(activity!!,"Every selected contact must be edited with area code.",R.color.Red,R.color.White)
                            }

                        }.setNegativeButton("Cancel") { dialogInterface, i ->
                            dialogInterface.dismiss()

                        }.show()
            }
        }
        DashBoard_Event_TaC_CheckBox.setOnClickListener {
            if (DashBoard_Event_TaC_CheckBox.isChecked) {
                DashBoard_Event_TaC_Text_Next.visibility = View.VISIBLE
            } else {
                DashBoard_Event_TaC_Text_Next.visibility = View.GONE
            }
        }
    }
}