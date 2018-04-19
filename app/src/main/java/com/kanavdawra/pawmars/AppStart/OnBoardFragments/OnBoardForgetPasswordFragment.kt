package com.kanavdawra.pawmars.AppStart.OnBoardFragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_on_board_authentication_sign_up.*
import kotlinx.android.synthetic.main.fragment_on_board_forget_password.*
import java.util.regex.Pattern

class OnBoardForgetPasswordFragment : Fragment() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onEmailListener()
        AuthForgetPasswordButton.setOnClickListener {

            val createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 15)
            activity!!.sendBroadcast(createLoader)
           FirebaseAuth.getInstance().sendPasswordResetEmail(AuthForgetPasswordEmail.text.toString()).addOnCompleteListener {
               if(it.isSuccessful){
                   val authSnackbar = Intent("AppStartActivity")
                   authSnackbar.putExtra("task", "snackbar")
                   authSnackbar.putExtra("message", "Password reset email has been sent successfully")
                   activity!!.sendBroadcast(authSnackbar)
               }
               else{
                   val authSnackbar = Intent("AppStartActivity")
                   authSnackbar.putExtra("task", "snackbar")
                   authSnackbar.putExtra("message", "Please Try Again")
                  activity!!.sendBroadcast(authSnackbar)
               }
               val dismissLoader = Intent("AppStartActivity")
               dismissLoader.putExtra("task", "loading")
              activity!!.sendBroadcast(dismissLoader)
           }
        }
        forget_password_toolbar_cross.setOnClickListener {
            val createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 71)
            activity!!.sendBroadcast(createLoader)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_board_forget_password, container, false)
    }

    fun onEmailListener() {
        val emailPattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$")
        AuthForgetPasswordEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val matcher = emailPattern.matcher(p0)
                val checkEmail = matcher.matches()
                if (checkEmail) {
                    AuthForgetPasswordButton.isEnabled = true
                    AuthForgetPasswordButton.background = ContextCompat.getDrawable(activity!!, R.drawable.button_signup_enable)
                    AuthForgetPasswordTILEmail.error = ""
                }
                if (!checkEmail) {
                    AuthForgetPasswordButton.isEnabled = true
                    AuthForgetPasswordButton.background = ContextCompat.getDrawable(activity!!, R.drawable.button_signup_disable)
                    AuthForgetPasswordTILEmail.error = "Enter a valid E-mail address"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

}
