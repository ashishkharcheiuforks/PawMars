package com.kanavdawra.pawmars

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardNavigationReciever
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardReciever
import com.kanavdawra.pawmars.Constants.logoutButton
import com.kanavdawra.pawmars.DashBoard.DashBoardFragments.*
import com.kanavdawra.pawmars.DashBoard.DashBoardUtility
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import com.yarolegovich.slidingrootnav.transform.YTranslationTransformation
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.navigation_drawer_dashboard.view.*
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.kanavdawra.pawmars.BroadCastReceiver.DashBoardToolBarReciever
import com.kanavdawra.pawmars.BroadCastReceiver.PopUpFragmentReceiver
import com.kanavdawra.pawmars.DataBase.PawMarsDataBase
import com.kanavdawra.pawmars.Checks.syncLocalContacts
import com.kanavdawra.pawmars.Constants.popUpName
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardContactsUpdateFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardFragments.DashBoardContactsViewPagerFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardAddEventFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardContactListDetailsFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardCreateContactListFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager.DashBoardEventDetailsFragment
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager.DashBoardEventViewPagerFragment
import com.kanavdawra.pawmars.InterFace.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class DashBoardActivity : AppCompatActivity() {
    var slidingRootNavBuilder: SlidingRootNav? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        syncContacts()

        toolBarUpdate()

        window.statusBarColor = ContextCompat.getColor(this@DashBoardActivity, R.color.PurpleDark)
        setContentView(R.layout.activity_dash_board)

        updateDashboardwithFragment()

        dashBoardUtility()

        navigationInterFace()
        toolBarButton()
        popUpFragment()

    }

    fun navigationInterFace() {
        val dashBoardNavigationInterFace = object : DashBoardNavigationInterFace {
            override fun Home() {
                home()
            }

            override fun Contacts() {
                contacts()
            }

            override fun Events() {
                events()
            }

            override fun Verify() {
                verify()
            }

            override fun History() {
                history()
            }

        }
        registerReceiver(DashBoardNavigationReciever(dashBoardNavigationInterFace), IntentFilter("Navigation"))
    }

    fun setnavigation() {
        val drawerArrow = DrawerArrowDrawable(this)
        drawerArrow.color = resources.getColor(R.color.White)
        DashBoard_ToolBar.navigationIcon = drawerArrow
        val slidingRootNavBuilder = SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withDragDistance(330)
                .withContentClickableWhenMenuOpened(true)
                .withRootViewYTranslation(7)
                .withRootViewElevation(10)
                .addRootTransformation(YTranslationTransformation(1f))
                .withMenuLayout(R.layout.navigation_drawer_dashboard)
                .addDragListener {
                    drawerArrow.progress = it
                    if (it > .59) {
                        window.statusBarColor = ContextCompat.getColor(this@DashBoardActivity, R.color.Black)
                    } else {
                        window.statusBarColor = ContextCompat.getColor(this@DashBoardActivity, R.color.PurpleDark)
                    }
                }
                .inject()
        DashBoard_ToolBar.setNavigationOnClickListener {
            if (slidingRootNavBuilder.isMenuOpened) {
                slidingRootNavBuilder.closeMenu()
            }

            if (slidingRootNavBuilder.isMenuClosed) {
                slidingRootNavBuilder.openMenu()
            }
        }
        setnavigationcontents(slidingRootNavBuilder)
        navigationButtons(slidingRootNavBuilder)
    }

    fun setnavigationcontents(slidingRootNavBuilder: SlidingRootNav) {
        slidingRootNavBuilder.layout.nav_home_email.text = Constants.currUser().email
        slidingRootNavBuilder.layout.nav_home_name.text = Constants.currUser().displayName

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this@DashBoardActivity.contentResolver, Constants.currUser().photoUrl)
            slidingRootNavBuilder.layout.nav_home_image.setImageBitmap(bitmap)
            println(Constants.currUser().photoUrl)
        } catch (e: Exception) {
        }
    }

    fun navigationButtons(slidingRootNav: SlidingRootNav) {
        slidingRootNavBuilder = slidingRootNav

        val googleApiClient = GoogleApiClient.Builder(this@DashBoardActivity)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build()
        googleApiClient.connect()

        slidingRootNavBuilder!!.layout.home.setOnClickListener {
            home()
        }

        slidingRootNavBuilder!!.layout.contacts.setOnClickListener {
            contacts()
        }

        slidingRootNavBuilder!!.layout.events.setOnClickListener {
            events()
        }

        slidingRootNavBuilder!!.layout.verify.setOnClickListener {
            verify()
        }

        slidingRootNavBuilder!!.layout.history.setOnClickListener {
            history()
        }

        slidingRootNavBuilder!!.layout.logout.setOnClickListener {

            slidingRootNavBuilder!!.layout.home.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.contacts.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.events.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.verify.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.history.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.logout.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.LightBlack))

            DashBoardUtility().create(this, "Please Wait...")

            FirebaseAuth.getInstance().signOut()
            Auth.GoogleSignInApi.signOut(googleApiClient)
            googleApiClient.clearDefaultAccountAndReconnect().setResultCallback {
                if (it.isSuccess) {
                    googleApiClient.disconnect()
                    finish()
                    logoutButton = true
                    startActivity(Intent(this, AppStartActivity::class.java))
                } else {
                    DashBoardUtility().snackBar(this, "App logout failed. Please try again",
                            ContextCompat.getColor(this, R.color.Red), ContextCompat.getColor(this, R.color.White))
                }
                DashBoardUtility().dismiss(this)
            }

        }

    }

    fun home() {
        if (supportFragmentManager.findFragmentByTag("home") == null) {
            slidingRootNavBuilder!!.layout.home.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.LightBlack))
            slidingRootNavBuilder!!.layout.contacts.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.events.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.verify.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.history.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.logout.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))

            supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardHomeFragment(), "home").commit()
            slidingRootNavBuilder!!.closeMenu()
        }
    }

    fun contacts() {
        if (packageManager.checkPermission(android.Manifest.permission.READ_CONTACTS, packageName) == PackageManager.PERMISSION_GRANTED) {
            if (supportFragmentManager.findFragmentByTag("contacts") == null) {
                slidingRootNavBuilder!!.layout.home.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
                slidingRootNavBuilder!!.layout.contacts.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.LightBlack))
                slidingRootNavBuilder!!.layout.events.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
                slidingRootNavBuilder!!.layout.verify.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
                slidingRootNavBuilder!!.layout.history.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
                slidingRootNavBuilder!!.layout.logout.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))

                supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardContactsViewPagerFragment(), "contacts").commit()
                slidingRootNavBuilder!!.closeMenu()
            }
        } else {
            Dexter.withActivity(this)
                    .withPermission(android.Manifest.permission.READ_CONTACTS)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            contacts()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) {
                            permissionAlertBox()
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                            permissionAlertBox()
                        }


                    }).check()

        }


    }

    fun permissionAlertBox() {
        AlertDialog.Builder(this@DashBoardActivity).setIcon(R.drawable.ic_contacts_purple)
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()

                }).setPositiveButton("Settings", DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setTitle("Permissions").setMessage("You have denied the permissions to access your contact list. Go to settings to provide permissions or reinstall the app.")
                .show()
    }

    fun events() {
        if (supportFragmentManager.findFragmentByTag("events") == null) {
            slidingRootNavBuilder!!.layout.home.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.contacts.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.events.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.LightBlack))
            slidingRootNavBuilder!!.layout.verify.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.history.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.logout.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))

            supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardEventsFragment(), "events").commit()
            slidingRootNavBuilder!!.closeMenu()
        }
    }

    fun verify() {
        if (supportFragmentManager.findFragmentByTag("verify") == null) {
            slidingRootNavBuilder!!.layout.home.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.contacts.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.events.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.verify.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.LightBlack))
            slidingRootNavBuilder!!.layout.history.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.logout.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))

            supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardVerifyFragment(), "verify").commit()
            slidingRootNavBuilder!!.closeMenu()
        }
    }

    fun history() {
        if (supportFragmentManager.findFragmentByTag("history") == null) {
            slidingRootNavBuilder!!.layout.home.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.contacts.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.events.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.verify.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            slidingRootNavBuilder!!.layout.history.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.LightBlack))
            slidingRootNavBuilder!!.layout.logout.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.FadedBlack))
            supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardHistoryFragment(), "history").commit()
            slidingRootNavBuilder!!.closeMenu()
        }
    }

    fun dashBoardUtility() {
        val dashBoard = object : DashBoard {
            override fun snackBar(message: String, snackBarColor: Int, textColor: Int) {
                val snackBar = Snackbar.make(DashBoardActivity, message, Snackbar.LENGTH_SHORT)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, snackBarColor))
                val textView = snackBarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                textView.setTextColor(ContextCompat.getColor(this@DashBoardActivity, textColor))
                snackBar.show()
            }

            override fun createPurple(text: String) {
                DashBoardLoaderText.text = text
                DashBoardLoaderLayout.visibility = View.VISIBLE
            }

            override fun dismissPurple() {
                DashBoardLoaderLayout.visibility = View.GONE
            }

            override fun updateEmailVerificationFragment(layout: Int) {
                if (layout == 1) {
                    supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardHomeFragment(), "home").commit()
                    setnavigation()
                }
                if (layout == 0) {
                    finish()
                    startActivity(Intent(this@DashBoardActivity, AppStartActivity::class.java))
                    //supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, OnBoardFragment()).commit()
                }
            }
        }
        val dashBoardReciever = DashBoardReciever(dashBoard)
        registerReceiver(dashBoardReciever, IntentFilter("DashBoard"))

    }

    fun updateDashboardwithFragment() {
        DashBoardUtility().create(this@DashBoardActivity, "Logging In...")
        if (getSharedPreferences("Main", 0).getString("EmailVerified", "false") == "false") {

            FirebaseAuth.getInstance().currentUser!!.reload().addOnCompleteListener {
                if (it.isSuccessful) {

                    if (Constants.currUser().isEmailVerified) {
                        DashBoard_ToolBar_Layout.visibility = View.VISIBLE
                        supportFragmentManager.beginTransaction().add(R.id.DashBoardFragmentFrameLayout, DashBoardHomeFragment(), "home").commit()
                        getSharedPreferences("Main", 0).edit().putString("EmailVerified", "true").apply()
                        setnavigation()
                    } else {
                        supportFragmentManager.beginTransaction().add(R.id.DashBoardFragmentFrameLayout, DashBoardEmailVerificationFragment()).commit()

                    }
                }

            }

        } else {
            supportFragmentManager.beginTransaction().add(R.id.DashBoardFragmentFrameLayout, DashBoardHomeFragment(), "home").commit()
            setnavigation()
        }
        DashBoardUtility().dismiss(this)
    }

    fun syncContacts() {
        syncLocalContacts = true
//        AsyncTask.execute {
//            PawMarsDataBase().contacts(this)
//            syncLocalContacts = false
//        }

//        Needle.onBackgroundThread().execute {
//            PawMarsDataBase().contacts(this)
//        }
        SyncContacts(this).execute()
    }

    fun toolBarUpdate() {
        val dashBoardToolBarInterface = object : DashBoardToolBarInterface {
            override fun addCancel() {
                DashBoard_ToolBar_add_cancel.visibility = View.VISIBLE
            }

            override fun clean() {
                DashBoard_ToolBar_add_cancel.visibility = View.GONE
            }

            override fun contacts() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        registerReceiver(DashBoardToolBarReciever(dashBoardToolBarInterface), IntentFilter("DashBoardToolBar"))
    }

    fun toolBarButton() {
        DashBoard_ToolBar_add.setOnClickListener {
            DashBoardToolBar.DashBoardToolBarButtons(this).add()
        }
        DashBoard_ToolBar_cancel.setOnClickListener {
            DashBoardToolBar.DashBoardToolBarButtons(this).cancel()
        }
    }

    var dashBoardContactsUpdateFragment: DashBoardContactsUpdateFragment? = null
    var dashBoardCreateContactListFragment: DashBoardCreateContactListFragment? = null
    var dashBoardContactListDetailsFragment: DashBoardContactListDetailsFragment? = null
    var dashBoardAddEventFragment: DashBoardAddEventFragment? = null
    var dashBoardEventViewPagerFragment: DashBoardEventViewPagerFragment?=null
    fun popUpFragment() {
        val popUpPage = object : PopUpPage {
            override fun create(fragment: String) {

                popUpName = fragment
                if (fragment == "ContactsUpdate") {
                    dashBoardContactsUpdateFragment = DashBoardContactsUpdateFragment()
                    supportFragmentManager.beginTransaction().add(R.id.popup_layout, dashBoardContactsUpdateFragment).commit()

                }
                if (fragment == "CreateContactList") {
                    dashBoardCreateContactListFragment = DashBoardCreateContactListFragment()
                    supportFragmentManager.beginTransaction().add(R.id.popup_layout, dashBoardCreateContactListFragment).commit()
                }
                if (fragment == "ContactListDetails") {
                    dashBoardContactListDetailsFragment = DashBoardContactListDetailsFragment()
                    supportFragmentManager.beginTransaction().add(R.id.popup_layout, dashBoardContactListDetailsFragment).commit()
                }
                if (fragment == "AddEvent") {
                    dashBoardAddEventFragment= DashBoardAddEventFragment()
                    supportFragmentManager.beginTransaction().add(R.id.popup_layout,dashBoardAddEventFragment).commit()
                }
                if (fragment == "EventViewPager") {
                    dashBoardEventViewPagerFragment= DashBoardEventViewPagerFragment()
                    supportFragmentManager.beginTransaction().add(R.id.popup_layout,dashBoardEventViewPagerFragment).commit()
                }
                DashBoard_ToolBar_Layout.visibility=View.GONE

            }

            override fun dismiss(fragment: String) {
                DashBoard_ToolBar_Layout.visibility=View.VISIBLE
                if (fragment == "ContactsUpdate") {
                    println("PopUpFragmnent")
                    supportFragmentManager.beginTransaction().remove(dashBoardContactsUpdateFragment).commit()
                }
                if (fragment == "CreateContactList") {
                    supportFragmentManager.beginTransaction().remove(dashBoardCreateContactListFragment).commit()
                }
                if (fragment == "ContactListDetails") {
                    supportFragmentManager.beginTransaction().remove(dashBoardContactListDetailsFragment).commit()

                }
                if (fragment == "AddEvent") {
                    supportFragmentManager.beginTransaction().remove(dashBoardAddEventFragment).commit()
                }
                if (fragment == "EventViewPager") {
                    supportFragmentManager.beginTransaction().remove(dashBoardEventViewPagerFragment).commit()
                }
            }

        }
        registerReceiver(PopUpFragmentReceiver(popUpPage), IntentFilter("PopUp"))
    }


    class SyncContacts(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg p0: Void?): Int {
            PawMarsDataBase().contacts(context)
            syncLocalContacts = false
            return 1
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        PopUpFragmnent(this).dismiss(popUpName)

    }

}
