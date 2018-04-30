package com.kanavdawra.pawmars

import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kanavdawra.pawmars.AppStart.OnBoardFragment
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardEmailVerificationFragment
import com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardForgetPasswordFragment
import com.kanavdawra.pawmars.AppStart.SplashFragment
import com.kanavdawra.pawmars.BroadCastReceiver.appStartReciever
import com.kanavdawra.pawmars.Constants.logoutButton
import com.kanavdawra.pawmars.InterFace.AppStartInterFace
import kotlinx.android.synthetic.main.activity_app_start.*
import com.kanavdawra.pawmars.Constants.signedIn
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.READ_CONTACTS
import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.content.Context


class AppStartActivity : AppCompatActivity() {
    var active = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_start)


        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (!logoutButton) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.AppStartFrameLayout, SplashFragment()).commit()


            Handler().postDelayed({
                if (!active) {
                    finish()
                }
                if (active && !signedIn) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.AppStartFrameLayout, OnBoardFragment()).commit()
                }
                if (signedIn) {
                    startActivity(Intent(this, DashBoardActivity::class.java))
                    finish()
                }

            }, Constants.splashTimeOut)

        }
        if (logoutButton) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.AppStartFrameLayout, OnBoardFragment()).commit()
        }
        var onBoardEmailVerificationFragment = OnBoardEmailVerificationFragment()
        var onBoardForgetPasswordFragment = OnBoardForgetPasswordFragment()
        val appStartInterFace = object : AppStartInterFace {
            override fun inflate(position: Int?) {
                if (position == 1) {
                    AppStartView.visibility = View.VISIBLE
                    AppStartLoaderLayout.visibility = View.VISIBLE
                }
                if (position == 0) {
                    AppStartAuthSignUpLoadingText.text = "Registering..."
                    AppStartView.visibility = View.GONE
                    AppStartLoaderLayout.visibility = View.GONE
                    AppStartLoaderLayoutWhite.visibility = View.GONE
                }
                if (position == 15) {
                    AppStartAuthSignUpLoadingText.text = "Sending..."
                    AppStartView.visibility = View.VISIBLE
                    AppStartLoaderLayout.visibility = View.VISIBLE
                }
                if (position == 51) {
                    AppStartAuthSignUpLoadingText.text = "Registering..."
                    AppStartView.visibility = View.GONE
                    AppStartLoaderLayout.visibility = View.GONE
                }
                if (position == 12) {
                    AppStartAuthSignUpLoadingText.text = "Uploading..."
                    AppStartView.visibility = View.VISIBLE
                    AppStartLoaderLayout.visibility = View.VISIBLE

                }
                if (position == 13) {
                    AppStartAuthSignUpLoadingTextPurple.text = "Checking..."
                    AppStartView.visibility = View.VISIBLE
                    AppStartLoaderLayoutWhite.visibility = View.VISIBLE

                }
                if (position == 14) {
                    AppStartAuthSignUpLoadingTextPurple.text = "Signing In..."
                    AppStartView.visibility = View.VISIBLE
                    AppStartLoaderLayoutWhite.visibility = View.VISIBLE

                }
                if (position == 10) {
                    onBoardEmailVerificationFragment = OnBoardEmailVerificationFragment()
                    AppStartEmailView.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                            .add(R.id.AppStartFrameLayoutTemp, onBoardEmailVerificationFragment).commit()
                }
                if (position == 20) {
                    AppStartEmailView.visibility = View.GONE

                    supportFragmentManager.beginTransaction().remove(onBoardEmailVerificationFragment).commit()
                }
                if (position == 17) {
                    onBoardForgetPasswordFragment = OnBoardForgetPasswordFragment()
                    AppStartEmailView.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                            .add(R.id.AppStartFrameLayoutTemp, onBoardForgetPasswordFragment).commit()
                }
                if (position == 71) {
                    AppStartEmailView.visibility = View.GONE
                    supportFragmentManager.beginTransaction().remove(onBoardForgetPasswordFragment).commit()
                }

            }

            override fun snackBar(message: String) {

                if (message == "Password reset email has been sent successfully") {
                    val snackbar = Snackbar.make(AppStartLayout, message, Snackbar.LENGTH_SHORT)
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(ContextCompat.getColor(this@AppStartActivity, R.color.Green))
                    val textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
                    textView.setTextColor(ContextCompat.getColor(this@AppStartActivity, R.color.White))
                    snackbar.show()
                } else {
                    val snackbar = Snackbar.make(AppStartLayout, message, Snackbar.LENGTH_SHORT)
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(ContextCompat.getColor(this@AppStartActivity, R.color.Red))
                    val textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
                    textView.setTextColor(ContextCompat.getColor(this@AppStartActivity, R.color.White))
                    snackbar.show()
                }

            }
        }
        val appStartReciever = appStartReciever(appStartInterFace)
        registerReceiver(appStartReciever, IntentFilter("AppStartActivity"))
    }

    fun permissions() {
        Dexter.withActivity(this)
                .withPermissions(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.READ_PHONE_STATE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if(!report!!.areAllPermissionsGranted()){
                            (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
                            finish()
                            startActivity(Intent(this@AppStartActivity,AppStartActivity::class.java))
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {}


                }).check()

    }

    override fun onStop() {
        super.onStop()
        active = false
    }

    override fun onStart() {
        super.onStart()
        permissions()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            Constants.fireBaseCurrentUser = currentUser
            signedIn = true
        }
        active = true
    }
}
