package com.example.akart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.akart.R
import com.example.akart.databinding.ActivityRegisterBinding
import com.example.akart.model.UserModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button4.setOnClickListener{
            openLogin()
        }

        binding.button3.setOnClickListener{
            validateUser()
        }

    }

    private fun validateUser() {
        if (binding.userName.text!!.isEmpty() || binding.userNumber.text!!.isEmpty())
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        else
            storeData()
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()
        builder.show()

        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString("name", binding.userName.text.toString())
        editor.putString("number", binding.userNumber.text.toString())
        editor.apply()

        val data = UserModel(userName = binding.userName.text.toString(), userPhoneNumber = binding.userNumber.text.toString())

        Firebase.firestore.collection("users").document(binding.userNumber.text.toString())
            .set(data).addOnSuccessListener {
                Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
                builder.dismiss()
                openLogin()
            }
            .addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}