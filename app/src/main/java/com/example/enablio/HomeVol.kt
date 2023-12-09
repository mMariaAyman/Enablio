package com.example.enablio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityHomeVolBinding

class HomeVol : AppCompatActivity() {
    private lateinit var binding: ActivityHomeVolBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeVolBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}