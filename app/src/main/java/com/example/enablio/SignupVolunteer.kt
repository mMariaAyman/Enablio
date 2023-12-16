package com.example.enablio

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivitySignupVolunteerBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupVolunteer : AppCompatActivity() {
    private lateinit var binding:ActivitySignupVolunteerBinding
    private lateinit var rootFBRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        rootFBRef=FirebaseDatabase.getInstance().getReference()
        rootFBRef.setValue("Maria ayman")

    }
}