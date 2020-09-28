package com.stockmanagerandroid.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "Created")

        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            val email = username.text.toString()
            val pass = password.text.toString()

            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                loading.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            API.authenticateUser(email, pass)
            API.authenticated.observe(this, Observer {
                if(it) {
                    allowUserInApp()
                } else {
                    login_failed_message.text = API.errorString
                    login_failed_message.visibility = View.VISIBLE
                }
            })

        }

        create_account.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

    }

    fun allowUserInApp() {
        Log.d("Login", "Allowing user in app")
        val loadingIntent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(loadingIntent)
        finishAffinity()
    }

    fun showSuccessfulToast() {
        loading.visibility = View.INVISIBLE
        Toast.makeText(this, "Please check your email to reset your password", Toast.LENGTH_SHORT).show()
    }

    fun showUnsuccessfulToast() {
        loading.visibility = View.INVISIBLE
        Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
    }
}
