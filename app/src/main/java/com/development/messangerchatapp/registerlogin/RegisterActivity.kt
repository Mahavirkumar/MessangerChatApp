package com.development.messangerchatapp.registerlogin

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.development.messangerchatapp.R
import com.development.messangerchatapp.messages.LatestMessagesActivity
import com.development.messangerchatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

public class RegisterActivity : AppCompatActivity() {
    val email_edittext_register = null
    val password_edittext_register = null
    var mSelectphotoButtonRegister: Button? = null

//    @BindView(R.id.username_edittext_register)
//    lateinit var musername_edittext_register: EditText
    var musername_edittext_register:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val mbtnRegister = findViewById<Button>(R.id.register_button_register)
        musername_edittext_register=findViewById(R.id.username_edittext_register)
        //Log.d("usename",musername_edittext_register?.getText().toString())
        mbtnRegister.setOnClickListener(View.OnClickListener {

            performRegister()            // Firebase Authentication to create a user with email and password
        })
        val mTextViewHaveAcnt = findViewById<TextView>(R.id.already_have_account_text_view)
        mTextViewHaveAcnt.setOnClickListener(View.OnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            // launch the login activity somehow
            Log.d("usename",musername_edittext_register?.getText().toString())
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        mSelectphotoButtonRegister = findViewById(R.id.selectphoto_button_register)
        mSelectphotoButtonRegister?.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        })
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val selectphoto_imageview_register =
                findViewById<CircleImageView>(R.id.selectphoto_imageview_register)
            selectphoto_imageview_register.setImageBitmap(bitmap)

            mSelectphotoButtonRegister?.alpha = 0f
//      val bitmapDrawable = BitmapDrawable(bitmap)
//      selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {

        val email = findViewById<EditText>(R.id.email_edittext_register).text.toString()
        val password = findViewById<EditText>(R.id.password_edittext_register).text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity", "Password: $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString(),musername_edittext_register?.getText().toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String, username:String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,username, profileImageUrl)
        Log.d("usename",username)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }
}
//class User(val uid: String, val username: String, val profileImageUrl: String)

