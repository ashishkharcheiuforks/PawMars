package com.kanavdawra.pawmars.AppStart.OnBoardFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeListener
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_on_board_third.*


class OnBoardThirdFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
var swipe=Swipe()
        swipe.setListener(object : SwipeListener {
            override fun onSwipedDown(event: MotionEvent?): Boolean {
                return true
            }

            override fun onSwipedRight(event: MotionEvent?): Boolean {
                println("swiped Right")
                return true
            }

            override fun onSwipedLeft(event: MotionEvent?): Boolean {
                println("swiped Right")
                return true
            }

            override fun onSwipedUp(event: MotionEvent?): Boolean {
                return true
            }

            override fun onSwipingUp(event: MotionEvent?) {}

            override fun onSwipingLeft(event: MotionEvent?) {
                println("swiping Left")
            }

            override fun onSwipingRight(event: MotionEvent?) {
                println("swiping Right")
            }

            override fun onSwipingDown(event: MotionEvent?) {}


        })
OnBoardThirdFragmentGestureView.setOnTouchListener(object:View.OnTouchListener{
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        swipe.dispatchTouchEvent(p1)
        return true
    }

})
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_board_third, container, false)
    }
}
