package com.kanavdawra.pawmars.AppStart

import android.app.backup.SharedPreferencesBackupHelper
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kanavdawra.pawmars.Adapters.OnBoardPagerAdapter
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardEmailVerificationFragment
import com.kanavdawra.pawmars.BroadCastReceiver.appStartReciever
import com.kanavdawra.pawmars.InterFace.AppStartInterFace

import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_on_board.*

class OnBoardFragment : Fragment() {

    var onBoardPagerAdapter: OnBoardPagerAdapter? = null
    var logincheck = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!activity!!.getSharedPreferences("Main", 0).getBoolean("OnBoardNavigationCheck", false)) {
            onBoardPagerAdapter = OnBoardPagerAdapter(activity!!.supportFragmentManager, false)
            logincheck = false

        } else {
            onBoardPagerAdapter = OnBoardPagerAdapter(activity!!.supportFragmentManager, true)
            logincheck = true
        }

        OnBoardViewPager.adapter = onBoardPagerAdapter
        var check = 0

        OnBoardViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    setOnBoardFlag()
                    check = 1
                    activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.PurpleDark)
                }
                if (position == 4) {
                    check = 4
                    activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.PurpleDark)
                }
                if (check == 4) {
                    OnBoardViewPager.setCurrentItem(4, true)
                }
                if (check == 1) {
                    OnBoardViewPager.setCurrentItem(3, true)
                }
            }
        })

        val appStartInterFace = object : AppStartInterFace {
            override fun inflate(position: Int?) {
                if (logincheck) {
                    OnBoardViewPager.setCurrentItem(position!! - 3, true)
                } else {
                    OnBoardViewPager.setCurrentItem(position!!, true)
                }
            }

            override fun snackBar(message: String) {}
        }

        val appStartReciever = appStartReciever(appStartInterFace)
        activity!!.registerReceiver(appStartReciever, IntentFilter("OnBoardFragment"))

    }

    fun setOnBoardFlag() {
        val editor = activity!!.getSharedPreferences("Main", 0).edit()
        editor.putBoolean("OnBoardNavigationCheck", true).apply()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_board, container, false)
    }
}

