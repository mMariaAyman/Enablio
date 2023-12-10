package com.example.enablio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityHomeDisBinding

class HomeDis : AppCompatActivity() {
    private lateinit var binding:ActivityHomeDisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDisBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}