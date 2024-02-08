package com.example.enablio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enablio.databinding.ActivityForgetPassBinding

class ForgetPass : AppCompatActivity() {
    private lateinit var binding:ActivityForgetPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}