package com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardAuthenticationLogIn

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.gms.auth.api.Auth
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_on_board_authentication_log_in.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import com.kanavdawra.pawmars.Constants.firebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.*
import com.kanavdawra.pawmars.Constants
import com.kanavdawra.pawmars.Constants.currUser
import com.kanavdawra.pawmars.Constants.fireBaseCurrentUser
import com.kanavdawra.pawmars.Constants.fireBaseDataBaseUsersID
import com.kanavdawra.pawmars.DashBoardActivity
import kotlinx.android.synthetic.main.fragment_on_board_authentication_sign_up.*


class OnBoardAuthenticationLogInFragment : Fragment() {
    var googleApiClient: GoogleApiClient? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onActivityCreated(savedInstanceState)
        AuthLogInTextView.typeface = Typeface.createFromAsset(activity?.assets, "font/enchanting_celebrations.ttf")

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken("792871887588-ck6038bb2q7lhcqf1lpffb3go2f085af.apps.googleusercontent.com")
                .requestProfile()
                .build()

        googleApiClient = GoogleApiClient.Builder(activity!!)
                .enableAutoManage(activity!!, GoogleApiClient.OnConnectionFailedListener {})
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build()

        KeyboardVisibilityEvent.setEventListener(activity!!)
        {
            if (it == true) {
                activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {

                activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            }
        }

        AuthSignUp.setOnClickListener {

            val intent = Intent("OnBoardFragment")
            intent.putExtra("task", "inflate")
            intent.putExtra("position", 4)
            activity!!.sendBroadcast(intent)
        }

        AuthLogInGooglePlus.setOnClickListener {

            val createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 13)
            activity!!.sendBroadcast(createLoader)

            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(signInIntent, 10)
        }


        AuthLogInButton.setOnClickListener {
            if (AuthLogInEmail.text.toString() != "" && AuthLogInPassword.text.toString() != "") {
                val createLoader = Intent("AppStartActivity")
                createLoader.putExtra("task", "loading")
                createLoader.putExtra("position", 14)
                activity!!.sendBroadcast(createLoader)
                firebaseAuth.signInWithEmailAndPassword(AuthLogInEmail.text.toString(), AuthLogInPassword.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                               activity!!.finish()
                                activity!!.startActivity(Intent(activity!!,DashBoardActivity::class.java))
                            } else {
                                val emailSnackbar = Intent("AppStartActivity")
                                emailSnackbar.putExtra("task", "snackbar")
                                emailSnackbar.putExtra("message", "Authentication failed or email/password is incorrect")
                                activity!!.sendBroadcast(emailSnackbar)
                            }
                            val dismissloader = Intent("AppStartActivity")
                            dismissloader.putExtra("task", "loading")
                            activity!!.sendBroadcast(dismissloader)

                        }
            }
        }

        AuthForgotPassword.setOnClickListener {
            println("forget")
            val createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 17)
            activity!!.sendBroadcast(createLoader)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 10) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val dismissloader = Intent("AppStartActivity")
                dismissloader.putExtra("task", "loading")
                activity!!.sendBroadcast(dismissloader)

                val account = task.getResult(ApiException::class.java)
                val createLoader = Intent("AppStartActivity")
                createLoader.putExtra("task", "loading")
                createLoader.putExtra("position", 14)
                activity!!.sendBroadcast(createLoader)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                val emailSnackbar = Intent("AppStartActivity")
                emailSnackbar.putExtra("task", "snackbar")
                emailSnackbar.putExtra("message", "Sign In failed.")
                activity!!.sendBroadcast(emailSnackbar)
                println("g+: $e")
            }

        }

    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        fireBaseCurrentUser = firebaseAuth.currentUser
                        val email = fireBaseCurrentUser!!.email
                        val userName = email!!.replace('.', '_', false)
                        fireBaseDataBaseUsersID.child(userName).setValue(email)
                        activity!!.startActivity(Intent(activity!!, DashBoardActivity::class.java))
                        activity!!.finish()
                    } else {

                        println("signInWithCredential:failure  " + it.exception)

                    }

                    val dismissloader = Intent("AppStartActivity")
                    dismissloader.putExtra("task", "loading")
                    activity!!.sendBroadcast(dismissloader)

                }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_board_authentication_log_in, container, false)
    }
}
