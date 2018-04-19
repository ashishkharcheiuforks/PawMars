package com.kanavdawra.pawmars.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.*
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardAuthenticationLogIn.OnBoardAuthenticationLogInFragment
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardAuthenticationSignUp.OnBoardAuthenticationSignUpFragment

class OnBoardPagerAdapter(fragmentManager: FragmentManager, val logoutButton: Boolean) : FragmentPagerAdapter(fragmentManager) {

    private val onBoardFirstFragment = OnBoardFirstFragment()
    private val onBoardSecondFragment = OnBoardSecondFragment()
    private val onBoardThirdFragment = OnBoardThirdFragment()
    private val onBoardAuthenticationLogInFragment = OnBoardAuthenticationLogInFragment()
    private val onBoardAuthenticationSignUpFragment = OnBoardAuthenticationSignUpFragment()
    var onBoardEmailVerificationFragment = OnBoardEmailVerificationFragment()

    override fun getCount(): Int {
        if (logoutButton) {
            return 2
        } else {
            return 5
        }

    }

    override fun getItem(position: Int): Fragment {
        if (logoutButton) {
            if (position == 0) {
                return onBoardAuthenticationLogInFragment
            } else {
                return onBoardAuthenticationSignUpFragment
            }
        } else {
            if (position == 0) {
                return onBoardFirstFragment
            } else if (position == 1) {
                return onBoardSecondFragment
            } else if (position == 2) {
                return onBoardThirdFragment
            } else if (position == 3) {
                return onBoardAuthenticationLogInFragment
            } else {
                return onBoardAuthenticationSignUpFragment
            }
        }

    }
}