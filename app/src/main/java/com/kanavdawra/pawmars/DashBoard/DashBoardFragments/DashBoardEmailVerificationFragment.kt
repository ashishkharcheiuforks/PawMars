package com.kanavdawra.pawmars.DashBoard.DashBoardFragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.kanavdawra.pawmars.AppStartActivity
import com.kanavdawra.pawmars.Constants
import com.kanavdawra.pawmars.Constants.currUser
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.kanavdawra.pawmars.DashBoardActivity
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_dash_board_email_verification.*

class DashBoardEmailVerificationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dash_board_email_verification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dashBoardEmailVerificationFragmentEmail.text = Constants.currUser().email

        DashBoardEmailVerificationFragmentResend.setOnClickListener {

            DashBoardUtility().create(activity!!, "Sending...")

            Constants.currUser().sendEmailVerification().addOnCompleteListener {

                if (it.isSuccessful) {
                    DashBoardUtility().snackBar(activity!!, "Sending verification email successful.", R.color.Green, R.color.White)
                } else {
                    DashBoardUtility().snackBar(activity!!, "Sending verification email failed.", R.color.Red, R.color.White)
                }
                DashBoardUtility().dismiss(activity!!)
            }

        }


        val googleApiClient = GoogleApiClient.Builder(activity!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build()
        googleApiClient.connect()






        DashBoardEmailVerificationFragmentCross.setOnClickListener {
            println("cross")

            FirebaseAuth.getInstance().signOut()
            Auth.GoogleSignInApi.signOut(googleApiClient)
            googleApiClient.clearDefaultAccountAndReconnect().setResultCallback {
                if (it.isSuccess) {
                    googleApiClient.disconnect()
                    DashBoardUtility().updateEmailVerificationFragment(activity!!, 0)
                } else {
                    DashBoardUtility().snackBar(activity!!, "App logout failed. Please try again",
                            ContextCompat.getColor(activity!!, R.color.Red), ContextCompat.getColor(activity!!, R.color.White))
                }
                DashBoardUtility().dismiss(activity!!)
            }



        }





        DashBoardEmailVerificationFragmentGetStarted.setOnClickListener {
            DashBoardUtility().create(activity!!, "Getting Started...")
            FirebaseAuth.getInstance().currentUser!!.reload().addOnCompleteListener {
                if (it.isSuccessful) {
                    if (currUser().isEmailVerified) {
                        println(true)
                        //DashBoardUtility().updateEmailVerificationFragment(activity!!, 1)
                        activity!!.finish()
                        startActivity(Intent(activity!!,DashBoardActivity::class.java))
                    } else {
                        println(false)
                        DashBoardUtility().snackBar(activity!!, "Please verify your email address first.", R.color.Blue, R.color.White)
                    }

                } else {
                    DashBoardUtility().snackBar(activity!!, "Please Try Again", R.color.Red, R.color.White)
                }
                DashBoardUtility().dismiss(activity!!)
            }

        }


    }
}
