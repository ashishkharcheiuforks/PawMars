package com.kanavdawra.pawmars.AppStart.OnBoardFragments.OnBoardAuthenticationSignUp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kanavdawra.pawmars.R
import kotlinx.android.synthetic.main.fragment_on_board_authentication_sign_up.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import android.text.Editable
import android.text.TextWatcher
import com.kanavdawra.pawmars.Constants
import com.kanavdawra.pawmars.InterFace.FireBaseSignUpInterFace
import com.nguyenhoanglam.imagepicker.model.Config
import java.util.regex.Pattern
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import android.graphics.BitmapFactory
import java.io.File
import com.developers.imagezipper.ImageZipper


class OnBoardAuthenticationSignUpFragment : Fragment() {
    var firstNameTrue = false
    var lastNameTrue = false
    var emailTrue = false
    var passwordTrue = false
    var confirmPasswordTrue = false
    var userImage: Bitmap? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AuthSignUpTextView.typeface = Typeface.createFromAsset(activity?.assets, "font/enchanting_celebrations.ttf")
        KeyboardVisibilityEvent.setEventListener(activity!!)
        {
            if (it == true) {
                Constants.signupkeyboard = true
                activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                Constants.signupkeyboard = false
                activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            }
        }

        onTextListener()

        onButtonClickListner()

        onImagePicker()
        AuthSignUpImageDelete.setOnClickListener {
            AuthSignUpImageDelete.visibility=View.GONE
            AuthSignUpImageIconDelete.visibility=View.GONE
            AuthSignUpImageIconEdit.visibility=View.GONE
            AuthSignUpImage.setImageResource(R.mipmap.default_user_512px)
            userImage=null
        }
    }

    fun onTextListener() {

        onFirstNameListener()
        onLastNameListener()
        onEmailListener()
        onPasswordListener()
        onConfirmPasswordListener()

    }

    fun onFirstNameListener() {
        val namePattern = Pattern.compile("[a-zA-Z]+")
        AuthSignUpFirstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val matcher = namePattern.matcher(p0)
                val checkName = matcher.matches()
                if (checkName) {

                    AuthSignUpTILFirstName.error = ""
                    firstNameTrue = true
                    signUpButtonEnableToggle()
                }
                if (!checkName) {
                    firstNameTrue = false
                    signUpButtonEnableToggle()
                    AuthSignUpTILFirstName.error = "Not a valid name"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

    }

    fun onLastNameListener() {
        val namePattern = Pattern.compile("[a-zA-Z]+")
        AuthSignUpLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val matcher = namePattern.matcher(p0)
                val checkName = matcher.matches()
                if (checkName) {
                    lastNameTrue = true
                    signUpButtonEnableToggle()
                    AuthSignUpTILLastName.error = ""
                }
                if (!checkName) {
                    lastNameTrue = false
                    signUpButtonEnableToggle()
                    AuthSignUpTILLastName.error = "Not a valid name"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    fun onEmailListener() {
        val emailPattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$")
        AuthSignUpEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val matcher = emailPattern.matcher(p0)
                val checkEmail = matcher.matches()
                if (checkEmail) {
                    emailTrue = true
                    signUpButtonEnableToggle()
                    AuthSignUpTILEmail.error = ""
                }
                if (!checkEmail) {
                    emailTrue = false
                    signUpButtonEnableToggle()
                    AuthSignUpTILEmail.error = "Enter a valid E-mail address"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    fun onPasswordListener() {

        val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.!@#$%^&*+=?-]).{8,}$")
        val passwordGoodPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{8,}$")
        val passwordWeakPattern = Pattern.compile("^(?=.*[a-z]).{8,}$")

        AuthSignUpPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                val strongMatcher = passwordPattern.matcher(p0).matches()
                if (strongMatcher) {
                    passwordTrue = true
                    signUpButtonEnableToggle()
                    AuthSignUpTILPassword.error = ""
                } else {
                    passwordTrue = false
                    signUpButtonEnableToggle()
                    AuthSignUpTILPassword.error = "At least 1 uppercase,1 lowercase,1 number,1 of these(.,!,@,#,$,%,^,&,*,+,=,?,-) and at least 8 char long"
                }
                if (AuthSignUpPassword.text.toString() == AuthSignUpRePassword.text.toString()) {
                    confirmPasswordTrue = true
                    signUpButtonEnableToggle()
                    AuthSignUpTILRePassword.error = ""
                }
                if (AuthSignUpPassword.text.toString() != AuthSignUpRePassword.text.toString()) {
                    confirmPasswordTrue = false
                    signUpButtonEnableToggle()
                    AuthSignUpTILRePassword.error = "Password and confirm password must be same"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    fun onConfirmPasswordListener() {

        val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.!@#$%^&*+=?-]).{8,}$")
        val passwordGoodPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{8,}$")
        val passwordWeakPattern = Pattern.compile("^(?=.*[a-z]).{8,}$")

        AuthSignUpRePassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                val matcher = passwordPattern.matcher(p0)
                val checkPassword = matcher.matches()
                if (checkPassword) {
                    AuthSignUpTILRePassword.error = ""
                    if (AuthSignUpPassword.text.toString() != AuthSignUpRePassword.text.toString()) {
                        confirmPasswordTrue = false
                        signUpButtonEnableToggle()
                        AuthSignUpTILRePassword.error = "Password and confirm password must be same"
                    }
                    if (AuthSignUpPassword.text.toString() == AuthSignUpRePassword.text.toString()) {
                        confirmPasswordTrue = true
                        signUpButtonEnableToggle()
                        AuthSignUpTILRePassword.error = ""
                    }
                }
                if (!checkPassword) {
                    confirmPasswordTrue = false
                    signUpButtonEnableToggle()
                    AuthSignUpTILRePassword.error = "At least 1 uppercase,1 lowercase,1 number,1 of these(.,!,@,#,$,%,^,&,*,+,=,?,-) and at least 8 char long"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }

    fun onButtonClickListner() {
        AuthLogIn.setOnClickListener {
            println("login")
            val intent = Intent("OnBoardFragment")
            intent.putExtra("task", "inflate")
            intent.putExtra("position", 3)
            activity!!.sendBroadcast(intent)
        }
        AuthSignUpButton.setOnClickListener {
            val createLoader = Intent("AppStartActivity")
            createLoader.putExtra("task", "loading")
            createLoader.putExtra("position", 1)
            activity!!.sendBroadcast(createLoader)

            val onBoardAuthenticationSignUpFireBaseInterface = object : FireBaseSignUpInterFace {
                override fun checkemail(check: Boolean) {
                    if (check) {
                        OnBoardAuthenticationSignUpFireBase().saveUser(AuthSignUpFirstName.text.toString(),
                                AuthSignUpLastName.text.toString(), AuthSignUpEmail.text.toString(), AuthSignUpPassword.text.toString(), userImage, activity!!, activity!!)
                    }
                    if (!check) {

                        val emailSnackbar = Intent("AppStartActivity")
                        emailSnackbar.putExtra("task", "snackbar")
                        emailSnackbar.putExtra("message", "E-mail address is already taken.")
                        activity!!.sendBroadcast(emailSnackbar)

                        val dismissLoader = Intent("AppStartActivity")
                        dismissLoader.putExtra("task", "loading")
                        activity!!.sendBroadcast(dismissLoader)
                    }
                }
            }

            OnBoardAuthenticationSignUpFireBase().emailCheck(AuthSignUpEmail.text.toString(), onBoardAuthenticationSignUpFireBaseInterface)
        }
    }

    fun onImagePicker() {
        val image = ArrayList<Image>()
        AuthSignUpImageAddEdit.setOnClickListener {
            ImagePicker.with(this)
                    .setToolbarColor("#9C27B0")
                    .setStatusBarColor("#7B1FA2")
                    .setToolbarTextColor("#FFFFFF")
                    .setToolbarIconColor("#FFFFFF")
                    .setProgressBarColor("#4CAF50")
                    .setBackgroundColor("#FFFFFF")
                    .setCameraOnly(false)
                    .setMultipleMode(false)
                    .setFolderMode(true)
                    .setShowCamera(true)
                    .setFolderTitle("Albums")
                    .setImageTitle("user_image")
                    .setDoneTitle("Done")
                    .setSavePath("PawMars")
                    .setSelectedImages(image)
                    .setKeepScreenOn(true)
                    .start()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Parcelable>(Config.EXTRA_IMAGES)
            val image = images[0] as Image

            val userImageFile = File(image.path)

            if (userImageFile.exists()) {
                AuthSignUpImageIconEdit.visibility = View.VISIBLE
                AuthSignUpImageIconDelete.visibility = View.VISIBLE
                AuthSignUpImageDelete.visibility = View.VISIBLE
//                val userBitmap = BitmapFactory.decodeFile(userImageFile.absolutePath)
//                userImage = userBitmap
                val userBitmapImage = ImageZipper(activity!!).compressToBitmap(userImageFile)
                userImage = userBitmapImage
                AuthSignUpImage.setImageBitmap(userBitmapImage)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    fun signUpButtonEnableToggle() {

        if (firstNameTrue && lastNameTrue && emailTrue && passwordTrue && confirmPasswordTrue) {
            AuthSignUpButton.isEnabled = true
            AuthSignUpButton.background = ContextCompat.getDrawable(activity!!, R.drawable.button_signup_enable)
        } else {
            AuthSignUpButton.isEnabled = false
            AuthSignUpButton.background = ContextCompat.getDrawable(activity!!, R.drawable.button_signup_disable)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_board_authentication_sign_up, container, false)
    }
}
