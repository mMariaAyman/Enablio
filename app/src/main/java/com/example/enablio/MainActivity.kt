package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        val intent = Intent(this, ProfileDis::class.java)
//         startActivity(intent)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Enablio")
        binding.volBtn.setOnClickListener {
            val intent = Intent(this, LoginVol::class.java)
            startActivity(intent)
        }
        binding.disBtn.setOnClickListener {
            val intent = Intent(this,LoginDis::class.java)
            startActivity(intent)
        }
    }
}