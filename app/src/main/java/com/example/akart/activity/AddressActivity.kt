package com.example.akart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.akart.R
import com.example.akart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddressBinding
    private lateinit var preferences : SharedPreferences

    private lateinit var totalCost: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()
        
        binding.proceed.setOnClickListener{
            validateData(
                binding.userName.text.toString(),
                binding.userNumber.text.toString(),
                binding.state.text.toString(),
                binding.district.text.toString(),
                binding.city.text.toString(),
                binding.pinCode.text.toString()
            )
        }
    }

    private fun validateData(name: String, number: String, state: String, district: String, city: String, pinCode: String) {
        
        if (number.isEmpty() || name.isEmpty() || state.isEmpty() || district.isEmpty() || city.isEmpty() || pinCode.isEmpty()){
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
        }else{
            storeData(state, district, city, pinCode)
        }

    }

    private fun storeData(state: String, district: String, city: String, pinCode: String) {
        val map = hashMapOf<String, Any>()
        map["state"] = state
        map["district"] = district
        map["city"] = city
        map["pinCode"] = pinCode
        
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .update(map).addOnSuccessListener {

                val b = Bundle()
                b.putStringArrayList("productIds", intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)

                val intent = Intent(this, CheckoutActivity::class.java)

                intent.putExtras(b)

                startActivity(intent)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadUserInfo() {
        
        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .get().addOnSuccessListener { 
                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.state.setText(it.getString("state"))
                binding.district.setText(it.getString("district"))
                binding.city.setText(it.getString("city"))
                binding.pinCode.setText(it.getString("pinCode"))
            }
    }
}