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
import android.graphics.Color
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
import com.kanavdawra.pawmars.DashBoard.DashBoardPopUpFragments.DashBoardEventViewPager.DashBoardEventViewPagerFragment
import com.kanavdawra.pawmars.InterFace.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import needle.Needle

class DashBoardActivity : AppCompatActivity() {
    var slidingRootNavBuilder: SlidingRootNav? = null
    val dashBoardVerify = DashBoardVerifyFragment()
    var sentMessage = ""
    var stausbarColor = R.color.PurpleDark
    var flashToggle = 0
    var popUpFragmnentManager = supportFragmentManager
    var currFragment = ""
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
                cameraPermission()
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
                        window.statusBarColor = ContextCompat.getColor(this, R.color.Black)
                    } else {
                        window.statusBarColor = ContextCompat.getColor(this, stausbarColor)
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
            events()
        }

        slidingRootNavBuilder!!.layout.contacts.setOnClickListener {
            contacts()
        }

        slidingRootNavBuilder!!.layout.events.setOnClickListener {
            events()
        }

        slidingRootNavBuilder!!.layout.verify.setOnClickListener {
            DashBoard_ToolBar.setBackgroundColor(Color.BLACK)
            DashBoard_ToolBar_Verify.visibility = View.VISIBLE
            cameraPermission()

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
                    getSharedPreferences("Main",0).edit().clear().apply()
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

            supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardEventsFragment(), "home").commit()
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
            Needle.onBackgroundThread().withThreadPoolSize(10).execute { supportFragmentManager.beginTransaction()
                    .replace(R.id.DashBoardFragmentFrameLayout, dashBoardVerify, "verify").commit() }
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

    fun permissionAlertBox() {
        AlertDialog.Builder(this@DashBoardActivity).setIcon(R.drawable.ic_contacts_purple)
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()

                }).setPositiveButton("Settings", DialogInterface.OnClickListener { dialogInterface, i ->
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setTitle("Permissions").setMessage("You have denied the permissions to access your contact list. Go to settings to provide permissions or reinstall the app.")
                .show()
    }

    fun dashBoardUtility() {
        val dashBoard = object : DashBoard {
            override fun statusBarColorChange(color: Int?) {
                window.statusBarColor = ContextCompat.getColor(this@DashBoardActivity, color!!)
                stausbarColor = color
            }

            override fun sendMessage(message: String, resource: Int?) {
                if (resource != null) {
                    val textView = findViewById<TextView>(resource)
                    textView.text = message
                }
                sentMessage = message
            }


            override fun toolBarColorChange(color: Int?) {
                if (color == null) {
                    DashBoard_ToolBar.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity, R.color.colorPrimary))
                } else {
                    DashBoard_ToolBar.background = ContextCompat.getDrawable(this@DashBoardActivity, color)
                }
            }

            override fun toggleToolBar() {
                if (DashBoard_ToolBar.visibility == View.VISIBLE) {
                    DashBoard_ToolBar.visibility = View.GONE
                } else {
                    DashBoard_ToolBar.visibility = View.VISIBLE
                }
            }

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
                    supportFragmentManager.beginTransaction().replace(R.id.DashBoardFragmentFrameLayout, DashBoardEventsFragment(), "home").commit()
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
                        DashBoard_ToolBar.visibility = View.VISIBLE
                        supportFragmentManager.beginTransaction().add(R.id.DashBoardFragmentFrameLayout, DashBoardEventsFragment(), "home").commit()
                        getSharedPreferences("Main", 0).edit().putString("EmailVerified", "true").apply()
                        setnavigation()
                    } else {
                        supportFragmentManager.beginTransaction().add(R.id.DashBoardFragmentFrameLayout, DashBoardEmailVerificationFragment()).commit()

                    }
                }

            }

        } else {
            supportFragmentManager.beginTransaction().add(R.id.DashBoardFragmentFrameLayout, DashBoardEventsFragment(), "home").commit()
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
            override fun verify() {
                clean()
                DashBoard_ToolBar_Verify.visibility = View.VISIBLE
            }

            override fun addCancel() {
                DashBoard_ToolBar_add_cancel.visibility = View.VISIBLE
            }

            override fun clean() {
                DashBoard_ToolBar_Verify.visibility = View.GONE
                DashBoard_ToolBar_add_cancel.visibility = View.GONE
            }

            override fun contacts() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        registerReceiver(DashBoardToolBarReciever(dashBoardToolBarInterface), IntentFilter("DashBoardToolBar"))
    }

    fun cameraPermission() {
        Dexter.withActivity(this).withPermission(android.Manifest.permission.CAMERA).withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                verify()
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                AlertDialog.Builder(this@DashBoardActivity)
                        .setTitle("Camera Permissions")
                        .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()

                        }).setPositiveButton("Settings", DialogInterface.OnClickListener { dialogInterface, i ->
                            startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                        }).setTitle("Permissions").setMessage("You have denied the permissions to access your camera. Go to settings to provide permissions or reinstall the app.")
                        .show()
                DashBoard_ToolBar.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity,R.color.colorPrimary))
                DashBoard_ToolBar_Verify.visibility = View.GONE
            }
        }).check()
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder(this@DashBoardActivity)
                    .setTitle("Camera Permissions")
                    .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()

                    }).setPositiveButton("Settings", DialogInterface.OnClickListener { dialogInterface, i ->
                        startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                    }).setTitle("Permissions").setMessage("You have denied the permissions to access your camera. Go to settings to provide permissions or reinstall the app.")
                    .show()
            DashBoard_ToolBar.setBackgroundColor(ContextCompat.getColor(this@DashBoardActivity,R.color.colorPrimary))
            DashBoard_ToolBar_Verify.visibility = View.GONE
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            verify()
        }
    }

    fun toolBarButton() {
        DashBoard_ToolBar_add.setOnClickListener {
            DashBoardToolBar.DashBoardToolBarButtons(this).add()
        }
        DashBoard_ToolBar_cancel.setOnClickListener {
            DashBoardToolBar.DashBoardToolBarButtons(this).cancel()
        }
        DashBoard_ToolBar_Invites_List.setOnClickListener {
            DashBoardToolBar.DashBoardToolBarButtons(this).inviteesList()
        }
        DashBoard_ToolBar_EventName.setOnClickListener {
            DashBoardToolBar.DashBoardToolBarButtons(this).selectEvent()
        }
        DashBoard_ToolBar_Flash.setOnClickListener {
            if (flashToggle == 0) {
                DashBoard_ToolBar_Flash.setImageResource(R.mipmap.flash_on)
                flashToggle = 1
            } else {
                DashBoard_ToolBar_Flash.setImageResource(R.mipmap.flash_off)
                flashToggle = 0
            }
            DashBoardToolBar.DashBoardToolBarButtons(this).flash()
        }
    }

    var dashBoardContactsUpdateFragment: DashBoardContactsUpdateFragment? = null
    var dashBoardCreateContactListFragment: DashBoardCreateContactListFragment? = null
    var dashBoardContactListDetailsFragment: DashBoardContactListDetailsFragment? = null
    var dashBoardAddEventFragment: DashBoardAddEventFragment? = null
    var dashBoardEventViewPagerFragment: DashBoardEventViewPagerFragment? = null
    fun popUpFragment() {
        val popUpPage = object : PopUpPage {
            override fun create(fragment: String) {

                popUpName = fragment
                if (fragment == "ContactsUpdate") {
                    dashBoardContactsUpdateFragment = DashBoardContactsUpdateFragment()
                    popUpFragmnentManager.beginTransaction().add(R.id.popup_layout, dashBoardContactsUpdateFragment).addToBackStack("ContactsUpdate").commit()

                }
                if (fragment == "CreateContactList") {
                    dashBoardCreateContactListFragment = DashBoardCreateContactListFragment()
                    popUpFragmnentManager.beginTransaction().add(R.id.popup_layout, dashBoardCreateContactListFragment).addToBackStack("CreateContactList").commit()
                }
                if (fragment == "ContactListDetails") {
                    dashBoardContactListDetailsFragment = DashBoardContactListDetailsFragment()
                    popUpFragmnentManager.beginTransaction().add(R.id.popup_layout, dashBoardContactListDetailsFragment).addToBackStack("ContactListDetails").commit()
                }
                if (fragment == "AddEvent") {
                    dashBoardAddEventFragment = DashBoardAddEventFragment()
                    popUpFragmnentManager.beginTransaction().add(R.id.popup_layout, dashBoardAddEventFragment).addToBackStack("AddEvent").commit()
                }
                if (fragment == "EventViewPager") {
                    dashBoardEventViewPagerFragment = DashBoardEventViewPagerFragment()
                    popUpFragmnentManager.beginTransaction().add(R.id.popup_layout, dashBoardEventViewPagerFragment).addToBackStack("EventViewPager").commit()
                    currFragment = "EventViewPager"
                }
                //DashBoard_ToolBar.visibility=View.GONE

            }

            override fun dismiss(fragment: String) {
                DashBoard_ToolBar.visibility = View.VISIBLE
                if (fragment == "ContactsUpdate") {
                    println("PopUpFragmnent")
                    popUpFragmnentManager.beginTransaction().remove(dashBoardContactsUpdateFragment).commit()
                    popUpFragmnentManager.popBackStack()
                }
                if (fragment == "CreateContactList") {
                    popUpFragmnentManager.beginTransaction().remove(dashBoardCreateContactListFragment).commit()
                    popUpFragmnentManager.popBackStack()

                }
                if (fragment == "ContactListDetails") {
                    popUpFragmnentManager.beginTransaction().remove(dashBoardContactListDetailsFragment).commit()
                    popUpFragmnentManager.popBackStack()


                }
                if (fragment == "AddEvent") {
                    popUpFragmnentManager.beginTransaction().remove(dashBoardAddEventFragment).commit()
                    popUpFragmnentManager.popBackStack()

                }
                if (fragment == "EventViewPager") {
                    popUpFragmnentManager.beginTransaction().remove(dashBoardEventViewPagerFragment).commit()
                    popUpFragmnentManager.popBackStack()
                    currFragment = ""
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
        if (currFragment == "EventViewPager") {

        }
        if (popUpFragmnentManager.backStackEntryCount > 0) {
            popUpFragmnentManager.popBackStack()

        } else if (popUpFragmnentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        }
    }

}
