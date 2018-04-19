package com.kanavdawra.pawmars.AppStart.OnBoardFragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardAuthenticationLogIn.OnBoardAuthenticationLogInFragment
import com.kanavdawra.pawmars.Constants.email
import com.kanavdawra.pawmars.Constants.emailVerificationSend
import com.kanavdawra.pawmars.Constants.fireBaseCurrentUser
import com.kanavdawra.pawmars.DashBoardActivity

import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.activity_app_start.*
import kotlinx.android.synthetic.main.fragment_on_board_email_verification.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class OnBoardEmailVerificationFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AuthEmailVerificationEmail.text=email
        println(email)
        if(emailVerificationSend){
            AuthEmailVerificationImageFailed.visibility=View.GONE
            AuthEmailVerificationImagePending.visibility=View.VISIBLE
            AuthEmailVerificationText.text="We have sent a verification email to"
            AuthEmailVerificationEmail.text=email
        }
        else{
            AuthEmailVerificationImageFailed.visibility=View.VISIBLE
            AuthEmailVerificationImagePending.visibility=View.GONE
            AuthEmailVerificationText.text="We have sent a verification email to"
            AuthEmailVerificationEmail.text=email
        }
        AuthEmailVerificationResend.setOnClickListener {
            var createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 15)
            activity!!.sendBroadcast(createLoader)
            fireBaseCurrentUser?.sendEmailVerification()!!.addOnCompleteListener {
                if(it.isSuccessful){
                    AuthEmailVerificationImageFailed.visibility=View.GONE
                    AuthEmailVerificationImagePending.visibility=View.VISIBLE
                    AuthEmailVerificationText.text="We have sent a verification email to"
                    AuthEmailVerificationEmail.text=email
                }
                else
                {
                    val authSnackbar = Intent("AppStartActivity")
                    authSnackbar.putExtra("task", "snackbar")
                    authSnackbar.putExtra("message", "Sending email for verification failed")
                    activity!!.sendBroadcast(authSnackbar)
                }
                var dismissLoader = Intent("AppStartActivity")
                dismissLoader.putExtra("task", "loading")
                dismissLoader.putExtra("position", 51)
                activity!!.sendBroadcast(dismissLoader)
            }
        }
        email_toolbar_cross.setOnClickListener {
            val intent = Intent("AppStartActivity")
            intent.putExtra("task", "inflate")
            intent.putExtra("position", 20)
            activity!!.sendBroadcast(intent)
        }
        AuthEmailVerificationIClicked.setOnClickListener{
            val intent = Intent("AppStartActivity")
            intent.putExtra("task", "inflate")
            intent.putExtra("position", 20)
            activity!!.sendBroadcast(intent)
            activity!!.startActivity(Intent(activity, DashBoardActivity::class.java))
            activity!!.finish()
        }

    }

    override fun onStart() {
        super.onStart()
        if(fireBaseCurrentUser?.isEmailVerified==true){
            activity!!.startActivity(Intent(activity, DashBoardActivity::class.java))
            activity!!.finish()
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_board_email_verification, container, false)
    }

}
