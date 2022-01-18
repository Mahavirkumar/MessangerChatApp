package com.development.messangerchatapp.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.development.messangerchatapp.R
import com.development.messangerchatapp.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_login)
            val mBtnLogin=findViewById<Button>(R.id.login_button_login)
            mBtnLogin.setOnClickListener {
                performLogin()

            }
            val mBtnBkRegstr=findViewById<TextView>(R.id.back_to_register_textview)
            mBtnBkRegstr.setOnClickListener{
                finish()
            }
        }
    private fun performLogin() {
        val email = findViewById<EditText>(R.id.email_edittext_login).text.toString()
        val password = findViewById<EditText>(R.id.password_edittext_login).text.toString()
        Log.d("Login", "Attempt login with email/pw: $email/***")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}