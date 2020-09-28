package com.stockmanagerandroid.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_create_account.password
import kotlinx.android.synthetic.main.activity_login.*

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        login_button.setOnClickListener {
            Log.d("Create", "Login clicked")
            val invitationCode = invitation_code.text.toString()
            val fName = first_name.text.toString()
            val lName = last_name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            val confirmPassword = confirm_password.text.toString()

            API.createAccount(invitationCode, fName, lName, email.toLowerCase(), password, confirmPassword)

            API.accountCreated.observe(this, Observer {
                if(it) {
                    allowUserInApp()
                } else {
                    create_failed_message.text = API.errorString
                    create_failed_message.visibility = View.VISIBLE
                }
            })
        }
    }

    fun allowUserInApp() {
        Log.d("Login", "Allowing user in app")
        val loadingIntent = Intent(this@CreateAccountActivity, MainActivity::class.java)
        loadingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(loadingIntent)
        finishAffinity()
    }
}