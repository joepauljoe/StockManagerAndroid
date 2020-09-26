package com.stockmanagerandroid.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.activity_create_account.*

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

            API.createAccount(invitationCode, fName, lName, email, password, confirmPassword)
        }
    }
}